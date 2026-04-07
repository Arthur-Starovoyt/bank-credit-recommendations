# Bank Credit Recommendation Service

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-blue)
![Maven](https://img.shields.io/badge/Maven-red)
![Docker](https://img.shields.io/badge/Docker-ready-blue)
![Build](https://img.shields.io/badge/build-passing-brightgreen)

REST API сервис для выдачи персонализированных рекомендаций банковских продуктов на основе анализа транзакций клиентов.

Проект реализует backend-сервис с поддержкой динамических правил, кэширования и интеграции с Telegram-ботом.

---

# Features

- REST API для рекомендаций
- Динамические правила
- Кэширование
- Telegram бот
- Статистика правил
- Liquibase миграции

---

# Технологический стек

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- JDBC Template
- PostgreSQL
- H2 Database
- Liquibase
- Caffeine Cache
- Telegram Bot API
- Maven
- Docker

---

# REST API

### Рекомендации

```
GET /recommendation/{userId}
```

### Управление правилами

```
POST /rule
GET /rule
DELETE /rule/{productId}
GET /rule/stats
```

### Управление сервисом

```
GET /health
GET /management/info
POST /management/clear-caches
```

---

# Архитектура

Проект построен по layered architecture:

```
Controller → Service → Repository → Database
```

---

# Project Structure

```
src/main/java/com/bank/recommendation

├── config
├── controller
├── entity
├── interfaces
│   └── impl
├── models
├── repositories
├── service
│   └── evaluator
└── telegram
```

---

# Процесс разработки

- Работа по спринтам
- Использование Pull Requests
- Code Review
- Работа в отдельных ветках
- Слияние через pull request

---

# Запуск проекта

### Запуск PostgreSQL

```bash
docker compose up -d
```

В корне проекта находится `docker-compose.yml`, который поднимает PostgreSQL c базой `credit_rules` на `localhost:5434`.

### Запуск приложения

```bash
mvn spring-boot:run
```

### Telegram Bot Token

Telegram-бот запускается только если задана переменная окружения `TELEGRAM_BOT_TOKEN`.
Для локальной работы можно скопировать `.env.example` в `.env`, подставить свой токен и загрузить переменные в текущую сессию shell.

```bash
cp .env.example .env
# отредактируйте .env и укажите реальный токен
source .env
mvn spring-boot:run
```

Файл `.env` не коммитится. Если переменная не задана, REST API продолжит работать, но Telegram-бот не будет зарегистрирован.

---

# Future Improvements

- Swagger documentation
- Unit tests
- Integration tests

---

# Статус проекта

Проект завершён и готов к демонстрации

---

# Автор

Артур Старовойт  
Java Backend Developer
