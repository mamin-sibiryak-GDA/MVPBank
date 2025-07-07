# 💳 MVPBank

**MVPBank** — минимально жизнеспособное банковское приложение, написанное на Spring Boot. Основная цель — реализовать безопасное управление пользователями и их счетами, переводы между счетами и логирование событий через Kafka.

---

## 📌 Возможности

* ✅ Регистрация пользователей с указанием username, email, password и роли (USER / ADMIN)
* ✅ Аутентификация с использованием **JWT токенов**
* ✅ Разграничение доступа по ролям: `USER` и `ADMIN`
* ✅ Создание счетов пользователем
* ✅ Просмотр собственных счетов
* ✅ Перевод средств между своими счетами
* ✅ Удаление счетов
* ✅ Администратор может:

  * Просматривать всех пользователей и их счета
  * Пополнять счета других пользователей
  * Удалять пользователей и их счета
* ✅ Логирование операций через **Kafka** (создание счёта, перевод денег и т.д.)

---

## 🧰 Используемые технологии

### 🛠 Backend:

* Java 17
* Spring Boot 3
* Spring Web (REST API)
* Spring Security (аутентификация и авторизация)
* JWT (io.jsonwebtoken) — генерация и валидация токенов
* Spring Data JPA — работа с базой данных
* Hibernate — ORM слой
* PostgreSQL — реляционная СУБД
* Kafka — система обмена сообщениями (через Apache Kafka)
* Lombok — генерация шаблонного кода (геттеры/сеттеры/конструкторы)
* Jakarta Validation — валидация данных
* Docker / Docker Compose — контейнеризация Kafka/Zookeeper

---

## 🗂 Структура проекта

* `model` — JPA-сущности (User, Account и т.д.)
* `repository` — интерфейсы для доступа к БД
* `service` — бизнес-логика
* `controller` — REST-контроллеры
* `security` — настройка JWT, фильтры, UserDetailsServiceImpl
* `dto` — объекты передачи данных между слоями (UserLoginRequest, AccountResponse и т.д.)
* `kafka` — логика отправки сообщений в Kafka

---

## 🔐 JWT Аутентификация

* При логине пользователь получает JWT токен.
* Доступ к защищённым маршрутам осуществляется через заголовок:

  ```
  ```

Authorization: Bearer <токен>

````
- Без токена доступ запрещён (403).

---

## 📦 API Эндпоинты

### Аутентификация:
- `POST /api/auth/register` — регистрация
- `POST /api/auth/login` — логин (возвращает JWT токен)

### Работа с аккаунтами:
- `POST /api/accounts` — создать счёт (USER)
- `GET /api/accounts` — получить список счетов (USER)
- `POST /api/accounts/transfer` — перевести средства между счетами (USER)
- `DELETE /api/accounts/{id}` — удалить счёт (USER/ADMIN)

### Админ-доступ:
- `GET /api/admin/users` — все пользователи
- `GET /api/admin/users/{id}` — пользователь по ID
- `POST /api/admin/accounts/topup` — пополнение чужого счёта
- `DELETE /api/admin/users/{id}` — удалить пользователя

---

## 🐳 Запуск Kafka через Docker

```bash
docker-compose -f kafka-docker-compose.yml up
````

Kafka будет доступна по адресу `localhost:9092`, Zookeeper — `localhost:2181`.

---
