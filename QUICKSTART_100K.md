# Quick Start Guide: Qwen 2.5-7B on 100K Bengali QA Dataset

## 📋 Dataset Info
- **File:** `qa_fixed.json`
- **Size:** 100,000 samples (1 lakh)
- **Model:** Qwen/Qwen2.5-7B-Instruct

---

## 🚀 Three-Step Launch

### ✅ Step 1: Authentication (One-time)
```bash
# Install HuggingFace CLI
pip install huggingface-hub

# Login (paste token from https://huggingface.co/settings/tokens)
huggingface-cli login
```

### ✅ Step 2: Quick Test (5 minutes)
```bash
# Test on 100 samples to verify everything works
python3 evaluate_qwen.py \
  --dataset qa_fixed.json \
  --max-samples 100 \
  --output-dir test_results

# Expected output:
# Overall EM: ~XX%
# Overall F1: ~XX%
# Time: ~5 minutes
```

### ✅ Step 3: Choose Your Evaluation Scale

#### Option A: Small Test (1K samples, ~30 min)
```bash
python3 evaluate_qwen.py \
  --dataset qa_fixed.json \
  --max-samples 1000 \
  --checkpoint-interval 200 \
  --output-dir results_1k
```

#### Option B: Medium Test (10K samples, ~5 hours)
```bash
python3 evaluate_qwen.py \
  --dataset qa_fixed.json \
  --max-samples 10000 \
  --checkpoint-interval 1000 \
  --output-dir results_10k
```

#### Option C: Full Evaluation (100K samples, ~2 days) ⚠️
```bash
# RECOMMENDED: Run in background with logging
nohup python3 evaluate_qwen.py \
  --dataset qa_fixed.json \
  --checkpoint-interval 1000 \
  --output-dir results \
  --temperature 0.7 \
  --top-p 0.9 \
  > evaluation.log 2>&1 &

# Save process ID for later
echo $! > evaluation.pid

# Monitor progress
tail -f evaluation.log
```

---

## 📊 Monitoring Your 100K Run

### Check Progress
```bash
# View live log
tail -f evaluation.log

# Count checkpoints completed
ls results/checkpoints/ | wc -l
# Output: N/100 checkpoints done = N% complete

# Check GPU usage (if using GPU)
nvidia-smi

# Check CPU/Memory usage
htop
```

### Estimate Time Remaining
```bash
# After 1000 samples, calculate ETA
python3 << 'EOF'
import json
with open('results/checkpoints/checkpoint_1000.json') as f:
    data = json.load(f)
# Assuming checkpoint shows current metrics
# Check your evaluation.log for actual timing
print("Check evaluation.log for timing per sample")
print("Multiply by 100 to get total time for 100K samples")
EOF
```

### Stop/Resume
```bash
# Stop the evaluation
kill $(cat evaluation.pid)

# Check last checkpoint
ls -lht results/checkpoints/ | head -3

# The script auto-saves checkpoints, resume by re-running
# (Note: Current version restarts, see advanced resume below)
```

---

## 🎯 Expected Results Format

After completion, you'll have:

```
results/
├── qwen_results.json          (500MB-1GB) - Full results
├── checkpoints/
│   ├── checkpoint_1000.json
│   ├── checkpoint_2000.json
│   ├── ...
│   └── checkpoint_100000.json (100 files total)
└── evaluation.log             (50-100MB) - Console output
```

### View Results
```bash
# Summary metrics
cat results/qwen_results.json | jq '.metrics'

# Sample predictions
cat results/qwen_results.json | jq '.predictions[0:3]'

# Subject breakdown
cat results/qwen_results.json | jq '.subject_breakdown'

# Total evaluation time
cat results/qwen_results.json | jq '.total_time_seconds'
```

---

## ⏱️ Time Estimates

| Test Size | Samples | Time (GPU) | Time (CPU) | Checkpoints |
|-----------|---------|------------|------------|-------------|
| Quick     | 100     | ~5 min     | ~10 min    | 0           |
| Small     | 1,000   | ~30 min    | ~1 hour    | 5           |
| Medium    | 10,000  | ~5 hours   | ~11 hours  | 10          |
| **Full**  | **100,000** | **~50 hours** | **~110 hours** | **100** |

**Note:** Times assume ~1.8s/sample on GPU (RTX 3090/4090), ~4s/sample on CPU

---

## 🔧 Optimization Options

### Faster Inference (8-bit Quantization)
```bash
python3 evaluate_qwen.py \
  --dataset qa_fixed.json \
  --max-samples 100 \
  --use-8bit  # Reduces memory, may improve speed
```

### Tuning Generation Parameters
```bash
# More deterministic (lower temperature)
python3 evaluate_qwen.py \
  --dataset qa_fixed.json \
  --max-samples 100 \
  --temperature 0.3 \
  --top-p 0.95

# More creative (higher temperature)
python3 evaluate_qwen.py \
  --dataset qa_fixed.json \
  --max-samples 100 \
  --temperature 0.9 \
  --top-p 0.85
```

---

## 🆘 Troubleshooting

### Problem: "Model not found"
```bash
# Verify authentication
huggingface-cli whoami

# Re-login
huggingface-cli login
```

### Problem: "CUDA out of memory"
```bash
# Option 1: Use 8-bit quantization
python3 evaluate_qwen.py --dataset qa_fixed.json --use-8bit

# Option 2: Use CPU (slower)
export CUDA_VISIBLE_DEVICES=""
python3 evaluate_qwen.py --dataset qa_fixed.json
```

### Problem: "Killed" or process dies
```bash
# Check system memory
free -h

# Check disk space (need ~12GB)
df -h

# Reduce batch size or use 8-bit
python3 evaluate_qwen.py --dataset qa_fixed.json --use-8bit
```

### Problem: Very slow inference
```bash
# Check if GPU is being used
python3 -c "import torch; print(torch.cuda.is_available())"

# If False, install CUDA-enabled PyTorch
pip3 install torch torchvision --index-url https://download.pytorch.org/whl/cu118
```

---

## 📈 Interpreting Results

### Good Performance Indicators
- ✅ Overall EM > 45%
- ✅ Overall F1 > 65%
- ✅ Answerable F1 > 70%
- ✅ Unanswerable EM > 40%

### Sample Output
```
============================================================
Results for Qwen 2.5-7B:
============================================================
Overall EM:        47.23%
Overall F1:        68.89%
─────────────────────────────────────────────────────────
Answerable EM:     54.10%
Answerable F1:     74.34%
─────────────────────────────────────────────────────────
Unanswerable EM:   41.50%
Unanswerable F1:   63.21%
─────────────────────────────────────────────────────────
Avg Inference:     1.82s
Total Time:        50.5 hours
============================================================
```

---

## 🎓 TCREI Workflow Recap

| Phase | Command | Time | Goal |
|-------|---------|------|------|
| **T**ask | Read QWEN_EVALUATION_PROMPT.md | - | Understand objectives |
| **C**ontext | Check qa_fixed.json | - | Verify dataset |
| **R**eference | Review prompt templates | - | Understand format |
| **E**valuate | `--max-samples 100` | 5 min | Quick test |
| **I**terate 1 | `--max-samples 1000` | 30 min | Validate metrics |
| **I**terate 2 | `--max-samples 10000` | 5 hrs | Scale test |
| **I**terate 3 | Full 100K | 50 hrs | Final evaluation |

---

## 🚀 Ready to Start?

**Recommended path:**

```bash
# 1. Test authentication and pipeline (5 min)
python3 evaluate_qwen.py --dataset qa_fixed.json --max-samples 100

# 2. If successful, run 1K test (30 min)
python3 evaluate_qwen.py --dataset qa_fixed.json --max-samples 1000

# 3. If metrics look good, launch full 100K (background, 2 days)
nohup python3 evaluate_qwen.py \
  --dataset qa_fixed.json \
  --checkpoint-interval 1000 \
  --output-dir results \
  > evaluation.log 2>&1 &

echo $! > evaluation.pid
echo "Evaluation started! Monitor with: tail -f evaluation.log"
```

---

**Questions?** Check `QWEN_EVALUATION_PROMPT.md` for detailed TCREI format and troubleshooting!
