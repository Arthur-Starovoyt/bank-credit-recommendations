# Bank Credit Recommendation Service

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-blue)
![Maven](https://img.shields.io/badge/Maven-red)
![Docker](https://img.shields.io/badge/Docker-ready-blue)

Backend-сервис на Spring Boot, который формирует персональные рекомендации банковских продуктов на основе транзакций клиента, поддерживает динамические правила, кэширование и интеграцию с Telegram-ботом.

Этот проект сделан как демонстрация backend-навыков:
- проектирование REST API;
- работа с PostgreSQL и Liquibase;
- разделение на controller/service/repository слои;
- кэширование и служебные management endpoint'ы;
- безопасная работа с секретами через переменные окружения.

## Table of Contents

- [Что умеет сервис](#что-умеет-сервис)
- [Технологии](#технологии)
- [Архитектура](#архитектура)
- [Быстрый старт](#быстрый-старт)
- [Как быстро проверить, что всё работает](#как-быстро-проверить-что-всё-работает)
- [REST API](#rest-api)
- [Что важно с инженерной точки зрения](#что-важно-с-инженерной-точки-зрения)
- [Что можно улучшить дальше](#что-можно-улучшить-дальше)

## Что умеет сервис

- Возвращает рекомендации по `userId`.
- Позволяет создавать, смотреть и удалять динамические правила.
- Считает статистику срабатывания правил.
- Использует PostgreSQL для бизнес-данных и H2 для транзакционного датасета.
- Поддерживает Telegram-бота для получения рекомендаций без хранения токена в git.

## Технологии

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Spring Data JDBC
- PostgreSQL
- H2 Database
- Liquibase
- Caffeine Cache
- Telegram Bot API
- Maven
- Docker Compose

## Архитектура

Приложение построено в классическом layered style:

```text
Controller -> Service -> Repository -> Database
```

Основные пакеты:

```text
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

## Быстрый старт

### Требования

- Java 17
- Maven 3.9+ или `./mvnw`
- Docker

### 1. Поднять PostgreSQL

В проекте уже есть [docker-compose.yml](./docker-compose.yml).

```bash
docker compose up -d
```

PostgreSQL будет доступен на `localhost:5434`, база: `credit_rules`.

### 2. Настроить Telegram token локально

Токен не хранится в репозитории. Для локального запуска:

```bash
cp .env.example .env
```

После этого укажи реальный токен в `.env` и загрузи переменную в shell:

```bash
source .env
```

Если переменная `TELEGRAM_BOT_TOKEN` не задана, REST API продолжит работать, но Telegram-бот не будет зарегистрирован.

### 3. Запустить приложение

```bash
./mvnw spring-boot:run
```

Приложение стартует на `http://localhost:8080`.

## Как быстро проверить, что всё работает

### Healthcheck

```bash
curl http://localhost:8080/health
```

Ожидаемый ответ:

```text
Credit Recommendation Service is running!
```

### Service info

```bash
curl http://localhost:8080/management/info
```

Пример ответа:

```json
{
  "name": "bank-credit-recommendations",
  "version": "0.0.1-SNAPSHOT"
}
```

## REST API

### Получить рекомендации

```http
GET /recommendation/{userId}
```

Пример:

```bash
curl http://localhost:8080/recommendation/00000000-0000-0000-0000-000000000001
```

Формат ответа:

```json
{
  "user_id": "00000000-0000-0000-0000-000000000001",
  "recommendation": [
    {
      "name": "Investment Product",
      "id": "11111111-1111-1111-1111-111111111111",
      "text": "Product description"
    }
  ]
}
```

### Управление динамическими правилами

```http
POST   /rule
GET    /rule
DELETE /rule/{productId}
GET    /rule/stats
```

Пример создания правила:

```bash
curl -X POST http://localhost:8080/rule \
  -H "Content-Type: application/json" \
  -d '{
    "product_name": "Custom Savings",
    "product_id": "123e4567-e89b-12d3-a456-426614174000",
    "product_text": "Savings product created from dynamic rule",
    "rule": []
  }'
```

### Service management

```http
GET  /health
GET  /management/info
POST /management/clear-caches
```

Пример очистки кэша:

```bash
curl -X POST http://localhost:8080/management/clear-caches
```

## Что важно с инженерной точки зрения

- Секреты вынесены из исходников: Telegram token читается из `TELEGRAM_BOT_TOKEN`.
- Telegram-бот запускается только если токен задан, поэтому API можно запускать независимо.
- Миграции базы автоматизированы через Liquibase.
- Для локального окружения есть готовый Docker Compose.
- Конфигурация приложения и инфраструктуры согласованы и воспроизводимы.

## Что можно улучшить дальше

- Добавить OpenAPI/Swagger документацию.
- Расширить покрытие unit и integration тестами.
- Добавить CI pipeline с автоматической проверкой сборки.
- Сформировать seed-данные для более удобной демонстрации API.

## Автор

Артур Старовойт  
Java Backend Developer  
GitHub: https://github.com/Arthur-Starovoyt
