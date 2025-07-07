# 💳 MVPBank

**MVPBank** — минимально жизнеспособное банковское приложение, написанное на Spring Boot. Основная цель — реализовать безопасное управление пользователями и их счетами, переводы между счетами и логирование событий через Kafka.

---

## 📌 Возможности

* ✅ Регистрация пользователей с указанием username, email, password и роли (USER / ADMIN)
* ✅ Аутентификация с использованием **JWT токенов**
* ✅ Разграничение доступа по ролям: `USER` и `ADMIN`
* ✅ Создание счетов пользователем
* ✅ Просмотр собственных счетов
* ✅ Перевод средств между собственными счетами
* ✅ Логирование операций через **Kafka**
* ✅ Администратор может:

  * Просматривать других пользователей и их счета
  * Удалять пользователей и счета
  * Пополнять и списывать со счетов других юзеров

---

## 🪰 Используемые технологии

### 🛠 Backend:

* Java 17
* Spring Boot 3
* Spring Web (REST API)
* Spring Security (JWT-аутентификация)
* Spring Data JPA (Hibernate)
* PostgreSQL
* Lombok
* Jakarta Validation
* Apache Kafka (логгирование)

---

## 🗂 Структура проекта

* `model` — JPA-сущности (User, Account)
* `repository` — репозитории JPA
* `service` — бизнес-логика
* `controller` — REST-контроллеры
* `dto` — объекты обмена данными (UserLoginRequest, AccountResponse и др.)
* `security` — JWT, фильтры, UserDetailsService
* `exception` — кастомные исключения
* `config` — конфигурационные классы (Kafka, Security и др.)

---

## 🔐 JWT Аутентификация

* При логине пользователь получает JWT-токен.
* Доступ к защищённым API через заголовок:

```http
Authorization: Bearer <token>
```

---

## 📦 API Эндпоинты

### Аутентификация:

* `POST /api/auth/register` — регистрация
* `POST /api/auth/login` — логин (возвращает JWT)

### Счета (USER):

* `POST /api/accounts` — создать счёт
* `GET /api/accounts` — получить все свои счета
* `POST /api/accounts/transfer` — перевести между своими счетами

### Админ (ADMIN):

* `GET /api/admin/users` — список всех пользователей
* `GET /api/admin/users/{userId}` — информация по пользователю
* `GET /api/admin/users/{userId}/accounts` — счета пользователя
* `POST /api/admin/accounts/deposit` — пополнение счёта
* `POST /api/admin/accounts/withdraw` — списать со счёта
* `DELETE /api/admin/users/{id}` — удалить пользователя
* `DELETE /api/admin/accounts/{accountId}` — удалить чужой счёт

---

## 📌 Будущие улучшения

* [ ] Swagger-документация (OpenAPI)
* [ ] Веб-интерфейс (frontend)
* [ ] Подтверждение email при регистрации
* [ ] Расширенные Kafka consumers (архив событий)
* [ ] Интеграционные и unit-тесты
