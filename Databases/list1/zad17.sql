Update film
Set film.language_id = 
	(Select language.language_id 
		From language
        Where language.name Like 'Mandarin')
Where film.title Like 'WON DARES';

Update film
Set film.language_id = 
	(Select language.language_id
		From language
		Where language.name Like 'German')
Where film.film_id in 
	(Select film_actor.film_id
		From film_actor 
			Inner Join actor On actor.actor_id = film_actor.actor_id
		Where actor.first_name Like 'NICK' And actor.last_name Like 'WAHLBERG');

Create View v1 As Select language.language_id, Count(film.language_id) as films_on
From  film Right Join language On language.language_id = film.language_id
Group By language.language_id;

Update language
	Left Join v1 On v1.language_id = language.language_id
Set language.films_on = v1.films_on ;

Drop View v1;