import json
import sys
from pathlib import Path

sys.stdout.reconfigure(encoding="utf-8", errors="replace")

transcript = Path(
    r"C:\Users\aruna\.cursor\projects\c-Users-aruna-OneDrive-Documents-NetBeansProjects-Thalam\agent-transcripts\b21eae3a-b89e-4508-bd58-66bf6cd6e06d\b21eae3a-b89e-4508-bd58-66bf6cd6e06d.jsonl"
)
early = Path(
    r"C:\Users\aruna\.cursor\projects\c-Users-aruna-OneDrive-Documents-NetBeansProjects-Thalam\agent-transcripts\07df8a0b-581a-4cb1-92e0-f7c4d6ecbf01\07df8a0b-581a-4cb1-92e0-f7c4d6ecbf01.jsonl"
)

out_dir = Path(r"C:\Users\aruna\OneDrive\Documents\NetBeansProjects\Thalam\_restore_dump")
out_dir.mkdir(exist_ok=True)


def extract_writes_and_replaces(path, fname):
    ops = []
    with open(path, "r", encoding="utf-8", errors="replace") as f:
        for i, line in enumerate(f, 1):
            if fname not in line:
                continue
            try:
                obj = json.loads(line)
            except Exception:
                continue
            content = obj.get("message", {}).get("content", [])
            if not isinstance(content, list):
                continue
            for bi, block in enumerate(content):
                if not isinstance(block, dict) or block.get("type") != "tool_use":
                    continue
                name = block.get("name")
                inp = block.get("input") or {}
                p = (inp.get("path") or "").replace("\\", "/")
                if fname not in p:
                    continue
                if name == "Write" and "contents" in inp:
                    ops.append((i, bi, "Write", inp))
                elif name == "StrReplace" and "old_string" in inp:
                    ops.append((i, bi, "StrReplace", inp))
    return ops


# Dump key fragments
ops = extract_writes_and_replaces(transcript, "Reports.fxml")
for o in ops:
    i, bi, kind, inp = o
    if kind == "Write":
        (out_dir / f"write_{i}.fxml").write_text(inp["contents"], encoding="utf-8")
        print("wrote", f"write_{i}.fxml", len(inp["contents"]))
    else:
        (out_dir / f"sr_{i}_{bi}_old.txt").write_text(inp["old_string"], encoding="utf-8")
        (out_dir / f"sr_{i}_{bi}_new.txt").write_text(inp["new_string"], encoding="utf-8")
        print("wrote", f"sr_{i}_{bi}", "old", len(inp["old_string"]), "new", len(inp["new_string"]))

early_ops = extract_writes_and_replaces(early, "Reports.fxml")
for o in early_ops:
    i, bi, kind, inp = o
    if kind == "Write":
        (out_dir / f"early_write_{i}.fxml").write_text(inp["contents"], encoding="utf-8")
        print("early write", i, len(inp["contents"]))

# Also dump write 890 and compare to 1407 old - does 1407 old contain write890?
w890 = (out_dir / "write_890.fxml").read_text(encoding="utf-8")
s1407 = (out_dir / "sr_1407_1_old.txt").read_text(encoding="utf-8") if (out_dir / "sr_1407_1_old.txt").exists() else None
# find correct bi
for p in out_dir.glob("sr_1407_*_old.txt"):
    s1407 = p.read_text(encoding="utf-8")
    print("1407 file", p.name, "contains write890 snippet?", w890[200:400] in s1407)
    print("write890 has FlowPane?", "FlowPane" in w890)
    print("write890 len", len(w890))
    print("--- write890 ---")
    print(w890)
