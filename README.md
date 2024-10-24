# Internal GOV-AI

The following project leverages Generative AI (LLMs subset) with actor mentions using langchain4j as the main library

## Quick Start

In order to start working with the project it needs some dependencies to be fulfilled

### Java

This project uses Java 21.

### PostgreSQL

You can download PostgreSQL from their main page [Official PostgreSQL Web](https://www.postgresql.org/) however \
we recommend to use [Docker](https://www.docker.com/) in order to run a small instance of **PostgreSQL** without the need of \
software download.

Once **Docker** is installed run the following command

`docker run --name postgres -p 5432:5432 -e POSTGRES_PASSWORD=<ANY_PASSWORD> -d postgres:<PG_VERSION>`

- Replace **ANY_PASSWORD** with any password you'd like to use for the **postgres** user.
- Replace **PG_VERSION** with the PostgreSQL version you want to run, this is commonly known as **tag** in Docker

Click here to check all PostgreSQL tags [PosgreSQL tags](https://hub.docker.com/_/postgres/tags)

Set the following Environment Variables
```
${POSTGRESQL_USERNAME}
${POSTGRESQL_PASSWORD}
```

### Brandwatch

The application connects to brandwatch to get mentions for a certain query through an API endpoint, you'd need to request \
the following information and provide it to the application through [Environment Variables](https://vercel.com/docs/projects/environment-variables)

```
${BRANDWATCH_API_HOST}
${BRANDWATCH_API_USERNAME}
${BRANDWATCH_API_PASSWORD}
${BRANDWATCH_API_PROJECT_ID}
```

### OPEN AI

This application uses OPEN AI as the main Generative AI for classification and analysis for multiple mentions.
We can certainly extend this functionality to use Different AI providers, please read the code carefully.

We need an **OPEN AI API KEY** to successfully connect to [Open AI](https://openai.com/index/openai-api/)

```
${OPENAI_API_KEY}
```

## Mailtrap IO (Optional)

For testing purposes we use Mailtrap to send emails for alerts, if you'd like to test this functionality you'd need to set the following Environment variables
```
${MAILTRAP_USERNAME}
${MAILTRAP_PASSWORD}
```

We need to set all environment variables before trying to run the project with the following command

```
./gradlew bootRun
```