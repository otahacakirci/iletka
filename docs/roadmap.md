# Iletka development roadmap

This is the stable milestone plan for the seven-month project. The milestones describe intended behavior and acceptance evidence, not implemented features. Keep live progress, completed work, blockers, and the active task in [status.md](status.md); do not turn this roadmap into a second progress tracker. A Spring Boot context smoke test proves application bootstrapping, not any product capability below.

The plan follows the reference report's seven-month sequence, with an explicit security gate before real outbound delivery. Work in a single organization context. Endpoint subscriptions, automatic retry, and recovery belong to the core. Manual replay and multi-tenancy are bonuses.

## Time and working rhythm

- Budget 8-10 hours per week across roughly 28 planned weeks: approximately 224-280 hours, including the final buffer.
- A practical rhythm is two weekday sessions of two hours and a weekend session of four to six hours. Learn a small topic, apply it, test the behavior, and record the evidence.
- Write meaningful unit tests from month 1. Later integration work adds real infrastructure and failure scenarios; testing does not begin in month 6.
- Preserve at least two weeks in month 7 for corrections, documentation, the demo, and delays. Reduce bonus scope when time is tight.
- Advance through dependency gates when their evidence is available. The month labels are a pacing guide, not permission to bypass an unfinished gate.

## Dependency gates

| Gate | Required evidence before advancing |
| --- | --- |
| Java foundation to REST | Small domain rules can be explained and verified with unit tests. |
| In-memory API to persistence | Endpoint, subscription, and event contracts work locally; restart data loss is documented. |
| Persistence to asynchronous processing | Event acceptance, matched deliveries, and their outbox records commit atomically; rollback and uniqueness tests pass. |
| Stub delivery to real outbound delivery | API key authentication, HMAC signing, secret handling, and SSRF controls pass their security tests. |
| Core to release | Clean Compose setup, migrations, CI, critical failure tests, and the core demo work. |
| Core to bonuses | Core acceptance evidence is recorded; bonus work does not consume the final two-week buffer. |

**Outbound security gate:** In months 1-4, endpoint URLs are inert metadata or controlled local test targets. Month 4 may send HTTP only to a fixed, controlled stub in an isolated development environment; it must not deliver to user-configured or public URLs. Real outbound delivery remains disabled until the month 5 security gate passes. A development-only stub exception must not become a production URL-policy bypass.

## Month 1 - Java foundation

**Learn:** Java 21 fundamentals, OOP, immutable values, collections, exceptions, Maven, Git, and HTTP concepts.

**Apply:** Build small domain exercises for event validation, simple state rules, and error types. Keep business rules independent of Spring and infrastructure. Start JUnit tests with these rules.

**Prove:** A small Java exercise runs reproducibly; valid and invalid inputs have meaningful tests; the README explains the project's problem and intended scope without claiming planned features are implemented.

**Exit gate:** Explain the tested rule and its failure behavior without needing a controller, database, broker, or network call.

## Month 2 - In-memory Spring REST API

**Learn:** Spring Boot dependency injection, controllers and services, DTOs, validation, REST status codes, consistent error responses, OpenAPI, and basic API key authentication.

**Apply:** Create a local in-memory prototype for endpoints, subscriptions, and event acceptance. Match subscriptions by exact, case-sensitive event type and enabled endpoint. One endpoint can subscribe to multiple types. Keep storage behind a clear boundary so that month 3 can replace it with JPA. URLs remain metadata; no outbound delivery occurs.

**Prove:** Example requests demonstrate endpoint/subscription changes, matching and no-match events, validation failures, and missing or invalid API keys. Controller and domain tests run. Document that restarting this prototype loses its in-memory data.

**Exit gate:** The REST contract and matching rules are understood and tested. Persistent storage is not yet an acceptance requirement for this milestone.

## Month 3 - PostgreSQL, JPA, and transaction boundaries

**Learn:** SQL, PostgreSQL, JPA mappings, transactions, foreign keys, uniqueness, indexes, and Flyway migrations.

**Apply:** Replace in-memory storage with JPA. Model Endpoint, EndpointSubscription, Event, Delivery, DeliveryAttempt, and OutboxMessage. Use one organization context; a tenant or Project model is unnecessary for the core. In the acceptance transaction, select enabled matching endpoints and atomically persist the Event, one Delivery per matched endpoint, and their OutboxMessage records. Preserve the selected targets and target URL snapshots. A valid no-match event is stored with deliveryCount=0 and no delivery/outbox records.

**Prove:** Data survives a restart; a rollback leaves no partial Event/Delivery/Outbox result; subscription and delivery uniqueness constraints are enforced. Begin PostgreSQL integration tests when these persistence guarantees are introduced.

**Exit gate:** Database integrity is demonstrated before introducing broker delivery. Endpoint/subscription changes affect newly accepted events rather than changing the targets already selected for existing deliveries.

## Month 4 - Asynchronous processing against a controlled stub

**Learn:** RabbitMQ, durable queues, producer/consumer responsibilities, publisher confirms, routing failure handling, consumer acknowledgments, transactional outbox, atomic claims, leases, and basic recovery.

**Apply:** Build the outbox relay and delivery worker. Publish committed outbox work and mark it published only after confirmation and successful routing checks. Let the worker claim eligible work atomically, record an attempt, call only the controlled local HTTP stub outside the database transaction, and persist its result or next action before ACK. Add the basic lease recovery path for interrupted work.

**Prove:** Broker and worker interruptions do not silently discard accepted work. Duplicate notifications do not create duplicate delivery records. Tests demonstrate the distinct meanings of a publisher confirm, consumer ACK, and receiver HTTP response.

**Exit gate:** The stub-based path and interruption recovery work. Public and user-configured outbound targets remain disabled pending month 5; the month 4 milestone does not establish production delivery readiness.

## Month 5 - Retry, idempotency, and the outbound security gate

**Learn:** Persisted retry schedules, bounded attempts, exponential backoff with jitter, idempotency, stale dispatch, lease recovery, API key protection, HMAC signatures, and SSRF prevention.

**Apply:** Persist next_attempt_at and attempt history. Bound automatic retry and recovery; FAILED is terminal in the core. Reuse deliveryId across attempts. Fence old worker results with claim tokens/versions and reject stale dispatch messages. Make Idempotency-Key request handling transactional: the same key and request returns the original event and target count without re-matching; conflicting content returns a conflict.

Complete the security gate before enabling real outbound delivery:

- Authenticate every protected API route; use high-entropy API keys verified against hashed configuration.
- Sign the defined timestamp, eventId, deliveryId, and raw body consistently with HMAC. Demonstrate verification with a controlled receiver.
- Encrypt reusable signing secrets at rest, keep the decryption key outside the repository, and avoid logging secrets or payloads.
- Enforce managed HTTPS target rules, validate DNS results and the actual connection target, reject private/loopback/metadata destinations in production, disable redirects, and set HTTP time and response-size limits.
- Keep any local mock exception confined to the development profile and test that it is unavailable in the production policy.

**Prove:** Tests cover 500-to-200 delivery, bounded permanent failure, duplicate keys, conflicting request content, stale dispatch, expired leases, signature verification, and rejected unsafe targets. Document that at-least-once behavior permits repeated HTTP delivery, including when the receiver processes a request but the response or result commit is lost.

**Exit gate:** API key, HMAC, secret handling, and SSRF evidence is recorded before any real outbound delivery is enabled. Do not claim exactly-once delivery.

## Month 6 - Integration evidence and repeatable operation

**Learn:** Testcontainers, controlled HTTP receivers, structured logging, metrics, health checks, Docker Compose, CI, and small reproducible load experiments.

**Apply:** Deepen PostgreSQL, RabbitMQ, and HTTP-stub integration tests. Cover confirm uncertainty, duplicate messages, a crash before ACK, stale dispatch, lease recovery, rollback, retry limits, and concurrent duplicate requests. Provide the core Compose setup, migrations, example configuration, and automated build/test workflow. Add protected health endpoints, correlation identifiers in logs, and a small set of delivery/retry/backlog metrics without high-cardinality event identifiers as metric labels.

**Prove:** A clean local environment can start the core system with Compose. CI runs the build and critical tests. Report a small load experiment with its machine, payload, worker count, and measured latency rather than unsupported throughput claims.

**Exit gate:** Compose and CI are core deliverables by the end of this milestone. The repository's instructions reproduce the verified behaviors from a clean setup.

## Month 7 - Release and portfolio presentation

**Learn:** Refactoring with evidence, explaining architecture decisions, useful documentation, versioning, and release preparation.

**Apply:** Close core gaps, improve the README, update architecture/data-flow diagrams and decision records, write operational recovery instructions, record the short demo, and prepare the first release. Reserve at least the final two weeks for test fixes, documentation, demo preparation, and delays.

**Prove:** Another person can follow the README to run the project. A release and a five-to-seven-minute demo show:

1. Endpoint/subscription creation and an event producing the expected matching deliveries.
2. A receiver returning 500, 500, then 200, with persisted attempts and retry intervals.
3. The same Idempotency-Key returning the original event without an extra event or new matching.
4. Work continuing after the worker is stopped and restarted.
5. A valid event without a matching subscription yielding deliveryCount=0, plus a failing receiver reaching the bounded FAILED state.

**Exit gate:** The core release is supported by runnable evidence and accurate documentation. The demo does not depend on manual replay, multi-tenancy, a UI, or cloud deployment.

## Bonus boundary

After core gates pass, select at most one or two bonuses only if time remains outside the reserved buffer:

- Authorized, audited manual replay that preserves attempt history.
- Multi-organization/project support with explicit ownership and isolation tests.
- A small administration UI, advanced dashboard, Redis-backed rate limiting, or cloud deployment.

Automatic retry, crash recovery, core Compose setup, and CI are not bonuses. Kafka, Kubernetes, multiple regions, and many microservices are outside this seven-month target.

## First development task - EventType validation

This is a task definition, not an implementation or a statement of completion. Track its actual state only in [status.md](status.md).

**Goal:** Introduce one small immutable Java value for an event type, with validation independent of Spring.

**Initial policy:** Reject null, blank text, leading/trailing whitespace, and values longer than 128 characters. Preserve the exact accepted text and its case; do not silently trim or lowercase it. Additional character restrictions require a documented domain decision rather than an accidental controller rule.

**Acceptance evidence:** Unit tests cover a valid value such as report.completed, case preservation, null/empty/whitespace input, surrounding whitespace, and the length boundary. Invalid construction has one clear, documented error behavior. Equality uses the exact accepted value.

**Scope:** A value type and its focused unit tests only. Do not add REST endpoints, storage, RabbitMQ, an HTTP client, or wider delivery behavior for this task.
