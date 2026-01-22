# Projet P02 — Testez et améliorez une application existante  
**Auteur : Quang NGUYEN**  
**Date de démarrage : Janvier 2026**

---

## Description du projet
Ce projet consiste à améliorer une application existante composée d’un **backend Spring Boot** sécurisé avec JWT, d’un **frontend Angular**, et d’une **base MySQL** exécutée via Docker.

Objectifs principaux :
- Ajout de fonctionnalités manquantes  
- Correction des bugs existants  
- Mise en place de tests unitaires et d’intégration  
- Amélioration de la qualité du code  

---

## Architecture du projet

### Backend (Spring Boot)
- API REST sécurisée JWT  
- CRUD complet sur les étudiants  
- Tests unitaires + intégration (≥ 80 %)  

### Frontend (Angular 16+)
- Formulaire de login  
- Gestion des étudiants (liste, ajout, suppression)  
- Interceptor JWT fonctionnel  

### Base de données
- MySQL via Docker Compose  
- Volume persistant  
- Charset UTF‑8 complet  

---

## Installation & Lancement

### 1. Lancer MySQL via Docker
```bash
docker compose up -d
```
