# Iletka

Reliable Event-Driven Webhook Delivery Platform — a seven-month Java backend learning and portfolio project.

Iletka is a coined name inspired by Turkish *ileti* (message) and *kalıcılık* (durability), pronounced approximately **ee-let-ka**. The [identity decision](docs/decisions/0002-project-name.md) records the rename; the original reference PDF keeps its title.

The planned platform accepts events, matches exact webhook subscriptions, persists delivery work atomically, and uses RabbitMQ workers with bounded retry and crash recovery. It targets one organization and at-least-once delivery. The [project contract](docs/project-contract.md) defines the core and bonus boundary.

This repository begins with a **non-web Spring Boot bootstrap and one context smoke test**. It has no REST endpoints, database, broker, delivery worker, authentication implementation or Docker setup yet. See [status](docs/status.md) for the canonical record of implemented work and actual verification.

## Quick start

Install a JDK 21 and set `JAVA_HOME` to it. Maven is supplied by the committed wrapper; the first build needs internet access to Maven Central. No Docker, credentials or environment file is needed for this bootstrap.

Windows PowerShell:

```powershell
.\mvnw.cmd --version
.\mvnw.cmd --batch-mode --no-transfer-progress verify
java -jar target/iletka-0.0.1-SNAPSHOT.jar
```

macOS/Linux:

```sh
./mvnw --version
./mvnw --batch-mode --no-transfer-progress verify
java -jar target/iletka-0.0.1-SNAPSHOT.jar
```

The application logs successful Spring startup and exits normally. It does **not** listen on port 8080. [Development instructions](docs/development.md) cover JDK selection, smoke-test limits and troubleshooting.

## Find the right context

See [the full repository tree](TREE.md) for the delivered files. Feature packages will be introduced with their first real implementation.

| Need | Read |
| --- | --- |
| What works, evidence, blockers, next task | [Status](docs/status.md) |
| Install, build, test and run | [Development](docs/development.md) |
| Scope, subscriptions, API and data-model baseline | [Project contract](docs/project-contract.md) |
| Package boundaries and runtime responsibilities | [Architecture](docs/architecture.md), [ADR 0001](docs/decisions/0001-modular-monolith.md) |
| Outbox, retry, claims and security boundaries | [Reliability and security](docs/reliability-security.md) |
| Learn / apply / prove milestones | [Roadmap](docs/roadmap.md) |
| Claude Code workflow and permission choices | [AI workflow](docs/ai-workflow.md) |
| Original Turkish visual report | [Reference PDF](docs/reference/HookRelay_Proje_Raporu_v2.pdf) |

## Start with Claude Code

Open a terminal in this folder and run `claude` using your installed, authenticated Claude Code client. Its root instructions are [CLAUDE.md](CLAUDE.md); detailed documents are read only for the task at hand. Check effective permissions with `/permissions`.

Suggested first request:

> Güncel durum belgesini ve yol haritasındaki EventType görevini oku. Doğrulama politikasını kısaca açıkla; yalnız bu immutable Java değerini ve odaklı birim testlerini geliştir. REST, persistence veya teslimat kodu ekleme. Testleri çalıştır, sonucu ve öğrendiğim Java kavramını Türkçe açıkla; durum kaydını güncelle.

## Repository decisions

One Maven module keeps builds and learning manageable. Future code grows by feature under `com.iletka`, introducing internal layers when real behavior needs them. No placeholder services or unused infrastructure dependencies are committed. API, relay and worker may later run as separate roles of the same codebase without an early microservice split.

The original PDF is the scope baseline. Markdown documents expose the relevant decisions for daily work; they do not replace the scope with new mandatory features. Code and test results prove implementation; only `docs/status.md` tracks current progress. If these disagree, resolve the mismatch explicitly before changing scope.

## Publishing

The CI workflow builds and tests on Java 21 when this project is hosted on GitHub; its remote execution is not established by a local build. The project license is an owner decision recorded as pending in [status](docs/status.md); no project license text or POM license grant has been added. Maven Wrapper's separate Apache license is included in `LICENSE-MAVEN-WRAPPER.txt`.
