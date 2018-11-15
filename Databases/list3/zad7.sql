DROP PROCEDURE IF EXISTS privacy;
DROP FUNCTION IF EXISTS laplace;

CREATE FUNCTION laplace(parameter FLOAT, true_value FLOAT)
RETURNS FLOAT DETERMINISTIC
RETURN (1/(2* parameter))*EXP( (-1/parameter) * ABS(true_value));

DELIMITER //
CREATE PROCEDURE privacy (IN col ENUM('growth','wage','salary'),
							profe ENUM('dealer','actor', 'agent', 'reporter', 'computer specialist'))
BEGIN

	DECLARE maximum FLOAT;
    DECLARE minimum FLOAT;
    DECLARE random_to_compare FLOAT;
    DECLARE true_sum FLOAT;
    DECLARE fake_sum FLOAT;
	DECLARE rand_raw_to_change FLOAT;
    DECLARE sensitiveness FLOAT;
    DECLARE added_privacy FLOAT;
    DECLARE parameter FLOAT;
    DECLARE epsilon FLOAT DEFAULT 0.05;

	DROP TABLE IF EXISTS temp_table;
	SET @set_creation = CONCAT("CREATE TEMPORARY TABLE temp_table AS (SELECT ", col, " AS  wanted_value
						FROM people INNER JOIN employees ON people.PESEL = employees.PESEL
                        WHERE profession = '", profe,"')");
	PREPARE create_table FROM @set_creation;
    EXECUTE create_table;
    DEALLOCATE PREPARE create_table;

	SET maximum = (SELECT MAX(wanted_value) FROM temp_table);
    SET minimum = (SELECT MIN(wanted_value) FROM temp_table);
	SET random_to_compare = RAND()*(maximum-minimum) + minimum;

	SET true_sum = (SELECT SUM(wanted_value) FROM temp_table);
    
    SET rand_raw_to_change = (SELECT wanted_value FROM temp_table ORDER BY RAND() LIMIT 1);
    UPDATE temp_table SET wanted_value = random_to_compare WHERE wanted_value = rand_raw_to_change;
	
    SET fake_sum = (SELECT SUM(wanted_value) FROM temp_table);
    SET sensitiveness = (SELECT ABS(true_sum - fake_sum));
    SET parameter = (SELECT sensitiveness/epsilon);
    SET added_privacy = laplace(parameter,true_sum);
    
    SELECT true_sum+added_privacy AS total;
    
END//
DELIMITER ;


CALL privacy('salary', 'reporter');