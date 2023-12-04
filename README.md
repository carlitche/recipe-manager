# RECIPE MANAGEMENT APPLICATION

This REST Api application provides a platform for users to manage their favorite recipes.  
It facilitates the addition, updating, removal, and retrieval of recipes with a focus on ease of use and flexibility. 
Users can filter recipes based on criteria such as vegetarian status, number of servings, specific ingredients, and search terms within instructions.

## Architecture

- **Framework & Language**: The application is built using Java 17 and Spring Boot, leveraging the Spring boot ecosystem.
- **Database**: The app uses a  relational database to persistently store recipe data. Spring Data JPA is utilized for ORM capabilities.
- **API Design**: The application follows RESTful principles, providing clear and intuitive endpoints for managing recipes.
- **Testing**: The application includes comprehensive unit and integration tests to ensure reliability and stability.
- **Additional Components**: This project make use of TestContainer for testing, so it is essential to have Docker install on local machine.

## Getting Started

### Prerequisites

To run this application, you need:
* Java JDK 17
* Maven

### Installing

Follow these steps to set up the project locally:

	1.	Clone the repository:

    git clone https://github.com/carlitche/recipe-manager
    

	2.	Navigate to the project directory:

    cd [project-name]


	3.	Install dependencies:

    mvn install

### Running the Application

To run the application, use the following command:

mvn spring-boot:run

### Running the Tests

To run the automated tests:

	•	For  tests:

    mvn test


### Docker Compose support
This project contains a Docker Compose file named `compose.yaml`.
In this file, the following services have been defined:

* postgres: [`postgres:latest`](https://hub.docker.com/_/postgres)
* pgadmin: [`dpage/pgadmin4:latest`](https://hub.docker.com/r/dpage/pgadmin4)


### Testcontainers support

This project uses [Testcontainers at development time](https://docs.spring.io/spring-boot/docs/3.2.0/reference/html/features.html#features.testing.testcontainers.at-development-time).

Testcontainers has been configured to use the following Docker images:

* [`postgres:latest`](https://hub.docker.com/_/postgres)


