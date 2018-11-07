DROP TABLE contracts;
DROP TABLE agents;

CREATE TABLE IF NOT EXISTS agents(
	license VARCHAR(30) NOT NULL ,
    name VARCHAR(64),
    age INT,
    agent_type ENUM('individual', 'agency', 'other'),
    PRIMARY KEY (license),
    CHECK (wiek >=21)
);

#ALTER TABLE actors ADD PRIMARY KEY (id);

CREATE TABLE IF NOT EXISTS contracts (
    id INT NOT NULL AUTO_INCREMENT,
    agent VARCHAR(30) NOT NULL,
    actor INT,
    beginning DATE,
    deadline DATE,
    pay INT,
    PRIMARY KEY (id),
    CONSTRAINT FK_AgentContract 
		FOREIGN KEY (agent)
        REFERENCES agents (license)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
	CONSTRAINT FK_ActorContract
		FOREIGN KEY (actor)
        REFERENCES actors (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CHECK (deadline > beginning AND pay >= 0)
);
                


					