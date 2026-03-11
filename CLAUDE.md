# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./mvnw clean package

# Install to local Maven repository
./mvnw clean install

# Run all tests
./mvnw test

# Run a specific test class
./mvnw test -Dtest=CountriesStatesCitiesSpringBootStarterApplicationTests

# Run a specific test method
./mvnw test -Dtest=CountriesStatesCitiesSpringBootStarterApplicationTests#contextLoads

# Skip tests during build
./mvnw clean package -DskipTests
```

## Architecture

This is a **Spring Boot autoconfiguration starter** (`csc-spring-boot-starter`) that embeds geographical data (countries, states, cities) directly in the JAR. It requires no external database from the consuming application.

**Stack:** Java 17, Spring Boot 4.0.3, Spring MVC, JDBC (no ORM), SQLite, Maven.

### Layers

```
Controllers (REST, optional) → Services (thin) → Repositories (JdbcTemplate) → world.sqlite3
```

All beans are wired by `CscAutoConfiguration` and annotated `@ConditionalOnMissingBean`, meaning consumers can override any bean by declaring their own.

### Autoconfiguration Entry Point

`src/main/java/com/csc/starter/autoconfigure/CscAutoConfiguration.java` — registered via:
`src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`

Activates when `DataSource` and `JdbcTemplate` are on the classpath and `csc.enabled=true` (default).

### Configuration Properties (`csc.*`)

Defined in `CscProperties.java`:

| Property | Default | Description |
|---|---|---|
| `csc.enabled` | `true` | Enable/disable the entire starter |
| `csc.expose-api` | `true` | Whether to register REST controllers |
| `csc.base-path` | `/api/v1/geo` | Base URL path for all REST endpoints |
| `csc.seed-data` | `true` | Placeholder, not yet used |

### Embedded Database

`src/main/resources/world.sqlite3` (~105MB SQLite3 file) is bundled in the JAR. Tables: `regions`, `subregions`, `countries`, `states`, `cities`.

Notable DB quirk: the country name column is `native` (SQL reserved word) — the SQL queries select it as-is; the Java model field is `nativeName`.

### Repository Pattern

Each repository holds a single `RowMapper<T>` as a field and uses Java text-block SQL strings as `private static final String` constants. Queries return `List<T>` for collections and `Optional<T>` for single lookups (using `jdbcTemplate.query()` + manual `Optional` wrapping — not `queryForObject`).

### Implementation Status

- **Fully implemented:** Country, State, City (repositories, services, controllers, CRUD queries)
- **Stubs only:** Region, Subregion (classes exist but have no query methods or endpoints yet)
- **Not yet implemented:** Pagination, filtering (mentioned in POM description), comprehensive test coverage
