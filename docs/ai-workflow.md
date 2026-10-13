# Working with Claude Code

This repository uses a short root `CLAUDE.md` for stable guidance and two path-scoped rules: production Java and test Java. Detailed product documents remain ordinary references, without automatic imports. Actual progress belongs only in [status.md](status.md); commands belong in [development.md](development.md); planned milestones belong in [roadmap.md](roadmap.md).

## One learning task at a time

1. Start at the repository root. Ask Claude to read status, the relevant contract/roadmap section and the code it will change.
2. Define one observable result and its boundaries. Use plan mode for uncertain, larger changes; a small clear task can proceed directly.
3. Ask for a short explanation, implementation and appropriate tests. Keep later REST, persistence and messaging work outside a domain-only task.
4. Have Claude run the documented verification and inspect the diff. Read the evidence and understand the changed rule before accepting a commit.
5. Update status with completed work, actual checks, limitations and the next small task. Start a fresh conversation when switching topics if the current context is crowded.

Example task prompt:

> Read the current status and the EventType task in the roadmap. Implement only that immutable Java value and its unit tests. Explain the validation choice in Turkish, run the documented checks and report the result. Do not add REST, storage or delivery code.

Example review prompt:

> Review this diff against the active task and project contract. Look for untested boundaries, hidden behavior changes and planned features described as implemented. Give actionable findings; do not expand the task.

## Permission behavior

The committed settings select `default` mode, with no allowlist or automatic acceptance. Normal prompts remain subject to Claude Code's effective user, local and managed configuration. Check `/permissions` and `/status` in the installed client rather than assuming one repository file is the whole policy.

The deny entries cover direct Bash/PowerShell `git push` and `gh repo create` commands. They are narrow guardrails, not a complete publication barrier: alternate command forms, wrappers or other tools may not match. Do not bypass a denial. If publication is later explicitly authorized, review it separately and let the owner perform the blocked operation or deliberately revise the policy. Never use a bypass flag to finish a task.

## Why there are no extra extensions

- No skill: the initial workflow is short and has no repeated specialized procedure yet.
- No hook: verification is an explicit task step; there is no established lifecycle automation to enforce.
- No custom agent: the starter does not need a persistent specialist configuration.
- No MCP server: local files and build tools cover the work; no external integration is required.

Add an extension only when a repeated task or concrete access need justifies its maintenance. Instructions guide behavior; they do not replace permissions, tests or review.

## Official references

Configuration syntax and workflow were checked against the official documentation on 12 October 2026:

- [Project memory and path-scoped rules](https://code.claude.com/docs/en/memory)
- [Settings and configuration precedence](https://code.claude.com/docs/en/settings)
- [Permission modes and command matching](https://code.claude.com/docs/en/permissions)
- [Choosing extensions](https://code.claude.com/docs/en/features-overview)
- [Verification and focused workflows](https://code.claude.com/docs/en/best-practices)
