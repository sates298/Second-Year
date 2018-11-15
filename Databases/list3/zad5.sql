DROP PROCEDURE IF EXISTS execute_function_on;

DELIMITER //
CREATE PROCEDURE execute_function_on (IN col ENUM('PESEL','first_name', 'last_name', 'date_of_birth',
													'growth', 'wage', 'shoe_size','favourite_color'),
										agg ENUM('COUNT', 'AVG','MIN', 'MAX', 'SUM'))
BEGIN

	CASE col
		WHEN 'PESEL' THEN
			CASE agg
				WHEN 'COUNT' THEN
					SELECT 'PESEL' AS col , 'COUNT' AS agg , COUNT(PESEL) AS X FROM people;
				WHEN 'AVG' THEN
                	SELECT 'PESEL' AS col, 'AVG' AS agg ,AVG(PESEL) AS X FROM people;
				WHEN 'MIN' THEN
                	SELECT 'PESEL' AS col, 'MIN' AS agg, MIN(PESEL) AS X FROM people;
				WHEN 'MAX' THEN
					SELECT 'PESEL' AS col, 'MAX' AS agg ,MAX(PESEL) AS X FROM people;
				WHEN 'SUM' THEN
                	SELECT 'PESEL' AS col, 'SUM' AS agg ,SUM(PESEL) AS X FROM people;
                ELSE
					SELECT 'function is not aggregate';
			END CASE;
		WHEN 'first_name' THEN
			CASE agg
				WHEN 'COUNT' THEN
					SELECT 'first_name' AS col , 'COUNT' AS agg , COUNT(first_name) AS X FROM people;
				WHEN 'AVG' THEN
                	SELECT 'first_name' AS col, 'AVG' AS agg ,AVG(first_name) AS X FROM people;
				WHEN 'MIN' THEN
                	SELECT 'first_name' AS col, 'MIN' AS agg, MIN(first_name) AS X FROM people;
				WHEN 'MAX' THEN
					SELECT 'first_name' AS col, 'MAX' AS agg ,MAX(first_name) AS X FROM people;
				WHEN 'SUM' THEN
                	SELECT 'first_name' AS col, 'SUM' AS agg ,SUM(first_name) AS X FROM people;
                ELSE
					SELECT 'function is not aggregate';
			END CASE;
		WHEN 'last_name' THEN
			CASE agg
				WHEN 'COUNT' THEN
					SELECT 'last_name' AS col , 'COUNT' AS agg , COUNT(last_name) AS X FROM people;
				WHEN 'AVG' THEN
                	SELECT 'last_name' AS col, 'AVG' AS agg ,AVG(last_name) AS X FROM people;
				WHEN 'MIN' THEN
                	SELECT 'last_name' AS col, 'MIN' AS agg, MIN(last_name) AS X FROM people;
				WHEN 'MAX' THEN
					SELECT 'last_name' AS col, 'MAX' AS agg ,MAX(last_name) AS X FROM people;
				WHEN 'SUM' THEN
                	SELECT 'last_name' AS col, 'SUM' AS agg ,SUM(last_name) AS X FROM people;
                ELSE
					SELECT 'function is not aggregate';
			END CASE;
		WHEN 'date_of_birth' THEN
			CASE agg
				WHEN 'COUNT' THEN
					SELECT 'date_of_birth' AS col , 'COUNT' AS agg , COUNT(date_of_birth) AS X FROM people;
				WHEN 'AVG' THEN
                	SELECT 'date_of_birth' AS col, 'AVG' AS agg ,AVG(date_of_birth) AS X FROM people;
				WHEN 'MIN' THEN
                	SELECT 'date_of_birth' AS col, 'MIN' AS agg, MIN(date_of_birth) AS X FROM people;
				WHEN 'MAX' THEN
					SELECT 'date_of_birth' AS col, 'MAX' AS agg ,MAX(date_of_birth) AS X FROM people;
				WHEN 'SUM' THEN
                	SELECT 'date_of_birth' AS col, 'SUM' AS agg ,SUM(date_of_birth) AS X FROM people;
                ELSE
					SELECT 'function is not aggregate';
			END CASE;
		WHEN 'growth' THEN
			CASE agg
				WHEN 'COUNT' THEN
					SELECT 'growth' AS col , 'COUNT' AS agg , COUNT(growth) AS X FROM people;
				WHEN 'AVG' THEN
                	SELECT 'growth' AS col, 'AVG' AS agg ,AVG(growth) AS X FROM people;
				WHEN 'MIN' THEN
                	SELECT 'growth' AS col, 'MIN' AS agg, MIN(growth) AS X FROM people;
				WHEN 'MAX' THEN
					SELECT 'growth' AS col, 'MAX' AS agg ,MAX(growth) AS X FROM people;
				WHEN 'SUM' THEN
                	SELECT 'growth' AS col, 'SUM' AS agg ,SUM(growth) AS X FROM people;
                ELSE
					SELECT 'function is not aggregate';
			END CASE;
		WHEN 'wage' THEN
			CASE agg
				WHEN 'COUNT' THEN
					SELECT 'wage' AS col , 'COUNT' AS agg , COUNT(wage) AS X FROM people;
				WHEN 'AVG' THEN
                	SELECT 'wage' AS col, 'AVG' AS agg ,AVG(wage) AS X FROM people;
				WHEN 'MIN' THEN
                	SELECT 'wage' AS col, 'MIN' AS agg, MIN(wage) AS X FROM people;
				WHEN 'MAX' THEN
					SELECT 'wage' AS col, 'MAX' AS agg ,MAX(wage) AS X FROM people;
				WHEN 'SUM' THEN
                	SELECT 'wage' AS col, 'SUM' AS agg ,SUM(wage) AS X FROM people;
                ELSE
					SELECT 'function is not aggregate';
			END CASE;
		WHEN 'shoe_size' THEN
			CASE agg
				WHEN 'COUNT' THEN
					SELECT 'shoe_size' AS col , 'COUNT' AS agg , COUNT(shoe_size) AS X FROM people;
				WHEN 'AVG' THEN
                	SELECT 'shoe_size' AS col, 'AVG' AS agg ,AVG(shoe_size) AS X FROM people;
				WHEN 'MIN' THEN
                	SELECT 'shoe_size' AS col, 'MIN' AS agg, MIN(shoe_size) AS X FROM people;
				WHEN 'MAX' THEN
					SELECT 'shoe_size' AS col, 'MAX' AS agg ,MAX(shoe_size) AS X FROM people;
				WHEN 'SUM' THEN
                	SELECT 'shoe_size' AS col, 'SUM' AS agg ,SUM(shoe_size) AS X FROM people;
                ELSE
					SELECT 'function is not aggregate';
			END CASE;
		WHEN 'favourite_color' THEN
			CASE agg
				WHEN 'COUNT' THEN
					SELECT 'favourite_color' AS col , 'COUNT' AS agg , COUNT(favourite_color) AS X FROM people;
				WHEN 'AVG' THEN
                	SELECT 'favourite_color' AS col, 'AVG' AS agg ,AVG(favourite_color) AS X FROM people;
				WHEN 'MIN' THEN
                	SELECT 'favourite_color' AS col, 'MIN' AS agg, MIN(favourite_color) AS X FROM people;
				WHEN 'MAX' THEN
					SELECT 'favourite_color' AS col, 'MAX' AS agg ,MAX(favourite_color) AS X FROM people;
				WHEN 'SUM' THEN
                	SELECT 'favourite_color' AS col, 'SUM' AS agg ,SUM(favourite_color) AS X FROM people;
                ELSE
					SELECT 'function is not aggregate';
			END CASE;
        ELSE
			SELECT 'column does not exists';
    END CASE;

END//
DELIMITER ;


CALL execute_function_on('pesel', 'SUM');