# Model Setup Guide for Bengali QA Evaluation

## Fixed Model Names

The script has been updated with valid HuggingFace model identifiers:

| Model Key | Fixed Name | Status |
|-----------|------------|--------|
| `qwen` | `Qwen/Qwen2.5-7B-Instruct` | ✅ Public (may need auth) |
| `banglagpt` | `BanglaLLM/bangla-llama-7b-instruct-v0.1` | ✅ Public |
| `llama` | `meta-llama/Meta-Llama-3-8B-Instruct` | 🔒 Gated (requires auth) |
| `gpt2-bengali` | `flax-community/gpt2-bengali` | ✅ Public |

## Authentication Setup

### Step 1: Get HuggingFace Token

1. Go to https://huggingface.co/settings/tokens
2. Create a new token with "read" access
3. Copy the token

### Step 2: Login via CLI

```bash
# Method 1: Interactive login
huggingface-cli login

# Method 2: Direct token
huggingface-cli login --token YOUR_TOKEN_HERE
```

### Step 3: Request Access to Gated Models

For **Meta Llama** models:
1. Visit: https://huggingface.co/meta-llama/Meta-Llama-3-8B-Instruct
2. Click "Request Access"
3. Accept the license agreement
4. Wait for approval (usually instant)

## Quick Test

Test with a single model and limited samples:

```bash
# Test Bengali model (no auth needed)
python3 evaluate_models.py --dataset qa_5000_test.json --models banglagpt --max-samples 10

# Test GPT-2 Bengali (lightweight, no auth)
python3 evaluate_models.py --dataset qa_5000_test.json --models gpt2-bengali --max-samples 10
```

## Alternative Bengali Models

If you encounter issues, try these alternatives:

```python
# Add to MODELS dict in evaluate_models.py

"bangla-llama": {
    "name": "asif00/bangla-llama",  # LLaMA 3 8B trained on Bengali
    "type": "instruct"
},
"bangla-alpaca": {
    "name": "iamshnoo/alpaca-2-13b-bengali",  # 13B Bengali Alpaca
    "type": "alpaca"
},
"bangla-llama-13b": {
    "name": "BanglaLLM/bangla-llama-13b-instruct-v0.1",  # Larger BanglaLLM
    "type": "instruct"
}
```

## Troubleshooting

### Error: "not a valid model identifier"
- Check internet connection
- Verify model name spelling
- Try logging in with `huggingface-cli login`

### Error: "gated repo" or "authentication"
- Request access to the model on HuggingFace
- Run `huggingface-cli login`
- Wait for access approval

### Error: "CUDA out of memory"
- Use `--max-samples 100` to test on fewer examples
- Try smaller models like `gpt2-bengali`
- Add quantization (requires code changes)

### Error: "token" or "connection"
- Check internet connection
- Retry: `pip install --upgrade huggingface-hub`
- Set env variable: `export HF_TOKEN=your_token`

## Running Full Evaluation

```bash
# All models (requires auth for Llama)
python3 evaluate_models.py --dataset qa_5000_test.json --models all

# Specific models only
python3 evaluate_models.py --dataset qa_5000_test.json --models banglagpt qwen

# Public models only (no auth required)
python3 evaluate_models.py --dataset qa_5000_test.json --models banglagpt gpt2-bengali
```

## Environment Setup

```bash
# Install dependencies
pip install torch transformers tqdm accelerate

# For GPU support (optional)
pip install torch torchvision torchaudio --index-url https://download.pytorch.org/whl/cu118

# Login to HuggingFace
pip install huggingface-hub
huggingface-cli login
```

## Expected Output

```
Evaluating models: ['banglagpt']
Dataset: qa_5000_test.json
Output: results

Loading model: BanglaLLM/bangla-llama-7b-instruct-v0.1
Loading dataset: qa_5000_test.json
Evaluating banglagpt...
100%|████████████████| 5000/5000

============================================================
Results for banglagpt:
============================================================
Overall EM:        45.23%
Overall F1:        67.89%
Answerable EM:     52.10%
Answerable F1:     72.34%
Unanswerable EM:   38.50%
Unanswerable F1:   63.21%
Avg Inference:     1.23s
============================================================
```

## Notes

- **Qwen 2.5** is multilingual and should handle Bengali well
- **BanglaLLM** is specifically trained for Bengali
- **GPT-2 Bengali** is lightweight but may have lower accuracy
- **Meta Llama 3** requires license acceptance but performs well on multilingual tasks
