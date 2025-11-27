[![CloudDrive Backend CI](https://github.com/ayussh203/CloudDrive/actions/workflows/clouddrive-ci.yml/badge.svg)](https://github.com/ayussh203/CloudDrive/actions/workflows/clouddrive-ci.yml)

# CloudDrive

**CloudDrive** is a scalable, secure, and feature-rich file storage platform built with **Spring Boot** and **AWS Cloud services**. It provides a reliable solution for managing user-uploaded files, handling metadata, and offering features like file versioning and pre-signed upload URLs.

With an emphasis on security and performance, **CloudDrive** integrates **CI/CD pipelines**, **Docker** for containerization, and uses **AWS S3** for cloud storage, **AWS RDS (PostgreSQL)** for database management, and **AWS Secrets Manager** for secure handling of sensitive information. 

## Key Features

- **File Management**: Upload, download, and versioning of files with easy-to-use APIs.
- **File Versioning**: Version control on files with support for restoring previous versions.
- **Pre-signed Upload URLs**: Provides pre-signed URLs for users to directly upload files to S3, reducing backend load and optimizing performance.
- **User Authentication**: Secure user authentication and authorization via JWT tokens.
- **Rate Limiting**: Prevents abuse of APIs with rate-limiting, particularly for login and file upload endpoints.
- **CI/CD Pipeline**: Fully automated deployment pipeline using **GitHub Actions** for continuous integration and delivery.
- **Dockerized Application**: The entire application is containerized using Docker for ease of deployment and scalability.
- **Cloud Infrastructure**: Uses **AWS RDS** for database, **AWS S3** for file storage, and **AWS Secrets Manager** for managing sensitive credentials securely.

## Tech Stack

- **Backend**: Java, Spring Boot
- **Database**: PostgreSQL (AWS RDS)
- **Cloud Storage**: AWS S3
- **CI/CD**: GitHub Actions
- **Authentication**: JWT (JSON Web Token)
- **Containerization**: Docker
- **Rate Limiting**: Custom rate limiting middleware
- **Secrets Management**: AWS Secrets Manager

## Getting Started

1. Clone the repository:
    ```bash
    git clone https://github.com/ayussh203/CloudDrive.git
    cd CloudDrive
    ```

2. Set up environment variables in your `.env` file (refer to `.env.example`).

3. Build the Docker containers:
    ```bash
    docker compose build
    ```

4. Run the application:
    ```bash
    docker compose up
    ```

5. You can access the API at `http://localhost:8080`.

## Infrastructure Overview

The application utilizes **AWS infrastructure** for storage, database, and secrets management:
- **AWS S3**: For file storage.
- **AWS RDS**: For relational data storage (PostgreSQL).
- **AWS Secrets Manager**: For storing and retrieving sensitive data such as API keys and database credentials.

### **CI/CD Setup with GitHub Actions**
This project is configured with a **CI/CD pipeline** powered by GitHub Actions. The workflow automatically runs on:
- **Push events** to any branch
- **Pull requests** to main or any other feature branch

The CI pipeline builds and tests the backend code and pushes the Docker image to **Docker Hub** for deployment.
