DROP PROCEDURE IF EXISTS theLongestAgent;

DELIMITER $$
CREATE PROCEDURE theLongestAgent()
BEGIN

	SELECT actor, agent, name, age, agent_type
    FROM contracts INNER JOIN agents ON contracts.agent = agents.license
    WHERE DATEDIFF(deadline, beginning) = (SELECT MAX(max) FROM (SELECT DATEDIFF(deadline, beginning) AS max FROM contracts) AS diffrence);
	
END $$
DELIMITER ;

CALL theLongestAgent();