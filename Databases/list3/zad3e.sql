SELECT first_name, COUNT(first_name) as amount
FROM actors
GROUP BY first_name
HAVING amount = (
		SELECT MAX(quantity) as maxi
			FROM (SELECT first_name, COUNT(first_name) as quantity
					FROM actors
					GROUP BY first_name) AS temp) ;
