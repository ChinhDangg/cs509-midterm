DROP TABLE IF EXISTS Account;

CREATE TABLE Account (
     Id integer NOT NULL auto_increment,
     Balance decimal(10,2) NOT NULL,
     HoldersName varchar(255) NOT NULL,
     Login varchar(255) NOT NULL,
     Pin integer NOT NULL,
     Role bit NOT NULL,
     Status bit NOT NULL,
     PRIMARY KEY (Id)
) engine=InnoDB;