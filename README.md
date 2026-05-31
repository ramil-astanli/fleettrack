# 🚗 FleetTrack — Vehicle Fleet Management System

FleetTrack is a robust and scalable monolithic backend platform built with **Java Spring Boot 4.x** designed to help organizations efficiently manage their vehicle fleets.

---

## 📋 Table of Contents

- [About](#about)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)

---

## 📖 About

FleetTrack provides a complete backend solution for managing vehicle fleets including:
- Vehicle registration and management
- Driver profile administration
- Maintenance and service history tracking
- Real-time GPS location tracking via WebSocket
- Automated maintenance reminders via Spring Scheduler
- Real-time notifications via Redis Pub/Sub

---

## ✅ Features

### Core Features
| Feature | Description |
|---|---|
| **Vehicle Management** | CRUD operations for vehicles with status tracking (ACTIVE, MAINTENANCE, INACTIVE) |
| **Driver Management** | Driver profiles with vehicle assignment |
| **Maintenance Tracking** | Service history with immutable audit records |
| **Real-Time Tracking** | Live GPS location streaming via WebSocket/STOMP |
| **JWT Authentication** | Stateless authentication with role-based access control |
| **Redis Caching** | Performance optimization with configurable TTL |
| **Flyway Migrations** | Version-controlled database schema management |
| **Filtering & Pagination** | Dynamic queries with Spring Specification |

### Bonus Features
| Feature | Description |
|---|---|
| **Redis Pub/Sub** | Real-time notification system for maintenance alerts |
| **Scheduled Tasks** | Automated maintenance reminders via Spring Scheduler |
| **Async Processing** | Non-blocking notification delivery via @Async |
| **PDF Reports** | Downloadable fleet status and maintenance reports |
| **Role-Based Access** | ADMIN and FLEET_MANAGER role separation |

---

## 🛠 Tech Stack

### Core
| Technology | Version | Purpose |
|---|---|---|
| **Java** | 21 | Programming language |
| **Spring Boot** | 4.0.6 | Application framework |
| **Spring Security** | 7.x | Authentication & Authorization |
| **Spring Data JPA** | 7.x | Database ORM |
| **Spring WebSocket** | 7.x | Real-time communication |
| **Spring Scheduler** | 7.x | Scheduled tasks |

### Database & Cache
| Technology | Version | Purpose |
|---|---|---|
| **PostgreSQL** | 15 | Primary relational database |
| **Redis** | 7 | Caching & Pub/Sub messaging |
| **Flyway** | 11.x | Database migration & versioning |

### Security
| Technology | Version | Purpose |
|---|---|---|
| **JWT (jjwt)** | 0.12.6 | Token-based authentication |
| **BCrypt** | - | Password hashing |

### Utilities
| Technology | Version | Purpose |
|---|---|---|
| **MapStruct** | 1.6.3 | DTO ↔ Entity mapping |
| **Lombok** | - | Boilerplate code reduction |
| **SpringDoc OpenAPI** | 3.0.3 | API documentation (Swagger) |
| **iText** | 8.0.4 | PDF report generation |
| **Docker** | - | Containerization |

---

## 🏗 Architecture

```
┌─────────────────────────────────────────────────┐
│                   Client                         │
│         (Swagger UI / Browser / Mobile)          │
└────────────────┬────────────────────────────────┘
                 │ HTTP / WebSocket
┌────────────────▼────────────────────────────────┐
│              Spring Boot Application             │
│                                                  │
│  ┌──────────┐  ┌──────────┐  ┌──────────────┐  │
│  │Controller│  │ Security │  │  WebSocket   │  │
│  │  Layer   │  │  (JWT)   │  │  Controller  │  │
│  └────┬─────┘  └──────────┘  └──────┬───────┘  │
│       │                             │           │
│  ┌────▼─────────────────────────────▼───────┐  │
│  │              Service Layer                │  │
│  │  VehicleService  DriverService            │  │
│  │  MaintenanceService  LocationService      │  │
│  │  AuthService  ReportService               │  │
│  └────┬──────────────────────────────────┬──┘  │
│       │                                  │      │
│  ┌────▼────┐    ┌────────────┐    ┌──────▼──┐  │
│  │  Redis  │    │ Scheduler  │    │  Redis  │  │
│  │  Cache  │    │  + Async   │    │ Pub/Sub │  │
│  └─────────┘    └────────────┘    └─────────┘  │
│                                                  │
│  ┌───────────────────────────────────────────┐  │
│  │           Repository Layer                │  │
│  │  VehicleRepo  DriverRepo  UserRepo        │  │
│  │  MaintenanceRepo                          │  │
│  └────────────────────┬──────────────────────┘  │
└───────────────────────┼─────────────────────────┘
                        │
           ┌────────────▼────────────┐
           │       PostgreSQL        │
           │  vehicles | drivers     │
           │  users | maintenance    │
           └─────────────────────────┘
```

---

## 🚀 Getting Started

### Prerequisites
- Java 21+
- Docker & Docker Compose
- Maven 3.8+

### 1. Clone the repository
```bash
git clone https://github.com/ramil-astanli/fleettrack.git
cd fleettrack
```

### 2. Set up environment variables

In IntelliJ IDEA:
```
Run → Edit Configurations → Environment Variables

JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
MAIL_USERNAME=your@gmail.com
MAIL_PASSWORD=your_password
```

### 3. Start Docker services
```bash
docker-compose up -d
```

This starts:
- **PostgreSQL** on port `5438`
- **Redis** on port `6385`

### 4. Run the application
```bash
mvn spring-boot:run
```

Or run `FleettrackApplication.java` from IntelliJ IDEA.

### 5. Access the application
| Service | URL |
|---|---|
| **Swagger UI** | http://localhost:8091/swagger-ui.html |
| **API Docs** | http://localhost:8091/api-docs |
| **WebSocket Test** | http://localhost:8091/location-test.html |

---

## 📚 API Documentation

### Authentication
| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/api/v1/auth/register` | Register new user | Public |
| POST | `/api/v1/auth/login` | Login & get JWT token | Public |

### Vehicles
| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/api/v1/vehicles` | Get all vehicles (filter, sort, paginate) | ADMIN, FLEET_MANAGER |
| GET | `/api/v1/vehicles/{id}` | Get vehicle by ID | ADMIN, FLEET_MANAGER |
| POST | `/api/v1/vehicles` | Create new vehicle | ADMIN |
| PUT | `/api/v1/vehicles/{id}` | Update vehicle | ADMIN |
| DELETE | `/api/v1/vehicles/{id}` | Delete vehicle | ADMIN |

### Drivers
| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/api/v1/drivers` | Get all drivers (filter, paginate) | ADMIN, FLEET_MANAGER |
| GET | `/api/v1/drivers/{id}` | Get driver by ID | ADMIN, FLEET_MANAGER |
| POST | `/api/v1/drivers` | Create new driver | ADMIN |
| PUT | `/api/v1/drivers/{id}` | Update driver | ADMIN |
| DELETE | `/api/v1/drivers/{id}` | Delete driver | ADMIN |

### Maintenance
| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/api/v1/vehicles/{vehicleId}/maintenance` | Get all maintenance records | ADMIN, FLEET_MANAGER |
| GET | `/api/v1/vehicles/{vehicleId}/maintenance/{id}` | Get record by ID | ADMIN, FLEET_MANAGER |
| POST | `/api/v1/vehicles/{vehicleId}/maintenance` | Add maintenance record | ADMIN, FLEET_MANAGER |
| DELETE | `/api/v1/vehicles/{vehicleId}/maintenance/{id}` | Delete record | ADMIN |

### Location (WebSocket)
| Method | Endpoint | Description | Access |
|---|---|---|---|
| WS | `/ws/location` | WebSocket connection endpoint | Authenticated |
| MSG | `/app/location` | Send GPS location update | Authenticated |
| SUB | `/topic/location` | Subscribe to all location updates | Authenticated |
| SUB | `/topic/location/{vehicleId}` | Subscribe to specific vehicle | Authenticated |
| GET | `/api/v1/locations` | Get all last known locations | ADMIN, FLEET_MANAGER |
| GET | `/api/v1/locations/{vehicleId}` | Get vehicle last location | ADMIN, FLEET_MANAGER |

### Reports
| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/api/v1/reports/fleet` | Download fleet PDF report | ADMIN |

---

## 📁 Project Structure

```
src/main/java/com/fleettrack/
│
├── config/                    # Configuration classes
│   ├── AsyncConfig.java       # Thread pool for @Async
│   ├── RedisConfig.java       # Redis cache & template
│   ├── RedisPubSubConfig.java # Pub/Sub channels & listeners
│   ├── SecurityConfig.java    # JWT & Spring Security
│   ├── SwaggerConfig.java     # OpenAPI documentation
│   └── WebSocketConfig.java   # STOMP & SockJS
│
├── controller/                # REST & WebSocket controllers
│   ├── AuthController.java
│   ├── DriverController.java
│   ├── LocationController.java
│   ├── MaintenanceController.java
│   ├── ReportController.java
│   └── VehicleController.java
│
├── dto/                       # Data Transfer Objects
│   ├── request/               # Incoming requests
│   └── response/              # Outgoing responses
│
├── entity/                    # JPA Entities
│   ├── BaseEntity.java        # id, createdAt, updatedAt, audit
│   ├── Driver.java
│   ├── MaintenanceRecord.java
│   ├── User.java
│   └── Vehicle.java
│
├── enums/                     # Enumerations
│   ├── UserRole.java          # ADMIN, FLEET_MANAGER
│   └── VehicleStatus.java     # ACTIVE, MAINTENANCE, INACTIVE
│
├── exception/                 # Exception handling
│   ├── BusinessException.java
│   ├── ErrorResponse.java
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
│
├── mapper/                    # MapStruct mappers
│   ├── DriverMapper.java
│   ├── MaintenanceMapper.java
│   └── VehicleMapper.java
│
├── pubsub/                    # Redis Pub/Sub
│   ├── AlertPublisher.java    # Publishes alerts to channels
│   └── AlertSubscriber.java   # Subscribes & processes alerts
│
├── repository/                # Spring Data JPA Repositories
│   ├── DriverRepository.java
│   ├── MaintenanceRepository.java
│   ├── UserRepository.java
│   └── VehicleRepository.java
│
├── scheduler/                 # Scheduled tasks
│   └── MaintenanceScheduler.java
│
├── security/                  # JWT Security
│   ├── JwtAuthFilter.java
│   ├── JwtService.java
│   └── UserDetailsServiceImpl.java
│
├── service/                   # Business logic
│   ├── AuthService.java
│   ├── DriverService.java
│   ├── LocationService.java
│   ├── MaintenanceService.java
│   ├── NotificationService.java
│   ├── ReportService.java
│   └── VehicleService.java
│
└── specification/             # JPA Specifications for filtering
    ├── DriverSpecification.java
    └── VehicleSpecification.java

src/main/resources/
├── db/migration/              # Flyway SQL migrations
│   ├── V1__create_users_table.sql
│   ├── V2__create_vehicles_table.sql
│   ├── V3__create_drivers_table.sql
│   ├── V4__create_maintenance_table.sql
│   └── V5__insert_initial_data.sql
├── static/
│   └── location-test.html     # WebSocket test page
└── application.yaml           # Application configuration
```

---

## 🔐 Security

### JWT Token Flow
```
POST /api/v1/auth/login
→ Returns JWT token (valid 24 hours)

All protected endpoints:
Authorization: Bearer <token>
```

### Role Permissions
| Operation | ADMIN | FLEET_MANAGER |
|---|---|---|
| View vehicles/drivers/maintenance | ✅ | ✅ |
| Create/Update vehicles & drivers | ✅ | ❌ |
| Add maintenance records | ✅ | ✅ |
| Delete any record | ✅ | ❌ |
| Generate PDF reports | ✅ | ❌ |
| View live locations | ✅ | ✅ |

---

## 🔄 Redis Pub/Sub Flow

```
MaintenanceScheduler (every night)
         ↓
AlertPublisher → "fleet.maintenance.alert" channel
         ↓
AlertSubscriber receives message
    ├── Sends email notification (log)
    └── Broadcasts to WebSocket /topic/alerts
         ↓
Connected browser clients receive real-time alert
```

---

## 🗄 Database Schema

```
users
├── id, username, password, email, role
├── created_at, updated_at, created_by, updated_by

vehicles
├── id, make, model, year, license_plate, status
├── created_at, updated_at, created_by, updated_by

drivers
├── id, first_name, last_name, license_number
├── phone, email, vehicle_id (FK → vehicles)
├── created_at, updated_at, created_by, updated_by

maintenance_records
├── id, vehicle_id (FK → vehicles)
├── description, service_date, next_service_date, cost
├── created_at, created_by
```

---

## 📦 Docker

```yaml
services:
  postgres:
    image: postgres:15-alpine
    ports: "5438:5432"

  redis:
    image: redis:7-alpine
    ports: "6385:6379"
```

```bash
# Start services
docker-compose up -d

# Stop services
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

---

## 👤 Default Users (from V5 migration)

| Username | Password | Role |
|---|---|---|
| `admin` | `password123` | ADMIN |
| `manager` | `password123` | FLEET_MANAGER |

---

*Built with ❤️ using Spring Boot 4.x*