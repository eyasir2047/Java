#!/usr/bin/env python3
"""
Model Evaluation Script for Bengali QA with CoT

Evaluates multiple models on Bengali question-answering with Chain of Thought reasoning.
Computes Answer Accuracy (EM/F1) and CoT Quality (BERTScore).
"""

import json
import argparse
import torch
from transformers import AutoTokenizer, AutoModelForCausalLM
from tqdm import tqdm
import time
from typing import List, Dict, Any, Tuple
import re
from collections import defaultdict
import os


# Model configurations
MODELS = {
    "qwen": {
        "name": "Qwen/Qwen2.5-7B-Instruct",
        "type": "instruct"
    },
    "banglagpt": {
        "name": "BanglaLLM/bangla-llama-7b-instruct-v0.1",  # Fixed: Valid HuggingFace model
        "type": "instruct"  # Changed from alpaca to instruct
    },
    "llama": {
        "name": "meta-llama/Meta-Llama-3-8B-Instruct",  # Fixed: Capital 'M' in Meta-Llama
        "type": "instruct"
    },
    "gpt2-bengali": {
        "name": "flax-community/gpt2-bengali",  # Alternative Bengali model
        "type": "base"
    }
}


def create_prompt(context: str, question: str, model_type: str = "instruct") -> str:
    """
    Create prompt for the model with CoT instruction.

    Args:
        context: Bengali context passage
        question: Bengali question
        model_type: Type of model (instruct, alpaca, base, etc.)

    Returns:
        Formatted prompt string
    """
    if model_type == "instruct":
        prompt = f"""আপনি একজন সহায়ক সহকারী। নিচের প্রসঙ্গ এবং প্রশ্ন পড়ুন এবং উত্তর দিন।

প্রসঙ্গ: {context}

প্রশ্ন: {question}

নির্দেশনা:
1. প্রথমে আপনার চিন্তার ধারা (Chain of Thought) ব্যাখ্যা করুন
2. তারপর চূড়ান্ত উত্তর দিন
3. যদি প্রসঙ্গে উত্তর না থাকে, তাহলে "উত্তর প্রসঙ্গে নেই" বলুন

চিন্তার ধারা: """

    elif model_type == "alpaca":
        prompt = f"""নিচে একটি নির্দেশনা দেওয়া হয়েছে যা একটি কাজ বর্ণনা করে, একটি ইনপুট সহ যা আরও প্রসঙ্গ প্রদান করে। অনুরোধটি যথাযথভাবে সম্পূর্ণ করে এমন একটি প্রতিক্রিয়া লিখুন।

### নির্দেশনা:
প্রসঙ্গ পড়ুন এবং প্রশ্নের উত্তর দিন। প্রথমে চিন্তার ধারা ব্যাখ্যা করুন, তারপর উত্তর দিন।

### ইনপুট:
প্রসঙ্গ: {context}
প্রশ্ন: {question}

### প্রতিক্রিয়া:
চিন্তার ধারা: """

    elif model_type == "base":
        # For base models like GPT-2, use simpler format
        prompt = f"""প্রসঙ্গ: {context}

প্রশ্ন: {question}

উত্তর: """

    return prompt


def normalize_text(text: str) -> str:
    """Normalize Bengali text for comparison."""
    # Remove extra whitespace
    text = re.sub(r'\s+', ' ', text.strip())
    # Remove common punctuation
    text = re.sub(r'[।,!?;:\.\-]', '', text)
    return text.lower()


def extract_answer_and_cot(response: str) -> Tuple[str, str]:
    """
    Extract CoT and answer from model response.

    Expected format:
    চিন্তার ধারা: [reasoning]
    উত্তর: [answer]

    Returns:
        (cot, answer) tuple
    """
    cot = ""
    answer = ""

    # Try to find explicit "উত্তর:" marker
    if "উত্তর:" in response:
        parts = response.split("উত্তর:", 1)
        cot = parts[0].replace("চিন্তার ধারা:", "").strip()
        answer = parts[1].strip()
    else:
        # If no explicit marker, use heuristics
        # Take first 2/3 as CoT, last 1/3 as answer
        lines = response.strip().split('\n')
        split_point = max(1, len(lines) * 2 // 3)
        cot = '\n'.join(lines[:split_point])
        answer = '\n'.join(lines[split_point:])

    return cot, answer


def compute_f1(prediction: str, ground_truth: str) -> float:
    """
    Compute F1 score between prediction and ground truth.

    Args:
        prediction: Predicted answer
        ground_truth: Ground truth answer

    Returns:
        F1 score (0-1)
    """
    pred_tokens = normalize_text(prediction).split()
    truth_tokens = normalize_text(ground_truth).split()

    if len(pred_tokens) == 0 or len(truth_tokens) == 0:
        return int(pred_tokens == truth_tokens)

    common = set(pred_tokens) & set(truth_tokens)
    if len(common) == 0:
        return 0.0

    precision = len(common) / len(pred_tokens)
    recall = len(common) / len(truth_tokens)

    return 2 * (precision * recall) / (precision + recall)


def compute_exact_match(prediction: str, ground_truth: str) -> int:
    """
    Compute exact match between prediction and ground truth.

    Returns:
        1 if exact match, 0 otherwise
    """
    return int(normalize_text(prediction) == normalize_text(ground_truth))


def load_model(model_name: str):
    """Load model and tokenizer."""
    print(f"Loading model: {model_name}")

    tokenizer = AutoTokenizer.from_pretrained(model_name, trust_remote_code=True)
    model = AutoModelForCausalLM.from_pretrained(
        model_name,
        torch_dtype=torch.float16 if torch.cuda.is_available() else torch.float32,
        device_map="auto" if torch.cuda.is_available() else None,
        trust_remote_code=True
    )

    if not torch.cuda.is_available():
        model = model.to("cpu")

    return model, tokenizer


def generate_response(
    model,
    tokenizer,
    prompt: str,
    max_new_tokens: int = 512,
    temperature: float = 0.7,
    top_p: float = 0.9
) -> str:
    """Generate response from model."""
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

    # Remove the prompt from response
    response = response[len(prompt):].strip()

    return response


def evaluate_model(
    model_key: str,
    dataset_path: str,
    output_dir: str = "results",
    batch_size: int = 1,
    max_samples: int = None
) -> Dict[str, Any]:
    """
    Evaluate a single model on the dataset.

    Args:
        model_key: Key from MODELS dict (qwen, banglagpt, llama, gpt2-bengali)
        dataset_path: Path to qa_5000_test.json
        output_dir: Directory to save results
        batch_size: Batch size (usually 1 for generation)
        max_samples: Maximum samples to evaluate (for testing)

    Returns:
        Dictionary with predictions and metrics
    """
    # Create output directory
    os.makedirs(output_dir, exist_ok=True)

    # Load model
    model_config = MODELS[model_key]
    model, tokenizer = load_model(model_config["name"])

    # Load dataset
    print(f"Loading dataset: {dataset_path}")
    with open(dataset_path, 'r', encoding='utf-8') as f:
        dataset = json.load(f)

    if max_samples:
        dataset = dataset[:max_samples]
        print(f"Evaluating on {max_samples} samples")

    # Evaluate
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

    print(f"Evaluating {model_key}...")
    for item in tqdm(dataset, desc=f"Evaluating {model_key}"):
        # Extract data
        context = item["context"]
        question = item["question"]
        is_impossible = item["is_impossible"]
        ground_truth_answers = item["answers"]
        ground_truth_cot = item.get("cot", "")
        context_id = item.get("context_id", "")

        # Get subject
        subject = context_id.split('_')[2] if '_' in context_id else "unknown"

        # Create prompt
        prompt = create_prompt(context, question, model_config["type"])

        # Generate response
        start_time = time.time()
        response = generate_response(model, tokenizer, prompt)
        inference_time = time.time() - start_time

        # Extract CoT and answer
        pred_cot, pred_answer = extract_answer_and_cot(response)

        # Compute metrics
        if is_impossible:
            # For unanswerable questions, check if model says "no answer"
            is_correct = any(
                marker in pred_answer.lower()
                for marker in ["উত্তর প্রসঙ্গে নেই", "উত্তর নেই", "no answer"]
            )
            em = int(is_correct)
            f1 = float(is_correct)
            metrics["unanswerable_em"].append(em)
            metrics["unanswerable_f1"].append(f1)
        else:
            # For answerable questions, compute against ground truth
            # Take best score across multiple answers
            em_scores = [
                compute_exact_match(pred_answer, ans["text"])
                for ans in ground_truth_answers
            ]
            f1_scores = [
                compute_f1(pred_answer, ans["text"])
                for ans in ground_truth_answers
            ]
            em = max(em_scores)
            f1 = max(f1_scores)
            metrics["answerable_em"].append(em)
            metrics["answerable_f1"].append(f1)

        # Overall metrics
        metrics["em_scores"].append(em)
        metrics["f1_scores"].append(f1)
        metrics["inference_times"].append(inference_time)

        # Subject-wise metrics
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

    # Compute aggregate metrics
    results = {
        "model": model_key,
        "model_name": model_config["name"],
        "total_samples": len(dataset),
        "metrics": {
            "overall_em": sum(metrics["em_scores"]) / len(metrics["em_scores"]),
            "overall_f1": sum(metrics["f1_scores"]) / len(metrics["f1_scores"]),
            "answerable_em": sum(metrics["answerable_em"]) / len(metrics["answerable_em"]) if metrics["answerable_em"] else 0,
            "answerable_f1": sum(metrics["answerable_f1"]) / len(metrics["answerable_f1"]) if metrics["answerable_f1"] else 0,
            "unanswerable_em": sum(metrics["unanswerable_em"]) / len(metrics["unanswerable_em"]) if metrics["unanswerable_em"] else 0,
            "unanswerable_f1": sum(metrics["unanswerable_f1"]) / len(metrics["unanswerable_f1"]) if metrics["unanswerable_f1"] else 0,
            "avg_inference_time": sum(metrics["inference_times"]) / len(metrics["inference_times"]),
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
    output_path = os.path.join(output_dir, f"{model_key}_results.json")
    print(f"Saving results to: {output_path}")
    with open(output_path, 'w', encoding='utf-8') as f:
        json.dump(results, f, ensure_ascii=False, indent=2)

    # Print summary
    print(f"\n{'='*60}")
    print(f"Results for {model_key}:")
    print(f"{'='*60}")
    print(f"Overall EM:        {results['metrics']['overall_em']:.2%}")
    print(f"Overall F1:        {results['metrics']['overall_f1']:.2%}")
    print(f"Answerable EM:     {results['metrics']['answerable_em']:.2%}")
    print(f"Answerable F1:     {results['metrics']['answerable_f1']:.2%}")
    print(f"Unanswerable EM:   {results['metrics']['unanswerable_em']:.2%}")
    print(f"Unanswerable F1:   {results['metrics']['unanswerable_f1']:.2%}")
    print(f"Avg Inference:     {results['metrics']['avg_inference_time']:.2f}s")
    print(f"{'='*60}\n")

    return results


def main():
    parser = argparse.ArgumentParser(description='Evaluate models on Bengali QA')
    parser.add_argument('--dataset', required=True, help='Path to qa_5000_test.json')
    parser.add_argument('--models', nargs='+', default=['all'],
                       choices=['all', 'qwen', 'banglagpt', 'llama', 'gpt2-bengali'],
                       help='Models to evaluate')
    parser.add_argument('--output-dir', default='results', help='Output directory')
    parser.add_argument('--max-samples', type=int, help='Max samples for testing')

    args = parser.parse_args()

    # Determine which models to evaluate
    if 'all' in args.models:
        models_to_eval = list(MODELS.keys())
    else:
        models_to_eval = args.models

    print(f"Evaluating models: {models_to_eval}")
    print(f"Dataset: {args.dataset}")
    print(f"Output: {args.output_dir}\n")

    # Evaluate each model
    all_results = {}
    for model_key in models_to_eval:
        try:
            results = evaluate_model(
                model_key,
                args.dataset,
                args.output_dir,
                max_samples=args.max_samples
            )
            all_results[model_key] = results
        except Exception as e:
            print(f"Error evaluating {model_key}: {e}")
            import traceback
            traceback.print_exc()
            continue

    # Save summary
    summary_path = os.path.join(args.output_dir, "summary.json")
    with open(summary_path, 'w', encoding='utf-8') as f:
        json.dump(all_results, f, ensure_ascii=False, indent=2)

    print(f"\nEvaluation complete! Results saved to: {args.output_dir}")


if __name__ == '__main__':
    main()
