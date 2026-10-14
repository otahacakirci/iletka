# Current development status

Last verified: 2026-10-14. This is the **only live progress record**. The [roadmap](roadmap.md) defines planned milestones; code and actual test results establish implementation. Keep this file short and replace outdated observations instead of appending a session transcript.

## Stage and active task

Stage: **bootstrap ready; month 1 domain work has started with the EventType value**.

| Stage | State |
| --- | --- |
| Bootstrap | Implemented and locally verified |
| Month 1 | In progress: EventType value done on a feature branch; the rest of month 1 is not started |
| Months 2-7 | Not started; planned gates in the roadmap |

Completed task: **[EventType validation](roadmap.md#first-development-task---eventtype-validation)** — an immutable `com.iletka.event.EventType` record with `InvalidEventTypeException`, under the stricter [owner-approved policy](project-contract.md#domain-decision-eventtype-validation). Only this task is recorded as done; the work is uncommitted on `feature/event-type-validation`.

Next small task: **not started** — month 1 continues with a small Spring-independent exercise from the [roadmap](roadmap.md) (for example a simple state rule or error type). Month 1's exit gate is not yet met. Agree its scope first; no endpoint, storage or delivery implementation belongs to it.

## Implemented and checked

- One Maven module, Java 21 target, minimal non-web Spring Boot application.
- Maven Wrapper, application name configuration and one context smoke test.
- Short Claude memory, two path-scoped rules, normal permission mode and narrow publication denials.
- PDF-derived contract, architecture/ADR, reliability/security plan, roadmap and local development instructions.
- `EventType` (record, Spring-independent) and `InvalidEventTypeException` (extends `IllegalArgumentException`). Rules apply at construction; accepted text is stored exactly; equality uses that text. 138 behavior-focused unit tests in `EventTypeTest`, including invisible/control Unicode, trailing line breaks, long separator runs and the 127/128/129 boundary.
- A GitHub verification workflow file exists. Remote CI execution has not been checked.
- Git (checked locally on 2026-10-13): two commits on `main` (`63504a3`, `6df5ba8`); local `origin/main` points at the same commit; remote `origin` is `https://github.com/otahacakirci/iletka.git`; work is on `feature/event-type-validation`. The remote's live state and CI results were not queried, so GitHub CI success is unverified.
- Product identity is now Iletka; see ADR 0002. Scope and the first domain task are unchanged.

## Latest verification evidence

Environment: Windows 11; Eclipse Temurin Java 21.0.12.1; Maven 3.9.16; Spring Boot 4.1.1. The wrapper and Maven artifacts were locally cached. No external services were started.

| Check | Actual result |
| --- | --- |
| `.\mvnw.cmd --version` with JDK 21 selected for the session | Maven 3.9.16 / Java 21.0.12.1 (the machine's default `java` is 25) |
| `.\mvnw.cmd --batch-mode --no-transfer-progress verify` (2026-10-14, after adding the literal-backslash test case) | BUILD SUCCESS; 139 tests (138 `EventTypeTest`, 1 context smoke), 0 failures/errors/skipped; executable jar built |
| Earlier bootstrap checks (2026-10-12): jar start, wrapper checksum failure path, static validation of settings/workflow | Passed then; not repeated for this task. No live Claude session or GitHub run |
| `claude --version` (2026-10-12) | Installed CLI reported 2.1.294 |

The 2026-10-12 bootstrap run used a relative Maven-cache override in a constrained preparation environment; the 2026-10-14 run used the normal user cache. Use the canonical commands in [development](development.md); an uncached first build requires approved network access.

## Limits and blockers

- Only the EventType value exists; no REST API, database schema, subscription matching, RabbitMQ, outbox relay, worker, retry, authentication or outbound HTTP.
- One smoke test proves context startup only. No persistence, broker or security guarantee is implemented.
- Fresh network bootstrap, macOS/Linux execution, GitHub Actions execution and live Claude instruction loading were not verified.
- The initial test attempt exposed unused Mockito agent attachment under restricted Windows execution. Mockito dependencies were excluded; the final build/test passed without them. Reintroduce mocking only with a demonstrated test need.
- Unicode surrogate pairs are rejected per UTF-16 unit (the message cites the first unit). No known blocker for further pure-Java domain work; infrastructure and outbound-security gates remain future work.

## Pending owner decision

Project license: not selected. MIT is recommended for a small portfolio project; Apache-2.0 is an alternative with explicit patent terms. No project LICENSE text or POM license metadata has been added. The separate wrapper license is retained.

## Handoff rule

After a substantive task, update: stage/active task, the implemented behavior, the last real verification, unresolved issues and the next small step. Keep milestone specifications in the roadmap and material design decisions in ADRs. Never mark planned work complete from documentation alone.
