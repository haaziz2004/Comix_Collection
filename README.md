# Comix

A web application for viewing comics and managing a personal collection.

## About this project

This project is a part of the Engineering of Software Subsystems course at RIT. The goal of this project was to focus on following different design patterns. The project is built using [Next.js](https://nextjs.org/) and [Spring Boot](https://spring.io/projects/spring-boot).

## Features

- View comics
- Add/Remove comics from your collection
- View your collection
- Edit comics in your collection

## Running Locally

1. Clone repo:

```sh
gh repo clone GCCIS-Software-Development/design-project-r1-phase-3-01-des-03
```

2. Rename `example.application.properties` to `application.properties` and update the variables.

3. Start the client:

```sh
cd client
npm i
npm run dev
```

4. Start the server:

```sh
cd server
mvn spring-boot:run
```
