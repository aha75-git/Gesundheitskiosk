# Gesundheitslotse - Mobile Gesundheitskiosk Plattform

![Gesundheitslotse](https://img.shields.io/badge/Project-Gesundheitslotse-blue)
![Version](https://img.shields.io/badge/Version-1.0.0-green)
![License](https://img.shields.io/badge/License-MIT-yellow)

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


## 🚀 Schnellstart

### Voraussetzungen
- Java 22
- Node.js 18+
- Docker & Docker Compose
- MongoDB
- Redis

### 1. Projekt klonen
```bash
git clone https://github.com/aha75-git/Gesundheitskiosk.git
cd gesundheitslotse