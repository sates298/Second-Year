#ALTER TABLE actors ADD films_no int;

ALTER TABLE actors DROP titles;
ALTER TABLE actors ADD titles varchar(140);

UPDATE actors 
		LEFT JOIN (SELECT actor_id, COUNT(film_id) As films_no
				FROM cast GROUP BY actor_id) AS cast1 ON actors.id = cast1.actor_id
SET actors.films_no = cast1.films_no;

UPDATE actors
        LEFT JOIN
    (SELECT 
        actors.id,
            films_no,
            GROUP_CONCAT(films.title) AS concat_titles
    FROM
        actors
    INNER JOIN cast ON cast.actor_id = actors.id
    INNER JOIN films ON cast.film_id = films.id
    WHERE
        films_no < 4
    GROUP BY cast.actor_id) AS actors_titles ON actors.id = actors_titles.id 
SET 
    actors.titles = actors_titles.concat_titles;