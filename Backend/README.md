# AutismCare - Plateforme d'Accompagnement pour Enfants Autistes

AutismCare est une application web microservices offrant des outils interactifs, éducatifs et organisationnels pour les enfants autistes, leurs parents, éducateurs et spécialistes.

## 📁 Structure du Dépôt

```text
Projet-AWD/
├── Backend/
│   ├── api-gateway/
│   ├── discovery-service/
│   ├── auth-user-service/
│   ├── schedule-service/          # Microservice commun
│   ├── consultation-service/ (molka)
│   ├── course-eval-service/ (dora)
│   ├── game-service/ (rouem)
│   ├── event-service/ (fares)
│   ├── communication-service/ (zakaria)
│   └── resource-service/ (ahmed)
```

## 🛠️ Détail des Composants

### api-gateway/
Point d'entrée unique pour toutes les requêtes clientes provenant du Frontend.

Responsabilités : routage des requêtes, équilibrage de charge, limitation de taux, et filtres de sécurité/JWT globaux.

### discovery-service/
Registre de services (Netflix Eureka / Consul).

Responsabilités : enregistrement et découverte dynamique des services. Permet aux microservices de se trouver et communiquer entre eux sans adresses IP ou ports codés en dur.

### auth-user-service/
Gestion de la sécurité et de l'identité des utilisateurs.

Responsabilités : authentification des utilisateurs, vérification des identifiants, contrôle d'accès basé sur les rôles (RBAC), et émission de JSON Web Tokens (JWT) utilisés dans tout le système.

### schedule-service/
Microservice commun pour la gestion des plannings et rendez-vous.

### consultation-service/
Gestion des consultations et suivis des enfants autistes.

### course-eval-service/
Évaluation des cours et apprentissages.

### game-service/
Jeux éducatifs interactifs adaptés.

### event-service/
Gestion des événements et notifications.

### resource-service/
Gestion des ressources et documents partagés.

### communication-service/
Bibliothèque de pictogrammes pour la communication des enfants autistes.

Responsabilités : bibliothèque de pictogrammes par catégorie (besoins, émotions, nourriture, activités, lieux) ; tableaux personnalisables par enfant gérés par les parents et éducateurs ; construction de demandes ou de choix par l'enfant en tapant sur des pictogrammes (ex. « Je veux + eau ») ; historique des requêtes de l'enfant visible aux parents et éducateurs ; pictogrammes favoris et fréquemment utilisés.