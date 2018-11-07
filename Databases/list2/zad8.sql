DROP PROCEDURE IF EXISTS findAveragePay;

DELIMITER $$
CREATE PROCEDURE findAveragePay(actual_license varchar(30))
BEGIN

	SELECT AVG(pay) AS average
    FROM contracts 
    WHERE agent = actual_license
		AND deadline > CURDATE()
        AND beginning < CURDATE();
        
END $$
DELIMITER ;

CALL findAveragePay('STLI-127913')