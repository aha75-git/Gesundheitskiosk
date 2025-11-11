# 🏥 Gesundheitslotse - Mobile Gesundheitskiosk Plattform

![Gesundheitslotse](https://img.shields.io/badge/Project-Gesundheitslotse-blue)
![Version](https://img.shields.io/badge/Version-1.0.0-green)
![License](https://img.shields.io/badge/License-MIT-yellow)

![java](https://img.shields.io/badge/Java-22-blue)
![spring](https://img.shields.io/badge/Spring_Boot-3.5.6-green)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=yolo-deploy_restaurant-finder&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=yolo-deploy_restaurant-finder)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=yolo-deploy_restaurant-finder&metric=coverage)](https://sonarcloud.io/summary/new_code?id=yolo-deploy_restaurant-finder)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=yolo-deploy_restaurant-finder&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=yolo-deploy_restaurant-finder)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=yolo-deploy_restaurant-finder&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=yolo-deploy_restaurant-finder)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=yolo-deploy_restaurant-finder&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=yolo-deploy_restaurant-finder)

## 📋 Projektbeschreibung

**Gesundheitskiosk** ist eine innovative digitale Plattform, die niederschwellige, mehrsprachige Gesundheitsberatung für alle Bevölkerungsgruppen ermöglicht. Die Anwendung verbindet Bürger mit qualifizierten Gesundheitsberatern und Experten im deutschen Gesundheitssystem.

### 🎯 Vision
Eine barrierefreie, mehrsprachige Gesundheitsplattform, die digitale Brücken zwischen Bürgern und Gesundheitsdienstleistern baut.

### 🏥 Angebote
- Hilfe beim Finden und Kontaktieren geeigneter Fachärzte, Therapeuten und Experten
- Expertenberatung rund um das Thema "Gesundheit"
- Kostenlose Beratung in mehreren Sprachen (Muttersprache)
- Möglichkeit von Hausbesuchen
- Beratung für alle Zielgruppen

## ✨ Features

### 🔐 Benutzerverwaltung
- **Registrierung & Login** mit JWT-Authentifizierung
- **Rollenbasierte Zugriffskontrolle** (Patient, Berater, Admin)
- **Mehrsprachige Profile** mit persönlichen Präferenzen
- **Datenschutzkonforme** Einwilligungsverwaltung

### 🔍 Experten-Findung
- **Intelligente Suche** nach Fachgebiet, Sprache und Verfügbarkeit
- **Detailierte Beraterprofile** mit Qualifikationen und Bewertungen
- **Filterung** nach Standort und Spezialisierungen
- **Verfügbarkeitsprüfung** in Echtzeit

### 📅 Terminmanagement
- **Online-Terminbuchung** mit Kalenderintegration
- **Verschiedene Beratungsformen**: Video, Telefon, Chat, Persönlich
- **Automatische Erinnerungen** und Benachrichtigungen
- **Terminverwaltung** für Patienten und Berater

### 💬 Kommunikation
- **Integrierter Chat** mit Echtzeit-Kommunikation
- **AI-Assistent** für erste Hilfestellung
- **Sprachunterstützung** für Barrierefreiheit
- **Sichere Dokumentenübertragung**

### 🎨 Benutzerfreundlichkeit
- **Responsive Design** für alle Geräte
- **Barrierefreie** Benutzeroberfläche
- **Intuitive Navigation** und Bedienung
- **Schnelle Ladezeiten** durch optimierte Performance

## 🛠 Technologiestack

### Frontend
- **React 18** mit modernen Hooks
- **TypeScript** für typsichere Entwicklung
- **Normal CSS** (keine Frameworks) für maximale Kontrolle
- **Axios** für API-Kommunikation
- **React Router** für Navigation

### Backend
- **Spring Boot 3.5.6** mit Java 22
- **Maven** als Build-Tool
- **Lombok** für reduzierte Boilerplate-Code
- **JWT** für sichere Authentifizierung

### Datenbank & Caching
- **MongoDB** als NoSQL-Datenbank
- **Vollständiges ER-Modell** mit 12 Hauptentitäten

### DevOps
- **Docker** für Containerisierung
- **Docker Compose** für lokale Entwicklung

## 🔒 Sicherheit
### Implementierte Sicherheitsmaßnahmen
- JWT-basierte Authentifizierung
- Rollenbasierte Zugriffskontrolle (RBAC)
- Datenverschlüsselung in MongoDB
- Sichere Passwort-Hashing mit BCrypt
- CORS Konfiguration für Cross-Origin Requests
- Input Validation und Sanitization

## 🚀 Schnellstart

### Voraussetzungen
- Java 22
- Node.js 18+
- Docker & Docker Compose
- MongoDB

### 1. Projekt klonen
```bash
git clone https://github.com/aha75-git/Gesundheitskiosk.git
cd gesundheitslotse
```

### 2. Set required environment variables (example):

```cmd
set MONGO_URI=mongodb://{user}:{password}@{url}/{database}?retryWrites=true&w=majority
set TOKEN_SECRET_KEY=replace_with_secure_key
set TOKEN_EXPIRATION_TIME=3600000
set APP_LOG_LEVEL=INFO
```

### 3. Build and run:

```cmd
mvn clean package -DskipTests
java -jar target\app.jar
```

Or run in development:

```cmd
mvn spring-boot:run
```

Or run with the dev profile (uses application-dev.yml):

```cmd
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## ⚙️ Configuration

- `src/main/resources/application.properties` maps the following environment variables:
    - `MONGO_URI`, `TOKEN_SECRET_KEY`, `TOKEN_EXPIRATION_TIME`, `APP_LOG_LEVEL`
- For development, use the `src/main/resources/application-dev.yml` file. Activate it by running with the `dev` profile:
    - Example: `mvn spring-boot:run -Dspring-boot.run.profiles=dev`
- The server runs under context path `/` (default port 8080).

## 📜 License

See `LICENSE` in project root.


## 👥 Team & Kontakt
### Projektleitung
- Product Owner: Andreas Haffner

- Technical Lead: Andreas Haffner

- UX/Design: Andreas Haffner

---

### Gesundheitslotse - Ihr digitaler Begleiter für eine bessere Gesundheitsversorgung. 🏥✨

<div align="center">
Made with ❤️ for better healthcare
</div>
