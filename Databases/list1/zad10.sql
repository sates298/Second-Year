Select distinct country.country
From city Inner Join country On country.country_id = city.country_id
Group By city.country_id
Having Count(city.country_id) >= (Select Count(city.country_id)
		From city Inner Join country On country.country_id = city.country_id
		Where country.country Like 'Canada') 


    
    
