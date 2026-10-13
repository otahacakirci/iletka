# Reliability and security plan

All behavior below is planned. The bootstrap smoke test proves context startup only.

## Durable processing

Persist Event, selected Delivery records and OutboxMessage records atomically. A relay claims committed outbox work using a short transaction and lease. Publish persistent messages to a durable RabbitMQ queue with publisher confirms and routing checks; mark published only after a positive confirm without a return. An uncertain result can publish the same job again.

The worker atomically claims a current `QUEUED` delivery and starts an attempt. HTTP runs outside the DB transaction. Commit the attempt and success, failure or durable retry decision before consumer ACK. Publisher confirm, consumer ACK and HTTP `2xx` are separate acknowledgements.

The job contains `deliveryId` and `dispatchVersion`; it is a notification to consult durable state. An atomic claim updates only an eligible row/version and assigns a claim token and lease deadline. The result transaction must still own that token/version. Do not keep a transaction or row lock open during HTTP.

ACK stale versions and terminal deliveries without another HTTP call. ACK of a duplicate with an active lease is safe only because durable lease-expiry recovery will requeue abandoned work. Unknown/malformed jobs need explicit poison-message handling and a DLQ policy, rather than being confused with an ordinary HTTP failure.

States are `QUEUED`, `IN_PROGRESS`, `WAITING`, `SUCCEEDED`, `FAILED`. Due retries and expired leases generate a new dispatch version and outbox row atomically. Recovery obeys the attempt limit; stale dispatches must not start HTTP. Claim token/version checks reject an old worker's result. Lease duration exceeds the HTTP deadline.

Allocate the attempt number at claim time, so interrupted attempts consume the same bounded budget as other attempts. Recovery records an interrupted/unknown outcome, then either schedules new work or terminates at the limit. It must not infer receiver failure from a missing local result. For a due retry, change `WAITING -> QUEUED`, increment the dispatch version and insert its outbox message in one transaction. A recovered lease uses the same atomic dispatch pattern. PostgreSQL is the scheduler's source of time/state; the broker is not the retry history.

Retry timeout/connection errors, `408`, `429` and `5xx`; bound valid Retry-After values. Other `4xx` and `3xx` fail without automatic redirect/retry. The report's example is one initial attempt plus four retries, with 10/30/90/270-second waits, jitter and a cap. Store timing in PostgreSQL instead of blocking workers. `FAILED` is terminal in the core. Broker DLQ messages are distinct from failed deliveries.

At-least-once means duplicate HTTP delivery remains possible, including a crash after remote success but before result commit. Preserve deliveryId across retries; receivers need durable idempotency. Do not claim exactly-once effects.

## Security and evidence

Verify a high-entropy API key against a stored hash. Encrypt reusable HMAC secrets and keep the decryption key outside the repository. Sign timestamp, eventId, deliveryId and raw body with defined separators; receivers check signature, freshness and duplicates. HMAC does not encrypt payloads.

Use a managed HTTPS allowlist, validate DNS and actual connection targets, block private/loopback/metadata IPv4/IPv6 destinations, disable redirects and limit HTTP duration/response size. Local mock exceptions belong only in the development profile. Limit incoming payloads, protect Actuator and avoid secret/payload logging.

Resolve and validate every destination and ensure the HTTP transport connects to an approved address while retaining correct TLS hostname verification. Checking DNS once and allowing a second unchecked resolution is insufficient against rebinding. Network egress restrictions are an additional boundary. API keys authenticate producer requests; HMAC authenticates webhook messages to receivers; neither replaces HTTPS or receiver-side durable deduplication.

Future evidence must cover exact matching/no-match, changed subscriptions, concurrent idempotency, rollback, duplicate messages, uncertain confirms, stale dispatch, worker crashes, lease recovery, `500 → 200`, timeout/429, retry exhaustion, HMAC and SSRF. Use unit tests plus real PostgreSQL/RabbitMQ integration tests and a controlled HTTP receiver. Keep identifiers in logs, not high-cardinality metric labels. PDF pages 8–15 and 19 define these expectations.
