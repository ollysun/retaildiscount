# retaildiscount

# Retail Store Discounts

## Description
This project calculates the net payable amount for a bill in a retail store considering various discounts.

1. 30% discount for store employees.
2. 10% discount for store affiliates.
3. 5% discount for customers who have been with the store for over 2 years.
4. $5 discount for every $100 on the bill.
5. Percentage-based discounts do not apply to groceries.
6. Only one percentage-based discount can be applied per bill.

## Technologies Used
- Java 17
- Spring Boot
- JPA/Hibernate
- H2 Database (for testing purposes)
- JUnit 5
- Mockito

## How to Run

1. Clone the repository:
    ```bash
    git clone https://github.com/ollysun/retaildiscount
    cd zenchallenge
    ```

2. Build and run the application:
    ```bash
    ./mvnw spring-boot:run
    ```

3. To run the tests and generate coverage reports:
    ```bash
    ./mvnw test
    ```

## API Endpoints

### User Controller
`We have three type of UserType: EMPLOYEE, AFFLICATE, CUSTOMER`
- `GET /localhost:8282/users`: Get all users
- `GET /localhost:8282/users/{id}`: Get user by ID
- `POST /localhost:8282/users`: Create a new user
```
{
    "name": "John Doe",
    "userType": "EMPLOYEE",
}
```
- `PUT /localhost:8282/users/{id}`: Update user
```
{
    "name": "John Doe2",
    "userType": "AFFILIATE",
}
```
- `DELETE /localhost:8282/users/{id}`: Delete a user by ID

### Product Controller
- `GET /localhost:8282/products`: Get all products
- `GET /localhost:8282/products/{id}`: Get product by ID
- `POST /localhost:8282/products`: Create a new product
```
{
    "name": "Milk",
    "category": "GROCERY",
    "price": 2.5
}
```
- `PUT /localhost:8282/products/{id}`: Update product
```
{
    "name": "Milky",
    "category": "GROCERY",
    "price": 2.5
}
```
- `DELETE /localhost:8282/products/{id}`: Delete a product by ID

### Bill Controller
- `GET /localhost:8282/bills`: Get all bills
- `GET /localhost:8282/bills/{id}`: Get bill by ID
- `POST /localhost:8282/bills`: Create a new bill

- `DELETE /localhost:8282/bills/{id}`: Delete a bill by ID

## UML Class Diagram
![UML Class Diagram](./UML_Class_Diagram.png)
