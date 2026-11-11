# Documentation API - AutismCare Microservices

Cette documentation regroupe les contrats d'API REST pour l'ensemble des microservices de la plateforme AutismCare. Elle inclut les endpoints, les rôles, les codes de réponse HTTP, ainsi que des exemples de payloads (JSON) pour faciliter l'intégration côté Frontend ou entre services.

---

## Table des matières
1. [Schedule Service (Emploi du temps)](#1-schedule-service)
2. [Consultation Service (Rendez-vous)](#2-consultation-service)
3. [Course Service (Cours & Évaluations)](#3-course-service)
4. [Game Service (Jeux & Récompenses)](#4-game-service)
5. [Event Service (Événements)](#5-event-service)
6. [Communication Service (Pictogrammes & Tableaux)](#6-communication-service)
7. [Resource Service (Ressources médias)](#7-resource-service)
8. [Communications Inter-services](#8-communications-inter-services)
9. [Lexique des Codes HTTP](#9-lexique-des-codes-http)

---

## 1. Schedule Service
**Base URL :** `/api/schedule`

Ce service gère le planning, les activités et les rappels des enfants.

| Méthode | Endpoint | Rôle | Codes |
| :--- | :--- | :--- | :--- |
| `GET` | `/{childId}?view=week` | Planning du jour ou de la semaine | 200, 403, 404 |
| `POST` | `/{childId}/activities` | Ajouter une activité | 201, 400, 409 |
| `PUT` | `/activities/{id}` | Modifier une activité | 200, 404, 409 |
| `PATCH` | `/activities/{id}/status` | Statut : fait ou non réalisé | 200, 400, 404 |
| `DELETE` | `/activities/{id}` | Supprimer une activité | 204, 404 |
| `POST` | `/activities/{id}/reminder` | Programmer un rappel | 201, 404 |

**Exemple : Ajouter une activité (`POST /api/schedule/child123/activities`)**
```json
// Request Body
{
  "title": "Séance d'orthophonie",
  "type": "THERAPY",
  "startAt": "2023-11-20T14:00:00",
  "durationMin": 45
}

// Response 201 Created
{
  "id": 101,
  "title": "Séance d'orthophonie",
  "status": "PLANNED"
}
```

## 2. Consultation Service
**Base URL :** `/api/consultations`

Gère la prise de rendez-vous avec les spécialistes, les disponibilités et les notes de suivi.

| Méthode | Endpoint | Rôle | Codes |
| :--- | :--- | :--- | :--- |
| `GET` | `/specialists` | Liste des spécialistes | 200, 400 |
| `POST` | `/specialists/{id}/availabilities` | Ajouter une disponibilité | 201, 400, 403, 409 |
| `POST` | `/appointments` | Prendre un rendez-vous | 201, 400, 403, 404, 409 |
| `GET` | `/appointments?childId=` | Historique d'un enfant | 200, 403 |
| `PUT` | `/appointments/{id}` | Modifier un rendez-vous | 200, 400, 404, 409 |
| `PATCH` | `/appointments/{id}/status` | Confirmer / terminer / annuler | 200, 403, 404, 409 |
| `POST` | `/appointments/{id}/notes` | Ajouter une note de suivi | 201, 403, 404 |

**Exemple : Prendre un rendez-vous (`POST /api/consultations/appointments`)**
```json
// Request Body
{
  "childId": "child123",
  "parentId": "parent456",
  "specialistId": "spec789",
  "startAt": "2023-11-25T10:00:00",
  "mode": "EN_LIGNE"
}

// Response 201 Created
{
  "appointmentId": "uuid-1234-5678",
  "status": "EN_ATTENTE"
}
```

> (Note : Le code 409 est retourné si le créneau est déjà réservé).

## 3. Course Service
**Base URL :** `/api/courses`

Gestion du catalogue de cours, du contenu pédagogique et des affectations aux enfants.

| Méthode | Endpoint | Rôle | Codes |
| :--- | :--- | :--- | :--- |
| `GET` | `?categorie=&niveau=&age=&page=&size=` | Liste filtrée et paginée | 200, 400 |
| `GET` | `/{id}` | Détail d'un cours | 200, 404 |
| `POST` | `/` | Créer un cours | 201, 400, 401, 403, 404 |
| `PUT` | `/{id}` | Modifier un cours | 200, 400, 401, 403, 404 |
| `DELETE` | `/{id}` | Supprimer un cours | 204, 401, 403, 404 |
| `GET` | `/{id}/contents` | Lister les contenus du cours | 200, 404 |
| `POST` | `/{id}/contents` | Ajouter un contenu (image, vidéo, etc.) | 201, 400, 403, 404, 413, 415 |
| `DELETE` | `/{id}/contents/{contentId}` | Retirer un contenu | 204, 403, 404 |
| `POST` | `/{id}/assignments` | Affecter le cours à des enfants | 201, 400, 403, 404, 409, 422 |

**Exemple : Affecter un cours (`POST /api/courses/12/assignments`)**
```json
// Request Body
{
  "childIds": ["child123", "child456"]
}

// Response 201 Created
{
  "message": "Cours affecté avec succès",
  "assignedCount": 2
}
```

> (Codes spécifiques : 413 fichier trop lourd, 415 type non supporté, 422 âge hors cible).

## 4. Game Service
**Base URL :** `/api/games`

Gestion des mini-jeux, des sessions de jeu et des récompenses.

| Méthode | Endpoint | Rôle | Codes |
| :--- | :--- | :--- | :--- |
| `GET` | `?category=` | Lister les jeux | 200 |
| `GET` | `/{id}` | Détail d'un jeu | 200, 404 |
| `POST` | `/` | Créer un jeu | 201, 400, 403 |
| `POST` | `/{id}/sessions` | Enregistrer une partie et score | 201, 400, 404 |
| `GET` | `/children/{id}/scores` | Scores de l'enfant | 200, 403, 404 |
| `GET` | `/children/{id}/rewards` | Récompenses obtenues | 200, 403, 404 |

**Exemple : Enregistrer une session (`POST /api/games/5/sessions`)**
```json
// Request Body
{
  "childId": "child123",
  "score": 850,
  "playedAt": "2023-11-15T16:30:00"
}

// Response 201 Created
{
  "sessionId": 987,
  "rewardUnlocked": "Badge Niveau 2"
}
```

## 5. Event Service
**Base URL :** `/api/events`

Calendrier des événements communautaires et gestion des inscriptions.

| Méthode | Endpoint | Rôle | Codes |
| :--- | :--- | :--- | :--- |
| `GET` | `/` | Calendrier des événements | 200 |
| `POST` | `/` | Créer un événement | 201, 400, 403 |
| `PUT` | `/{id}` | Modifier un événement | 200, 403, 404 |
| `POST` | `/{id}/registrations` | S'inscrire à un événement | 201, 404, 409 |
| `DELETE` | `/{id}/registrations/{userId}` | Annuler une inscription | 204, 404 |
| `GET` | `/{id}/registrations` | Liste des inscrits | 200, 403, 404 |

> (Note : Le code 409 est retourné si l'événement est complet).

## 6. Communication Service
**Base URL :** `/api`

Gestion des pictogrammes (CAA) et des tableaux de communication.

| Méthode | Endpoint | Rôle | Codes |
| :--- | :--- | :--- | :--- |
| `GET` | `/pictograms?category=` | Bibliothèque de pictogrammes | 200 |
| `GET` | `/pictograms?ids=` | Pictogrammes par identifiants | 200, 400 |
| `POST` | `/boards` | Créer un tableau | 201, 400, 403 |
| `GET` | `/boards/{childId}` | Tableau de l'enfant | 200, 403, 404 |
| `PUT` | `/boards/{id}/items` | Modifier les pictogrammes du tableau | 200, 400, 404 |
| `POST` | `/boards/{id}/requests` | Envoyer une demande (enfant) | 201, 400, 403 |
| `GET` | `/boards/{childId}/requests` | Historique des demandes | 200, 403, 404 |

**Exemple : Envoyer une demande (`POST /api/boards/10/requests`)**
```json
// Request Body
{
  "childId": "child123",
  "text": "Je veux boire"
}

// Response 201 Created
{
  "requestId": 55,
  "sentAt": "2023-11-15T10:15:00",
  "status": "SENT"
}
```

## 7. Resource Service
**Base URL :** `/api/resources`

Gestion des fichiers médias (vidéos, images, documents) et des catégories.

| Méthode | Endpoint | Rôle | Codes |
| :--- | :--- | :--- | :--- |
| `GET` | `/` | Liste des ressources | 200, 400 |
| `GET` | `/{id}` | Détail d'une ressource | 200, 404 |
| `POST` | `/` | Créer une ressource | 201, 400, 401, 403 |
| `PUT` | `/{id}` | Modifier une ressource | 200, 400, 401, 403, 404 |
| `DELETE` | `/{id}` | Supprimer une ressource | 204, 401, 403, 404 |
| `GET` | `?category=&type=` | Filtrer les ressources | 200, 400 |

## 8. Communications Inter-services

L'architecture repose sur des communications synchrones et asynchrones.

### A. Communication Synchrone (OpenFeign)

Utilisée lorsqu'un service a besoin d'une réponse immédiate d'un autre service.

**Exemple :** Le service course-eval-service a besoin de récupérer un média.

- **Appel :** `GET http://resource-service/api/resources/{id}`
- **Mécanisme :** Client Feign configuré avec un load-balancer.

### B. Communication Asynchrone (RabbitMQ)

Utilisée pour découpler les services et déclencher des actions en arrière-plan (Event-Driven).

#### Événement : ConsultationConfirmed
- **Émetteur :** consultation-service
- **Données :** appointmentId, childId, parentId, début, fin, mode
- **Consommateur :** schedule-service
- **Effet :** Ajoute automatiquement le rendez-vous dans le planning de l'enfant.

#### Événement : ConsultationCancelled
- **Émetteur :** consultation-service
- **Données :** appointmentId, childId, parentId, motif
- **Consommateur :** schedule-service
- **Effet :** Retire le rendez-vous du planning.

#### Événement : resource.created
- **Émetteur :** resource-service
- **Consommateurs :** À définir avec l'équipe (ex: course-service pour mettre à jour un cache).

## 9. Lexique des Codes HTTP

| Code | Signification | Description |
| :--- | :--- | :--- |
| 200 | OK | La requête a réussi. |
| 201 | Created | La ressource a été créée avec succès (suite à un POST). |
| 204 | No Content | La requête a réussi, mais il n'y a pas de contenu à renvoyer (souvent suite à un DELETE). |
| 400 | Bad Request | La requête est mal formée ou contient des données invalides. |
| 401 | Unauthorized | L'utilisateur n'est pas authentifié (token manquant ou invalide). |
| 403 | Forbidden | L'utilisateur est authentifié, mais n'a pas les droits (rôle insuffisant). |
| 404 | Not Found | La ressource demandée n'existe pas. |
| 409 | Conflict | Conflit avec l'état actuel de la ressource (ex: créneau déjà réservé, événement complet). |
| 413 | Payload Too Large | Le fichier envoyé est trop volumineux. |
| 415 | Unsupported Media Type | Le format du fichier n'est pas supporté. |
| 422 | Unprocessable Entity | La requête est bien formée, mais impossible à traiter (ex: âge de l'enfant hors cible). |