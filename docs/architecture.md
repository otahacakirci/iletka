# Architecture

The accepted structure is one Maven module and a modular monolith. The initial non-web bootstrap does not start an API, database, relay, scheduler or worker. The following structure is planned as features become necessary.

## Package direction

Use feature packages under `com.iletka`: `apiaccess`, `endpoint` (including subscriptions), `event`, `delivery`, `outbox` and `observability`. Add `domain`, `application` and `infrastructure` subpackages only when real code needs them; do not create placeholder classes or empty layers.

- Domain code expresses rules without HTTP, JPA or RabbitMQ dependencies.
- Application code coordinates use cases and transaction boundaries.
- Infrastructure code provides controllers, persistence and messaging/HTTP adapters.
- Cross-feature calls use application boundaries, not another feature's repositories. Keep dependency direction acyclic.

When the worker later needs event data, review the dependency graph explicitly; do not introduce an `event -> delivery -> event` service cycle. Choose the smallest query/data boundary that meets that implemented use case.

The planned acceptance flow calls endpoint matching, delivery creation and outbox recording from the event use case in one PostgreSQL transaction. Delivery scheduling records new outbox work; the outbox relay publishes jobs without owning delivery policy.

## Future runtime roles

| Role | Responsibility |
| --- | --- |
| API | Authenticate, validate, manage targets and accept/query work. |
| Relay | Claim committed outbox rows and confirm broker publishing. |
| Worker | Claim delivery, send HTTP outside a DB transaction, persist outcome. |
| Scheduler/recovery | Requeue due retries and expired leases atomically. |

These are responsibilities inside the same application, not a requirement for separate services. PostgreSQL will be the source of delivery state; RabbitMQ will carry job notifications. Shared identifiers are `deliveryId` and `dispatchVersion`.

The learning sequence starts with an in-memory REST prototype in month 2 and replaces storage with JPA/PostgreSQL in month 3. Persistence guarantees begin with that milestone. See [the contract](project-contract.md) and [reliability/security plan](reliability-security.md).
