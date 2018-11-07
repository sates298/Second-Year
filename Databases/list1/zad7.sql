Select distinct film.title
From film Inner Join inventory On film.film_id = inventory.film_id
	Inner Join rental On inventory.inventory_id = rental.inventory_id
Where rental.rental_date >= '2005-05-25' And rental.rental_date <= '2005-05-30'
