USE moviedb;

CREATE TABLE IF NOT EXISTS movies(
    id VARCHAR(10) PRIMARY KEY,
    title VARCHAR(100),
    year INT,
    director VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS stars(
    id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100),
    birth_year INT,
);

CREATE TABLE IF NOT EXISTS stars_in_movies(
    star_id VARCHAR(10) REFERENCES stars(id),
    movie_id VARCHAR(10) REFERENCES movies(id)
);

CREATE TABLE IF NOT EXISTS genres(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(32)
);

CREATE TABLE IF NOT EXISTS genres_in_movies(
    genre_id INT REFERENCES genres(id),
    movie_id VARCHAR(10) REFERENCES movies(id)
);

CREATE TABLE IF NOT EXISTS customers(
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    cc_id VARCHAR(20) REFERENCES credit_cards(id),
    address VARCHAR(200),
    email VARCHAR(50),
    password VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS sales(
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT REFERENCES customers(id),
    movie_id VARCHAR(10) REFERENCES movies(id),
    sale_date DATE
);

CREATE TABLE IF NOT EXISTS credit_cards(
    id VARCHAR(20) PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    expiration DATE
);

CREATE TABLE IF NOT EXISTS ratings(
    movie_id VARCHAR(10)  REFERENCES movies(id),
    rating FLOAT,
    num_votes INT
);

CREATE TABLE IF NOT EXISTS carts(
	id INT AUTO_INCREMENT NOT NULL,
    user_email VARCHAR(50) UNIQUE NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS cart_items(
	id INT AUTO_INCREMENT NOT NULL,
    cart_id INT NOT NULL,
    movie_id VARCHAR(10) UNIQUE NOT NULL,
	quantity INT NOT NULL,
    price FLOAT NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(cart_id) REFERENCES carts(id)
);
