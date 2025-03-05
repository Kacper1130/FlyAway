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
1. Navigate to backend directory
2. Build the project
```bash
./mvnw clean install
```

### Frontend Setup
1. Navigate to frontend directory
2. Install dependencies
```bash
npm install
```

### Docker Configuration
Start all services using Docker Compose:
```bash
docker-compose up -d
```

## 🔐 Authentication

The system uses JWT (JSON Web Token) for secure authentication with three user roles:
- Client
- Employee
- Admin

## 💳 Payment Integration
Integrated with Stripe for secure online payments during flight bookings.

## 📧 Email Verification
New users receive an email with an account activation link.

## 🌐 Support Features
- Clients can create support tickets
- Employees can respond via real-time WebSocket chat

## 🚧 Development

### Running the Application
- Backend: `./mvnw spring-boot:run`
- Frontend: `ng serve`

## 🤝 Contributing
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License
[Specify Your License - e.g., MIT]

## 📞 Contact
Your Name - [your.email@example.com]
Project Link: [https://github.com/Kacper1130/FlyAway](https://github.com/Kacper1130/FlyAway)
