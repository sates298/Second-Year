SELECT DISTINCT films.title
FROM films 
	INNER JOIN cast ON cast.film_id = films.id
    INNER JOIN actors ON cast.actor_id = actors.id
WHERE actor_id IN 
		(SELECT actor_id 
        FROM cast
			INNER JOIN actors ON cast.actor_id = actors.id
		WHERE film_id IN 
			(SELECT cast.film_id 
			FROM cast 
				INNER JOIN actors ON actors.id=cast.actor_id
                INNER JOIN films ON films.id = cast.film_id
			WHERE actors.last_name = 'Cage' AND actors.first_name = 'Zero'))
AND (actors.last_name <> 'Cage' OR actors.first_name <> 'Zero');