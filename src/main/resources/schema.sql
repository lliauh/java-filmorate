create table if not exists films
(
    film_id int generated by default as identity primary key,
    name varchar(200) not null,
    description varchar (200),
    release_date date not null,
    duration int not null,
    rate int,
    rating_id int
);

create table if not exists genres
(
    genre_id int generated by default as identity primary key,
    genre_name varchar(200) not null
);

create table if not exists film_genres
(
    film_id int not null,
    genre_id int not null,
    CONSTRAINT fk_films_genre FOREIGN KEY(film_id) REFERENCES films(film_id),
    CONSTRAINT fk_genres FOREIGN KEY(genre_id) REFERENCES genres(genre_id)
);

create table if not exists ratings
(
    rating_id int generated by default as identity primary key,
    name varchar(20) not null
);

create table if not exists users
(
    id int generated by default as identity primary key,
    email varchar(200) not null,
    login varchar (200) not null,
    name varchar (200) not null,
    birthday date not null
);

create table if not exists friends
(
    first_user_id int not null,
    second_user_id int not null,
    CONSTRAINT fk_friends FOREIGN KEY(first_user_id) REFERENCES users(id),
    CONSTRAINT fk_friends2 FOREIGN KEY(second_user_id) REFERENCES users(id)
);

create table if not exists likes
(
    film_id int not null,
    user_id int not null,
    CONSTRAINT fk_films_likes FOREIGN KEY(film_id) REFERENCES films(film_id),
    CONSTRAINT fk_users_likes FOREIGN KEY(user_id) REFERENCES users(id)
);