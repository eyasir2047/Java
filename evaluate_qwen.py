#!/usr/bin/env python3
"""
Qwen 2.5-7B Bengali QA Evaluation Script
Optimized single-model evaluation with authentication and checkpointing
"""

import json
import argparse
import torch
from transformers import AutoTokenizer, AutoModelForCausalLM
from tqdm import tqdm
import time
import re
from collections import defaultdict
import os
from datetime import datetime


def setup_authentication(token=None):
    """Setup HuggingFace authentication."""
    if token:
        from huggingface_hub import login
        login(token=token)
        print("✓ Authenticated with HuggingFace")
    else:
        print("⚠ No token provided. Assuming already logged in via 'huggingface-cli login'")


def load_qwen_model(use_8bit=False, use_flash_attention=False):
    """
    Load Qwen 2.5-7B Instruct model with optimizations.

    Args:
        use_8bit: Enable 8-bit quantization for memory efficiency
        use_flash_attention: Enable Flash Attention 2 for speed
    """
    model_name = "Qwen/Qwen2.5-7B-Instruct"
    print(f"\n{'='*60}")
    print(f"Loading Qwen 2.5-7B Instruct...")
    print(f"{'='*60}")

    # Load tokenizer
    print("📦 Loading tokenizer...")
    tokenizer = AutoTokenizer.from_pretrained(
        model_name,
        trust_remote_code=True
    )

    # Configure model loading
    load_kwargs = {
        "trust_remote_code": True,
    }

    if torch.cuda.is_available():
        print("🚀 GPU detected! Using FP16...")
        load_kwargs["torch_dtype"] = torch.float16
        load_kwargs["device_map"] = "auto"
    else:
        print("💻 No GPU found. Using CPU (FP32)...")
        load_kwargs["torch_dtype"] = torch.float32

    # Optional: 8-bit quantization
    if use_8bit and torch.cuda.is_available():
        print("🔧 Enabling 8-bit quantization...")
        from transformers import BitsAndBytesConfig
        load_kwargs["quantization_config"] = BitsAndBytesConfig(load_in_8bit=True)

    # Optional: Flash Attention
    if use_flash_attention:
        print("⚡ Enabling Flash Attention 2...")
        load_kwargs["attn_implementation"] = "flash_attention_2"

    # Load model
    print("🤖 Loading model (this may take a few minutes)...")
    model = AutoModelForCausalLM.from_pretrained(model_name, **load_kwargs)

    if not torch.cuda.is_available():
        model = model.to("cpu")

    print(f"✓ Model loaded successfully!")
    print(f"Device: {model.device}")
    print(f"{'='*60}\n")

    return model, tokenizer


def create_bengali_prompt(context: str, question: str) -> str:
    """Create Bengali QA prompt with CoT instruction."""
    prompt = f"""আপনি একজন সহায়ক সহকারী। নিচের প্রসঙ্গ এবং প্রশ্ন পড়ুন এবং উত্তর দিন।

প্রসঙ্গ: {context}

প্রশ্ন: {question}

নির্দেশনা:
1. প্রথমে আপনার চিন্তার ধারা (Chain of Thought) ব্যাখ্যা করুন
2. তারপর চূড়ান্ত উত্তর দিন
3. যদি প্রসঙ্গে উত্তর না থাকে, তাহলে "উত্তর প্রসঙ্গে নেই" বলুন

চিন্তার ধারা: """
    return prompt


def generate_answer(model, tokenizer, prompt, max_new_tokens=512, temperature=0.7, top_p=0.9):
    """Generate answer from Qwen model."""
    inputs = tokenizer(prompt, return_tensors="pt").to(model.device)

    with torch.no_grad():
        outputs = model.generate(
            **inputs,
            max_new_tokens=max_new_tokens,
            temperature=temperature,
            top_p=top_p,
            do_sample=True,
            pad_token_id=tokenizer.eos_token_id
        )

    response = tokenizer.decode(outputs[0], skip_special_tokens=True)
    # Remove prompt from response
    response = response[len(prompt):].strip()
    return response


def extract_cot_and_answer(response: str):
    """Extract Chain of Thought and final answer."""
    cot = ""
    answer = ""

    if "উত্তর:" in response:
        parts = response.split("উত্তর:", 1)
        cot = parts[0].replace("চিন্তার ধারা:", "").strip()
        answer = parts[1].strip()
    else:
        lines = response.strip().split('\n')
        split_point = max(1, len(lines) * 2 // 3)
        cot = '\n'.join(lines[:split_point])
        answer = '\n'.join(lines[split_point:])

    return cot, answer


def normalize_bengali_text(text: str) -> str:
    """Normalize Bengali text for comparison."""
    text = re.sub(r'\s+', ' ', text.strip())
    text = re.sub(r'[।,!?;:\.\-]', '', text)
    return text.lower()


def compute_exact_match(prediction: str, ground_truth: str) -> int:
    """Compute exact match score."""
    return int(normalize_bengali_text(prediction) == normalize_bengali_text(ground_truth))


def compute_f1(prediction: str, ground_truth: str) -> float:
    """Compute F1 score between prediction and ground truth."""
    pred_tokens = normalize_bengali_text(prediction).split()
    truth_tokens = normalize_bengali_text(ground_truth).split()

    if len(pred_tokens) == 0 or len(truth_tokens) == 0:
        return float(pred_tokens == truth_tokens)

    common = set(pred_tokens) & set(truth_tokens)
    if len(common) == 0:
        return 0.0

    precision = len(common) / len(pred_tokens)
    recall = len(common) / len(truth_tokens)

    return 2 * (precision * recall) / (precision + recall)


def save_checkpoint(predictions, metrics, checkpoint_dir, checkpoint_num):
    """Save intermediate checkpoint."""
    os.makedirs(checkpoint_dir, exist_ok=True)
    checkpoint_path = os.path.join(checkpoint_dir, f"checkpoint_{checkpoint_num}.json")

    checkpoint_data = {
        "num_predictions": len(predictions),
        "predictions": predictions,
        "current_metrics": {
            "em": sum(metrics["em_scores"]) / len(metrics["em_scores"]) if metrics["em_scores"] else 0,
            "f1": sum(metrics["f1_scores"]) / len(metrics["f1_scores"]) if metrics["f1_scores"] else 0,
        }
    }

    with open(checkpoint_path, 'w', encoding='utf-8') as f:
        json.dump(checkpoint_data, f, ensure_ascii=False, indent=2)

    print(f"💾 Checkpoint saved: {checkpoint_path}")


def evaluate_qwen(
    dataset_path: str,
    output_dir: str = "results",
    max_samples: int = None,
    checkpoint_interval: int = 1000,
    hf_token: str = None,
    temperature: float = 0.7,
    top_p: float = 0.9,
    use_8bit: bool = False,
    use_flash_attention: bool = False
):
    """
    Main evaluation function for Qwen 2.5-7B.

    Args:
        dataset_path: Path to qa_5000_test.json
        output_dir: Output directory for results
        max_samples: Maximum samples to evaluate (None = all)
        checkpoint_interval: Save checkpoint every N samples
        hf_token: HuggingFace token for authentication
        temperature: Generation temperature
        top_p: Top-p sampling parameter
        use_8bit: Enable 8-bit quantization
        use_flash_attention: Enable Flash Attention 2
    """
    # Setup
    print(f"\n🚀 Qwen 2.5-7B Bengali QA Evaluation")
    print(f"{'='*60}")
    print(f"Dataset: {dataset_path}")
    print(f"Output: {output_dir}")
    print(f"Temperature: {temperature}, Top-p: {top_p}")
    print(f"{'='*60}\n")

    # Authentication
    setup_authentication(hf_token)

    # Create output directory
    os.makedirs(output_dir, exist_ok=True)
    checkpoint_dir = os.path.join(output_dir, "checkpoints")

    # Load model
    model, tokenizer = load_qwen_model(use_8bit=use_8bit, use_flash_attention=use_flash_attention)

    # Load dataset
    print(f"📖 Loading dataset: {dataset_path}")
    with open(dataset_path, 'r', encoding='utf-8') as f:
        dataset = json.load(f)

    if max_samples:
        dataset = dataset[:max_samples]
        print(f"⚠️  Evaluating on {max_samples} samples (limited)")
    else:
        print(f"✓ Loaded {len(dataset)} samples")

    # Initialize metrics
    predictions = []
    metrics = {
        "em_scores": [],
        "f1_scores": [],
        "answerable_em": [],
        "answerable_f1": [],
        "unanswerable_em": [],
        "unanswerable_f1": [],
        "inference_times": [],
        "by_subject": defaultdict(lambda: {"em": [], "f1": []})
    }

    # Evaluation loop
    print(f"\n🔄 Starting evaluation...")
    start_time = datetime.now()

    for idx, item in enumerate(tqdm(dataset, desc="Evaluating Qwen"), 1):
        # Extract data
        context = item["context"]
        question = item["question"]
        is_impossible = item["is_impossible"]
        ground_truth_answers = item["answers"]
        ground_truth_cot = item.get("cot", "")
        context_id = item.get("context_id", "")
        subject = context_id.split('_')[2] if '_' in context_id else "unknown"

        # Create prompt
        prompt = create_bengali_prompt(context, question)

        # Generate response
        inference_start = time.time()
        response = generate_answer(model, tokenizer, prompt, temperature=temperature, top_p=top_p)
        inference_time = time.time() - inference_start

        # Extract CoT and answer
        pred_cot, pred_answer = extract_cot_and_answer(response)

        # Compute metrics
        if is_impossible:
            is_correct = any(
                marker in pred_answer.lower()
                for marker in ["উত্তর প্রসঙ্গে নেই", "উত্তর নেই", "no answer"]
            )
            em = int(is_correct)
            f1 = float(is_correct)
            metrics["unanswerable_em"].append(em)
            metrics["unanswerable_f1"].append(f1)
        else:
            em_scores = [compute_exact_match(pred_answer, ans["text"]) for ans in ground_truth_answers]
            f1_scores = [compute_f1(pred_answer, ans["text"]) for ans in ground_truth_answers]
            em = max(em_scores)
            f1 = max(f1_scores)
            metrics["answerable_em"].append(em)
            metrics["answerable_f1"].append(f1)

        metrics["em_scores"].append(em)
        metrics["f1_scores"].append(f1)
        metrics["inference_times"].append(inference_time)
        metrics["by_subject"][subject]["em"].append(em)
        metrics["by_subject"][subject]["f1"].append(f1)

        # Save prediction
        predictions.append({
            "context_id": context_id,
            "question": question,
            "ground_truth_answer": ground_truth_answers,
            "ground_truth_cot": ground_truth_cot,
            "predicted_answer": pred_answer,
            "predicted_cot": pred_cot,
            "full_response": response,
            "is_impossible": is_impossible,
            "em": em,
            "f1": f1,
            "inference_time": inference_time,
            "subject": subject
        })

        # Save checkpoint
        if idx % checkpoint_interval == 0:
            save_checkpoint(predictions, metrics, checkpoint_dir, idx)

    # Compute final results
    total_time = (datetime.now() - start_time).total_seconds()

    results = {
        "model": "qwen",
        "model_name": "Qwen/Qwen2.5-7B-Instruct",
        "evaluation_date": datetime.now().isoformat(),
        "total_samples": len(dataset),
        "total_time_seconds": total_time,
        "hyperparameters": {
            "temperature": temperature,
            "top_p": top_p,
            "max_new_tokens": 512,
            "use_8bit": use_8bit,
            "use_flash_attention": use_flash_attention
        },
        "metrics": {
            "overall_em": sum(metrics["em_scores"]) / len(metrics["em_scores"]),
            "overall_f1": sum(metrics["f1_scores"]) / len(metrics["f1_scores"]),
            "answerable_em": sum(metrics["answerable_em"]) / len(metrics["answerable_em"]) if metrics["answerable_em"] else 0,
            "answerable_f1": sum(metrics["answerable_f1"]) / len(metrics["answerable_f1"]) if metrics["answerable_f1"] else 0,
            "unanswerable_em": sum(metrics["unanswerable_em"]) / len(metrics["unanswerable_em"]) if metrics["unanswerable_em"] else 0,
            "unanswerable_f1": sum(metrics["unanswerable_f1"]) / len(metrics["unanswerable_f1"]) if metrics["unanswerable_f1"] else 0,
            "avg_inference_time": sum(metrics["inference_times"]) / len(metrics["inference_times"]),
            "total_inference_time": sum(metrics["inference_times"])
        },
        "subject_breakdown": {
            subject: {
                "em": sum(data["em"]) / len(data["em"]),
                "f1": sum(data["f1"]) / len(data["f1"]),
                "count": len(data["em"])
            }
            for subject, data in metrics["by_subject"].items()
        },
        "predictions": predictions
    }

    # Save results
    output_path = os.path.join(output_dir, "qwen_results.json")
    print(f"\n💾 Saving results to: {output_path}")
    with open(output_path, 'w', encoding='utf-8') as f:
        json.dump(results, f, ensure_ascii=False, indent=2)

    # Print summary
    print(f"\n{'='*60}")
    print(f"✓ EVALUATION COMPLETE")
    print(f"{'='*60}")
    print(f"Model:             Qwen/Qwen2.5-7B-Instruct")
    print(f"Samples:           {len(dataset)}")
    print(f"Total Time:        {total_time/60:.2f} minutes")
    print(f"{'='*60}")
    print(f"📊 RESULTS")
    print(f"{'='*60}")
    print(f"Overall EM:        {results['metrics']['overall_em']*100:.2f}%")
    print(f"Overall F1:        {results['metrics']['overall_f1']*100:.2f}%")
    print(f"─" * 60)
    print(f"Answerable EM:     {results['metrics']['answerable_em']*100:.2f}%")
    print(f"Answerable F1:     {results['metrics']['answerable_f1']*100:.2f}%")
    print(f"─" * 60)
    print(f"Unanswerable EM:   {results['metrics']['unanswerable_em']*100:.2f}%")
    print(f"Unanswerable F1:   {results['metrics']['unanswerable_f1']*100:.2f}%")
    print(f"─" * 60)
    print(f"Avg Inference:     {results['metrics']['avg_inference_time']:.3f}s")
    print(f"{'='*60}\n")

    # Print subject breakdown
    print("📚 SUBJECT BREAKDOWN")
    print(f"{'='*60}")
    for subject, stats in sorted(results['subject_breakdown'].items(), key=lambda x: x[1]['em'], reverse=True):
        print(f"{subject:20s} | EM: {stats['em']*100:5.2f}% | F1: {stats['f1']*100:5.2f}% | N={stats['count']}")
    print(f"{'='*60}\n")

    return results


def main():
    parser = argparse.ArgumentParser(
        description='Evaluate Qwen 2.5-7B on Bengali QA with CoT',
        formatter_class=argparse.ArgumentDefaultsHelpFormatter
    )

    # Required arguments
    parser.add_argument('--dataset', required=True,
                       help='Path to qa_5000_test.json')

    # Optional arguments
    parser.add_argument('--output-dir', default='results',
                       help='Output directory for results')
    parser.add_argument('--max-samples', type=int,
                       help='Maximum samples to evaluate (for testing)')
    parser.add_argument('--checkpoint-interval', type=int, default=1000,
                       help='Save checkpoint every N samples')
    parser.add_argument('--hf-token', type=str,
                       help='HuggingFace token (or use huggingface-cli login)')

    # Generation parameters
    parser.add_argument('--temperature', type=float, default=0.7,
                       help='Generation temperature (0.0-1.0)')
    parser.add_argument('--top-p', type=float, default=0.9,
                       help='Top-p sampling parameter (0.0-1.0)')

    # Optimization flags
    parser.add_argument('--use-8bit', action='store_true',
                       help='Enable 8-bit quantization (reduces memory)')
    parser.add_argument('--use-flash-attention', action='store_true',
                       help='Enable Flash Attention 2 (requires installation)')

    args = parser.parse_args()

    # Run evaluation
    evaluate_qwen(
        dataset_path=args.dataset,
        output_dir=args.output_dir,
        max_samples=args.max_samples,
        checkpoint_interval=args.checkpoint_interval,
        hf_token=args.hf_token,
        temperature=args.temperature,
        top_p=args.top_p,
        use_8bit=args.use_8bit,
        use_flash_attention=args.use_flash_attention
    )


if __name__ == '__main__':
    main()
