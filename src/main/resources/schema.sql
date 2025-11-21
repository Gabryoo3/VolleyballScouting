CREATE TABLE player
(
    name varchar(100) NOT NULL,
    surname varchar(11) NOT NULL ,
    number int(10) NOT NULL,
    cardcode varchar(100) DEFAULT NULL,
    role varchar(100) DEFAULT NULL,
    team varchar(100) NOT NULL,
    PRIMARY KEY (cardcode)
);      