---
name: commit-message
description: Use when the user asks to generate/suggest a commit message, propose commit names, or asks "jak nazwać commit" / "podaj nazwę commita" / "commit message" — even without being told the exact format, since this skill defines the user's preferred output shape.
version: 1.0.0
---

# Commit message generator

Generates commit message proposals for the user's currently pending changes (staged and/or unstaged), following a fixed personal format the user has established. Do not guess a different format — this skill's format is the standing preference.

## What to do

1. Inspect what actually changed: `git status --porcelain` and `git diff` / `git diff --staged` (and `git diff --staged --stat` for a quick overview if the diff is large). If there are both staged and unstaged changes, consider the full set of pending changes unless the user specifies otherwise.
2. Understand the change at the level of *what happened in the logic/project/app* — not just which files were touched. A docs-only change (e.g. new `CLAUDE.md` files) should read as a docs change, not be inflated into a feature description.
3. Produce **exactly 3** commit message proposals as a numbered list.

## Format rules (non-negotiable, this is the user's fixed preference)

- English only.
- Exactly 3 proposals per request.
- Each message starts with a **capital letter**.
- Short and concise — aim for a single line, roughly under ~70 characters where possible. No body/footer unless the user asks for one.
- Imperative mood (e.g. "Add", "Fix", "Refactor"), not past tense ("Added") or gerund ("Adding").
- Describe *what changed in the logic/project/app*, not mechanical file operations (avoid things like "Update files" or "Edit CLAUDE.md").
- No conventional-commit prefixes (`feat:`, `docs:`, etc.) unless the user asks for that convention explicitly for this repo.

## What NOT to do

- Don't run `git commit` yourself — this skill only proposes messages. Only commit if the user separately asks you to, following the standard git safety rules (review staged files first, never commit secrets, never use `--amend`/`--no-verify` without being asked).
- Don't pad a single small change into multiple unrelated-sounding proposals just to fill 3 slots — 3 valid phrasings of the same real change is correct and expected.
