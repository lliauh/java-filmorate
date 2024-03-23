# java-filmorate
Репозиторий приложения Filmorate.

## Схема БД
![Схема БД filmorate](https://github.com/lliauh/java-filmorate/blob/main/filmorate_db.png)

## Осноные запросы
1. Получить все фильмы
```
SELECT *
FROM films;
```

2. Получить фильм по ID
```
SELECT *
FROM films
WHERE film_id = $id;
```

3. Получить популярные фильмы
```
SELECT *
FROM films AS f
JOIN (SELECT film_id,
             COUNT(film_id) AS likes_count
      FROM likes
      GROUP BY film_id) AS l ON f.film_id=l.film_id
ORDER BY likes_count DESC
LIMIT 10;

```

4. Получить всех пользователей
```
SELECT *
FROM users;
```

5. Получить пользователя по ID
```
SELECT *
FROM users
WHERE id = $id;
```
