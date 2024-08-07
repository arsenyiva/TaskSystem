## Запуск БД из контейнера

Для запуска базы данных из контейнера используйте файл `docker-compose.yml`. 

## Файл application.properties

Послкольку это учебный проект, для удобства файл `application.properties` добавлен в  удаленный репозиторий. 

## Скрипт для создания базы данных

Скрипт для создания базы данных находится в `resource/db`. 

### Регистрация пользователя:
/registration
```json
{
  "username": "User",
  "password": "123123",
}
```
### Получение JWT токена

После отправления Json с верным логином и паролем в ответ придет токен, который нужно необходимо будет добавить для всех последующих запросов с аутентификацией. В случае с Postman, это делается в разделе Auth
### Авторизация пользователя:
/auth

```json
{
  "username": "User",
  "password": "123123"
}

```
### Добавление задачи:
/tasks

```json
{
  "title": "Task",
  "description": "This is a  task",
  "status": "PENDING",
  "priority": "HIGH"
}
```
### Добавление комментария:
/tasks/{id}/comments

```json
{
  "content": "comment"
}

```
### Просмотр задач задач:
/tasks

### Поиск по исполнителю для просмотра задач:
http://localhost:8080/tasks/assignee/7?page=0&size=10&priority=MEDIUM

### Поиск по автору для просмотра задач:
http://localhost:8080/tasks/author/1?page=0&size=10
