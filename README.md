# Countries States Cities — Spring Boot Starter

> A Spring Boot autoconfiguration starter that embeds **250 countries, 5,000+ states/provinces, and 150,000+ cities** directly into your application — no external database required.

This project is a Java/Spring Boot implementation of the [countries-states-cities-database](https://github.com/dr5hn/countries-states-cities-database) by [@dr5hn](https://github.com/dr5hn). All geographical data comes from that dataset and is bundled as a SQLite file inside the JAR.

---

## Features

- **Zero-config setup** — drop the dependency in and it works
- **No external database** — the SQLite database is embedded in the JAR
- **REST API out of the box** — fully functional endpoints for countries, states, and cities
- **Field selection** — request only the fields you need (e.g. `?fields=name,iso2,capital`)
- **Overridable beans** — every bean is `@ConditionalOnMissingBean`, so you can replace any component with your own
- **Spring Boot 4.x compatible**

---

## Data Source

All geographical data is sourced from the [countries-states-cities-database](https://github.com/dr5hn/countries-states-cities-database) project. The bundled `world.sqlite3` file contains:

| Table | Description |
|---|---|
| `countries` | 250 countries with ISO codes, phone codes, currencies, timezones, and more |
| `states` | 5,000+ states, provinces, and regions |
| `cities` | 150,000+ cities worldwide |
| `regions` | World regions (stub, not yet exposed) |
| `subregions` | World subregions (stub, not yet exposed) |

---

## Quick Start

### 1. Add the dependency

```xml
<dependency>
    <groupId>com.csc</groupId>
    <artifactId>countries-states-cities-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

> **Note:** The starter is not yet published to Maven Central. See [Building locally](#building-locally) to install it in your local repository.

### 2. That's it

The starter auto-configures itself. Add it to any Spring Boot application and the REST endpoints are available at `/api/v1/geo` by default.

---

## REST API

All endpoints are served under the base path (default: `/api/v1/geo`).

### Countries

| Method | Endpoint | Parameters | Description |
|---|---|---|---|
| `GET` | `/api/v1/geo/countries` | `fields?` | List all 250 countries |
| `GET` | `/api/v1/geo/countries?countryCode={iso2}` | `countryCode` (required), `fields?` | Get a country by ISO 3166-1 alpha-2 code |

**Examples:**

```bash
# All countries
GET /api/v1/geo/countries

# Single country
GET /api/v1/geo/countries?countryCode=US

# Specific fields only
GET /api/v1/geo/countries?fields=name,iso2,capital,currency
```

**Available country fields:** `id`, `name`, `iso2`, `iso3`, `numericCode`, `phoneCode`, `capital`, `currency`, `currencyName`, `currencySymbol`, `tld`, `nativeName`, `population`, `gdp`, `region`, `regionId`, `subregion`, `subregionId`, `nationality`, `areaSqKm`, `postalCodeFormat`, `postalCodeRegex`, `timezones`, `latitude`, `longitude`, `emoji`, `emojiU`, `wikiDataId`

---

### States / Provinces

| Method | Endpoint | Parameters | Description |
|---|---|---|---|
| `GET` | `/api/v1/geo/states?countryCode={iso2}` | `countryCode` (required), `fields?` | List all states for a country |
| `GET` | `/api/v1/geo/states?countryCode={iso2}&stateCode={iso2}` | `countryCode` + `stateCode` (both required), `fields?` | Get a specific state |

**Examples:**

```bash
# All states in the US
GET /api/v1/geo/states?countryCode=US

# A specific state
GET /api/v1/geo/states?countryCode=US&stateCode=CA

# Specific fields
GET /api/v1/geo/states?countryCode=BR&fields=name,iso2,type
```

**Available state fields:** `id`, `name`, `countryId`, `countryCode`, `fipsCode`, `iso2`, `iso31662`, `type`, `level`, `parentId`, `nativeName`, `latitude`, `longitude`, `timezone`, `wikiDataId`, `population`

---

### Cities

| Method | Endpoint | Parameters | Description |
|---|---|---|---|
| `GET` | `/api/v1/geo/cities?countryCode={iso2}` | `countryCode` (required), `fields?` | List all cities in a country |
| `GET` | `/api/v1/geo/cities?countryCode={iso2}&stateCode={iso2}` | `countryCode` + `stateCode` (both required), `fields?` | List cities in a specific state |

**Examples:**

```bash
# All cities in Germany
GET /api/v1/geo/cities?countryCode=DE

# Cities in a specific state
GET /api/v1/geo/cities?countryCode=US&stateCode=TX

# Specific fields
GET /api/v1/geo/cities?countryCode=JP&fields=name,latitude,longitude
```

**Available city fields:** `id`, `name`, `stateId`, `stateCode`, `countryId`, `countryCode`, `type`, `level`, `parentId`, `latitude`, `longitude`, `nativeName`, `population`, `timezone`, `translations`, `wikiDataId`

---

## Configuration

All properties are under the `csc.*` prefix:

| Property | Default | Description |
|---|---|---|
| `csc.enabled` | `true` | Enable or disable the entire starter |
| `csc.expose-api` | `true` | Enable or disable the REST controllers |
| `csc.base-path` | `/api/v1/geo` | Base URL path for all REST endpoints |

**Example `application.properties`:**

```properties
# Change the base path
csc.base-path=/geo

# Disable REST exposure (use services directly)
csc.expose-api=false

# Disable the starter entirely
csc.enabled=false
```

---

## Using the Services Directly

If you don't want the REST API but still want programmatic access, disable `csc.expose-api` and inject the services:

```java
@Service
public class MyService {

    private final CountryService countryService;
    private final StateService stateService;
    private final CityService cityService;

    // Constructor injection ...

    public void example() {
        Optional<Country> country = countryService.findByIso2("US");
        List<State> states = stateService.findByCountryCode("US");
        List<City> cities = cityService.findByCountryCodeAndStateCode("US", "CA");
    }
}
```

## Overriding Beans

Every bean registered by the starter is annotated `@ConditionalOnMissingBean`. You can replace any component by declaring your own bean of the same type:

```java
@Bean
public CountryService countryService(CountryRepository repo) {
    return new MyCustomCountryService(repo); // your implementation
}
```

---

## Architecture

```
REST Controllers  →  Services (thin)  →  Repositories (JdbcTemplate)  →  world.sqlite3 (embedded)
```

- **No ORM** — raw JDBC via `JdbcTemplate` with text-block SQL
- **`CscAutoConfiguration`** — single entry point that wires all beans
- **`CscDataSourceConfiguration`** — creates a dedicated `DataSource` pointing to the embedded SQLite file
- All beans are registered conditionally and can be overridden

### Package Structure

```
com.csc/
├── autoconfigure/        # CscAutoConfiguration, CscDataSourceConfiguration
├── config/               # CscProperties, CscDatasource
├── controller/           # CountryController, StateController, CityController (+ stubs)
├── model/                # Country, State, City, Region, Subregion
├── repository/           # JdbcTemplate-based repositories
├── service/              # Thin service layer
└── util/                 # ResultSetUtils
```

---

## Building Locally

**Requirements:** Java 17+, Maven (or use the included `./mvnw` wrapper)

```bash
# Clone the repository
git clone <repo-url>
cd countries-states-cities-spring-boot-starter

# Build
./mvnw clean package

# Install to your local Maven repository (~/.m2)
./mvnw clean install

# Run tests
./mvnw test

# Build without tests
./mvnw clean package -DskipTests
```

---

## Project Status & Roadmap

| Feature | Status |
|---|---|
| Countries API | Done |
| States/Provinces API | Done |
| Cities API | Done |
| Dynamic field selection | Done |
| Regions API | Stub (not implemented) |
| Subregions API | Stub (not implemented) |
| Pagination | Not implemented |
| Advanced filtering | Not implemented |
| Published to Maven Central | Not yet |

---

## Contributing

Contributions are welcome! Here are some areas that need work:

- **Regions & Subregions** — the models and repositories exist as stubs; query methods and endpoints still need to be added
- **Pagination** — all list endpoints currently return full result sets
- **Filtering** — query parameters for filtering by name, region, etc.
- **Tests** — test coverage is minimal and needs expansion
- **Maven Central publishing** — setting up the release pipeline

### How to contribute

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/my-feature`)
3. Make your changes — run `./mvnw verify` before committing (Spotless code formatting is enforced)
4. Open a pull request with a clear description of what you changed and why

**Code style:** The project uses [Palantir Java Format](https://github.com/palantir/palantir-java-format) enforced via the Spotless Maven plugin. Run `./mvnw spotless:apply` to auto-format your code before committing.

---

## Credits

- Geographical data: [countries-states-cities-database](https://github.com/dr5hn/countries-states-cities-database) by [@dr5hn](https://github.com/dr5hn) — licensed under the [Open Database License (ODbL)](https://opendatacommons.org/licenses/odbl/)
- Built with [Spring Boot](https://spring.io/projects/spring-boot) and [sqlite-jdbc](https://github.com/xerial/sqlite-jdbc)

---

## License

This project is open source. The bundled geographical data is subject to the [ODbL license](https://opendatacommons.org/licenses/odbl/) from the upstream dataset.
