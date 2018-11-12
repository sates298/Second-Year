#ALTER TABLE contracts ADD INDEX finish(deadline);

SELECT actors.id, deadline
FROM actors INNER JOIN contracts ON actors.id = contracts.actor
WHERE deadline <= DATE_ADD(CURDATE(), INTERVAL 1 MONTH) AND deadline > CURDATE();