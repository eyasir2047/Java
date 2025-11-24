# 🎯 START HERE: Qwen 2.5-7B Evaluation Ready!

## ✅ What's Been Created

All files for evaluating Qwen 2.5-7B on your 100K Bengali QA dataset are ready!

```
Java/
├── 📘 START_HERE.md                   ← YOU ARE HERE
├── 📘 README_EVALUATION.md            ← Project overview
├── 📗 QUICKSTART_100K.md              ← Quick start commands ⭐
├── 📕 QWEN_EVALUATION_PROMPT.md       ← Full TCREI prompt
├── 📙 MODEL_SETUP.md                  ← Authentication help
├── 🐍 evaluate_qwen.py                ← Main evaluation script
├── 🐍 evaluate_models.py              ← Multi-model script (alternative)
└── 📊 qa_fixed.json                   ← YOUR DATASET (100K samples) ⚠️
```

---

## ⚠️ IMPORTANT: Dataset Location

**Expected:** Your dataset should be at `/home/user/Java/qa_fixed.json`

**Currently:** Dataset not detected in this location.

### Action Required:
```bash
# If your dataset is elsewhere, move it here:
mv /path/to/your/qa_fixed.json /home/user/Java/qa_fixed.json

# Or update the script commands to point to your dataset location
```

---

## 🚀 How to Start (3 Steps)

### Step 1: Authenticate with HuggingFace (2 minutes)
```bash
# Install CLI
pip install huggingface-hub

# Login (get token from https://huggingface.co/settings/tokens)
huggingface-cli login
```

### Step 2: Quick Test (5 minutes)
```bash
# Verify everything works with 100 samples
python3 evaluate_qwen.py --dataset qa_fixed.json --max-samples 100
```

**Expected output:**
```
Loading model: Qwen/Qwen2.5-7B-Instruct
✓ Model loaded successfully!
Evaluating Qwen...
100%|████████████████████| 100/100

Results for qwen:
Overall EM:        45.00%
Overall F1:        67.50%
Avg Inference:     1.8s
```

### Step 3: Choose Your Evaluation Scale

#### Option A: Small Test (1,000 samples = ~30 min)
```bash
python3 evaluate_qwen.py \
  --dataset qa_fixed.json \
  --max-samples 1000 \
  --output-dir results_1k
```

#### Option B: Full Evaluation (100,000 samples = ~2 days)
```bash
# Run in background with logging
nohup python3 evaluate_qwen.py \
  --dataset qa_fixed.json \
  --checkpoint-interval 1000 \
  --output-dir results \
  > evaluation.log 2>&1 &

# Save process ID
echo $! > evaluation.pid

# Monitor progress
tail -f evaluation.log
```

---

## 📚 Documentation Quick Reference

| Want to... | Read this file... | Time |
|------------|-------------------|------|
| **Start immediately** | `QUICKSTART_100K.md` | 2 min |
| **Understand TCREI format** | `QWEN_EVALUATION_PROMPT.md` | 15 min |
| **Fix auth errors** | `MODEL_SETUP.md` | 5 min |
| **Get overview** | `README_EVALUATION.md` | 10 min |

---

## 🎓 TCREI Format Summary

Your prompt follows the **TCREI methodology**:

1. **T**ask: Evaluate Qwen 2.5-7B on 100K Bengali QA with CoT
2. **C**ontext: Dataset structure, model specs, metrics explained
3. **R**eference: Code templates, prompt formats, examples
4. **E**valuate: Success criteria, validation checks, benchmarks
5. **I**terate: 4-phase approach (100 → 1K → 10K → 100K)

**Why TCREI?**
- ✅ Clear task definition
- ✅ Complete context provided
- ✅ Reference implementations included
- ✅ Success metrics defined
- ✅ Iterative testing strategy

---

## ⏱️ Time & Resource Planning

### Testing Phases

| Phase | Samples | GPU Time | CPU Time | Purpose |
|-------|---------|----------|----------|---------|
| Quick | 100 | ~5 min | ~10 min | Verify setup |
| Small | 1,000 | ~30 min | ~1 hour | Check metrics |
| Medium | 10,000 | ~5 hours | ~11 hours | Scale validation |
| **Full** | **100,000** | **~50 hours** | **~110 hours** | **Final results** |

### Requirements

- **GPU:** 12GB+ VRAM (RTX 3090/4090 or better)
  - OR use `--use-8bit` flag for 8GB GPUs
- **CPU:** 32GB+ RAM (if no GPU)
- **Disk:** ~12 GB free space
- **Network:** Internet for model download (~14GB first time)

---

## 📊 What You'll Get

### Final Output Files
```
results/
├── qwen_results.json          (~500MB-1GB)
│   ├── 100,000 predictions
│   ├── Complete metrics (EM, F1)
│   ├── Subject-wise breakdown
│   └── All raw responses
│
├── checkpoints/               (~5-10GB)
│   ├── checkpoint_1000.json
│   ├── checkpoint_2000.json
│   └── ... (100 checkpoints)
│
└── evaluation.log             (~50-100MB)
```

### Metrics Provided
- **Overall EM:** Exact match accuracy (%)
- **Overall F1:** Token overlap score (%)
- **Answerable EM/F1:** Performance on questions with answers
- **Unanswerable EM/F1:** Ability to detect "no answer"
- **Subject Breakdown:** Performance by topic (history, science, etc.)
- **Inference Time:** Speed metrics

---

## 🎯 Recommended Workflow

### For First-Time Users:
```bash
# 1. Read quick start (2 min)
cat QUICKSTART_100K.md

# 2. Setup authentication (2 min)
huggingface-cli login

# 3. Run quick test (5 min)
python3 evaluate_qwen.py --dataset qa_fixed.json --max-samples 100

# 4. If successful, run 1K test (30 min)
python3 evaluate_qwen.py --dataset qa_fixed.json --max-samples 1000
```

### For Production Run:
```bash
# 1. Verify metrics from 1K test are good (EM > 40%, F1 > 60%)

# 2. Launch full 100K evaluation in background
nohup python3 evaluate_qwen.py \
  --dataset qa_fixed.json \
  --checkpoint-interval 1000 \
  --output-dir results \
  > evaluation.log 2>&1 &

echo $! > evaluation.pid

# 3. Monitor progress
tail -f evaluation.log

# 4. Check checkpoints periodically
ls -lh results/checkpoints/ | tail -5
```

---

## 🆘 Quick Troubleshooting

### Issue: "Dataset not found"
```bash
# Check if dataset exists
ls -lh qa_fixed.json

# If missing, move your dataset here:
mv /path/to/qa_fixed.json /home/user/Java/qa_fixed.json
```

### Issue: "Model not found" or authentication error
```bash
# Login to HuggingFace
huggingface-cli login

# Paste your token from: https://huggingface.co/settings/tokens
```

### Issue: "CUDA out of memory"
```bash
# Use 8-bit quantization (reduces memory by ~50%)
python3 evaluate_qwen.py \
  --dataset qa_fixed.json \
  --max-samples 100 \
  --use-8bit
```

### Issue: "Process killed" or crashes
```bash
# Check RAM
free -h

# Check disk space (need 12GB+)
df -h .

# Use smaller test first
python3 evaluate_qwen.py --dataset qa_fixed.json --max-samples 100
```

---

## 💡 Pro Tips

1. **Always start with 100 samples** to verify setup
2. **Check metrics at 1K** before committing to 100K
3. **Use background execution** (`nohup`) for full run
4. **Monitor checkpoints** to track progress
5. **Save process ID** to stop/resume if needed
6. **Expect 2-3 days** for full 100K evaluation on GPU

---

## 🔗 Key Commands Cheat Sheet

```bash
# Authentication (one-time)
huggingface-cli login

# Quick test (5 min)
python3 evaluate_qwen.py --dataset qa_fixed.json --max-samples 100

# Small test (30 min)
python3 evaluate_qwen.py --dataset qa_fixed.json --max-samples 1000

# Full evaluation (background, 2 days)
nohup python3 evaluate_qwen.py --dataset qa_fixed.json > evaluation.log 2>&1 &
echo $! > evaluation.pid

# Monitor progress
tail -f evaluation.log

# Check results
cat results/qwen_results.json | jq '.metrics'
```

---

## ✅ Checklist Before Starting

- [ ] Dataset `qa_fixed.json` is in `/home/user/Java/`
- [ ] Python packages installed: `torch transformers tqdm`
- [ ] HuggingFace CLI installed: `pip install huggingface-hub`
- [ ] Authenticated: `huggingface-cli login`
- [ ] Read `QUICKSTART_100K.md` (2 minutes)
- [ ] 12GB+ disk space available
- [ ] GPU available (or prepared for slow CPU run)

---

## 🎯 Next Steps

### **Absolute Beginner?**
1. Open `QUICKSTART_100K.md`
2. Follow the 3-step process
3. Start with 100 samples

### **Want to Understand More?**
1. Read `QWEN_EVALUATION_PROMPT.md` for full TCREI breakdown
2. Review `evaluate_qwen.py` code
3. Check `README_EVALUATION.md` for overview

### **Ready to Run Full Evaluation?**
1. Verify dataset location: `ls -lh qa_fixed.json`
2. Run authentication: `huggingface-cli login`
3. Quick test: `python3 evaluate_qwen.py --dataset qa_fixed.json --max-samples 100`
4. Launch full run: See `QUICKSTART_100K.md` Step 3

---

## 📞 Support Resources

- **Authentication Issues:** See `MODEL_SETUP.md`
- **Detailed Commands:** See `QUICKSTART_100K.md`
- **TCREI Format:** See `QWEN_EVALUATION_PROMPT.md`
- **General Overview:** See `README_EVALUATION.md`

---

**🚀 Ready to begin? → Open `QUICKSTART_100K.md` and start with Step 1!**

Good luck with your evaluation! 🎉
