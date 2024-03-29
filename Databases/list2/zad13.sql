DROP TRIGGER IF EXISTS deleteInCast;

DELIMITER $$
CREATE TRIGGER deleteInCast AFTER DELETE ON films
FOR EACH ROW
BEGIN
	DELETE FROM cast WHERE cast.film_id NOT IN (SELECT id FROM films);
END$$
DELIMITER ;