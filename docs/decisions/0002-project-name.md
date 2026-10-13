# ADR 0002: Iletka product identity

Status: Accepted for naming only, 2026-10-12.

## Decision

Rename HookRelay to **Iletka**, a coined name inspired by Turkish *ileti* (message) and *kalıcılık* (durability). Pronounce it approximately **ee-let-ka**. It describes the reliability goal, not an exactly-once guarantee.

Use `iletka` for the repository/folder slug, Maven artifactId and Spring application name; `com.iletka` for the Maven groupId and Java base package; `IletkaApplication` / `IletkaApplicationTests` for bootstrap classes.

Limited indexed web/GitHub screening found no same-name webhook product or repository. A similar spelling, [ILETIA](https://mdl.shsmu.edu.cn/ILETIA/), belongs to a medical research web tool. This screening does not establish exclusive use, trademark clearance or domain availability.

## Consequences

The single-module architecture, product scope, seven-month roadmap and first EventType task are unchanged. The original `HookRelay_Proje_Raporu_v2.pdf` is preserved under its original title and remains the scope source. ADR 0001 is unchanged. Record current implementation evidence only in `docs/status.md`.
