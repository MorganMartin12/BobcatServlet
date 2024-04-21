# Java Networking Project: From TCP Server to Servlet Container

## Project Overview
This project is designed to guide the development of fundamental networking skills in Java by building several increasingly complex servers. We will start with a simple TCP server, evolve it into an HTTP server, then create a basic implementation of `javax.servlet`, and finally develop a simple servlet container to host and manage our servlets.

## Goals
- **TCP Server**: Understand the basics of socket programming in Java.
- **HTTP Server**: Learn about the HTTP protocol and how to handle HTTP requests and responses.
- **Simple `javax.servlet` Implementation**: Create a simplified version of the HttpServlet to handle HTTP requests.
- **Servlet Container**: Develop a basic servlet container to manage the lifecycle of servlets and route requests to them.

## Technologies Used
- Java
- TCP/IP Networking
- HTTP Protocol

## Project Structure  (For Now)
- `src/`
  - `com/`
    - `yourapp/`
      - `main/`
        - `MainApplication.java` - The entry point of the application.
      - `network/`
        - `MainServer.java` - Implements the TCP server.
        - `ClientHandler.java` - Handles client connections.

## Getting Started
1. **Set up your Java environment**: Ensure you have Java installed and configured.
2. **Clone the repository**: `git clone <repository-url>`
3. **Compile the project**: Navigate to the project directory and compile the code using `javac` or an IDE of your choice.
4. **Run the main application**: Execute `MainApplication.java` to start the server.

## Phase 1: TCP Server
The initial phase involves setting up a TCP server that listens on a specified port, accepts client connections, and echoes back any received messages.

## Phase 2: HTTP Server
Modify the TCP server to understand and respond to HTTP requests. It should be able to serve static content based on the request URLs.

## Phase 3: Simple Servlet Implementation
Develop a simple version of the HttpServlet that can process GET and POST requests, mimicking the functionality of `javax.servlet.http.HttpServlet`.

## Phase 4: Servlet Container
Create a basic servlet container that can dynamically load, initialize, and invoke servlets based on the incoming request paths.

