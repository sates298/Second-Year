DROP VIEW IF EXISTS aboutActor;

CREATE VIEW aboutActor AS 
SELECT first_name, last_name, agents.name, DATEDIFF(contracts.deadline, CURDATE()) AS deadline_left
FROM actors  
	INNER JOIN contracts ON contracts.actor = actors.id 
	INNER JOIN agents ON agents.license = contracts.agent
WHERE deadline > CURDATE() AND beginning < CURDATE();

#DROP VIEW IF EXISTS aboutActor;
