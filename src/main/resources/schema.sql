CREATE TABLE IF NOT EXISTS users(
id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
email varchar,
login varchar,
name varchar NULL,
birthday Date
);
CREATE TABLE IF NOT EXISTS genres(
id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
genre_name varchar
);
CREATE TABLE IF NOT EXISTS MPARating(
id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
rating_name varchar
);
CREATE TABLE IF NOT EXISTS film (
id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name varchar,
description varchar,
releaseDate Date,
duration int,
genres varchar,
MPARating_id int REFERENCES MPARating(id),
count_likes int
);
CREATE TABLE IF NOT EXISTS user_likes(
id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
user_id int REFERENCES users(id),
film_id int REFERENCES film(id)
);
CREATE TABLE IF NOT EXISTS friends(
id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
user_id int REFERENCES users(id),
friend_id int REFERENCES users(id),
accepted Boolean
);
