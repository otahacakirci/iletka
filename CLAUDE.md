# Iletka working instructions

## Purpose and language

- Help the owner learn Java backend development through small, reviewable changes.
- Communicate with the owner in Turkish; keep code, repository docs and identifiers in English.
- Explain the relevant Java idea and verification briefly, without a long tutorial unless requested.
- Implement the requested slice; do not generate the complete backend from the project plan.

## Read the right source

- Read `docs/status.md` first for implemented behavior, the active task and verification evidence.
- Read `docs/development.md` for prerequisites and canonical build/test/run commands.
- Read only the relevant milestone in `docs/roadmap.md`; it describes future work.
- Read `docs/project-contract.md` before changing product behavior.
- Consult `docs/architecture.md`, `docs/reliability-security.md` and relevant ADRs when needed.
- The PDF-derived contract is the product baseline; the roadmap does not prove implementation.
- If code, status and the contract disagree, explain the concrete mismatch before expanding scope.

## Architecture

- Use Java 21 and the existing Maven Wrapper; versions are defined in the build files.
- Keep one Maven module and the root package `com.iletka`.
- Grow packages by feature, then domain/application/infrastructure where actual code requires them.
- Do not add empty package classes, speculative interfaces or unused dependencies.
- Keep domain rules independent of Spring, JPA, RabbitMQ and HTTP clients.
- Keep cross-feature calls acyclic and use application boundaries rather than foreign repositories.
- Do not add REST, persistence or broker dependencies merely because later milestones need them.

## Product boundaries

- The core has one organization; public signup and tenancy are outside the first version.
- Exact event-type subscriptions, automatic retry and crash recovery are planned core behavior.
- Manual replay, tenant isolation, UI, Redis and cloud deployment are bonuses.
- Preserve acceptance-time routing, target URL snapshots and Idempotency-Key semantics.
- PostgreSQL is the planned state source; RabbitMQ carries delivery jobs.
- Never imply publisher confirm, consumer ACK and HTTP success mean the same thing.
- Do not claim exactly-once delivery; retries may repeat the same deliveryId.
- Respect the roadmap's security gate before enabling real outbound HTTP delivery.

## Work and verification

- Inspect the relevant code and existing tests before proposing a change.
- For a substantive task, state a short scope and acceptance check; proceed within the request.
- Keep changes focused; do not refactor unrelated files or introduce the next milestone early.
- Write behavior-focused tests for new domain rules and changed behavior.
- Use the documented Maven Wrapper checks; fix failures instead of weakening assertions.
- Report the command and actual result; say explicitly when a check could not run.
- A context smoke test proves bootstrapping, not product delivery or security.
- Review the final diff for scope, accidental files and unsupported feature claims.
- Update `docs/status.md` after substantive work with truthful evidence and the next small step.
- Do not turn README, roadmap or this file into parallel live progress trackers.
- Record a new ADR only for a material decision, not every implementation detail.

## Permissions and repository hygiene

- Keep normal permission prompts; do not enable auto-accept or bypass modes for convenience.
- Respect tool denials. Do not reroute a denied action through another shell, script or tool.
- Do not create a remote repository, push or publish without the owner's explicit authorization.
- Keep secrets, local environment settings, build output and personal machine paths out of commits.
- Never weaken authentication, secret handling, target checks or tests to make a demo pass.
- Preserve changes you did not make; never reset or overwrite unrelated work.
- Commit only when asked, with a message describing the verified change.
- End with the change, the useful learning point, verification and any real limitation.

## Keeping these instructions useful

- Put stable project guidance here and file-specific conventions in `.claude/rules/`.
- Add skills, hooks, custom agents or MCP only after a concrete repeated need is established.
- Keep this file concise; link to detailed docs instead of importing their complete contents.
