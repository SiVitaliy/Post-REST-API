# Post REST Service
[![Java](https://img.shields.io/badge/Java-blue.svg)](https://adoptium.net/)

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F.svg)](https://spring.io/projects/spring-security)
[![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F.svg)](https://spring.io/projects/spring-data-jpa)

[![Hibernate](https://img.shields.io/badge/Hibernate-59666C.svg)](https://hibernate.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791.svg)](https://www.postgresql.org/)
[![JWT](https://img.shields.io/badge/JWT-black.svg)](https://jwt.io/)

[![MapStruct](https://img.shields.io/badge/MapStruct-orange.svg)](https://mapstruct.org/)

[![REST API](https://img.shields.io/badge/REST%20API-orange.svg)](https://restfulapi.net/)


[![Docker](https://img.shields.io/badge/Docker-ready-blue.svg)](https://www.docker.com/)

`PostRestService` — REST API сервер для PostApp.

Сервис отвечает за пользователей, авторизацию, JWT, посты, комментарии, изображения, валидацию, права доступа и работу с PostgreSQL.




---

## Что умеет сервис

### 👤 Пользователи

- Регистрация пользователя
- Авторизация по email/password
- Генерация JWT
- Получение текущего пользователя
- Получение своего профиля с постами
- Получение публичного профиля пользователя с постами
- Поиск пользователей по имени
- Редактирование профиля
- Удаление текущего пользователя
- Загрузка аватара пользователя

### 📝 Посты

- Получение списка постов с пагинацией
- Получение поста по id вместе с комментариями
- Создание поста
- Редактирование поста
- Удаление поста
- Проверка авторства при редактировании и удалении
- Загрузка изображений к посту

### 💬 Комментарии


- Получение комментария по id
- Создание комментария к посту
- Редактирование комментария
- Удаление комментария
- Автор комментария может удалить свой комментарий
- Автор поста может удалить любой комментарий под своим постом
- Пагинация комментариев на странице поста

### 🔐 Безопасность

- JWT аутентификация
- Stateless конфигурация
- Защита всех API endpoint-ов, кроме login, registration, uploads
- Проверка ролей через Spring Security
- Проверка авторства постов и комментариев
- Единый формат ошибок для валидации и авторизации

---

## Архитектура

Проект разделен на слои:

```text
controller  → REST endpoints
service     → business logic
repository  → Spring Data JPA
entity      → JPA entities
dto         → API contracts
mapper      → MapStruct mappers
config      → Security/JWT/configuration
```

Схема взаимодействия:

```text
Client
  |
  | HTTP + JWT 
  ↓ 
PostRestService
  |
  | Spring Data JPA / Hibernate
  ↓ 
PostgreSQL
```

REST-сервер не рендерит HTML.  
HTML-страницы находятся в отдельном приложении [`Post Client`](https://github.com/SiVitaliy/post-client).

---

## Технологии

- Java 
- Spring Boot
- Spring Web
- Spring Security
- Spring Data JPA
- Hibernate
- PostgreSQL
- Bean Validation
- JWT 
- MapStruct
- Maven
- Docker

---

## Авторизация

Авторизация построена на JWT.

### Login процесс

1. Клиент отправляет email/password на endpoint логина.
2. Сервер ищет пользователя по email.
3. Пароль проверяется через `PasswordEncoder`.
4. Если данные верны, сервер генерирует и возвращает JWT.
5. Клиент отправляет JWT в следующих запросах через заголовок:

```http
Authorization: Bearer <jwt-token>
```

### JWT filter

JWT обрабатывается фильтром `JwtAuthFilter`.

Фильтр:

- читает `Authorization` header;
- проверяет наличие `Bearer` token;
- валидирует JWT;
- достает email из токена;
- загружает пользователя;
- создает `UsernamePasswordAuthenticationToken`;
- кладет authentication в `SecurityContext`.

### Security config

Сервис работает в stateless-режиме:

```text
SessionCreationPolicy.STATELESS
```

CSRF отключен, потому что REST API использует `Authorization: Bearer JWT`, а не cookie-based авторизацию.

Публичные endpoint-ы:

```text
/api/auth/login
/api/auth/registration
/api/uploads/**
```

Остальные endpoint-ы требуют авторизации.

---

## Основные endpoint-ы

### Auth

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/auth/registration` | Регистрация пользователя | 
| `POST` | `/api/auth/login` | Логин и получение JWT |

### Users

| Method | Endpoint | Description | 
|---|---|---|
| `GET` | `/api/users` | Получить пользователей / поиск по имени |
| `GET` | `/api/me` | Получить текущего пользователя |
| `GET` | `/api/me/posts` | Получить текущего пользователя с его постами | 
| `GET` | `/api/user/id{id}` | Получить публичный профиль пользователя с постами | 
| `POST` | `/api/me` | Обновить аватар текущего пользователя |
| `PUT` | `/api/me` | Обновить профиль текущего пользователя |
| `DELETE` | `/api/me` | Удалить текущего пользователя | 

### Posts

| Method | Endpoint | Description | 
|---|---|---|
| `GET` | `/api/posts?page=0&size=10` | Получить посты с пагинацией |
| `GET` | `/api/posts/{id}` | Получить пост с комментариями |
| `POST` | `/api/posts` | Создать пост |
| `PUT` | `/api/posts/{postId}` | Обновить пост | 
| `DELETE` | `/api/posts/{id}` | Удалить пост | 
| `POST` | `/api/posts/{postId}/images` | Добавить изображения к посту |

### Commentaries

| Method | Endpoint | Description | 
|---|---|---|
| `GET` | `/api/commentaries/{id}` | Получить комментарий по id | 
| `POST` | `/api/posts/{postId}` | Создать комментарий к посту | 
| `PUT` | `/api/commentaries/{id}` | Обновить комментарий | 
| `DELETE` | `/api/commentaries/{id}` | Удалить комментарий | 

### Images

| Method | Endpoint | Description | 
|---|---|---|
| `GET` | `/api/uploads/posts/{id}/{filename}` | Получить изображение поста | 
| `GET` | `/api/uploads/profilePictures/{filename}` | Получить аватар пользователя | 

---

## Примеры запросов

### Registration

```http
POST /api/auth/registration
Content-Type: application/json

{
  "fullName": "Ivan Ivanov",
  "email": "ivan@example.com",
  "password": "password123"
}
```

Response:

```json
{
  "token": "<jwt-token>"
}
```

### Login

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "ivan@example.com",
  "password": "password123"
}
```

Response:

```json
{
  "token": "<jwt-token>"
}
```

### Create post

```http
POST /api/posts
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "title": "My first post",
  "text": "Hello world"
}
```

### Get posts

```http
GET /api/posts?page=0&size=10
Authorization: Bearer <jwt-token>
```

### Update current user

```http
PUT /api/me
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "fullName": "Ivan Ivanov",
  "email": "ivan@example.com",
  "yearOfBirth": "2000-01-01",
  "countryCode": "RU",
  "bio": "Java backend developer"
}
```

---

## Валидация

Сервис использует Bean Validation.

Примеры правил:

### Registration

- `fullName` — не пустое, от 2 до 100 символов
- `email` — не пустой, валидный email, максимум 150 символов
- `password` — не пустой, от 6 до 50 символов

### User update

- `fullName` — не пустое, от 2 до 100 символов
- `email` — не пустой, валидный email, максимум 150 символов
- `yearOfBirth` — не может быть в будущем
- `countryCode` — 2 заглавные латинские буквы
- `bio` — максимум 2000 символов

### Posts

- `title` — не пустой
- `text` — не пустой

### Comments

- `text` — не пустой

---

## Обработка ошибок

Ошибки обрабатываются через `@RestControllerAdvice`.

### Validation error

При ошибке валидации сервер возвращает `400 Bad Request`.

Пример:

```json
{
  "code": "validation_error",
  "message": "Запрос содержит некорректные данные",
  "errors": [
    {
      "field": "email",
      "message": "Некорректный email"
    }
  ]
}
```

### Bad credentials

При неверном email/password сервер возвращает `401 Unauthorized`.

```json
{
  "code": "auth.bad_credentials",
  "message": "Неверная почта или пароль"
}
```

### Email already exists

При регистрации или обновлении профиля с уже существующим email сервер возвращает `409 Conflict`.

```json
{
  "code": "user.email_already_exists",
  "message": "Пользователь с такой почтой уже существует"
}
```

### Forbidden

Если пользователь пытается редактировать или удалить чужой пост, сервер возвращает `403 Forbidden`.

---

## Пагинация

Посты и комментарии возвращаются в формате `PageResponse`.

Пример DTO:

```java
public record PageResponse<T>(
    List<T> content,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean hasNext,
    boolean hasPrevious
) {
}
```

Для постов используется сортировка по дате создания:

```text
creationDate DESC
```

То есть сначала возвращаются новые посты.

Для комментариев также используется пагинация и сортировка по дате создания.

---

## Работа с изображениями

Сервис поддерживает загрузку:

- аватаров пользователей;
- изображений к постам.

Файлы сохраняются локально:

```text
uploads/profilePictures
uploads/posts
```

URL сохраняется в БД, например:

```text
/uploads/profilePictures/...
/uploads/posts/{postId}/...
```

Изображения доступны через публичные endpoint-ы:

```text
/api/uploads/profilePictures/{filename}
/api/uploads/posts/{postId}/{filename}
```

Важно: при деплое на Render.com (free версия) локальное хранение файлов не является production-ready, т.к. при редеплое локальное хранилище Render.com затирается. Этого ограничения нет при локальном запуске.

---

## Конфигурация

Локальный конфиг:

```text
src/main/resources/application.properties
```

Production-конфиг:

```text
src/main/resources/application-prod.properties
```

### application.properties

Локально сервис ожидает PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/RestServiceDB
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### application-prod.properties

Production-конфиг использует environment variables:

```properties
server.port=${PORT:8080}

spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}

spring.jpa.hibernate.ddl-auto=${SPRING_JPA_HIBERNATE_DDL_AUTO:update}
spring.jpa.show-sql=${SPRING_JPA_SHOW_SQL:false}

jwt.secret=${JWT_SECRET}
jwt.expiration=${JWT_EXPIRATION:3600000}

spring.servlet.multipart.max-file-size=${MAX_FILE_SIZE:10MB}
spring.servlet.multipart.max-request-size=${MAX_REQUEST_SIZE:50MB}
```

---

## Environment variables

| Variable | Description | Example |
|---|---|---|
| `SPRING_PROFILES_ACTIVE` | Spring profile | `prod` |
| `SPRING_DATASOURCE_URL` | JDBC URL PostgreSQL | `jdbc:postgresql://host:5432/db` |
| `SPRING_DATASOURCE_USERNAME` | DB username | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | DB password | `postgres` |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | Hibernate schema strategy | `update` |
| `SPRING_JPA_SHOW_SQL` | SQL logging | `false` |
| `SPRING_JPA_FORMAT_SQL` | SQL formatting | `false` |
| `JWT_SECRET` | JWT signing secret | `base64-secret` |
| `JWT_EXPIRATION` | JWT lifetime in ms | `3600000` |
| `MAX_FILE_SIZE` | Max size of one uploaded file | `10MB` |
| `MAX_REQUEST_SIZE` | Max multipart request size | `50MB` |
| `HIBERNATE_SQL_LOG_LEVEL` | Hibernate SQL log level | `INFO` |
| `HIBERNATE_BIND_LOG_LEVEL` | Hibernate bind params log level | `INFO` |

---

## Локальный запуск без Docker

Перед запуском нужна PostgreSQL БД:

```text
RestServiceDB
```

Запуск через Maven Wrapper:

```powershell
.\mvnw.cmd spring-boot:run
```

Или через установленный Maven:

```powershell
mvn spring-boot:run
```

API будет доступно по адресу:

```text
http://localhost:8080
```

---

## Сборка jar

```powershell
.\mvnw.cmd clean package
```

Если нужно пропустить тесты:

```powershell
.\mvnw.cmd clean package -DskipTests
```

После сборки jar появится в директории:

```text
target/
```

---

## Docker

### Dockerfile

```dockerfile
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

CMD ["java", "-jar", "app.jar"]
```

### Сборка Docker image

```powershell
docker build -t post-rest-service .
```

### Запуск Docker container

```powershell
docker run --rm -p 8085:8080 `
  -e SPRING_PROFILES_ACTIVE=prod `
  -e PORT=8080 `
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/RestServiceDB `
  -e SPRING_DATASOURCE_USERNAME=postgres `
  -e SPRING_DATASOURCE_PASSWORD=postgres `
  -e JWT_SECRET=your-base64-secret `
  post-rest-service
```

После запуска API будет доступно по адресу:

```text
http://localhost:8085
```

### Почему используется `8085:8080`

```text
-p 8085:8080
   ↑    ↑
   |    порт внутри контейнера
   порт на локальной машине
```

Это удобно, если локальный порт `8080` уже занят.

---

## Связанный клиент

Для этого REST API есть отдельный Thymeleaf web-клиент:

```text
PostClientService
```

Клиент взаимодействует с этим сервером через HTTP и передает JWT в заголовке:

```http
Authorization: Bearer <jwt-token>
```

---

## Что важно знать по проекту

- REST API отделен от web-клиента.
- Авторизация реализована через JWT.
- Сервер работает в stateless-режиме.
- Пароли хранятся в BCrypt.
- Пользовательские ошибки обрабатываются через `@RestControllerAdvice`.
- DTO маппятся через MapStruct.
- Посты и комментарии поддерживают пагинацию.
- Редактирование и удаление постов защищены проверкой авторства.
- Изображения раздаются через отдельные `/api/uploads/**` endpoint-ы.
- Production-настройки передаются через environment variables.
- Dockerfile сам собирает jar и запускает приложение.
