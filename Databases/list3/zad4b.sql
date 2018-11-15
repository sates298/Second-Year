DROP PROCEDURE IF EXISTS fill_tables;
DROP FUNCTION IF EXISTS generate_pesel;
DROP TRIGGER IF EXISTS check_computer_specialist_salary_insert;
DROP TRIGGER IF EXISTS check_computer_specialist_salary_update;

CREATE FUNCTION generate_pesel (given_date DATE)
RETURNS VARCHAR(11) DETERMINISTIC
RETURN CONCAT(RIGHT(REPLACE(CONVERT(given_date, CHAR),'-', ''),6), FLOOR(RAND()*(99999-10000) + 10000));


DELIMITER //
CREATE PROCEDURE fill_tables ()
BEGIN

	DECLARE iterator INT;
    DECLARE employer INT;
    DECLARE dealer INT;
    DECLARE temp_date DATE;
    DECLARE age INT;
    DECLARE temp_pesel VARCHAR(11);
    SET iterator=0;
    SET employer = 0;
    SET dealer=0;
    
    WHILE iterator < 200 DO
    
		SET temp_date = DATE_ADD(CURDATE(), INTERVAL -FLOOR(RAND() * (28470 - 5475)+ 5475) DAY);
        SET temp_pesel = generate_pesel(temp_date);
		SET age = DATEDIFF(CURDATE(), temp_date)/365;
    
		INSERT INTO people (PESEL, first_name, last_name, date_of_birth,
							growth, wage, shoe_size, favourite_color)
		VALUES( temp_pesel,
				(SELECT first_name FROM `laboratory-cinematheque`.actors ORDER BY RAND() LIMIT 1),
				(SELECT last_name FROM `laboratory-cinematheque`.actors ORDER BY RAND() LIMIT 1),
				temp_date, RAND()*(2.05 - 1.5) + 1.5, RAND()*(105-45)+45, FLOOR(RAND()*(49-35) + 35),
				ELT(FLOOR(RAND()*5 + 1), 'black', 'red', 'green', 'blue', 'white')
				); 

		IF age >= 18 THEN
			IF age <= 65 AND dealer < 77 THEN
				INSERT INTO employees(PESEL, profession, salary)
				VALUES ( temp_pesel, 'dealer', RAND()*5000 + 5000);
				SET dealer = dealer + 1;
			ELSEIF employer < 50 THEN
				INSERT INTO employees(PESEL, profession, salary)
				VALUES ( temp_pesel, 'actor', RAND()*5000 + 5000);
				SET employer = employer + 1;
			ELSEIF employer < 83 THEN
				INSERT INTO employees(PESEL, profession, salary)
				VALUES ( temp_pesel, 'agent', RAND()*5000 + 5000);
				SET employer = employer + 1;
			ELSEIF employer < 96 THEN
				INSERT INTO employees(PESEL, profession, salary)
				VALUES ( temp_pesel, 'computer specialist', RAND()*5000 + 5000);
				SET employer = employer + 1;
			ELSEIF employer < 98 THEN 
				INSERT INTO employees(PESEL, profession, salary)
				VALUES ( temp_pesel, 'reporter', RAND()*5000 + 5000);
				SET employer = employer + 1;
			END IF;
        END IF;
        
      SET iterator = iterator + 1;
    END WHILE;
    
   #SELECT employer + dealer;
    
    
END //


CREATE TRIGGER check_computer_specialist_salary_insert BEFORE INSERT ON employees
FOR EACH ROW
BEGIN

	DECLARE maxSalary FLOAT;
    DECLARE minSalary FLOAT;
    
    SET maxSalary = (SELECT MAX(salary) FROM employees WHERE profession='computer specialist');
    SET minSalary = (SELECT MIN(salary) FROM employees WHERE profession='computer specialist');
    
    
	IF NEW.salary > 3*minSalary AND NEW.profession = 'computer specialist' THEN
		SIGNAL SQLSTATE '40004'
			SET MESSAGE_TEXT = 'too big salary';
    ELSEIF NEW.salary*3 < maxSalary AND NEW.profession = 'computer specialist' THEN
		SIGNAL SQLSTATE '40004'
			SET MESSAGE_TEXT = 'too low salary';
    END IF;
    
END//

CREATE TRIGGER check_computer_specialist_salary_update BEFORE UPDATE ON employees
FOR EACH ROW
BEGIN

	DECLARE maxSalary FLOAT;
    DECLARE minSalary FLOAT;
    
    SET maxSalary = (SELECT MAX(salary) FROM employees WHERE profession='computer specialist');
    SET minSalary = (SELECT MIN(salary) FROM employees WHERE profession='computer specialist');
    
    
	IF NEW.salary > 3*minSalary AND NEW.profession = 'computer specialist' THEN
		SIGNAL SQLSTATE '40004'
			SET MESSAGE_TEXT = 'too big salary';
    ELSEIF NEW.salary*3 < maxSalary AND NEW.profession = 'computer specialist' THEN
		SIGNAL SQLSTATE '40004'
			SET MESSAGE_TEXT = 'too low salary';
    END IF;
    
END//
DELIMITER ;

#CALL fill_tables();