# Repository tree

Delivered files; generated build output and local Git metadata are excluded. Only the `event` package exists; other planned feature packages are not scaffolded.

```text
iletka/
├── .claude/
│   ├── rules/
│   │   ├── java.md
│   │   └── tests.md
│   └── settings.json
├── .github/
│   └── workflows/
│       └── verify.yml
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties
├── docs/
│   ├── decisions/
│   │   ├── 0001-modular-monolith.md
│   │   └── 0002-project-name.md
│   ├── reference/
│   │   └── HookRelay_Proje_Raporu_v2.pdf
│   ├── ai-workflow.md
│   ├── architecture.md
│   ├── development.md
│   ├── project-contract.md
│   ├── reliability-security.md
│   ├── roadmap.md
│   └── status.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── iletka/
│   │   │           ├── IletkaApplication.java
│   │   │           └── event/
│   │   │               ├── EventType.java
│   │   │               └── InvalidEventTypeException.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/
│               └── iletka/
│                   ├── IletkaApplicationTests.java
│                   └── event/
│                       └── EventTypeTest.java
├── .editorconfig
├── .gitattributes
├── .gitignore
├── .java-version
├── CLAUDE.md
├── LICENSE-MAVEN-WRAPPER.txt
├── README.md
├── THIRD_PARTY_NOTICES.md
├── TREE.md
├── mvnw
├── mvnw.cmd
└── pom.xml
```
