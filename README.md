# Sprint_7
Финальный проект 7 спринта (Тестирование API).
Технологии:
1. Java 11
2. JUnit 4.13.2
3. RestAssured 5.5.1
4. Allure 2.15.0

```shell
## запуск тестов
mvn clean test 
## генерация отчета в Allure
mvn allure:serve
```

Тестовые сценарии:
1. Создание курьера
- курьера можно создать;
- нельзя создать двух одинаковых курьеров;
- чтобы создать курьера, нужно передать в ручку все обязательные поля;
- запрос возвращает правильный код ответа;
- успешный запрос возвращает ok: true;
- если одного из полей нет, запрос возвращает ошибку;
- если создать пользователя с логином, который уже есть, возвращается ошибка.
2. Логин курьера
- курьер может авторизоваться;
- для авторизации нужно передать все обязательные поля;
- система вернёт ошибку, если неправильно указать логин или пароль;
- если какого-то поля нет, запрос возвращает ошибку;
- если авторизоваться под несуществующим пользователем, запрос возвращает ошибку;
- успешный запрос возвращает id.
3. Создание заказа
- можно указать один из цветов — BLACK или GREY;
- можно указать оба цвета;
- можно совсем не указывать цвет;
- тело ответа содержит track.
4. Список заказов
- проверка, что в тело ответа возвращается список заказов.
5. Отчёт Allure
