# Qwen 2.5-7B Evaluation on 100K Bengali QA Dataset

Complete evaluation pipeline for testing Qwen 2.5-7B Instruct model on large-scale Bengali Question-Answering with Chain of Thought reasoning.

---

## 📁 Files Overview

| File | Purpose | Size |
|------|---------|------|
| `qa_fixed.json` | Bengali QA dataset | 100,000 samples |
| `evaluate_qwen.py` | Main evaluation script | Optimized for Qwen |
| `QWEN_EVALUATION_PROMPT.md` | Full TCREI prompt | Detailed guide |
| `QUICKSTART_100K.md` | Quick start commands | **START HERE** ⭐ |
| `MODEL_SETUP.md` | Model authentication guide | Setup help |

---

## 🚀 Quick Start (3 Commands)

```bash
# 1. Login to HuggingFace
huggingface-cli login

# 2. Quick test (5 minutes)
python3 evaluate_qwen.py --dataset qa_fixed.json --max-samples 100

# 3. Full evaluation (2 days, run in background)
nohup python3 evaluate_qwen.py --dataset qa_fixed.json > evaluation.log 2>&1 &
```

**For detailed instructions, see: `QUICKSTART_100K.md`**

---

## 📊 What This Does

1. **Loads** Qwen 2.5-7B (7 billion parameter) model from HuggingFace
2. **Evaluates** on 100,000 Bengali question-answering examples
3. **Extracts** Chain of Thought reasoning + final answers
4. **Computes** metrics:
   - Exact Match (EM): % exactly correct
   - F1 Score: Token overlap score
   - Subject-wise breakdown
   - Answerable vs unanswerable performance
5. **Saves** results with 100 checkpoints for safety

---

## ⏱️ Time Estimates

| Scale | Samples | Time (GPU) | When to Use |
|-------|---------|------------|-------------|
| Quick Test | 100 | ~5 min | Verify setup works |
| Small Test | 1,000 | ~30 min | Check metrics quality |
| Medium Test | 10,000 | ~5 hours | Validate at scale |
| **Full Run** | **100,000** | **~50 hours** | **Final evaluation** |

**Hardware:** Based on RTX 4090 GPU. CPU will be ~2.5x slower.

---

## 📋 Requirements

### Python Packages
```bash
pip install torch transformers tqdm accelerate huggingface-hub
```

### HuggingFace Account
- Sign up: https://huggingface.co/join
- Get token: https://huggingface.co/settings/tokens
- Login: `huggingface-cli login`

### Disk Space
- ~12 GB free space for results and checkpoints

### Memory
- **GPU:** 12GB+ VRAM (or use `--use-8bit` for 8GB)
- **CPU:** 32GB+ RAM

---

## 🎯 Expected Results

After full evaluation, you'll get:

```json
{
  "model": "qwen",
  "total_samples": 100000,
  "metrics": {
    "overall_em": 0.47,      // 47% exactly correct
    "overall_f1": 0.69,      // 69% token overlap
    "answerable_em": 0.54,   // 54% on answerable questions
    "answerable_f1": 0.74,   // 74% F1 on answerable
    "unanswerable_em": 0.41, // 41% on unanswerable
    "avg_inference_time": 1.8 // seconds per question
  },
  "subject_breakdown": {
    "history": {"em": 0.52, "f1": 0.71, "count": 15234},
    "science": {"em": 0.45, "f1": 0.68, "count": 12456},
    ...
  },
  "predictions": [...]  // All 100K predictions
}
```

---

## 🗂️ Output Structure

```
results/
├── qwen_results.json          # Main results file (~500MB-1GB)
├── checkpoints/               # Safety checkpoints
│   ├── checkpoint_1000.json   # After 1K samples
│   ├── checkpoint_2000.json   # After 2K samples
│   └── ...                    # 100 checkpoints total
└── evaluation.log             # Console output log
```

---

## 📖 Documentation Guide

### For Beginners
1. Start with **`QUICKSTART_100K.md`** - Copy-paste commands
2. Read **`MODEL_SETUP.md`** - If authentication fails

### For Understanding
1. Read **`QWEN_EVALUATION_PROMPT.md`** - Full TCREI breakdown
2. Study **`evaluate_qwen.py`** - Implementation details

### For Troubleshooting
1. Check **`MODEL_SETUP.md`** - Common errors
2. See **`QUICKSTART_100K.md`** - Troubleshooting section

---

## 🔄 Recommended Workflow (TCREI)

### Phase 1: Quick Validation (5 min)
```bash
python3 evaluate_qwen.py --dataset qa_fixed.json --max-samples 100
```
**Goal:** Ensure pipeline works end-to-end

### Phase 2: Metric Check (30 min)
```bash
python3 evaluate_qwen.py --dataset qa_fixed.json --max-samples 1000
```
**Goal:** Verify metrics are reasonable (EM > 40%, F1 > 60%)

### Phase 3: Scale Test (5 hours)
```bash
python3 evaluate_qwen.py --dataset qa_fixed.json --max-samples 10000
```
**Goal:** Confirm stability at scale, no memory issues

### Phase 4: Full Evaluation (2 days)
```bash
nohup python3 evaluate_qwen.py \
  --dataset qa_fixed.json \
  --checkpoint-interval 1000 \
  --output-dir results \
  > evaluation.log 2>&1 &

echo $! > evaluation.pid
```
**Goal:** Complete 100K evaluation with checkpointing

---

## 🎓 TCREI Format Explained

| Letter | Meaning | In This Project |
|--------|---------|-----------------|
| **T** | Task | Evaluate Qwen on 100K Bengali QA |
| **C** | Context | Dataset structure, metrics, model specs |
| **R** | Reference | Prompt templates, code examples |
| **E** | Evaluate | Success criteria, validation checks |
| **I** | Iterate | 4 phases: 100 → 1K → 10K → 100K |

**Full details:** See `QWEN_EVALUATION_PROMPT.md`

---

## 💡 Key Features

✅ **Checkpointing:** Auto-save every 1,000 samples
✅ **Bengali-optimized:** Prompts in Bengali with CoT
✅ **Comprehensive metrics:** EM, F1, subject breakdown
✅ **GPU/CPU support:** Automatic device detection
✅ **8-bit quantization:** Optional memory optimization
✅ **Background execution:** Run for days with `nohup`
✅ **Progress monitoring:** Real-time log tracking

---

## 🆘 Quick Troubleshooting

| Problem | Solution |
|---------|----------|
| Model not found | Run `huggingface-cli login` |
| CUDA out of memory | Use `--use-8bit` flag |
| Process killed | Check RAM with `free -h` |
| Very slow | Verify GPU: `nvidia-smi` |
| Authentication error | Get token from HF settings |

**Detailed help:** See `QUICKSTART_100K.md` troubleshooting section

---

## 📊 Benchmark Expectations

Based on similar evaluations:

- **Good Performance:** EM > 50%, F1 > 70%
- **Acceptable:** EM > 40%, F1 > 60%
- **Needs Improvement:** EM < 40%, F1 < 60%

Qwen 2.5-7B is multilingual and should perform well on Bengali.

---

## 🔗 Useful Links

- **Qwen Model:** https://huggingface.co/Qwen/Qwen2.5-7B-Instruct
- **HuggingFace Tokens:** https://huggingface.co/settings/tokens
- **Transformers Docs:** https://huggingface.co/docs/transformers

---

## 📝 Citation

If you use this evaluation pipeline:

```bibtex
@software{qwen_bengali_eval_2024,
  title={Qwen 2.5-7B Bengali QA Evaluation Pipeline},
  author={Your Name},
  year={2024},
  note={Large-scale evaluation on 100K Bengali QA dataset}
}
```

---

## 🎯 Next Steps

1. ✅ **Read:** `QUICKSTART_100K.md` (5 min)
2. ✅ **Setup:** `huggingface-cli login` (2 min)
3. ✅ **Test:** Run 100 samples (5 min)
4. ✅ **Evaluate:** Run 1K samples (30 min)
5. ✅ **Deploy:** Launch full 100K run (2 days)
6. ✅ **Analyze:** Review results and metrics

---

**Ready to start?** → Open `QUICKSTART_100K.md` and follow Step 1! 🚀
