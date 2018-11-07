Select distinct customer.customer_id, customer.first_name, customer.last_name
From customer Inner Join rental On rental.customer_id = customer.customer_id
Group By rental.customer_id
Having Count(rental.customer_id) > (Select Count(rental.customer_id) As quantity
	From rental Inner Join customer ON customer.customer_id = rental.customer_id
	Where customer.email Like 'PETER.MENARD@sakilacustomer.org')
Order By customer.customer_id