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

#### 🌍 `discovery-server`:
- Centralized service registry using Netflix Eureka
- Central point where all microservices (user-service, marketplace-service, …) register themselves
- Enables dynamic service discovery (no need to hardcode URLs between services)
- Provides a web dashboard at http://localhost:8761
 to monitor registered instances

#### 🔐 `user-service`:
- User registration and login
- Google OAuth2 authentication
- JWT authentication (access and refresh tokens)
- RSA256 signing for JWT

#### 🛒 `marketplace-service`:
- Beat file upload (MP3, WAV, STEMS)
- Adding beat licenses
- (Upcoming) Real-time playback tracking — Redis and Kafka will be used to handle high-throughput “beat played” events for real-time listener and popularity stats.
- (Upcoming) Short-term data analytics — MongoDB will store detailed play events (user, country, timestamp) for flexible querying and 7–30 day trend analysis.
- (Upcoming) Long-term reporting — Aggregated statistics (daily/weekly plays, unique listeners, etc.) will be periodically transferred to PostgreSQL for historical insights and producer reports.

### 🛠️ Upcoming Features:
- Separate authentication from user management — The current user-service handles both user data and authentication logic (JWT, login, etc.). This will be split into two independent microservices to follow proper domain boundaries and improve scalability.
- auth-service will handle authentication, JWT/refresh tokens, OAuth2 login, and role management. user-service will manage user profiles, public information, and relationships (followers, subscriptions, etc.).
- Session implementation for storing `userHash`, `subscriptionHash`, etc.
- Support for `applyToAllBeats` to assign licenses to all beats on creation
- New `subscriptions` microservice for managing monthly billing and paid plans
- `role-permission` microservice with fine-grained access control via `@PreAuthorize`
- Additional marketplace features: beat editing, license purchases, search functionality
- Implement proper security headers to protect against XSS (e.g., Content-Security-Policy)
- Add CSRF protection using techniques
- Introduce an API Gateway layer to centralize authentication and authorization logic, ensuring that only authorized requests reach downstream services
- Configure the gateway to perform pre-filtering of incoming requests
- Add JWT signature verification for every request that requires authorization
- Add RequestContext extraction from JWT per request
- Add Redis-based JWT blacklist: Store invalidated JWTs in Redis with TTL matching token expiry. On each request, reject tokens found in the blacklist.
- Implement OpenAPI-Based API Contracts
  - Introduce **contract-first communication** across all microservices (`auth-service`, `user-service`, `marketplace-service`, etc.) using **OpenAPI 3**  
  - Automatically **generate and publish** `openapi.yaml` specs for each service, then use **OpenAPI Generator** to create clients and DTOs from those contracts  
  - Add **API versioning and CI/CD validation** to detect and prevent breaking changes between microservice APIs
- Extend `ContentVisibility` enum with a new status (e.g. `COPYRIGHT_CLAIMED`)  
  - Used when a copyright infringement claim is filed against a beat  
  - Beats with this status should be **hidden from all users**, including the author, until the claim is resolved  
  - Only **admins** can view and manage content under this status  

## 🧩 Future Microservice Decomposition Plan

In the future, the **BeatStore** platform could be further decomposed into the following microservices for better scalability, modularity, and team autonomy:

<details>
<summary>📜 Click to expand — View all 40 potential microservices</summary>

### 🎧 1. CONTENT & BEATS DOMAIN
1. **Beat Service** — manages beat metadata (title, BPM, mood, tags, etc.).
2. **Beat File Service** — handles upload and storage of audio files (WAV, MP3, STEMS).
3. **Beat Genre Service** — manages available genres and beat–genre relations.
4. **Beat License Service** — defines license types (basic, premium, exclusive).
5. **Beat Visibility Service** — controls beat visibility (`PUBLIC`, `PRIVATE`, `CLAIMED`, etc.).
6. **Beat Analytics Service** — tracks plays, downloads, and engagement stats.
7. **Beat Recommendation Service** — recommends beats based on user taste or behavior.
8. **Beat Claim Service** — manages copyright or ownership claims.
9. **Beat Moderation Service** — handles moderation actions and content reports.
10. **Beat Search Service** — enables search and filtering across beats and metadata.

---

### 👤 2. USER & AUTH DOMAIN
11. **Auth Service** — manages authentication, JWT, and OAuth2 (Google, Facebook).
12. **User Service** — stores public user profiles (display name, avatar, bio, links).
13. **User Verification Service** — handles email confirmation, KYC, or 2FA.
14. **User Settings Service** — stores preferences like theme, language, and notifications.
15. **User Badge Service** — manages producer levels, achievements, and badges.
16. **User Relationship Service** — handles follow/block functionality.
17. **Notification Service** — delivers email, push, and in-app notifications.
18. **Subscription Service** — manages premium plans or artist subscriptions.
19. **Identity Service** — provides unique user identifiers (UUID/hash).
20. **Activity Service** — logs user sessions and activity history.

---

### 💰 3. PAYMENT & MARKETPLACE DOMAIN
21. **Marketplace Service** — core service for listing and browsing beats.
22. **Cart Service** — stores items users intend to purchase.
23. **Order Service** — manages order lifecycle (creation, confirmation, delivery).
24. **Payment Gateway Service** — integrates with providers like Stripe or PayPal.
25. **Billing Service** — issues invoices and stores transaction history.
26. **Refund Service** — handles refunds and payment disputes.
27. **Revenue Split Service** — manages royalty splits for co-producers.
28. **Discount Service** — provides coupon and promotional code management.
29. **Tax Service** — calculates VAT or sales tax per region.
30. **Wallet Service** — user balance, withdrawals, and earnings tracking.

---

### 💬 4. SOCIAL & COMMUNITY DOMAIN
31. **Comment Service** — allows users to comment on beats and profiles.
32. **Like Service** — manages likes, favorites, and reactions.
33. **Playlist Service** — enables creating personal beat collections.
34. **Collaboration Service** — supports co-production and shared ownership.
35. **Messaging Service** — private chats and collaboration invites.
36. **Feed Service** — generates personalized activity feeds.
37. **Report Service** — processes abuse or copyright violation reports.
38. **Contest Service** — manages beat battles and community challenges.
39. **Achievement Service** — tracks achievements and gamification rewards.
40. **Recommendation Feed Service** — generates personalized homepage content.

</details>


---

## 📦 Tech Stack

- Java 23 + Spring Boot (microservices)
- PostgreSQL
- Docker + Docker Compose
- API Gateway + BFF pattern
- JWT Auth (Access & Refresh tokens)
- OAuth2 (Google)
