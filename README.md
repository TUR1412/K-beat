<div align="center">
  <img src="docs/hero.svg" alt="K-beat" width="100%" />
  <h1>K-beat</h1>
  <p>SSR-first Todo app with progressive enhancement and durable persistence.</p>
  <p>
    <a href="./README.md">English</a> ·
    <a href="./README.zh-CN.md">简体中文</a>
  </p>
  <p>
    <a href="https://github.com/TUR1412/K-beat/actions/workflows/main.yml">
      <img alt="CI" src="https://github.com/TUR1412/K-beat/actions/workflows/main.yml/badge.svg?branch=main" />
    </a>
    <img alt="Java" src="https://img.shields.io/badge/Java-17-informational" />
    <img alt="Spring Boot" src="https://img.shields.io/badge/Spring%20Boot-3.3.x-success" />
  </p>
</div>

## What is K-beat?

K-beat is a Todo app that looks like a modern frontend, but is designed to work like a classic web app:

- No JavaScript? Still works (forms + SSR + redirects).
- JavaScript available? Automatically upgrades to a snappy, fetch-based experience.
- Data survives restarts via an H2 file database (`./data/` by default).

## Highlights

- Persistence: H2 file DB (restart-safe, no extra services required)
- Modern UI: Aurora background + Glassmorphism + Bento layout, light/dark theme
- Progressive enhancement: the UI never depends on JSON being rendered directly
- JSON API: `/api/todos` for the enhanced UX (and for programmatic access)
- Friendly error handling:
  - API errors include `message`, `actionable_suggestion`, and `request_id`
  - Every response includes `X-Request-Id` for debugging and support
- Observability & performance:
  - Actuator endpoints: `/actuator/health`, `/actuator/metrics`, `/actuator/prometheus`
  - `Server-Timing` header and slow-request logging
  - Gzip compression + cache-control for static assets
- Security headers by default (CSP, nosniff, etc.)

## Quick start

Default port: `8080`.

### Windows

```bash
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

### macOS / Linux

```bash
./mvnw test
./mvnw spring-boot:run
```

Open:

- UI: `http://localhost:8080/todos`
- API: `http://localhost:8080/api/todos`
- Health: `http://localhost:8080/actuator/health`

## Build a Jar

```bash
./mvnw -DskipTests package
java -jar target/k-beat-0.0.1-SNAPSHOT.jar
```

## Configuration

Common settings (`src/main/resources/application.properties`):

- `app.assets.version`: cache busting for `/assets/*`
- `spring.datasource.url`: H2 persistence location (default `jdbc:h2:file:./data/kbeat`)
- `app.http.slowRequestThresholdMs`: log slow requests (ms)
- `management.endpoints.web.exposure.include`: Actuator exposure (protect it in production)

## Docs

- Architecture: `docs/ARCHITECTURE.md`
- API: `docs/API.md`
- Deployment: `docs/DEPLOYMENT.md`

## Contributing

PRs are welcome. Please read `CONTRIBUTING.md` and make sure CI is green.

