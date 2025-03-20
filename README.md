Mastering API Mocking with WireMock
===================================

## What is WireMock?
WireMock is a flexible and powerful API mocking tool that allows developers to simulate HTTP-based APIs. It provides a simple and intuitive interface for creating mock APIs, making it easier to test applications that depend on external services. With WireMock, you can create mock responses, simulate various scenarios, and validate the behaviour of your application under different conditions.

## Getting Started with WireMock
To get started with WireMock, you can follow these steps:
1. **Installation**: You can install WireMock using various methods, including using a standalone JAR file, Docker, or as a dependency in your project. Here we will use the standalone JAR method for simplicity. Download the latest WireMock standalone JAR from the official website. 
2. You can use the script located in `bin/download-wiremock.sh` to download the latest version of WireMock standalone JAR. Make sure to give it execute permissions using `chmod +x bin/download-wiremock.sh` and then run the script.
3. Put the downloaded JAR file in a `bin` directory in your project for easy access.
4. Run it using the command line:
    ```bash
    java -jar wiremock-standalone-x.x.x.jar --port 8080 --verbose
    ```
5. Check if WireMock is running by navigating to `http://localhost:8080/__admin` in your browser or IDE testing tool (like Bruno). You should see the JSON response with actually default mappings.
