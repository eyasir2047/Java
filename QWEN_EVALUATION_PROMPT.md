# TCREI Prompt: Qwen 2.5-7B Bengali QA Evaluation

## 🎯 TASK

Evaluate the **Qwen/Qwen2.5-7B-Instruct** model on a large-scale Bengali Question-Answering dataset (**qa_fixed.json** - 100,000 samples) with Chain of Thought (CoT) reasoning. Generate predictions, compute metrics (EM/F1), and produce comprehensive analysis.

**Primary Objectives:**
1. Load Qwen 2.5-7B from HuggingFace with proper authentication
2. Process all **100,000** Bengali QA examples with context-based prompting
3. Extract Chain of Thought reasoning and final answers
4. Compute Answer Accuracy (Exact Match & F1 Score)
5. Analyze performance on answerable vs unanswerable questions
6. Generate subject-wise breakdown and detailed results
7. Implement checkpointing every 1,000 samples for safety (100 checkpoints total)

## 📋 CONTEXT

### Dataset Structure
```json
{
  "context_id": "qa_bengali_history_001",
  "context": "১৯৭১ সালে বাংলাদেশ স্বাধীনতা লাভ করে...",
  "question": "বাংলাদেশ কোন সালে স্বাধীন হয়?",
  "answers": [{"text": "১৯৭১", "answer_start": 0}],
  "is_impossible": false,
  "cot": "প্রসঙ্গে উল্লেখ আছে যে ১৯৭১ সালে..."
}
```

### Model Specifications
- **Model:** Qwen/Qwen2.5-7B-Instruct
- **Architecture:** 7B parameter transformer with instruction-tuning
- **Language Support:** Multilingual (includes Bengali support)
- **Context Window:** 32,768 tokens
- **Quantization:** FP16 for GPU, FP32 for CPU

### Evaluation Metrics
1. **Exact Match (EM):** Percentage of predictions exactly matching ground truth
2. **F1 Score:** Token-level overlap (precision × recall)
3. **Answerable Accuracy:** Performance on questions with answers
4. **Unanswerable Detection:** Ability to identify "no answer" cases
5. **Subject-wise Performance:** Breakdown by topic (history, science, etc.)
6. **Inference Speed:** Average time per prediction

### Technical Requirements
- PyTorch + Transformers library
- HuggingFace authentication (token required)
- GPU support (optional, recommended)
- Memory: ~14GB GPU VRAM or 28GB RAM for CPU

## 📚 REFERENCE

### Expected Prompt Format (Bengali)
```python
prompt = f"""আপনি একজন সহায়ক সহকারী। নিচের প্রসঙ্গ এবং প্রশ্ন পড়ুন এবং উত্তর দিন।

প্রসঙ্গ: {context}

প্রশ্ন: {question}

নির্দেশনা:
1. প্রথমে আপনার চিন্তার ধারা (Chain of Thought) ব্যাখ্যা করুন
2. তারপর চূড়ান্ত উত্তর দিন
3. যদি প্রসঙ্গে উত্তর না থাকে, তাহলে "উত্তর প্রসঙ্গে নেই" বলুন

চিন্তার ধারা: """
```

### Expected Model Response Format
```
চিন্তার ধারা: প্রসঙ্গে স্পষ্টভাবে উল্লেখ আছে যে ১৯৭১ সালে বাংলাদেশ স্বাধীনতা লাভ করে। এটি একটি ঐতিহাসিক ঘটনা।

উত্তর: ১৯৭১
```

### Code Template
```python
# Authentication
from huggingface_hub import login
login(token="YOUR_HF_TOKEN")

# Load Model
from transformers import AutoTokenizer, AutoModelForCausalLM
import torch

tokenizer = AutoTokenizer.from_pretrained("Qwen/Qwen2.5-7B-Instruct")
model = AutoModelForCausalLM.from_pretrained(
    "Qwen/Qwen2.5-7B-Instruct",
    torch_dtype=torch.float16,
    device_map="auto"
)

# Generate Response
inputs = tokenizer(prompt, return_tensors="pt").to(model.device)
outputs = model.generate(
    **inputs,
    max_new_tokens=512,
    temperature=0.7,
    top_p=0.9,
    do_sample=True
)
response = tokenizer.decode(outputs[0], skip_special_tokens=True)
```

### Example Metrics Output
```
============================================================
Results for qwen:
============================================================
Overall EM:        52.34%
Overall F1:        71.28%
Answerable EM:     61.45%
Answerable F1:     78.92%
Unanswerable EM:   43.23%
Unanswerable F1:   63.64%
Avg Inference:     1.87s
============================================================
```

## ✅ EVALUATE

### Success Criteria

#### Must-Have (Critical)
- [ ] Model loads successfully without errors
- [ ] All **100,000** examples processed without crashes
- [ ] EM and F1 scores computed for every prediction
- [ ] Checkpoints saved every 1,000 samples (100 checkpoints)
- [ ] Results saved to JSON file (`results/qwen_results.json`)
- [ ] Summary statistics printed to console

#### Should-Have (Important)
- [ ] Overall EM > 40% (baseline quality check)
- [ ] Overall F1 > 60% (reasonable performance)
- [ ] Inference time < 3s per example (efficiency)
- [ ] Subject-wise breakdown included in results
- [ ] Separate metrics for answerable/unanswerable questions

#### Nice-to-Have (Optional)
- [ ] GPU utilization if available
- [ ] Progress bar with ETA (tqdm)
- [ ] Error handling for edge cases
- [ ] Checkpoint saving every 1000 examples
- [ ] Memory usage logging

### Validation Checks

```python
# Post-evaluation validation
assert len(predictions) == 100000, "Missing predictions"
assert 0 <= results['metrics']['overall_em'] <= 1, "Invalid EM score"
assert 0 <= results['metrics']['overall_f1'] <= 1, "Invalid F1 score"
assert os.path.exists('results/qwen_results.json'), "Results not saved"
assert len(os.listdir('results/checkpoints')) == 100, "Missing checkpoints"
```

### Quality Benchmarks
- **Good Performance:** EM > 50%, F1 > 70%
- **Acceptable Performance:** EM > 40%, F1 > 60%
- **Poor Performance:** EM < 30%, F1 < 50% (investigate issues)

## 🔄 ITERATE

### Iteration Strategy

#### Phase 1: Quick Test (Iteration 0)
```bash
python3 evaluate_qwen.py --dataset qa_fixed.json --max-samples 100
```
**Goal:** Verify pipeline works end-to-end
**Time:** ~5 minutes
**Expected:** No errors, reasonable output format

#### Phase 2: Small Scale Test (Iteration 1)
```bash
python3 evaluate_qwen.py --dataset qa_fixed.json --max-samples 1000
```
**Goal:** Check metric stability and performance trends
**Time:** ~30 minutes
**Expected:** Consistent EM/F1 scores, identify any issues

#### Phase 3: Medium Scale Test (Iteration 2)
```bash
python3 evaluate_qwen.py --dataset qa_fixed.json --max-samples 10000
```
**Goal:** Validate at scale, monitor memory and speed
**Time:** ~5 hours
**Expected:** Stable performance, no memory issues

#### Phase 4: Full Evaluation (Iteration 3)
```bash
python3 evaluate_qwen.py --dataset qa_fixed.json
```
**Goal:** Complete evaluation on all **100,000** examples
**Time:** ~50 hours (2+ days) with checkpointing
**Expected:** Final metrics and comprehensive results

### Improvement Checklist

If metrics are unsatisfactory, iterate by:

1. **Prompt Engineering**
   - [ ] Adjust Bengali prompt phrasing
   - [ ] Add more explicit CoT instructions
   - [ ] Experiment with few-shot examples
   - [ ] Try different instruction formats

2. **Generation Parameters**
   - [ ] Tune temperature (try 0.3-0.9)
   - [ ] Adjust top_p (try 0.85-0.95)
   - [ ] Increase max_new_tokens if answers truncated
   - [ ] Try greedy decoding (temperature=0)

3. **Post-Processing**
   - [ ] Improve answer extraction regex
   - [ ] Better handling of Bengali punctuation
   - [ ] Normalize different Bengali text representations
   - [ ] Handle edge cases (empty responses, errors)

4. **Model Configuration**
   - [ ] Try 8-bit quantization for faster inference
   - [ ] Experiment with different device_map strategies
   - [ ] Adjust batch size if memory allows
   - [ ] Enable torch.compile() for speed

### Debugging Common Issues

| Issue | Solution |
|-------|----------|
| "Authentication error" | Run `huggingface-cli login` with valid token |
| "CUDA out of memory" | Use `--max-samples 100` or CPU mode |
| "Model not found" | Verify exact name: `Qwen/Qwen2.5-7B-Instruct` |
| Low EM/F1 scores | Check prompt format, try few-shot examples |
| Slow inference | Use GPU, reduce max_tokens, try quantization |

---

## 🚀 EXECUTION COMMAND

### Step 1: Authentication Setup
```bash
# Login to HuggingFace (one-time setup)
pip install huggingface-hub
huggingface-cli login
# Paste your token from: https://huggingface.co/settings/tokens
```

### Step 2: Quick Test (Recommended First)
```bash
# Test on 100 samples (~5 minutes)
python3 evaluate_qwen.py \
  --dataset qa_fixed.json \
  --max-samples 100 \
  --output-dir results_test
```

### Step 3: Small Scale Test
```bash
# Test on 1,000 samples (~30 minutes)
python3 evaluate_qwen.py \
  --dataset qa_fixed.json \
  --max-samples 1000 \
  --output-dir results_1k
```

### Step 4: Full Evaluation (100K samples)
```bash
# Full dataset evaluation (~50 hours / 2+ days)
# IMPORTANT: Use checkpointing and run in background
nohup python3 evaluate_qwen.py \
  --dataset qa_fixed.json \
  --checkpoint-interval 1000 \
  --output-dir results \
  > evaluation.log 2>&1 &

# Monitor progress
tail -f evaluation.log
```

### Step 5: Monitor Progress (For Full Run)
```bash
# Check latest checkpoint
ls -lh results/checkpoints/

# View checkpoint metrics
cat results/checkpoints/checkpoint_10000.json | jq '.current_metrics'

# Monitor GPU/CPU usage
watch -n 5 nvidia-smi  # For GPU
htop                   # For CPU
```

### Step 6: Review Results
```bash
# View summary
cat results/qwen_results.json | jq '.metrics'

# Check predictions
cat results/qwen_results.json | jq '.predictions[0:5]'
```

---

## 📊 EXPECTED OUTPUT STRUCTURE

```
results/
├── qwen_results.json          # Full results with predictions (LARGE FILE ~500MB-1GB)
│   ├── model: "qwen"
│   ├── model_name: "Qwen/Qwen2.5-7B-Instruct"
│   ├── total_samples: 100000
│   ├── total_time_seconds: ~180000
│   ├── metrics: {...}
│   ├── subject_breakdown: {...}
│   └── predictions: [...]     # 100,000 predictions
├── checkpoints/
│   ├── checkpoint_1000.json   # After 1K samples
│   ├── checkpoint_2000.json   # After 2K samples
│   ├── ...
│   └── checkpoint_100000.json # Final checkpoint (100 checkpoints total)
└── evaluation.log             # Console output log
```

---

## 🎓 LEARNING OBJECTIVES

By following this prompt, you will:
1. ✅ Understand end-to-end LLM evaluation pipelines
2. ✅ Learn Bengali-specific NLP challenges
3. ✅ Master HuggingFace model loading and inference
4. ✅ Practice Chain of Thought prompting techniques
5. ✅ Gain experience with EM/F1 metrics
6. ✅ Develop iterative improvement strategies

---

## 💡 PRO TIPS

1. **Start Small:** Always test with `--max-samples 100` first
2. **Monitor Memory:** Use `nvidia-smi` or `htop` to watch resources
3. **Use Background Jobs:** Run full evaluation with `nohup` for 100K samples
4. **Checkpointing is Critical:** Save every 1000 samples (100 checkpoints for 100K)
5. **Log Everything:** Keep detailed logs of parameters tried
6. **Analyze Errors:** Review predictions where EM=0 to understand failure modes
7. **Estimate Time:** Test 100 samples first, then calculate: `(100K / 100) × test_time`

## 🏭 LARGE-SCALE EVALUATION (100K Samples)

### Time & Resource Estimates

| Hardware | Inference Time | Total Time | Memory |
|----------|----------------|------------|---------|
| **RTX 4090** | ~1.5s/sample | ~42 hours | 12GB VRAM |
| **A100 GPU** | ~1.0s/sample | ~28 hours | 16GB VRAM |
| **CPU (32 cores)** | ~4.0s/sample | ~111 hours | 32GB RAM |
| **8-bit Quant GPU** | ~1.0s/sample | ~28 hours | 8GB VRAM |

### Best Practices for 100K Evaluation

#### 1. **Use Background Execution**
```bash
# Run in background with logging
nohup python3 evaluate_qwen.py \
  --dataset qa_fixed.json \
  --checkpoint-interval 1000 \
  --temperature 0.7 \
  --output-dir results \
  > evaluation.log 2>&1 &

# Save the process ID
echo $! > evaluation.pid
```

#### 2. **Monitor Progress in Real-Time**
```bash
# Watch the log file
tail -f evaluation.log

# Check progress percentage
grep -c "Evaluating Qwen" results/checkpoints/*.json | tail -1

# Calculate ETA
python3 -c "
import json
with open('results/checkpoints/checkpoint_10000.json') as f:
    data = json.load(f)
    samples_done = data['num_predictions']
    # Calculate based on your timing
    print(f'Samples: {samples_done}/100000 ({samples_done/1000:.1f}%)')
"
```

#### 3. **Resume from Checkpoint** (if interrupted)
```python
# Add to evaluate_qwen.py
def load_last_checkpoint(checkpoint_dir):
    checkpoints = sorted(glob.glob(f"{checkpoint_dir}/checkpoint_*.json"))
    if checkpoints:
        with open(checkpoints[-1]) as f:
            return json.load(f)
    return None
```

#### 4. **Distributed Evaluation** (Advanced)
Split dataset into chunks:
```bash
# Split dataset into 10 chunks (10K each)
python3 split_dataset.py --input qa_fixed.json --chunks 10

# Run parallel evaluations
for i in {0..9}; do
    python3 evaluate_qwen.py \
      --dataset qa_fixed_chunk_$i.json \
      --output-dir results_chunk_$i &
done

# Merge results later
python3 merge_results.py --output results/qwen_results.json
```

#### 5. **Disk Space Requirements**
- **Dataset (qa_fixed.json):** ~200-500 MB
- **Checkpoints (100 files):** ~5-10 GB
- **Final Results:** ~500 MB - 1 GB
- **Logs:** ~50-100 MB
- **Total:** ~6-12 GB free space needed

#### 6. **Error Recovery**
```bash
# If process crashes, check last checkpoint
ls -lht results/checkpoints/ | head -5

# Manually continue from checkpoint N
python3 evaluate_qwen.py \
  --dataset qa_fixed.json \
  --resume-from-checkpoint results/checkpoints/checkpoint_45000.json \
  --output-dir results
```

---

## ⚠️ TROUBLESHOOTING GUIDE

### Issue: "Qwen model not found"
```bash
# Verify model exists
python3 -c "from transformers import AutoTokenizer; AutoTokenizer.from_pretrained('Qwen/Qwen2.5-7B-Instruct')"
```

### Issue: "Out of memory"
```python
# Use 8-bit quantization
from transformers import BitsAndBytesConfig
quantization_config = BitsAndBytesConfig(load_in_8bit=True)
model = AutoModelForCausalLM.from_pretrained(
    "Qwen/Qwen2.5-7B-Instruct",
    quantization_config=quantization_config
)
```

### Issue: "Slow inference"
```python
# Enable Flash Attention 2
model = AutoModelForCausalLM.from_pretrained(
    "Qwen/Qwen2.5-7B-Instruct",
    attn_implementation="flash_attention_2"
)
```

---

**Ready to Execute?** Run the quick test first, then scale to full evaluation! 🚀
