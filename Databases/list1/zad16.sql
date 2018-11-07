#Alter Table language Add films_on int  ;
Create View v1 As Select language.language_id, Count(film.language_id) as films_on
From  film Right Join language On language.language_id = film.language_id
Group By language.language_id;

Update language
	Left Join v1 On v1.language_id = language.language_id
Set language.films_on = v1.films_on ;

Drop View v1;

