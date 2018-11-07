PREPARE statement 
FROM 
'SELECT COUNT(DISTINCT actor) AS clients_no
	FROM contracts 
    WHERE agent = ?';

SET @lic = 'STLI-127600';
EXECUTE statement USING @lic;

DEALLOCATE PREPARE statement;