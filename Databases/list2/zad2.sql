USE `laboratory-cinematheque`;
CREATE TABLE IF NOT EXISTS actors(
	id	INT,
    first_name VARCHAR(32),
	last_name VARCHAR(64)
);
CREATE TABLE IF NOT EXISTS films(
	id INT,
	title VARCHAR(32),
    genre VARCHAR(16),
    length INT,
    rating ENUM('G', 'PG', 'PG-13', 'R', 'NC-17')
);
CREATE TABLE IF NOT EXISTS cast (
	actor_id INT,
    film_id INT
);

TRUNCATE actors;
TRUNCATE films;
TRUNCATE cast;

INSERT INTO `laboratory-cinematheque`.actors (id, first_name, last_name)
	SELECT actor_id, first_name, last_name
    FROM sakila.actor
    WHERE (first_name NOT LIKE '%q%' 
			AND first_name NOT LIKE '%x%' 
			AND first_name NOT LIKE '%v%' ) AND
		(last_name NOT LIKE '%q%' 
			AND last_name NOT LIKE '%x%' 
			AND last_name NOT LIKE '%v%' );
                
INSERT INTO `laboratory-cinematheque`.films (id, title, genre, length, rating)
    SELECT film.film_id, film.title, category.name, film.length, film.rating
    FROM sakila.film 
		INNER JOIN sakila.film_category ON film.film_id = film_category.film_id
		INNER JOIN sakila.category ON film_category.category_id = category.category_id
    WHERE title NOT LIKE '%q%' 
		AND title NOT LIKE '%x%' 
		AND title NOT LIKE '%v%';
          
INSERT INTO `laboratory-cinematheque`.cast (actor_id, film_id)
	SELECT film_actor.actor_id, film_actor.film_id
    FROM sakila.film_actor 
		WHERE film_actor.actor_id IN (SELECT actor_id FROM actors);
    
    
    
    
                

