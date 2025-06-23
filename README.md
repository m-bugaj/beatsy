## 📌 Project Overview (EN)

**BeatStore** is a modern web platform inspired by BeatStars, designed to buy and sell music beats. The project is built using microservice architecture to ensure scalability, modularity, and maintainability.

### ✅ Implemented Features:

#### 🔐 `user-service`:
- User registration and login
- Google OAuth2 authentication
- JWT authentication (access and refresh tokens)

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

---

## 📦 Tech Stack

- Java 23 + Spring Boot (microservices)
- PostgreSQL
- Docker + Docker Compose
- API Gateway + BFF pattern
- JWT Auth (Access & Refresh tokens)
- OAuth2 (Google)
