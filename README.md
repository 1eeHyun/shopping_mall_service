# Shopping Mall Project

A **Spring Boot + MySQL + PayPal + Bootstrap** based e-commerce shopping
mall project.\
This project implements both **backend services** and **frontend UI**
for a complete shopping mall system.

------------------------------------------------------------------------

## Tech Stack

-   **Backend**: Java 17, Spring Boot 3, Spring Security (JWT), Spring
    Data JPA, Hibernate\
-   **Database**: MySQL 8\
-   **Payment**: PayPal Sandbox API\
-   **Frontend**: Thymeleaf (HTML templates), Bootstrap 5, CSS,
    JavaScript\
-   **Other**: Gradle, Lombok, JPA/Hibernate, Exception Handling, REST
    API

------------------------------------------------------------------------

## Project Structure

    src/
     ┣ main/
     ┃ ┣ java/com/ldh/shoppingmall/
     ┃ ┃ ┣ api/                  # REST APIs for Cart, Payment, Product
     ┃ ┃ ┣ config/               # PayPal, Security, CORS, Data Initialization
     ┃ ┃ ┣ controller/           # MVC Controllers (Cart, Order, Product, User, Auth)
     ┃ ┃ ┣ dto/                  # Data Transfer Objects
     ┃ ┃ ┣ entity/               # Entities (User, Product, Order, Cart)
     ┃ ┃ ┣ exception/            # Global Exception Handling
     ┃ ┃ ┣ mapper/               # DTO ↔ Entity Mappers
     ┃ ┃ ┣ repository/           # JPA Repositories
     ┃ ┃ ┣ security/             # Custom UserDetails for JWT & Security
     ┃ ┃ ┣ service/              # Business Logic (Cart, Order, Payment, User)
     ┃ ┃ ┗ ShoppingmallApplication.java
     ┃ ┗ resources/
     ┃   ┣ static/               # CSS, JS, Images
     ┃   ┣ templates/            # Thymeleaf Templates (HTML views)
     ┃   ┗ application.properties
     ┣ test/                     # Unit and Integration Tests

------------------------------------------------------------------------

## Backend Features

-   **User Authentication** (Login, Signup) with JWT & Spring Security\
-   **Product Management** (list, detail, search)\
-   **Shopping Cart** (add, update, remove items, guest cart)\
-   **Order Processing** (manual & default order processor)\
-   **Payment Integration** with PayPal Sandbox\
-   **Exception Handling** with `GlobalExceptionHandler`\
-   **REST APIs** for cart, product, payment, user

------------------------------------------------------------------------

## Frontend Features (Bootstrap + Thymeleaf)

-   **Home Page** (`home.html`)
    -   Product listing, categories, navigation bar
-   **Authentication** (`login.html`, `signup.html`)
    -   User login & registration
-   **Cart Page** (`cart.html`)
    -   Add/remove products, update quantities
-   **Product Detail** (`detail.html`)
    -   View detailed product info
-   **Layout** (`layout.html`)
    -   Shared layout with Bootstrap Navbar/Footer

------------------------------------------------------------------------

## ⚙️ Configuration

### MySQL Database

``` properties
spring.datasource.url=jdbc:mysql://localhost:3306/shoppingmall_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```

### PayPal API

``` properties
paypal.client.id=your-client-id
paypal.client.secret=your-client-secret
paypal.mode=sandbox  # or live
```

------------------------------------------------------------------------

## Running the Project

### 1. Clone the Repository

``` bash
git clone https://github.com/your-username/shoppingmall.git
cd shoppingmall
```

### 2. Build & Run with Gradle

``` bash
./gradlew bootRun
```

### 3. Access the Application

-   **Frontend**: <http://localhost:8080>\
-   **API Docs**: Available in controller classes

------------------------------------------------------------------------

## Testing

Run tests with:

``` bash
./gradlew test
```
