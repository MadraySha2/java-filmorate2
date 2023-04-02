# java-filmorate
Схема БД для приложения. <br>
Таблица USER_LIKES хранит в себе информацию о лайках в виде 
USER_ID связанного с юзером в таблице USERS и FILM_ID связанного с фильмом в таблице FILM <br>
Таблица USERS, также связана с таблицей FRIENDS в которой хранится информация о запросах в друзья между юзерами<br>
Таблица FILM связана с таблицами MPARATING и FILMS_GENRES : MPARATING - хранит в себе список всех рейтнгов
в таблице FILM MPARATING хранится в виде MPARATING_ID <br>
Таблица FILMS_GENRES хранит информацию про жанры присвоенные фильмам, <br>
где FILM_ID связывает ее с фильмом, а GENRE_ID с Жанром из
таблицы GENRES
<br>![f](https://i.ibb.co/fGgGPD8/Screenshot-6.png)