---
paths:
  - "src/main/java/**/*.java"
---

# Java production code

- Use four-space Java indentation and explicit constructor injection instead of field injection.
- Prefer immutable domain values; use records only when their construction and invariants stay clear.
- Keep validation failure behavior explicit. Do not silently trim/lowercase event types.
- Avoid Lombok/code generation for this learning baseline and avoid broad exception swallowing.
- Constructors and application startup must not perform remote calls or create infrastructure implicitly.
