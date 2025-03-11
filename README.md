# FlyAway - Airline Management System

## 📋 Project Overview

FlyAway is a comprehensive airline management system that provides seamless interactions for clients, employees, and administrators. The application is built with a robust backend using Spring Boot and a dynamic frontend using Angular, offering a full-featured airline booking and management platform.

## 🚀 Key Features

### Client Features
- User Registration with Email Verification
- Flight Search and Filtering
- Flight Booking
- Online Payment via Stripe Integration
- Reservation Management
- Support Ticket Creation

### Employee Features
- Flight Creation
- Customer Reservation Management
- Customer Support Ticket Handling
- Real-time Customer Support Chat

### Admin Features
- Employee Management
- Country and Destination Management
- Airport and Aircraft Administration

## 🛠 Tech Stack

### Backend
- Java Spring Boot
- JWT Authentication
- WebSocket Support
- PostgreSQL (Main Database)
- MongoDB (Support Chat Storage)

### Frontend
- Angular

### DevOps
- Docker
- Docker Compose
- Stripe Webhook Integration
- MailHog (Email Testing)

## 🔧 Prerequisites

- Java 17+
- Node.js
- Angular CLI
- Docker
- Docker Compose

## 🛢 Database Setup

The project uses Docker to manage databases:
- PostgreSQL for primary data storage
- MongoDB for support chat functionality

## 📦 Installation and Setup

### Clone the Repository
```bash
git clone https://github.com/Kacper1130/FlyAway.git
cd FlyAway
```

### Backend Setup
1. Navigate to the backend directory
```bash
cd backend
```
2. Build the project
```bash
./mvnw clean install
```

### Frontend Setup
1. Navigate to the frontend directory
```bash
cd frontend
```
2. Install dependencies
```bash
npm install
```

### Docker Configuration
Start all services using Docker Compose:
```bash
docker-compose up -d
```

## ▶️ How to Run the Project

### Running the Backend
1. Navigate to the backend directory:
```bash
cd backend
```
2. Start the Spring Boot application:
```bash
./mvnw spring-boot:run
```

### Running the Frontend
1. Navigate to the frontend directory:
```bash
cd frontend
```
2. Start the Angular development server:
```bash
ng serve
```
3. The frontend will be available at:
```bash
http://localhost:4200
```

### Running with Docker (Recommended)
To start both backend and frontend along with the databases using Docker, simply run:
```bash
docker-compose up --build
```
This will launch all required services automatically.

---
You are now ready to use the FlyAway Airline Management System! 🎉

