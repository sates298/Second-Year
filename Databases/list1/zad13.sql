Create view v As
Select distinct a.actor_id
From actor As a Join film_actor as fa On a.actor_id = fa.actor_id
	Join film As f On fa.film_id = f.film_id
Where f.title Like 'B%';

Select distinct actor.last_name
From actor 
Where actor_id not in (Select actor_id From v);

Drop View v;