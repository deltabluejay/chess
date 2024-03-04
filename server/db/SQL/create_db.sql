-- Create the "chess" database
CREATE DATABASE IF NOT EXISTS chess;

-- Switch to the "chess" database
USE chess;

-- Create the "user" table
CREATE TABLE IF NOT EXISTS user (
    username VARCHAR(50) PRIMARY KEY NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL
);

-- Create the "auth" table
CREATE TABLE IF NOT EXISTS auth (
    authToken VARCHAR(100) PRIMARY KEY NOT NULL,
    username VARCHAR(50) NOT NULL,
    FOREIGN KEY (username) REFERENCES user(username)
);

-- Create the "game" table
CREATE TABLE IF NOT EXISTS game (
    gameID INT PRIMARY KEY AUTO_INCREMENT,
    gameName VARCHAR(100) NOT NULL,
    gameString VARCHAR(1000),
    whitePlayer VARCHAR(50),
    blackPlayer VARCHAR(50),
    FOREIGN KEY (whitePlayer) REFERENCES user(username),
    FOREIGN KEY (blackPlayer) REFERENCES user(username)
);

-- Create the "observers" table
CREATE TABLE IF NOT EXISTS observers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    gameID INT,
    username VARCHAR(50),
    FOREIGN KEY (gameID) REFERENCES game(gameID),
    FOREIGN KEY (username) REFERENCES user(username)
);
