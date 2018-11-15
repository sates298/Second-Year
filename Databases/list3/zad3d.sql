SELECT actors.id, actors.first_name, actors.last_name, contracts.deadline
FROM actors 
	INNER JOIN contracts ON contracts.actor = actors.id
WHERE actors.id IN
	(SELECT actor_id
    FROM cast 
    WHERE DATEDIFF(deadline,CURDATE()) = (SELECT MIN(DATEDIFF(deadline, CURDATE())) AS minim 
											FROM contracts WHERE deadline > CURDATE()) );