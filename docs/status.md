# Current development status

Last verified: 2026-10-12. This is the **only live progress record**. The [roadmap](roadmap.md) defines planned milestones; code and actual test results establish implementation. Keep this file short and replace outdated observations instead of appending a session transcript.

## Stage and active task

Stage: **bootstrap ready; month 1 product/domain work has not started**.

| Stage | State |
| --- | --- |
| Bootstrap | Implemented and locally verified |
| Month 1 | Not started; next task below |
| Months 2-7 | Not started; planned gates in the roadmap |

Active task: **not started** — [EventType validation](roadmap.md#first-development-task---eventtype-validation). First explain the proposed validation policy; implement only the immutable value and focused unit tests. No endpoint, storage or delivery implementation belongs to that task.

## Implemented and checked

- One Maven module, Java 21 target, minimal non-web Spring Boot application.
- Maven Wrapper, application name configuration and one context smoke test.
- Short Claude memory, two path-scoped rules, normal permission mode and narrow publication denials.
- PDF-derived contract, architecture/ADR, reliability/security plan, roadmap and local development instructions.
- A GitHub verification workflow file; remote execution is not yet verified.
- The delivered folder is initialized on local branch `main`, with the starter files staged for review. No commits or remote exist; the ZIP excludes Git metadata.
- Product identity is now Iletka; see ADR 0002. Scope and the first domain task are unchanged.

## Latest verification evidence

Environment: Windows 11; Eclipse Temurin Java 21.0.12.1; Maven 3.9.16; Spring Boot 4.1.1. The wrapper and Maven artifacts were locally cached. No external services were started.

| Check | Actual result |
| --- | --- |
| `.\mvnw.cmd --version` with JDK 21 selected | Maven 3.9.16 / Java 21.0.12.1 |
| `.\mvnw.cmd --batch-mode --no-transfer-progress --offline "-Dmaven.repo.local=../../work/maven-repository" clean verify` | Exit 0, BUILD SUCCESS after rename; 1 test, 0 failures/errors/skipped; Iletka executable jar created |
| `java -jar target/iletka-0.0.1-SNAPSHOT.jar` with JDK 21 selected | Exit 0; Spring context started and process ended normally |
| Isolated Windows wrapper bootstrap, verified local ZIP and a fresh work cache | Exit 0; Maven 3.9.16 installed; no network download |
| Isolated Windows wrapper bootstrap, one-byte-corrupted ZIP and the same trusted checksum | Exit 1; checksum failure before extraction; no Maven executable installed |
| Claude settings JSON, rule frontmatter, document links, workflow and repository hygiene | Static validation; no live Claude model session or GitHub run |
| `claude --version` | Installed CLI reports 2.1.294 |

The relative Maven-cache override above was used only for this constrained preparation environment. That cache is outside the repository and **not included in the ZIP**. Use the canonical commands in [development](development.md) on your own machine; an uncached first build requires approved network access.

## Limits and blockers

- No product features exist: no REST API, database schema, subscription matching, RabbitMQ, outbox relay, worker, retry, authentication or outbound HTTP.
- One smoke test proves context startup only. No persistence, broker or security guarantee is implemented.
- Fresh network bootstrap, macOS/Linux execution, GitHub Actions execution and live Claude instruction loading were not verified here. Git Bash could not start under the environment's process restrictions.
- The initial test attempt exposed unused Mockito agent attachment under restricted Windows execution. Mockito dependencies were excluded; the final build/test passed without them. Reintroduce mocking only with a demonstrated test need.
- No known blocker for the first pure-Java EventType task. Infrastructure and outbound-security gates remain future work.

## Pending owner decision

Project license: not selected. MIT is recommended for a small portfolio project; Apache-2.0 is an alternative with explicit patent terms. No project LICENSE text or POM license metadata has been added. The separate wrapper license is retained.

## Handoff rule

After a substantive task, update: stage/active task, the implemented behavior, the last real verification, unresolved issues and the next small step. Keep milestone specifications in the roadmap and material design decisions in ADRs. Never mark planned work complete from documentation alone.
