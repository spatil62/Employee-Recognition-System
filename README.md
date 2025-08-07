# Employee Recognition System API

![CI Status](https://github.com/yourrepo/employee-recognition/workflows/CI/badge.svg)
![Coverage](https://img.shields.io/badge/coverage-85%25-green)

A modern GraphQL API for peer recognition in organizations with real-time notifications and analytics.

## Features

- **Real-time Recognition**: Send kudos with messages and emojis
- **Flexible Visibility**: Public, Private, or Anonymous recognitions
- **Comprehensive Analytics**: Team, keyword, and engagement metrics
- **Secure**: JWT authentication and role-based access control
- **Resilient**: Fallback mechanisms for notifications

## Tech Stack

- Java 17
- Spring Boot 3.1
- GraphQL
- WebSockets (STOMP)
- Reactor (for reactive streams)

## API Documentation

Explore the API using GraphiQL: `http://localhost:8080/graphiql`

### Sample Mutations

```graphql
mutation CreateRecognition {
  createRecognition(input: {
    senderId: "user1",
    receiverId: "user2",
    message: "Great work on the project!",
    emoji: "🚀",
    visibility: PUBLIC
  }) {
    id
    message
    timestamp
  }
}