# 🚀 Quick Start Guide - Production Deployment

## ⚡ 5-Minute Setup

### Step 1: Configure Environment
```bash
cp .env.example .env
# Edit .env with your actual values
```

### Step 2: Start with Docker
```bash
docker-compose up -d
```

### Step 3: Verify
```bash
curl http://localhost:8080/actuator/health
curl http://localhost:8080/api/products
```

**Done!** Your application is running at `http://localhost:8080`

---

## 📋 API Endpoints

### Public Endpoints

#### Get Products
```bash
GET /api/products
```

#### Get Gallery Items
```bash
GET /api/gallery
```

#### Create Checkout Session
```bash
POST /api/stripe/create-checkout-session
Content-Type: application/json

{
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}

Response: { "url": "https://checkout.stripe.com/..." }
```

#### Stripe Webhook (Stripe calls this)
```bash
POST /api/stripe/webhook
Headers: Stripe-Signature
```

### Monitoring Endpoints

```bash
GET /actuator/health      # Health check
GET /actuator/metrics     # Application metrics
GET /actuator/info        # Application info
```

---

## 🔧 Environment Variables Required

```bash
# Stripe (REQUIRED)
STRIPE_SECRET_KEY=sk_test_...
STRIPE_WEBHOOK_SECRET=whsec_...
STRIPE_SUCCESS_URL=http://localhost:8080/success.html
STRIPE_CANCEL_URL=http://localhost:8080/cancel.html

# Database (REQUIRED for production)
DATABASE_URL=jdbc:postgresql://localhost:5432/demoapp
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=your_password

# Email (REQUIRED)
EMAIL_USERNAME=your-email@gmail.com
EMAIL_PASSWORD=your-app-password
```

---

## 🐳 Docker Commands

```bash
# Start everything
docker-compose up -d

# View logs
docker-compose logs -f app

# Stop everything
docker-compose down

# Rebuild after code changes
docker-compose up -d --build

# Clean everything
docker-compose down -v
```

---

## 🧪 Testing Locally

### 1. Test Product Endpoint
```bash
curl http://localhost:8080/api/products
```

### 2. Test Checkout Creation
```bash
curl -X POST http://localhost:8080/api/stripe/create-checkout-session \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      {"productId": 1, "quantity": 1}
    ]
  }'
```

### 3. Test Webhook (with Stripe CLI)
```bash
# Install Stripe CLI
stripe listen --forward-to localhost:8080/api/stripe/webhook

# Trigger test event
stripe trigger checkout.session.completed
```

---

## ☁️ Deploy to AWS (Easiest Method)

### AWS Elastic Beanstalk
```bash
# Install EB CLI
pip install awsebcli

# Initialize
eb init -p docker demoamazonq --region us-east-1

# Create environment with RDS
eb create demoamazonq-prod \
  --database.engine postgres \
  --database.username postgres

# Set environment variables
eb setenv \
  STRIPE_SECRET_KEY=sk_live_... \
  STRIPE_WEBHOOK_SECRET=whsec_... \
  EMAIL_USERNAME=your-email@gmail.com \
  EMAIL_PASSWORD=your-password

# Deploy
eb deploy

# Open in browser
eb open
```

---

## 🔒 Security Checklist Before Going Live

- [ ] Change all default passwords
- [ ] Use Stripe LIVE keys (not test keys)
- [ ] Update CORS origins in `SecurityConfig.java`
- [ ] Enable HTTPS (use AWS ALB or CloudFront)
- [ ] Set up database backups
- [ ] Configure Stripe webhook URL in dashboard
- [ ] Test webhook signature verification
- [ ] Review security headers
- [ ] Set up monitoring alerts
- [ ] Test email sending

---

## 📊 What's Included

✅ **Security**
- Spring Security with CORS
- Rate limiting (100 req/min)
- CSRF protection
- XSS protection
- Security headers

✅ **Database**
- PostgreSQL support
- Flyway migrations
- Connection pooling
- Automatic schema creation

✅ **Resilience**
- Async email sending
- Retry logic (3 attempts)
- Graceful error handling
- Health checks

✅ **Monitoring**
- Spring Boot Actuator
- Prometheus metrics
- Structured logging
- Health endpoints

✅ **Performance**
- Product caching
- Connection pooling
- Async operations
- Optimized Docker image

---

## 🆘 Troubleshooting

### Application won't start
```bash
# Check logs
docker-compose logs app

# Common issues:
# 1. Database not ready - wait 30 seconds
# 2. Missing environment variables - check .env
# 3. Port 8080 in use - change PORT in .env
```

### Database connection failed
```bash
# Check database is running
docker-compose ps

# Restart database
docker-compose restart db

# Check connection
docker-compose exec db psql -U postgres -d demoapp
```

### Webhook signature verification fails
```bash
# Ensure webhook secret matches Stripe dashboard
# Test with Stripe CLI:
stripe listen --forward-to localhost:8080/api/stripe/webhook
```

---

## 📞 Support

- **Documentation**: See `REFACTORING_SUMMARY.md` for detailed changes
- **Deployment**: See `DEPLOYMENT.md` for cloud deployment options
- **Testing**: See `TESTING.md` for test coverage

---

## 🎯 Next Steps

1. **Immediate**: Configure `.env` file
2. **Immediate**: Test locally with `docker-compose up`
3. **Before Production**: Update CORS origins
4. **Before Production**: Switch to Stripe live keys
5. **Before Production**: Set up SSL/TLS
6. **Optional**: Add admin authentication
7. **Optional**: Add API documentation (Swagger)

---

**Status**: ✅ READY TO DEPLOY

**Time to Production**: ~2 hours (including testing)
