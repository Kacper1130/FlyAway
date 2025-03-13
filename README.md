# FlyAway - Airline Management System

## 👋 Project Overview

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

## 🔧 Tech Stack

### Backend

- Java Spring Boot
- JWT Authentication
- WebSocket Support
- PostgreSQL (Main Database)
- MongoDB (Support Chat Storage)

### Frontend

- Angular

## 🔧 Prerequisites

- Java 17+
- Node.js
- Angular CLI
- Docker
- Docker Compose

## 📦 Installation and Setup

### Clone the Repository

```bash
git clone https://github.com/Kacper1130/FlyAway.git
cd FlyAway
```

### Configuration Setup

Before running the project, ensure that you configure the necessary properties.

1. **Config Properties**
   - Edit the existing `config.properties` file in the root directory and add the following environment variables:

```properties
POSTGRES_USERNAME=your_postgres_username
POSTGRES_PASSWORD=your_postgres_password

MONGO_USERNAME=your_mongo_username
MONGO_PASSWORD=your_mongo_password

JWT_SECRET_KEY=your_jwt_secret_key

STRIPE_SECRET_KEY=your_stripe_secret_key
```

- Replace the placeholders with your actual configuration details:

  - `POSTGRES_USERNAME` and `POSTGRES_PASSWORD`: Your PostgreSQL database credentials.
  - `MONGO_USERNAME` and `MONGO_PASSWORD`: Your MongoDB database credentials.
  - `JWT_SECRET_KEY`: A strong secret key for JWT authentication. You can generate one at [jwtsecret.com](https://jwtsecret.com/generate).
  - `STRIPE_SECRET_KEY`: Your Stripe API secret key. You can obtain it by registering on the [Stripe Dashboard](https://dashboard.stripe.com/register) under **Developers > API Keys**.
  - Leave `STRIPE_WEBHOOK_SECRET` empty for now, it will be obtained later.

- **Note:** Do **not** commit this file to version control as it contains sensitive information.

> ⚠️ **Important:** Ensure that `spring.config.import=optional:config.properties` is included in `application.properties` to import the configuration correctly.

### Docker Configuration

Start all services using Docker Compose:

```bash
docker-compose up -d
```

### Obtaining STRIPE\_WEBHOOK\_SECRET

After running Docker, obtain the `STRIPE_WEBHOOK_SECRET` by checking the logs of the Stripe CLI container:

```bash
docker logs flyaway_stripe-cli | grep "whsec_"
```

- Add the obtained `STRIPE_WEBHOOK_SECRET` to the `config.properties` file.

### Backend Setup

1. Build the project:

```bash
./mvnw clean install
```

2. Start the Spring Boot application:

```bash
./mvnw spring-boot:run
```

### Frontend Setup

1. Navigate to the frontend directory:

```bash
cd frontend
```

2. Install dependencies:

```bash
npm install
```

3. Start the Angular development server:

```bash
ng serve
```

4. The frontend will be available at:

```bash
http://localhost:4200
```

### Default Admin Credentials

By default, the application provides an admin account:

- **Login:** `admin@flyaway.com`
- **Password:** `password`

