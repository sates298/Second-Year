DROP TRIGGER IF EXISTS insertIntoCast;
DROP TRIGGER IF EXISTS updateCast;
DROP TRIGGER IF EXISTS deleteFromCast;

DELIMITER $$
CREATE TRIGGER insertIntoCast AFTER INSERT ON cast
FOR EACH ROW
BEGIN

	UPDATE actors 
		LEFT JOIN (SELECT actor_id, COUNT(film_id) As films_no
				FROM cast GROUP BY actor_id) As cast1 ON actors.id = cast1.actor_id
	SET actors.films_no = cast1.films_no;

END $$

CREATE TRIGGER updateCast AFTER UPDATE ON cast
FOR EACH ROW
BEGIN

	UPDATE actors 
		LEFT JOIN (SELECT actor_id, COUNT(film_id) As films_no
				FROM cast GROUP BY actor_id) As cast1 ON actors.id = cast1.actor_id
	SET actors.films_no = cast1.films_no;

END $$

CREATE TRIGGER deleteFromCast AFTER DELETE ON cast
FOR EACH ROW
BEGIN

	UPDATE actors 
		LEFT JOIN (SELECT actor_id, COUNT(film_id) As films_no
				FROM cast GROUP BY actor_id) As cast1 ON actors.id = cast1.actor_id
	SET actors.films_no = cast1.films_no;

END $$
DELIMITER ;