DROP TRIGGER IF EXISTS addContract;

DELIMITER $$
CREATE TRIGGER addContract BEFORE INSERT ON contracts
FOR EACH ROW
BEGIN

	IF EXISTS (SELECT * FROM contracts WHERE actor = NEW.actor AND beginning <= NEW.beginning AND deadline > NEW.beginning) THEN
		SIGNAL SQLSTATE '40004'
			SET MESSAGE_TEXT = 'This actor has an actual contract';
    END IF;
     
	IF NEW.agent NOT IN (SELECT license FROM agents)
    THEN
		INSERT INTO agents(license, name, age, agent_type)
			VALUES (NEW.agent, 
				(SELECT actors.last_name FROM actors ORDER BY RAND() LIMIT 1) ,
				RAND()*(70-21) + 21,
				ELT(FLOOR(RAND()*3 + 1),'individual', 'agency', 'other'));
    END IF;
	
END $$
DELIMITER ;


INSERT INTO contracts(agent, actor, beginning, deadline, pay)
	VALUES ('STLI-11111',
		(SELECT id FROM actors WHERE id=7),
		CURDATE(),
        DATE_ADD(CURDATE(), INTERVAL 20 DAY),
		FLOOR(RAND()*(10000-2000) + 2000));
        
    /*    
DROP PROCEDURE IF EXISTS triggerContracts
        
DELIMITER $$
CREATE PROCEDURE triggerContracts(agent_license VARCHAR(30), actor_id INT, newBeginning DATE, newDeadline DATE, salary INT)
BEGIN 
	IF NOT EXISTS (SELECT * FROM agents WHERE agent_license=license) THEN
		INSERT INTO agents(license, name, age, agent_type)
			VALUES (agent_license, 
				(SELECT actors.last_name FROM actors ORDER BY RAND() LIMIT 1) ,
				RAND()*(70-21) + 21,
				ELT(FLOOR(RAND()*3 + 1),'individual', 'agency', 'other'));
	ELSEIF EXISTS(SELECT * FROM contracts WHERE actor_id = actor AND deadline>newBeginning) THEN 
		UPDATE contracts
			SET deadline=newBeginning
            WHERE actor_id = actor AND  deadline > newBeginning;
    END IF;
    INSERT INTO contracts (agent, actor, beginning, deadline, pay)
		VALUES (agent_license, actor_id, newBeginning, newDeadline, salary);
END $$
DELIMITER ;
        */
        