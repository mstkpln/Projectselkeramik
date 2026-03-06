# Production Deployment Guide

## 🚀 Pre-Deployment Checklist

### 1. Environment Variables
Copy `.env.example` to `.env` and configure:
```bash
cp .env.example .env
# Edit .env with your production values
```

### 2. Database Setup
- Use PostgreSQL 15+ for production
- Run Flyway migrations automatically on startup
- Ensure database credentials are secure

### 3. Security Configuration
- Update CORS allowed origins in `SecurityConfig.java`
- Set strong database passwords
- Use SSL/TLS for database connections
- Enable HTTPS in production

### 4. Email Configuration
- Use app-specific passwords for Gmail
- Consider using AWS SES or SendGrid for production

## 🐳 Docker Deployment

### Build and Run
```bash
# Build the image
docker build -t demoamazonq:latest .

# Run with docker-compose
docker-compose up -d

# View logs
docker-compose logs -f app
```

### Production Docker Run
```bash
docker run -d \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DATABASE_URL=jdbc:postgresql://your-db:5432/demoapp \
  -e DATABASE_USERNAME=postgres \
  -e DATABASE_PASSWORD=secure_password \
  -e STRIPE_SECRET_KEY=sk_live_xxx \
  -e STRIPE_WEBHOOK_SECRET=whsec_xxx \
  -e EMAIL_USERNAME=your-email@gmail.com \
  -e EMAIL_PASSWORD=app_password \
  --name demoamazonq \
  demoamazonq:latest
```

## ☁️ AWS Deployment Options

### Option 1: AWS Elastic Beanstalk
```bash
# Install EB CLI
pip install awsebcli

# Initialize
eb init -p docker demoamazonq

# Create environment
eb create demoamazonq-prod

# Deploy
eb deploy
```

### Option 2: AWS ECS (Fargate)
1. Push image to ECR
2. Create ECS task definition
3. Configure RDS PostgreSQL
4. Set up Application Load Balancer
5. Configure environment variables in task definition

### Option 3: AWS App Runner
1. Push image to ECR
2. Create App Runner service
3. Configure environment variables
4. Connect to RDS database

## 🗄️ Database Migration

### Using Flyway (Automatic)
Migrations run automatically on startup from `src/main/resources/db/migration/`

### Manual Migration
```bash
# Connect to PostgreSQL
psql -h your-db-host -U postgres -d demoapp

# Run migration script
\i src/main/resources/db/migration/V1__initial_schema.sql
```

## 📊 Monitoring

### Health Check
```bash
curl http://localhost:8080/actuator/health
```

### Metrics (Prometheus)
```bash
curl http://localhost:8080/actuator/metrics
```

### Application Logs
```bash
# Docker
docker logs -f demoamazonq

# Local
tail -f logs/application.log
```

## 🔒 Security Hardening

1. **Enable HTTPS**: Use reverse proxy (nginx/Apache) or AWS ALB
2. **Firewall Rules**: Only allow necessary ports
3. **Database Security**: Use VPC, security groups, encrypted connections
4. **Secrets Management**: Use AWS Secrets Manager or Parameter Store
5. **Rate Limiting**: Already configured for webhook endpoint
6. **Security Headers**: Already configured in SecurityConfig

## 🧪 Pre-Production Testing

```bash
# Run all tests
mvn clean test

# Build production JAR
mvn clean package -Pprod

# Test with production profile locally
java -jar -Dspring.profiles.active=prod target/demoAmazonq-0.0.1-SNAPSHOT.jar
```

## 📈 Performance Tuning

### JVM Options
```bash
java -Xms512m -Xmx1024m \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     -jar app.jar
```

### Database Connection Pool
Already configured in `application-prod.properties`:
- Max pool size: 10
- Min idle: 5
- Connection timeout: 30s

## 🔄 CI/CD Pipeline

### GitHub Actions Example
```yaml
name: Deploy to Production

on:
  push:
    branches: [main]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
      - name: Build with Maven
        run: mvn clean package -DskipTests
      - name: Build Docker image
        run: docker build -t demoamazonq:${{ github.sha }} .
      - name: Push to ECR
        run: |
          aws ecr get-login-password | docker login --username AWS --password-stdin $ECR_REGISTRY
          docker push demoamazonq:${{ github.sha }}
```

## 🚨 Troubleshooting

### Application won't start
- Check environment variables are set
- Verify database connectivity
- Check logs: `docker logs demoamazonq`

### Webhook failures
- Verify Stripe webhook secret
- Check rate limiting isn't blocking requests
- Ensure CSRF is disabled for webhook endpoint

### Email not sending
- Verify SMTP credentials
- Check firewall allows port 587
- Review email service logs

## 📞 Support

For issues, check:
1. Application logs
2. Database connectivity
3. Environment variables
4. Stripe dashboard for webhook events
