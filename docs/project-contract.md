# Project contract

Primary source: [HookRelay_Proje_Raporu_v2.pdf](reference/HookRelay_Proje_Raporu_v2.pdf), version 2, 19 pages. This document records planned product behavior, not implemented capabilities. The initial milestone is only a non-web Spring Boot context and smoke test.

## Scope

Iletka will accept application events and deliver signed HTTP webhooks with durable history, bounded automatic retry and crash recovery. The core serves one organization, with API key authentication and no public signup.

Core work includes endpoint/subscription management, event acceptance, delivery queries, PostgreSQL transactions, RabbitMQ delivery jobs, outbox publishing, claim/lease recovery, HMAC, SSRF controls, tests and basic observability. Manual replay, multiple organizations, secret rotation, a management UI, advanced dashboards, Redis rate limiting and cloud deployment are bonuses. Kafka, Kubernetes and multiple microservices are outside the core plan.

## Routing and acceptance

- An endpoint can subscribe to multiple validated, case-sensitive event types. Matching uses exact equality; no wildcard or regex.
- Within the acceptance transaction, select enabled matching endpoints and persist Event, one Delivery per selected endpoint, and one initial OutboxMessage per delivery. Respond `202 Accepted` after commit, with `eventId` and `deliveryCount`.
- No match still persists the event and returns `deliveryCount: 0`; no delivery/outbox is created.
- The selected targets are fixed at acceptance. Subscription/enabled changes affect new events only. Existing deliveries continue using their stored target URL snapshot; later subscriptions do not backfill old events.
- Idempotency keys are unique within the single organization. Same key/content returns the original event and target count without matching again. Different content with that key returns `409`.

## Planned API

Route names below are the repository's proposed contract detail, to review with the month 2 work. They do not describe existing endpoints or expand the PDF's core scope.

| Method and path | Purpose |
| --- | --- |
| `POST /v1/endpoints` | Create a validated HTTPS target. |
| `PATCH /v1/endpoints/{id}` | Change URL/enabled for future events. |
| `PUT /v1/endpoints/{id}/subscriptions` | Atomically replace the eventTypes list. |
| `POST /v1/events` | Accept type/payload with Idempotency-Key. |
| `GET /v1/deliveries` | Filter by status/event/date, with pagination. |
| `GET /v1/deliveries/{id}/attempts` | Read results, durations and error categories. |

Manual replay has no core endpoint.

## Planned data model

Six tables: Endpoint, EndpointSubscription, Event, Delivery, DeliveryAttempt and OutboxMessage. Event-to-subscription matching is a rule, not a foreign key. Event and Endpoint each have many deliveries; Delivery has many attempts/outbox messages.

Required uniqueness: event idempotency key; subscription `(endpoint_id, event_type)`; delivery `(event_id, endpoint_id)`; outbox `(delivery_id, dispatch_version)`; attempt `(delivery_id, attempt_no)`. Use foreign keys, checks, UTC timestamps and indexes for matching, due retries and unpublished outbox work.

## PDF traceability

| PDF pages | Contract topic |
| --- | --- |
| 4–5 | Core/bonus boundary and technology direction |
| 6–8 | Architecture, acceptance transaction and outbox |
| 9–10 | States, retry, idempotency, routing and snapshots |
| 11–12 | Schema, API and security |
| 13, 19 | Evidence and final core acceptance |
| 16–17 | Learning sequence; in-memory API before persistence |
