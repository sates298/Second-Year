Create view v1 As Select actor.actor_id, Count(film_actor.actor_id) as horrors
From actor Join film_actor On film_actor.actor_id = actor.actor_id
	Join film_category On film_actor.film_id = film_category.film_id 
		Join category On film_category.category_id = category.category_id
Where category.name Like 'Horror'
Group By actor.actor_id;

Create view v2 As Select actor.actor_id, Count(film_actor.actor_id) as actions
From actor Join film_actor On film_actor.actor_id = actor.actor_id
	Join film_category On film_actor.film_id = film_category.film_id 
		Join category On film_category.category_id = category.category_id
Where category.name Like 'Action'
Group By actor.actor_id;

Select actor.last_name
From actor Join v1 On actor.actor_id = v1.actor_id
	Left join v2 On actor.actor_id = v2.actor_id
Where v1.horrors > v2.actions Or (actor.actor_id not in (Select actor_id From v2) And actor.actor_id in (Select actor_id From v1)) ;
    
Drop View v1;
Drop View v2;