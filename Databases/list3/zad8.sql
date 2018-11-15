CREATE DATABASE IF NOT EXISTS `3.ex8logs`;

USE `3.ex8logs`;
CREATE TABLE IF NOT EXISTS logs(
	old_value FLOAT,
	new_value FLOAT,
    change_date DATE,
    executor VARCHAR(50)
);

USE list3people;
DROP TRIGGER IF EXISTS new_log;

DELIMITER //
CREATE TRIGGER new_log AFTER UPDATE ON employees
FOR EACH ROW
BEGIN

	IF OLD.salary <> NEW.salary THEN
		INSERT INTO `3.ex8logs`.logs
		VALUES (OLD.salary, NEW.salary, CURDATE(), CURRENT_USER());
	END IF;

END//
DELIMITER ;