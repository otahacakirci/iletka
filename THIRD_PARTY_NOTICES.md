# Third-party notices

`mvnw` and `mvnw.cmd` are Apache Maven Wrapper 3.3.4, only-script distribution. Their Apache copyright/license headers are preserved. The Apache License 2.0 text is supplied in `LICENSE-MAVEN-WRAPPER.txt`; this is not a license selection for Iletka itself.

The scripts were reused unchanged from a local cached copy because network download was unavailable during preparation. No other application's source or configuration was imported. Reference: [Apache Maven Wrapper](https://maven.apache.org/tools/wrapper/) and [upstream source](https://github.com/apache/maven-wrapper).

Delivered script SHA-256 values (before any Git line-ending conversion):

```text
mvnw      cae96cef89ebea3531221f4ae17c23cf8edf67d00eae8306d4186ae1bbed4d02
mvnw.cmd  4a361e1374a3e5ad6d03e18e9adc0cf181ac5058ac6203b76f0ba3b456b56481
```

The wrapper distribution URL pins Maven 3.9.16 from Maven Central. Maven itself and resolved dependencies are not vendored in this repository or ZIP; their licenses apply independently. A first build on a fresh machine needs network access.

## Maven distribution integrity

The Maven 3.9.16 binary ZIP (9,395,475 bytes) matched the SHA-512 published in the [Apache archive](https://archive.apache.org/dist/maven/maven-3/3.9.16/binaries/apache-maven-3.9.16-bin.zip.sha512) and [Apache downloads](https://downloads.apache.org/maven/maven-3/3.9.16/binaries/apache-maven-3.9.16-bin.zip.sha512) on 2026-10-12:

```text
ed41650d42485cfc243fad22158caf9cbb5dc408ce7a09ddb94dd42a019de929ca43065bfa450612cf12bf78b5cafa3884b96c090de326ff590448c933454af3
```

SHA-256 computed from that verified archive, now pinned as `distributionSha256Sum`:

```text
5af3b743dd8b876b5c45da33b676251e5f1687712644abb4ee519ca56e1d89ce
```

This is a derived SHA-256, not a SHA-256 value directly published by Apache. Both unchanged wrapper scripts support the property. It checks a new distribution installation; it does not revalidate an existing extracted cache or every Maven dependency.
