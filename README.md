# Car Sharing Service App

## Project Overview

Welcome to the **Car Sharing Service App**!  
This application is designed to simplify vehicle rentals, providing a seamless experience for both customers and administrators. Customers can search for available vehicles, make bookings, and manage their rentals, while administrators oversee vehicle listings and manage rental operations.

---

## Technologies Used

- **Spring Boot** - Backend framework for building the core application.  
- **Spring Security** - For managing authentication and authorization.  
- **Spring Data JPA** - For database interaction and ORM.  
- **MySQL** - Relational database for storing vehicle, user, and rental information.  
- **Docker** - Containerization for consistent deployment.  
- **Swagger** - API documentation and testing.  
- **MapStruct** - Object mapping for DTOs and entities.  
- **Liquibase** - Database change management and version control.  
- **JUnit & Mockito** - Unit testing for service and controller layers.  
- **Testcontainers** - Integration testing with real databases.  
- **Telegram Bots API** - Integrated bot for sending notifications about rentals.
- **Stripe API** - Integrated service for creating and processing online payments

---

## Key Functionalities

### **User Features**
- **Vehicle Browsing:** Users can view a list of available vehicles with details such as brand, model, type, and daily fee.  
- **Booking Management:** Users can book vehicles, and view active bookings.  
- **Notifications:** Users receive Telegram notifications about booking confirmations and reminders.
- **Online payments:** Users can create online payments and pay for a rental.

### **Admin Features**
- **Vehicle Management:** Admins can add, update, or remove vehicles from the system.  
- **Booking Oversight:** Admins can view all bookings, and modify their statuses.

---

## How to Run the Project

### **Prerequisites**
- Java 17 or higher  
- Maven for dependency management  
- Docker and Docker Compose for setting up the environment  

### **Steps to Launch Application**

1. **Clone the repository**
   ```
   git clone git@github.com:your-username/car-sharing-app.git
   cd car-sharing-app
   ```


2. **Clone the repository**
   ```
   TELEGRAM_BOT_USERNAME=<your_telegram_bot_username>
   TELEGRAM_BOT_TOKEN=<your_telegram_bot_token>
   
   JWT_SECRET=<your_jwt_secret>
   
   STRIPE_SECRET_KEY=<your_stripe_secret_key>
   
   MYSQLDB_DATABASE=<your_database_name>
   MYSQLDB_USER=<your_username>
   MYSQLDB_PASSWORD=<your_password>
   MYSQLDB_ROOT_PASSWORD=<your_root_password>
   
   SPRING_DATASOURCE_PORT=<your_spring_datasource_port>
   MYSQLDB_LOCAL_PORT=<your_local_port>
   MYSQLDB_DOCKER_PORT=<your_docker_port>
   SPRING_LOCAL_PORT=<your_spring_local_port>
   SPRING_DOCKER_PORT=<your_spring_docker_port>
   ```
   
3. **Build and start the application**
Use Docker Compose to set up the containers:
  docker-compose up --build

The application will be accessible at http://localhost:<SPRING_LOCAL_PORT>/.

## Running Tests
To run unit and integration tests using Testcontainers:
```
mvn clean test
```
## API Documentation
Swagger UI is available for testing the API and is accessible at:
http://localhost:<SPRING_LOCAL_PORT>/swagger-ui.html

This documentation includes endpoints for:

* Browsing and managing vehicles
* Booking vehicles
* Managing rentals and users

## Challenges faced 
### Telegram Bot Integration
Challenge:
Configuring the Telegram bot for notifications was tricky due to incorrect token and username settings.
Solution:
Validated token and username using Telegram's BotFather and reconfigured initialization logic to handle errors gracefully.

### Database Migration Issues
Challenge:
Liquibase migrations occasionally failed due to misconfigured YAML scripts.
Solution:
Reviewed and corrected migration scripts locally before deploying them to production environments.

### Testing with Docker
Challenge:
Setting up isolated testing environments using Testcontainers required precise database configurations.
Solution:
Leveraged official documentation and examples to ensure smooth integration with MySQL and Docker.

### Stripe API Integration
Challenge:
There was a need to provide an opportunity to create online payments for rentals.
Solution:
Leveraged official documentation and examples to ensure smooth integration with application.

## Postman Collection
A Postman collection with all API requests for testing and interacting with the application is included in the project.

### How to Use
1. Locate the collection in the postman folder:
  * File path: ./postman/car_sharing_postman_collection.json

Import into Postman:

2. Open Postman.
  * Click the Import button in the top-left corner.
  * Choose the file from the postman folder.
  * The collection will be added to your workspace, ready for testing!

This project demonstrates my ability to design and develop a modern backend application for real-world scenarios using Java and related technologies.
Feel free to explore, test, and contribute! 😊








