# Event Server API Endpoints

Base URL: `http://localhost:8085/api`

## Health

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/ping` | Health check — returns `"event-server is up"` |

---

## Event Types

### List all event types
**`GET /event-types`**

Returns all event types.

**Response (200):**
```json
[
  { "id": 1, "name": "Conference" },
  { "id": 2, "name": "Workshop" }
]
```

### Get event type by ID
**`GET /event-types/{id}`**

| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | path | Event type ID |

**Response (200):**
```json
{ "id": 1, "name": "Conference" }
```

**Response (404):** Event type not found.

### Create event type
**`POST /event-types`**

**Request body:**
```json
{ "name": "Hackathon" }
```

**Response (201):** Created event type with `Location` header.

### Update event type
**`PUT /event-types/{id}`**

**Request body:**
```json
{ "name": "Updated Conference" }
```

**Response (200):** Updated event type.

**Response (404):** Event type not found.

### Delete event type
**`DELETE /event-types/{id}`**

**Response (204):** Deleted successfully.

**Response (404):** Event type not found.

### Get events under an event type
**`GET /event-types/{id}/events`**

| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | path | Event type ID |

**Response (200):** List of events categorized under this type.

---

## Events

### List all events
**`GET /events`**

Returns all events.

**Response (200):**
```json
[
  {
    "id": 1,
    "title": "Tech Conference 2026",
    "startAt": "2026-12-01T09:00:00",
    "location": "Main Hall",
    "capacity": 200,
    "eventTypeId": 1
  }
]
```

### Get event by ID
**`GET /events/{id}`**

| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | path | Event ID |

**Response (200):** Event object.

**Response (404):** Event not found.

### Create event
**`POST /events`**

**Request body:**
```json
{
  "title": "Tech Conference 2026",
  "startAt": "2026-12-01T09:00:00",
  "location": "Main Hall",
  "capacity": 200,
  "eventTypeId": 1
}
```

**Response (201):** Created event with `Location` header.

### Update event
**`PUT /events/{id}`**

**Request body:** Same fields as create.

**Response (200):** Updated event.

**Response (404):** Event not found.

### Delete event
**`DELETE /events/{id}`**

**Response (204):** Deleted successfully.

**Response (404):** Event not found.

### Get registrations for an event
**`GET /events/{id}/registrations`**

| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | path | Event ID |

**Response (200):** List of registrations for this event.

---

## Registrations

### List all registrations
**`GET /registrations`**

Returns all registrations.

**Response (200):**
```json
[
  { "id": 1, "eventId": 1, "userId": "user-123", "status": "CONFIRMED" }
]
```

### Get registration by ID
**`GET /registrations/{id}`**

| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | path | Registration ID |

**Response (200):** Registration object.

**Response (404):** Registration not found.

### Create registration
**`POST /registrations`**

**Request body:**
```json
{ "eventId": 1, "userId": "user-123", "status": "CONFIRMED" }
```

**Response (201):** Created registration with `Location` header.

### Update registration
**`PUT /registrations/{id}`**

**Request body:** Same fields as create.

**Response (200):** Updated registration.

**Response (404):** Registration not found.

### Delete registration
**`DELETE /registrations/{id}`**

**Response (204):** Deleted successfully.

**Response (404):** Registration not found.

### Get registrations by event ID
**`GET /registrations/events/{eventId}`**

| Parameter | Type | Description |
|-----------|------|-------------|
| `eventId` | path | Event ID |

**Response (200):** List of registrations for the given event.

---

## Data Models

### EventType
| Field | Type | Description |
|-------|------|-------------|
| `id` | Long | Primary key |
| `name` | String | Event type name |

### Event
| Field | Type | Description |
|-------|------|-------------|
| `id` | Long | Primary key |
| `title` | String | Event title |
| `startAt` | LocalDateTime | Start date/time (ISO-8601) |
| `location` | String | Event location |
| `capacity` | int | Maximum attendees |
| `eventTypeId` | Long | FK to event type |

### Registration
| Field | Type | Description |
|-------|------|-------------|
| `id` | Long | Primary key |
| `eventId` | Long | FK to event |
| `userId` | String | User identifier |
| `status` | String | Registration status (e.g. CONFIRMED, PENDING, CANCELLED) |

---

## Relationships

- `EventType` **1 → N** `Event` (categorized as)
- `Event` **1 → N** `Registration` (has)