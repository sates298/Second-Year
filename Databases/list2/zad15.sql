DROP VIEW IF EXISTS vActors;
DROP VIEW IF EXISTS vFilms;
DROP VIEW IF EXISTS vAgents;

CREATE VIEW vActors AS
SELECT first_name, last_name, films_no FROM actors;

CREATE VIEW vFilms AS
SELECT title, length FROM films;

CREATE VIEW vAgents AS 
SELECT name, agent_type FROM agents;


