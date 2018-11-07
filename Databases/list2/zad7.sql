DROP PROCEDURE IF EXISTS findActor;

DELIMITER $$
CREATE PROCEDURE findActor(first_n varchar(32), last_n varchar(64))
BEGIN

	SELECT agent, DATEDIFF(deadline, CURDATE()) AS time_left
    FROM contracts INNER JOIN actors ON actors.id = contracts.actor
    WHERE actors.first_name = first_n 
		AND actors.last_name = last_n
		AND deadline > CURDATE();
        
END $$
DELIMITER ;

CALL findActor('NICK', 'WAHLBERG');