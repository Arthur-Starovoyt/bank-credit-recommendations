# Credit Recommendation Service

Сервис для выдачи персонализированных рекомендаций банковских продуктов на основе анализа транзакций клиентов.

## Технологический стек

- Java 17
- Spring Boot 3.x
- Spring Web, JDBC Template, Data JPA
- H2 Database (транзакции клиентов, read-only)
- PostgreSQL (динамические правила и статистика)
- Liquibase (миграции)
- Caffeine (кэширование)
- Telegram Bot API
- Maven

## Быстрый старт

### Требования
- Java 17
- PostgreSQL (локально или в Docker)
- Файл `transaction.mv.db` в корне проекта

### Запуск PostgreSQL (Docker)
```bash
docker run --name postgres-credit -e POSTGRES_DB=credit_rules -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin -p 5432:5432 -d postgres:15
Сборка и запуск

bash
git clone https://github.com/Arthur3323/bank-credit-recommendations.git
cd bank-credit-recommendations
./mvnw spring-boot:run
Проверка: curl http://localhost:8080/health

Основные возможности

REST API

GET /recommendation/{userId} – получить рекомендации для пользователя (UUID).
POST /rule – создать динамическое правило.
GET /rule – список всех правил.
DELETE /rule/{productId} – удалить правило по product_id.
GET /rule/stats – статистика срабатываний правил.
GET /management/info – информация о сервисе.
POST /management/clear-caches – сброс кешей.
Telegram-бот

Бот @bank_credit_recommendations_bot позволяет получать рекомендации по имени пользователя.

/start – приветствие и справка.
/recommend <username> – получить рекомендации для пользователя с указанным именем (из базы H2).
Документация

Подробное описание архитектуры, API и планов спринтов доступно в Wiki.

Команда

Артур Старовойт – разработка, настройка инфраструктуры
Илья Бичаев – разработка бизнес-логики, динамические правила
Статус

✅ Спринт 1 – базовая архитектура, статические правила, H2
✅ Спринт 2 – динамические правила, PostgreSQL, Liquibase, кэширование
✅ Спринт 3 – Telegram-бот, статистика, управляющие эндпоинты
🚀 Проект готов к демонстрации

*Последнее обновление: 2026-03-04*