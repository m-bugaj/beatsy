## 📌 Project Overview (EN)

**BeatStore** is a modern web platform inspired by BeatStars, designed to buy and sell music beats. The project is built using microservice architecture to ensure scalability, modularity, and maintainability.

### ✅ Implemented Features:

#### 🌐 `api-gateway-web`:
- Centralized routing using Spring Cloud Gateway (WebFlux)

- Reactive stack with Spring WebFlux

- Routes for microservices defined in application.properties

- Route forwarding with path rewriting using RewritePath

- CORS configuration allowing cross-origin requests from the frontend (to be restricted later)

- CSRF protection enabled (important due to use of JWT in HttpOnly cookies)

- Eureka client integration for service discovery (lb://SERVICE-NAME URI)
  
- Per-request JWT verification using RSA256 public key in API Gateway for secure authentication

#### 🔐 `user-service`:
- User registration and login
- Google OAuth2 authentication
- JWT authentication (access and refresh tokens)
- RSA256 signing for JWT

#### 🛒 `marketplace-service`:
- Beat file upload (MP3, WAV, STEMS)
- Adding beat licenses

### 🛠️ Upcoming Features:
- Session implementation for storing `userHash`, `subscriptionHash`, etc.
- Support for `applyToAllBeats` to assign licenses to all beats on creation
- New `subscriptions` microservice for managing monthly billing and paid plans
- `role-permission` microservice with fine-grained access control via `@PreAuthorize`
- Additional marketplace features: beat editing, license purchases, search functionality
- Implement proper security headers to protect against XSS (e.g., Content-Security-Policy)
- Add CSRF protection using techniques
- Introduce an API Gateway layer to centralize authentication and authorization logic, ensuring that only authorized requests reach downstream services
- Use a modern, widely adopted gateway solution such as Spring Cloud Gateway (recommended for Spring-based apps), instead of the older Netflix Zuul
- Configure the gateway to perform pre-filtering of incoming requests
- Add JWT signature verification for every request that requires authorization
- Add RequestContext extraction from JWT per request
- Add Redis-based JWT blacklist: Store invalidated JWTs in Redis with TTL matching token expiry. On each request, reject tokens found in the blacklist.

---

## 📦 Tech Stack

- Java 23 + Spring Boot (microservices)
- PostgreSQL
- Docker + Docker Compose
- API Gateway + BFF pattern
- JWT Auth (Access & Refresh tokens)
- OAuth2 (Google)
