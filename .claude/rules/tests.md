---
paths:
  - "src/test/**/*.java"
---

# Tests

- Test observable rules, boundaries and failure behavior rather than private implementation details.
- Use plain unit tests for domain values; reserve Spring context tests for framework wiring.
- Use descriptive names and deterministic assertions; avoid timing sleeps and unnecessary mocks.
- Cover valid input and meaningful invalid/boundary cases for each new domain rule.
- Keep the bootstrap smoke test's claim limited to context startup.
- Introduce PostgreSQL/RabbitMQ/Testcontainers tests when their corresponding features exist.
- Do not substitute mocks for evidence of database constraints, atomicity or broker semantics.
- Use controlled HTTP receivers for future retry/security tests; do not call public targets.
- Run the relevant checks from `docs/development.md` and report their real results.
- Do not disable tests or loosen assertions simply to obtain a passing build.
