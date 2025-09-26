# Parking Lot Reservation System

## Technologies
- Spring Boot 3.x
- Java 17
- MySQL 8

## Setup

1. clone repository and import existing project 
2. maven -> update project 
3. run as -> maven clean 
4. run as -> maven install 
5. run as -> java application

#API calls
1. POST /floors – Create a parking floor
2. POST /slots – Create parking slots for a floor
3. POST /reserve – Reserve a slot for a given time range
4. GET /availability – List available slots for a given time range
5. GET /reservations/{id} – Fetch reservation details
6. DELETE /reservations/{id} – Cancel a reservation

## Swagger UI

the application is running:
	http://localhost:8081/swagger-ui/index.html
	
swagger api attached screenhot:
	/valcare/Swagger_Api_ScreenShots	
	
api running console log screenshot:
	park_application_console.png
	
JUnit test screenshot:
	JUnit_test_log.png


