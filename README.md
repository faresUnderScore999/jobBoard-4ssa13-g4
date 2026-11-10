# AutismCare

## Architecture

```text
┌───────────────────────────┐
│      Frontend (SPA)       │
└─────────────┬─────────────┘
              │ (HTTP / REST)
              ▼
┌───────────────────────────┐
│   API Gateway (Port 8080) │
└─────────────┬─────────────┘
              │
┌─────────────┴─────────────┴─────────────┐
│                                         │
▼                                         ▼
┌──────────────────┐    ┌──────────────────┐
│ User & Auth      │    │    Schedule      │
│     Service      │    │     Service      │
│  (DB: PostgreSQL)│    │  (DB: PostgreSQL)│
└──────────────────┘    └──────────────────┘
         │                        │
         └────────────┬───────────┘
                      │
                      ▼
            ┌──────────────────┐
            │   Consultation   │
            │     Service      │
            │  (DB: PostgreSQL)│
            └──────────────────┘
                      │
                      ▼
            ┌──────────────────┐
            │     Event        │
            │    Service       │
            │  (DB: PostgreSQL)│
            └──────────────────┘
                      │
                      ▼
            ┌──────────────────┐
            │   Gamification   │
            │     Service      │
            │   (DB: MongoDB)  │
            └──────────────────┘
                      │
                      ▼
            ┌──────────────────┐
            │  Resource Serv.  │
            │   (DB: MongoDB)  │
            └──────────────────┘
```

## Backend Services

The backend is a microservices architecture located in the `Backend/` directory:

| Service | Database |
|---------|----------|
| api-gateway | Routing & entry point |
| discovery-service | Service registry |
| auth-user-service | PostgreSQL |
| schedule-service | PostgreSQL |
| consultation-service | PostgreSQL |
| course-eval-service | MongoDB |
| event-service | PostgreSQL |
| game-service | MongoDB |
| resource-service | MongoDB |
