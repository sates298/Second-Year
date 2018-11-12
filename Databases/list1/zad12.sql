Create View v1 As Select a.actor_id As first_actor, b.actor_id As second_actor
	From film_actor As a , film_actor As b
	Where a.film_id = b.film_id And a.actor_id < b.actor_id;
    
Select first_actor,a.last_name, second_actor, b.last_name
From v1
	Inner Join actor As a On a.actor_id = v1.first_actor 
	Inner Join actor As b On b.actor_id = v1.second_actor
Group By first_actor, second_actor
Having Count(*) > 1;  

Drop View v1;
