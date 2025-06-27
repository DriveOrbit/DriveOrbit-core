# DriveOrbit Docker Setup

This guide will help you run DriveOrbit using Docker containers.

## 🐳 Prerequisites

- **Docker**: Install Docker Desktop from [docker.com](https://www.docker.com/products/docker-desktop)
- **Docker Compose**: Usually included with Docker Desktop
- **Git**: To clone the repository

## 🚀 Quick Start

### Option 1: Automated Script (Recommended)

#### Windows:
```bash
start-docker.bat
```

#### Linux/Mac:
```bash
chmod +x start-docker.sh
./start-docker.sh
```

### Option 2: Manual Steps

1. **Build the application:**
   ```bash
   mvn clean package -DskipTests
   ```

2. **Start with Docker Compose:**
   ```bash
   docker-compose up --build -d
   ```

3. **Access the application:**
   - Application: http://localhost:8080
   - Database: localhost:5432

## 📊 Services

| Service | Port | Description |
|---------|------|-------------|
| DriveOrbit App | 8080 | Main Spring Boot application |
| PostgreSQL | 5432 | Database server |

## 🔧 Configuration

### Environment Variables

Key environment variables (defined in `docker-compose.yml`):

```yaml
# Database
DATABASE_URL=jdbc:postgresql://postgres:5432/driveorbit
DATABASE_USERNAME=driveorbit_admin
DATABASE_PASSWORD=DOB@admin123

# Application
SPRING_APPLICATION_NAME=DriveOrbit-core
SERVER_PORT=8080
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

### Volumes

- **postgres_data**: Database persistence
- **qrcode_uploads**: QR code file storage
- **Firebase credentials**: Mounted from host

## 📋 Useful Commands

### Starting and Stopping
```bash
# Start all services
docker-compose up -d

# Stop all services
docker-compose down

# Restart specific service
docker-compose restart app
```

### Monitoring
```bash
# View all logs
docker-compose logs -f

# View application logs only
docker-compose logs -f app

# View database logs only
docker-compose logs -f postgres

# Check service status
docker-compose ps
```

### Database Operations
```bash
# Connect to PostgreSQL container
docker-compose exec postgres psql -U driveorbit_admin -d driveorbit

# Backup database
docker-compose exec postgres pg_dump -U driveorbit_admin driveorbit > backup.sql

# Restore database
docker-compose exec -T postgres psql -U driveorbit_admin driveorbit < backup.sql
```

### Development
```bash
# Rebuild only the app (after code changes)
docker-compose build app
docker-compose up -d app

# View real-time application logs
docker-compose logs -f app

# Execute commands in app container
docker-compose exec app bash
```

## 🏥 Health Checks

The application includes health checks:

- **Application Health**: http://localhost:8080/actuator/health
- **Database Health**: Included in application health check

## 🗂️ File Structure

```
DriveOrbit-core/
├── Dockerfile              # Application container definition
├── docker-compose.yml      # Multi-service orchestration
├── .dockerignore           # Files to exclude from Docker context
├── start-docker.sh         # Linux/Mac startup script
├── start-docker.bat        # Windows startup script
├── .env.template           # Environment variables template
└── DOCKER_README.md        # This file
```

## 🛠️ Troubleshooting

### Common Issues

#### 1. Port Already in Use
```bash
# Check what's using port 8080
netstat -tulpn | grep 8080  # Linux/Mac
netstat -ano | findstr 8080  # Windows

# Stop conflicting services or change port in docker-compose.yml
```

#### 2. Database Connection Issues
```bash
# Check if PostgreSQL container is running
docker-compose ps postgres

# View PostgreSQL logs
docker-compose logs postgres

# Restart database
docker-compose restart postgres
```

#### 3. Application Won't Start
```bash
# Check application logs
docker-compose logs app

# Check if JAR file exists
ls -la target/DriveOrbit-core-0.0.1-SNAPSHOT.jar

# Rebuild application
mvn clean package -DskipTests
docker-compose build app
```

#### 4. Firebase Issues
```bash
# Check if credentials file exists
ls -la src/main/resources/google-services.json

# View application logs for Firebase errors
docker-compose logs app | grep -i firebase
```

### Performance Issues

#### Increase Memory Limits
Add to `docker-compose.yml`:
```yaml
services:
  app:
    deploy:
      resources:
        limits:
          memory: 2G
        reservations:
          memory: 1G
```

#### Monitor Resource Usage
```bash
# View resource usage
docker stats

# View container resource limits
docker-compose exec app cat /sys/fs/cgroup/memory/memory.limit_in_bytes
```

## 🔄 Development Workflow

### Code Changes
1. Make your code changes
2. Rebuild and restart:
   ```bash
   mvn clean package -DskipTests
   docker-compose build app
   docker-compose up -d app
   ```

### Database Schema Changes
1. Update your JPA entities
2. Restart the application:
   ```bash
   docker-compose restart app
   ```
3. Check logs to see schema updates:
   ```bash
   docker-compose logs app | grep -i "hibernate"
   ```

## 🔒 Security Considerations

### Production Deployment
- Change default passwords in `docker-compose.yml`
- Use Docker secrets for sensitive data
- Enable SSL/TLS termination
- Set up proper firewall rules
- Use non-root user in containers

### Environment Variables
Create a `.env` file for local overrides:
```bash
cp .env.template .env
# Edit .env with your values
```

## 📈 Scaling

### Horizontal Scaling
```bash
# Run multiple app instances
docker-compose up -d --scale app=3
```

### Load Balancer (Optional)
Uncomment the nginx service in `docker-compose.yml` and configure load balancing.

## 🆘 Support

If you encounter issues:

1. Check the logs: `docker-compose logs -f`
2. Verify all services are running: `docker-compose ps`
3. Check health endpoints: http://localhost:8080/actuator/health
4. Review this troubleshooting guide
5. Contact the development team

## 📝 Additional Resources

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Spring Boot Docker Guide](https://spring.io/guides/topicals/spring-boot-docker/)
- [PostgreSQL Docker Hub](https://hub.docker.com/_/postgres)
