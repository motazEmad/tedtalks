# Ted Talks API Project

## Description

This project is a simple API that allows users to interact with a database of Ted Talks. The API allows users to create, read, update, and delete Ted Talks from the database. The API also allows users to search for Ted Talks by speaker, title, or event.

## Technologies

This project was built using the following technologies:
- Java 21
- Spring Boot 2.6.3
- Postgres database 17
- Docker


## Getting Started
to start the database:
```
docker compose up -d
```

to start the application with local profile:
```
mvn spring-boot:run -D"spring-boot.run.profiles"=local
```

## Assumptions
- url is unique
- likes and views do not exceed long max value and is not less than 0
- duplicate entire are tolerated and will be added once to the database
- the Talk date do not have the day, only the month and year
- Authentication is not required, an improvement would implement oAuth2 for security
- parsing the file is done in memory, for large files it would be better to use a stream
