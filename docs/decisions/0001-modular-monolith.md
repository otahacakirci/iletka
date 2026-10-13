# ADR 0001: One Maven module, modular monolith

Status: Accepted for repository structure. Product features remain planned.

## Context

The version 2 report targets a seven-month Java learning project. Distributed deployment would add coordination work before the delivery rules are understood.

## Decision

Keep one Maven module and one Spring Boot application. Organize future code by feature, introducing domain/application/infrastructure boundaries when needed. API, relay, worker and scheduler are responsibilities, not separate-service commitments. Begin with the non-web context and smoke test; do not scaffold empty feature classes.

## Consequences

One build and deployment keep iteration manageable. Feature boundaries still require discipline. PostgreSQL transactions can coordinate acceptance across features; broker publishing and remote HTTP remain separate reliability boundaries. Future service extraction would require another decision and demonstrated need.

Source: report pages 4, 6 and 16–17.
