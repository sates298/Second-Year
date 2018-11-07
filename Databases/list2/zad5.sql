DROP PROCEDURE IF EXISTS fillAgents;
DROP PROCEDURE IF EXISTS fillContracts;

DELIMITER $$
CREATE PROCEDURE fillAgents()
BEGIN
	DECLARE i INT;
	SET i = 1;

	WHILE i<=1000 DO
		INSERT INTO agents(license, name, age, agent_type)
			SELECT CONCAT('STLI-',i + 127530),
					(SELECT actors.last_name FROM actors ORDER BY RAND() LIMIT 1) ,
					RAND()*(70-21) + 21,
					ELT(FLOOR(RAND()*3 + 1),'individual', 'agency', 'other') ;
		SET i = i + 1;    
	END WHILE;

END $$
DELIMITER ;

CALL fillAgents();

DELIMITER $$
CREATE PROCEDURE fillContracts()
BEGIN 
	DECLARE j INT;
	SET j = 1;
	WHILE j <=200 DO
    
		INSERT INTO contracts(actor, agent, beginning, deadline, pay)
			SELECT (SELECT actors.id FROM actors WHERE actors.id = j),
					(SELECT license FROM agents ORDER BY RAND() LIMIT 1),
					DATE_ADD('2018-06-01', INTERVAL -FLOOR(RAND()*365) DAY),
					DATE_ADD('2018-06-01', INTERVAL FLOOR(RAND()*365)+1 DAY),
					FLOOR(RAND()*(10000-2000) + 2000);
	SET j = j +1;  
    DELETE FROM contracts WHERE actor IS NULL;
	
    END WHILE;
END $$
DELIMITER ;
CALL fillContracts();