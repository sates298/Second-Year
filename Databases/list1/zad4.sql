Select film.title
From film 
	Inner Join film_category On film.film_id = film_category.film_id 
		Inner Join category On category.category_id = film_category.category_id
Where  film.description Not Like '%Documentary%'
	And category.name Like 'Documentary';