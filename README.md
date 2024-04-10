# Ticket Master Backend Application Clone

This project replicates the core functionalities of a backend application that powers a ticketing platform like
TicketMaster (https://ticketmaster.sg/). This was done by me for one of my projects in my University course. The project
was done in a team of 6 members and I was responsible for the backend development of the application.

# Tech Stack

- Springboot
- Java 21
- Stripe (Payment Service)
- Brevo (Notification Service)
- AWS S3 (Object Storage)
- AWS RDS (MySQL Database)
- Swagger (API Documentation)

# Getting Started

## 1. Prerequisites

- Java 21+ installed
- Maven 3.9.0+ installed
- Docker installed
- AWS Account
- Stripe Account
- Brevo Account

## 2. Set up database

- Create a MySQL Database, this can be done using AWS or any cloud provider of your choice, or you can use your local
  MySQL database.
- Create the Application's database and tables using the `schema.sql` file in the `src/main/resources` folder.
- Add the database url and password to the given properties in the `application.yml` file in the `src/main/resources`
  folder.

## 3. Set up AWS S3

- Create an AWS S3 bucket and add its meta data (e.g., Region, Bucket name) to the `application.yml` file in the
  `src/main/resources` folder.
- Add the AWS access key and secret key to the `application.yml` file in the `src/main/resources` folder.
- In your bucket, make sure to create a subfolder called `qrCodeImgUrl` where the QR code images will be stored.

## 4. Set up API Keys

- Create accounts with Stripe and Brevo and obtain API keys.
- Configure the API keys in the application.properties file.

## 5. Run the application

- With maven installed, run the following commands in the root directory of the project to start the application:
    - `mvn clean install` (Only required for the first time you are running the project to install the dependencies)
    - `mvn spring-boot:run`
- The application will start on port 8080 by default. You can access the application at `http://localhost:8080`.

## 6. API Documentation

- This project uses Swagger for API documentation. You can access the API documentation at
  ```
  http://localhost:8080/swagger-ui/index.html.
    ```
- The API documentation provides information about the endpoints, request and response bodies.

# Disclaimer:

This project is for educational purposes only and does not intend to infringe upon TicketMaster's intellectual property.
