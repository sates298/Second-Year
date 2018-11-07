Select Distinct actor.first_name, actor.last_name
From actor  
	Inner Join film_actor On actor.actor_id = film_actor.actor_id
		Inner Join film On film_actor.film_id = film.film_id
Where film.special_features Like '%Deleted Scenes%';