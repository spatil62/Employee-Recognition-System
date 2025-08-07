🚀 Employee Recognition System – GraphQL API
A real-time, role-aware recognition platform enabling employees to celebrate each other with kudos, emojis, and messages — all while supporting scalable analytics and future-facing features.
💡 Designed as part of the Nest Solutions Engineering Assessment, this project mirrors real-world ambiguity, trade-offs, and architectural decision-making under evolving requirements.


🌟 Core Features
✨ Send Recognitions
Employees can recognize peers with a message and emoji in real-time.
🔐 Flexible Visibility Controls
Recognitions can be public, private, or anonymous, with business rules enforced at the API level.
📡 Real-Time Notifications
Subscriptions allow employees to receive recognitions instantly.
📊 Analytics-Ready Design
Built to support team-level, keyword-based, and engagement analytics (sender/receiver metrics).
🧱 Extensible Architecture
Thoughtfully structured to support upcoming features: badges, likes, comments, reactions, and Slack/Teams integration.
🔒 Security & Access Control
Includes role definitions (EMPLOYEE, MANAGER, ADMIN, HR), laying groundwork for RBAC and secure query handling.


⚙️ Tech Stack
Layer	                Tech Stack
Language	            Java 17
Framework	            Spring Boot 3.1
API Design	            GraphQL (Official Spring Boot starter)
Real-Time	            Reactor (Project Reactor), WebSocket / Flux
Testing	                JUnit 5, Mockito, Reactor Test
Storage	                In-memory repository (mocked for simplicity)


📁 Project Structure
├── src/main/java/com/nest/recognition
│   ├── model          # Domain entities (Recognition, User, Enum types)
│   ├── dto            # GraphQL DTOs (Input, Output)
│   ├── service        # Business logic (RecognitionService, NotificationService)
│   ├── repository     # In-memory storage layers
│   ├── subscription   # Real-time Flux-based subscription logic
│   └── config         # WebSocket, GraphQL, and app configuration
├── src/test/java/…    # Unit and integration tests
├── resources/graphql  # Schema definition
│   └── schema.graphqls
└── README.md


🧠 Architectural Decisions & Trade-offs
1. Real-time > Batch:
Implemented live subscription using Flux for true real-time delivery. Since the complexity was manageable within the scope, real-time was prioritized over a 10-minute batch fallback.
2. In-Memory Repositories:
To keep the focus on API logic and extensibility, data persistence was mocked with in-memory storage. This can be easily swapped with JPA or NoSQL without changing the business logic.
3. No RBAC Enforcement Yet:
Roles like EMPLOYEE, MANAGER, HR, and ADMIN are defined but access control enforcement is deferred. The system is designed to allow easy integration of role-based access control using interceptors or GraphQL directives.
4. Keyword & Team Analytics:
Analytics design supports keyword extraction via simple message parsing. Team-based analytics is planned via a team field in the User entity for future expansion.
5. Slack/Teams Integration:
Integration with internal messaging platforms (like Slack or Microsoft Teams) is not implemented yet. However, NotificationService is structured with extension points to support this in the future.


🧪 Sample GraphQL API Usage
🔧 Mutation: Create a Recognition
mutation {
  createRecognition(input: {
    senderId: "user1",
    receiverId: "user2",
    message: "Amazing leadership on that release!",
    emoji: "🔥",
    visibility: ANONYMOUS
  }) {
    id
    message
    visibility
    timestamp
  }
}

📬 Subscription: Real-Time Recognition Feed
subscription {
  recognitionReceived(userId: "user2") {
    senderId
    message
    emoji
    visibility
  }
}

📊 Query: Keyword Analytics
query {
  keywordStats {
    keyword
    count
  }
}

🔐 Access Control (Planned)
Role	            Capabilities
EMPLOYEE	        Send/receive recognitions
MANAGER	            View team analytics
HR	                Access org-wide recognition logs
ADMIN	            Manage users and content
🔧 Enforcement to be implemented via schema directives or service-level guards.


🚀 Future Enhancements
✅ Persistence with PostgreSQL or MongoDB
✅ OAuth2 + JWT Auth
✅ Slack/MS Teams integration via NotificationService
✅ Comments, likes, and reaction support
✅ RBAC enforcement on analytics and queries


📝 How to Run Locally
Clone the repo
Open in VS Code
Run the Spring Boot application
Open http://localhost:8080/graphiql
Try out the provided queries and subscriptions