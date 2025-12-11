# Vehicle Tool

Vehicle Tool is a full-stack vehicle operations dashboard that combines Spring Boot, MyBatis, and MySQL on the backend with a Vue 3 and Element Plus frontend. It provides scheduling, auditing, vehicle inventory, geofence management, and supporting dictionary maintenance to help teams coordinate day-to-day logistics.

## Features
- User authentication and account lifecycle management (creation, status updates, password resets, reviewer selection).
- Vehicle cataloging with create/read/update/delete flows plus geofence binding and unbinding.
- Geofence creation and spatial binding workflows for associating vehicles to areas.
- Application scheduling and approval flows with audit history tracking.
- Dictionary and dictionary option maintenance to keep shared lookup values consistent across the UI.
- Knife4j-powered interactive API documentation for quickly exploring REST endpoints.

## Project structure
```
├── tool_backend/      # Spring Boot 2.6 service (Java 8, MyBatis, MySQL, Knife4j)
│   ├── src/main/java/ # Controllers, services, mappers, and DTO/VO definitions
│   └── src/main/resources/ # Mapper XML files, static assets, application.properties
├── tool_frontend/     # Vue 3 single-page app (Element Plus UI, Vue Router, Vuex, Axios)
│   ├── src/views/     # Feature pages for users, vehicles, geofences, audits, etc.
│   └── src/main.js    # Global bootstrap, BASE_URL configuration, and Element Plus setup
└── package.json       # Repository metadata
```

## Prerequisites
- Java 8+ and Maven 3.8+
- Node.js 16+ and npm
- MySQL 5.7+ with a database available (defaults to `insurance_tool`)

## Backend setup (Spring Boot)
1. Update database credentials in [`tool_backend/src/main/resources/application.properties`](tool_backend/src/main/resources/application.properties) if they differ from the defaults (`root`/`root`).
2. From the repository root, start the service:
   ```bash
   cd tool_backend
   mvn spring-boot:run
   ```
   The API listens on `http://localhost:8081` by default.
3. Open Knife4j at `http://localhost:8081/doc.html` to explore REST endpoints.

## Frontend setup (Vue 3)
1. Install dependencies:
   ```bash
   cd tool_frontend
   npm install
   ```
2. Start the development server:
   ```bash
   npm run serve
   ```
   The app runs on `http://localhost:9090` and proxies API calls to the backend via the `BASE_URL` configured in `src/main.js`.

## Development tips
- Mapper XML files live in `tool_backend/src/main/resources/mappers` and pair with MyBatis interfaces in `tool_backend/src/main/java`.
- Adjust log verbosity via `logging.level` keys in `tool_backend/src/main/resources/application.properties`.
- The frontend uses Element Plus icons globally (see `src/main.js`), and stores authenticated user info in `localStorage` for reuse across routes.
- When updating API routes, keep `BASE_URL` in sync between environments to avoid cross-origin issues during local development.
