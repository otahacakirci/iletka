# Local development and verification

## Toolchain

- JDK 21 (a JDK, not only a JRE). Set `JAVA_HOME` and put its `bin` on PATH.
- Maven Wrapper: use the committed `mvnw` / `mvnw.cmd`; the distribution version is pinned in `.mvn/wrapper/maven-wrapper.properties`.
- Spring Boot and Java target versions are defined in `pom.xml`. The Boot parent manages dependency/plugin versions.
- Git and an editor. Docker and external services are unnecessary at the bootstrap milestone.

The wrapper scripts are Apache Maven Wrapper 3.3.4 (only-script), reused from a local official distribution without modification. `THIRD_PARTY_NOTICES.md` records provenance. The first uncached invocation downloads the pinned Maven distribution over HTTPS; Maven then resolves dependencies. Offline builds work only with all required artifacts already cached.

The wrapper properties pin a distribution SHA-256. The cached ZIP was first matched against Apache's published SHA-512, then SHA-256 was computed from that verified archive; details are in `THIRD_PARTY_NOTICES.md`. Wrapper verification runs when installing a distribution, not on every use of an already-extracted Maven cache. Changing the Maven version requires verifying and updating the checksum as well.

Windows PowerShell, using your actual JDK path:

```powershell
$env:JAVA_HOME = 'C:\path\to\jdk-21'
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
java --version
.\mvnw.cmd --version
```

macOS/Linux, using your actual JDK path:

```sh
export JAVA_HOME=/path/to/jdk-21
export PATH="$JAVA_HOME/bin:$PATH"
java --version
./mvnw --version
```

These are session-local environment changes. Confirm Maven reports Java 21 before building. `.java-version` is a hint for compatible version managers; it does not install or enforce a JDK.

The delivered project folder has a local Git repository with the starter staged and no commits. Review the files and make your own baseline commit before the first feature task. The portable ZIP deliberately excludes `.git`; after extracting it, run `git init -b main` if you want Git tracking. No remote is required to begin with Claude Code.

## Canonical commands

Run from the repository root. Windows uses `.\mvnw.cmd`; replace it with `./mvnw` on macOS/Linux.

| Purpose | Windows command |
| --- | --- |
| Compile and run all current tests | `.\mvnw.cmd --batch-mode --no-transfer-progress test` |
| Build executable jar and run tests | `.\mvnw.cmd --batch-mode --no-transfer-progress verify` |
| Run bootstrap after verify | `java -jar target/iletka-0.0.1-SNAPSHOT.jar` |
| Run one test class | `.\mvnw.cmd -Dtest=IletkaApplicationTests test` |

Expected current behavior: Spring logs `Started IletkaApplication` and exits with code 0. There is no web server or API route. The context smoke test verifies application wiring only, not routing, persistence, delivery or security. Test reports appear under ignored `target/surefire-reports/`.

The jar output timestamp is fixed for repeatable archives. Dependency versions are pinned through the Boot parent and wrapper. Reproducibility across different operating systems or JDK patch versions must be measured, not assumed.

Mockito's two default starter-test dependencies are excluded because the current tests do not use mocks. This also avoids unnecessary runtime agent attachment. JUnit and Spring context testing remain available; add a mocking dependency only when a real test requires it.

## Infrastructure arrives with its behavior

- Month 2: REST/validation/OpenAPI and local API key protection, in-memory storage; add only the dependencies this work uses.
- Month 3: PostgreSQL/JPA/Flyway. Create `src/main/resources/db/migration/` with the first real migration; never edit an applied migration.
- Month 4: RabbitMQ/relay/worker and a controlled HTTP stub. Introduce local infrastructure configuration when these components are exercised.
- Month 5: complete outbound security before real destinations are enabled.
- Month 6: complete core Compose and expand CI for infrastructure/failure tests. CI exists now for the minimal Maven verification.

No Compose command or connection setting is documented as working before the corresponding files exist. See [roadmap](roadmap.md) for acceptance gates.

## Configuration and secrets

The bootstrap has no required credentials or `.env` file. Do not add fake production values or unused example variables. When configuration is introduced, document each variable at the same time as its consumer and provide placeholders only. Spring Boot does not load `.env` automatically.

Keep real keys, signing secrets, decrypted values and production URLs out of commits and logs. `.env*`, local Claude settings and common key files are ignored; ignore rules are not a substitute for reviewing `git diff --cached`. Add an `.env.example` only with real local infrastructure and document how it is consumed. Use CI secrets only when a future task requires them.

## Troubleshooting

- Wrong JDK: compare `java --version` and wrapper `--version`; correct `JAVA_HOME` and PATH rather than lowering the Java target.
- Download failure: check your network/proxy or approved Maven mirror. Do not disable TLS/checksum validation or manufacture a successful build report.
- Linux permission error after unpacking a ZIP: run `chmod +x mvnw`; the Git index records the executable flag.
- No response at localhost: expected at this milestone; the bootstrap is non-web.
- Missing Docker: expected to be irrelevant until infrastructure milestones.

When a task ends, record its real command/result and remaining limitations in [status](status.md). Preserve evidence compactly; do not commit build output.

## Official tool references

- [Spring Boot system requirements](https://docs.spring.io/spring-boot/system-requirements.html)
- [Maven Wrapper](https://maven.apache.org/tools/wrapper/)
- [Maven reproducible builds](https://maven.apache.org/guides/mini/guide-reproducible-builds.html)
- [GitHub setup-java action](https://github.com/actions/setup-java)
