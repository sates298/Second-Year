DROP PROCEDURE IF EXISTS fill30Contracts;

DELIMITER $$
CREATE PROCEDURE fill30Contracts()
BEGIN 
	DECLARE id_value INT;
    DECLARE inserted INT; 
	SET id_value = 1;
    SET inserted = 0;
	WHILE inserted < 30 DO
    
		IF EXISTS (SELECT id FROM actors WHERE id = id_value) THEN
		
    		INSERT INTO contracts(actor, agent, beginning, deadline, pay)
			VALUES( (SELECT actors.id FROM actors WHERE actors.id = id_value),
					(SELECT license FROM agents ORDER BY RAND() LIMIT 1),
					DATE_ADD('2015-01-01', INTERVAL -FLOOR(RAND()*365) DAY),
					DATE_ADD('2015-01-01', INTERVAL FLOOR(RAND()*365) DAY),
					FLOOR(RAND()*(10000-2000) + 2000));
            
            SET inserted = inserted + 1;
		END IF;
		SET id_value = id_value +1;

    END WHILE;
END $$
DELIMITER ;

CALL fill30Contracts();

ALTER TABLE contracts CHANGE pay pay INT COMMENT '[$/1 month]';


DROP TRIGGER IF EXISTS checkAge;
DROP TRIGGER IF EXISTS checkContract;
DROP TRIGGER IF EXISTS checkAgeU;
DROP TRIGGER IF EXISTS checkContractU;

DELIMITER $$
CREATE TRIGGER checkAge BEFORE INSERT ON agents
FOR EACH ROW
BEGIN

	IF NEW.age < 21 THEN
		SIGNAL SQLSTATE '10001'
			SET MESSAGE_TEXT = 'age of agent has to be at least 21';
    END IF;

END $$ 

CREATE TRIGGER checkContract BEFORE INSERT ON contracts
FOR EACH ROW
BEGIN

	IF NEW.pay < 0 THEN 
		SIGNAL SQLSTATE '20002'
			SET MESSAGE_TEXT = 'pay hast to be at least 0';
    END IF;
    
    IF NEW.beginning >= NEW.deadline THEN
		SIGNAL SQLSTATE '30003'
			SET MESSAGE_TEXT = 'over has to be after beginning at least 1 day';
    END IF;

END $$

CREATE TRIGGER checkAgeU BEFORE UPDATE ON agents
FOR EACH ROW
BEGIN

	IF NEW.age < 21 THEN
		SIGNAL SQLSTATE '10001'
			SET MESSAGE_TEXT = 'age of agent has to be at least 21';
    END IF;

END $$ 

CREATE TRIGGER checkContractU BEFORE UPDATE ON contracts
FOR EACH ROW
BEGIN

	IF NEW.pay < 0 THEN 
		SIGNAL SQLSTATE '20002'
			SET MESSAGE_TEXT = 'pay hast to be at least 0';
    END IF;
    
    IF NEW.beginning >= NEW.deadline THEN
		SIGNAL SQLSTATE '30003'
			SET MESSAGE_TEXT = 'over has to be after beginning at least 1 day';
    END IF;

END $$
DELIMITER ;


/*
UPDATE contracts 
	INNER JOIN contracts AS c2 ON contracts.id = c2.id
SET contracts.pay = -2000
WHERE contracts.actor = '1';
*/