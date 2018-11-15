CREATE DATABASE IF NOT EXISTS list3people;

CREATE TABLE IF NOT EXISTS people(
	PESEL VARCHAR(11),
    first_name VARCHAR(30),
    last_name VARCHAR(30),
    date_of_birth DATE,
    growth FLOAT,
    wage FLOAT,
    shoe_size INT,
    favourite_color ENUM('black', 'red', 'green', 'blue', 'white')
);

CREATE TABLE IF NOT EXISTS employees(
	PESEL VARCHAR(11),
    profession VARCHAR(50),
    salary FLOAT
);

DELIMITER //
CREATE TRIGGER check_employees_insert BEFORE INSERT ON employees
FOR EACH ROW
BEGIN

	DECLARE birthday DATE;
    
    SET birthday = (SELECT date_of_birth FROM people WHERE PESEL = NEW.PESEL);


	IF NOT (NEW.PESEL REGEXP '[0-9]{11}' ) THEN
		SIGNAL SQLSTATE '30003'
			SET MESSAGE_TEXT= 'wrong PESEL';
    END IF;


	IF  DATEDIFF(CURDATE(), birthday) < 18  THEN
		SIGNAL SQLSTATE '30003'
			SET MESSAGE_TEXT = 'too young to employee';
    END IF;

	IF NEW.salary < 0 THEN
		SIGNAL SQLSTATE '30003'
			SET MESSAGE_TEXT = 'salary can not be less then zero';
    END IF;


END //

CREATE TRIGGER check_employees_update BEFORE UPDATE ON employees
FOR EACH ROW
BEGIN

	DECLARE birthday DATE;
    
    SET birthday = (SELECT date_of_birth FROM people WHERE PESEL = NEW.PESEL);

	IF NOT (NEW.PESEL REGEXP '[0-9]{11}' ) THEN
		SIGNAL SQLSTATE '30003'
			SET MESSAGE_TEXT= 'wrong PESEL';
    END IF;

	IF  DATEDIFF(CURDATE(), birthday) < 18  THEN
		SIGNAL SQLSTATE '30003'
			SET MESSAGE_TEXT = 'too young to employee';
    END IF;

	IF NEW.salary < 0 THEN
		SIGNAL SQLSTATE '30003'
			SET MESSAGE_TEXT = 'salary can not be less then zero';
    END IF;


END //

CREATE TRIGGER check_people_insert BEFORE INSERT ON people
FOR EACH ROW
BEGIN
	
    IF NOT (NEW.PESEL REGEXP '[0-9]{11}' ) THEN
		SIGNAL SQLSTATE '60006'
			SET MESSAGE_TEXT= 'wrong PESEL';
    END IF;
    
    IF NEW.growth <=0  OR NEW.shoe_size <=0 OR NEW.wage <= 0 THEN
		SIGNAL SQLSTATE '60006'
			SET MESSAGE_TEXT= 'something is too low';
    END IF;
    
END //

CREATE TRIGGER check_people_update BEFORE UPDATE ON people
FOR EACH ROW
BEGIN
	
    IF NOT (NEW.PESEL REGEXP '[0-9]{11}' ) THEN
		SIGNAL SQLSTATE '60006'
			SET MESSAGE_TEXT= 'wrong PESEL';
    END IF;
    
    IF NEW.growth <=0  OR NEW.shoe_size <=0 OR NEW.wage <= 0 THEN
		SIGNAL SQLSTATE '60006'
			SET MESSAGE_TEXT= 'something is too low';
    END IF;
    
END //

DELIMITER ;