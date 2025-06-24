# DriveOrbit Core 🚗

A comprehensive vehicle fleet management system built with Spring Boot, providing real-time monitoring, driver management, work assignment tracking, and notification services.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Firebase Integration](#firebase-integration)
- [Testing](#testing)


## 🎯 Overview

DriveOrbit Core is a fleet management backend service that enables organizations to efficiently manage their vehicle fleets, drivers, work assignments, and notifications. The system provides dual database support (PostgreSQL and Firebase Firestore) for enhanced reliability and real-time capabilities.

## ✨ Features

### 🚙 Vehicle Management
- Complete vehicle lifecycle management (CRUD operations)
- QR code generation for vehicle identification
- Vehicle status tracking (active, maintenance, etc.)
- Trip history recording
- Vehicle condition monitoring
- Fuel consumption tracking

### 👨‍💼 Driver Management
- Driver registration and authentication via Firebase
- Profile management with comprehensive driver details
- License management and validation
- Driver status tracking
- Integration with Firebase Authentication

### 📋 Work Assignment System
- Assignment creation and management
- Driver assignment to tasks
- Status tracking (pending, in-progress, completed)
- Date-based assignment filtering
- Urgency level management
- Customer information tracking

### 🔔 Notification System
- Real-time notification delivery
- User-specific notification management
- Read/unread status tracking
- Notification type categorization
- Bulk notification operations

### 🔧 Additional Features
- Dual database synchronization (PostgreSQL + Firestore)
- RESTful API architecture
- Firebase Authentication integration
- QR code generation and management
- Comprehensive logging and diagnostics
- CORS support for web applications

## 🛠 Technology Stack

### Backend
- **Framework**: Spring Boot 2.5.4
- **Language**: Java 17
- **Database**: PostgreSQL (Primary), Firebase Firestore (Secondary)
- **Authentication**: Firebase Authentication
- **Security**: Spring Security with password encoding
- **ORM**: Spring Data JPA with Hibernate

### Dependencies
- **Firebase Admin SDK**: Real-time database and authentication
- **Lombok**: Code generation and boilerplate reduction
- **Jackson**: JSON processing with JSR-310 support
- **ZXing**: QR code generation
- **Maven**: Build and dependency management

## 🏗 Architecture

The application follows a layered architecture pattern:

```
├── Controller Layer    (REST endpoints)
├── Service Layer      (Business logic)
├── Repository Layer   (Data access)
├── Model/Entity Layer (Data models)
└── Configuration     (Firebase, Security, Web)
```

### Module Structure
```
src/main/java/lk/driveorbit/DriveOrbit_core/
├── assignment/        # Work assignment management
│   ├── controller/    # Assignment REST controllers
│   ├── model/         # Assignment entities and DTOs
│   ├── repository/    # Assignment data access
│   └── service/       # Assignment business logic
├── driver/           # Driver management
│   ├── controller/   # Driver REST controllers
│   ├── model/        # Driver entities and Firebase models
│   ├── repository/   # Driver data access
│   └── security/     # Driver authentication
├── notification/     # Notification system
│   ├── controller/   # Notification REST controllers
│   ├── model/        # Notification entities
│   ├── repository/   # Notification data access
│   └── service/      # Notification business logic
├── vehicle/          # Vehicle management
│   ├── controller/   # Vehicle REST controllers
│   ├── entity/       # Vehicle entities
│   ├── model/        # Vehicle DTOs and messages
│   ├── repository/   # Vehicle data access
│   └── service/      # Vehicle business logic
└── config/           # Application configuration
    ├── FirebaseConfig.java
    ├── SecurityConfig.java
    └── WebConfig.java
```

## 📋 Prerequisites

- **Java**: JDK 17 or higher
- **Maven**: 3.6 or higher
- **PostgreSQL**: 12 or higher
- **Firebase Project**: With Authentication and Firestore enabled

## 🚀 Installation

### 1. Clone the Repository
```bash
git clone <repository-url>
cd DriveOrbit-core
```

### 2. Configure Database
Create a PostgreSQL database:
```sql
CREATE DATABASE driveorbit;
CREATE USER driveorbit_admin WITH PASSWORD 'your_pw';
GRANT ALL PRIVILEGES ON DATABASE driveorbit TO driveorbit_admin;
```

### 3. Configure Firebase
1. Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
2. Enable Authentication and Firestore
3. Generate a service account key
4. Place the `google-services.json` file in `src/main/resources/`

### 4. Build and Run
```bash
# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## ⚙ Configuration

### Application Properties
Update `src/main/resources/application.properties`:

```properties
# Application
spring.application.name=DriveOrbit-core

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/driveorbit
spring.datasource.username=database_name
spring.datasource.password=database_password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# QR Code Configuration
qrcode.upload.dir=upload/qrcodes
app.baseUrl=http://localhost:8080

# Logging
logging.level.lk.driveorbit.DriveOrbit_core=DEBUG
```

### Firebase Configuration
Ensure your `google-services.json` includes:
- Project configuration
- Firestore database URL
- Service account credentials

## 📚 API Documentation

### 🚙 Vehicle Endpoints
```
POST   /vehicles                    # Create vehicle
GET    /vehicles                    # Get all vehicles
GET    /vehicles/{id}               # Get vehicle by ID
PUT    /vehicles/{id}               # Update vehicle
DELETE /vehicles/{id}               # Delete vehicle
PATCH  /vehicles/{id}/status        # Update vehicle status
GET    /vehicles/{id}/qrcode        # Get vehicle QR code
```

### 👨‍💼 Driver Endpoints
```
POST   /api/drivers/register        # Register new driver
GET    /api/drivers/{email}         # Get driver by email
PUT    /api/drivers/{email}         # Update driver
DELETE /api/drivers/{email}         # Delete driver
```

### 📋 Work Assignment Endpoints
```
POST   /api/work-assignments        # Create assignment
GET    /api/work-assignments        # Get all assignments
GET    /api/work-assignments/{id}   # Get assignment by ID
PUT    /api/work-assignments/{id}   # Update assignment
DELETE /api/work-assignments/{id}   # Delete assignment
GET    /api/work-assignments/driver/{driverId}         # Get assignments by driver
GET    /api/work-assignments/driver/{driverId}/active  # Get active assignments
PATCH  /api/work-assignments/{id}/status              # Update assignment status
PATCH  /api/work-assignments/{id}/driver              # Assign driver
```

### 🔔 Notification Endpoints
```
POST   /api/notifications           # Create notification
GET    /api/notifications           # Get user notifications
GET    /api/notifications/{id}      # Get notification by ID
PATCH  /api/notifications/{id}/read # Mark as read
DELETE /api/notifications/{id}      # Delete notification
GET    /api/notifications/unread    # Get unread notifications
```

### 🔧 Utility Endpoints
```
GET    /trip-history               # Get all trip histories
POST   /trip-history               # Create trip history
GET    /diagnostics/firebase-status # Firebase diagnostics
GET    /firebase-test              # Test Firebase connection
```

## 🗄 Database Schema

### Core Entities

#### Vehicle
- `vehicleId` (Primary Key)
- `vehicleNumber`, `vehicleType`, `vehicleModel`
- `plateNumber`, `condition`, `fuelConsumption`
- `vehicleStatus`, `qrCodeURL`
- Equipment flags: `hasSpareTools`, `hasEmergencyKit`

#### Driver
- `id` (Primary Key)
- Personal info: `firstName`, `lastName`, `email`, `phoneNumber`
- License details: `licenseNumber`, `licenseType`, `licenseIssueDate`
- Employment: `joinDate`, `companyId`, `status`

#### WorkAssignment
- `assignId` (Primary Key)
- Assignment details: `description`, `urgency`, `status`
- References: `driverId`, `vehicleId`, `customerName`
- Tracking: `date`, `isComplete`

#### Notification
- `id` (Primary Key)
- Content: `type`, `title`, `message`
- Metadata: `userId`, `timestamp`, `isRead`

## 🔥 Firebase Integration

The application implements a dual-database strategy:

### Synchronization Strategy
- **Primary**: PostgreSQL for transactional data
- **Secondary**: Firestore for real-time capabilities
- **Automatic Sync**: All CRUD operations sync to both databases

### Firebase Collections
- `vehicles` - Vehicle data for real-time tracking
- `drivers` - Driver profiles and status
- `work_assignments` - Assignment tracking
- `notifications` - Real-time notification delivery
- `tripHistory` - Trip recording and analytics

### Error Handling
- Firebase operations are non-blocking
- PostgreSQL remains functional if Firebase is unavailable
- Comprehensive error logging for troubleshooting

## 🧪 Testing

### Run Tests
```bash
# Run all tests
./mvnw test

# Run with coverage
./mvnw test jacoco:report
```

### Test Static Pages
The application includes test pages for different modules:
- `/static/vehicle-test.html` - Vehicle management UI
- `/static/driver-test.html` - Driver management UI
- `/static/notification-test.html` - Notification testing
- `/static/work-assignment-test.html` - Assignment management



## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.


## 🔮 Future Enhancements

- Real-time dashboard with WebSocket support
- Mobile app integration
- Advanced analytics and reporting
- Route optimization algorithms
- IoT device integration for vehicle tracking
- Multi-tenant support for multiple organizations

---

**DriveOrbit Core** - Driving fleet management into the future! 🚀
