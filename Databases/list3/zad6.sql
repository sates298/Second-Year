DROP PROCEDURE IF EXISTS pay_salary;

DELIMITER //
CREATE PROCEDURE pay_salary(IN budget FLOAT, profe ENUM('dealer', 'actor', 'agent', 'reporter', 'computer specialist'))
BEGIN
	DECLARE done INT DEFAULT 0;
    DECLARE success INT DEFAULT 0;
    DECLARE tmp_salary FLOAT;
    DECLARE tmp_pesel VARCHAR(11);
    DECLARE curs_sal CURSOR FOR
		SELECT salary FROM employees WHERE profession=profe; 
	DECLARE curs_pes CURSOR FOR
		SELECT PESEL FROM employees WHERE profession=profe;
        
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done=1;
	DROP TABLE IF EXISTS paid;

	CREATE TEMPORARY TABLE paid (
		person VARCHAR(11),
		state ENUM('paid', 'not paid')
	);
    
    START TRANSACTION;
		OPEN curs_sal;
        OPEN curs_pes;
		WHILE done=0 DO
			FETCH curs_sal INTO tmp_salary;
            FETCH curs_pes INTO tmp_pesel;
            IF done=0 THEN
				SET budget = budget - tmp_salary;
				IF budget < 0 THEN
					SET done = 1;
					SET success = 0;
				ELSE
					SET tmp_pesel = CONCAT('********', RIGHT(tmp_pesel,3));
					INSERT 	INTO paid VALUES(tmp_pesel,'paid');
					SET success = 1;
				END IF;
            END IF;
        END WHILE;
        CLOSE curs_sal;
        CLOSE curs_pes;
	IF success = 0 THEN
		ROLLBACK;
	ELSE
		COMMIT;
	END IF;
    
    SELECT * FROM paid;
    
    DROP TABLE IF EXISTS paid;

END//
DELIMITER ;



CALL pay_salary(2000000, 'dealer');