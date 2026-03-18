# Order Management Service

## Overview

This is a simple Spring Boot application developed as part of a Java/Spring Boot take-home assignment.

The application provides REST APIs to create and retrieve orders and also includes logic to calculate monthly revenue.

Technologies used:

* Java 17
* Spring Boot 3
* Maven
* Bean Validation
* In-memory storage

---

# Features

The application supports the following operations:

### 1. Create Order

Create a new order using the POST API.

Endpoint

```
POST /orders
```

Example Request

```json
{
  "customerType": "PREMIUM",
  "amount": 500,
  "orderDate": "2026-03-13T12:00:00"
}
```

Response

```
201 CREATED
```

---

### 2. Get Order By ID

Fetch a specific order using its id.

Endpoint

```
GET /orders/{id}
```

Example

```
GET /orders/1
```

---

### 3. Get Orders By Month

Retrieve all orders for a specific month.

Endpoint

```
GET /orders?month=YYYY-MM
```

Example

```
GET /orders?month=2026-03
```

---

# Revenue Calculation Logic

Monthly revenue is calculated using the following rules:

* Orders are grouped by **YearMonth**
* **PREMIUM customers receive a 10% discount**
* Orders with **null or negative amounts are ignored**
* Implemented using **Java Streams**

---

# Validation and Error Handling

The application uses:

* **Bean Validation** for request validation
* **Global Exception Handling** using `@RestControllerAdvice`
* Proper HTTP status codes such as:

```
201 CREATED
400 BAD REQUEST
404 NOT FOUND
```

---

# Storage

Orders are stored using **in-memory storage** (no database).

This keeps the application simple and satisfies the assignment requirement.

---

# Assumptions

* Orders are stored only in memory.
* Duplicate orders are allowed.
* The application does not persist data after restart.
* Customer types supported: **REGULAR** and **PREMIUM**.

# Part-4 (Assessment)

I choose Spring Boot for building modern applications such as REST APIs and microservices because it is simple to use, requires very little setup, and allows faster development and deployment.
Would use OSGi when the application needs to be split into multiple independent modules, and when there is a requirement to update or manage those modules at runtime without restarting the system.
OSGi is not a good fit for normal applications like basic CRUD or REST services, because it might cause extra complexity and makes the system harder to maintain when such advanced modularity is not really needed.