
DROP SCHEMA IF EXISTS daryll;
CREATE SCHEMA daryll;

USE daryll;

SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE Period(
	idPeriod TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    startingTime TIME NOT NULL,
    finishingTime TIME NOT NULL,
    PRIMARY KEY (idPeriod)
);

CREATE TABLE TakePlace(
    date DATE NOT NULL,
    idPeriod TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    roomName VARCHAR(10) NOT NULL,
    FOREIGN KEY (idPeriod) REFERENCES Period (idPeriod),
    FOREIGN KEY (roomName) REFERENCES Classroom (roomName)
);

CREATE TABLE Classroom(
	roomName VARCHAR(15) NOT NULL,
    isLocked BOOL NOT NULL,
    floorName VARCHAR(15) NOT NULL,
    idEquipments TINYINT UNSIGNED NOT NULL,
    PRIMARY KEY (roomName),
    FOREIGN KEY (floorName) REFERENCES Floor (floorName),
    FOREIGN KEY (idEquipments) REFERENCES Equipments (idEquipments)
);


CREATE TABLE Equipments(
	idEquipments TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    
    beamer BOOL NOT NULL,
    electricalSocket BOOL NOT NULL,
    computers BOOL NOT NULL,
    board BOOL NOT NULL,
    PRIMARY KEY (idEquipments)
);

CREATE TABLE Floor(
	floorNumber SMALLINT,
	floorName VARCHAR(10) NOT NULL,
    place TINYINT NOT NULL,
    
	PRIMARY KEY (floorName),
    idFloorEquipments TINYINT UNSIGNED NOT NULL,
    FOREIGN KEY (idFloorEquipments) REFERENCES FloorEquipments (idFloorEquipments)
);

CREATE TABLE FloorEquipments(
	idFloorEquipments TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    toiletM BOOL NOT NULL,
    toiletF BOOL NOT NULL,
    coffeeMachine BOOL NOT NULL,
    selecta BOOL NOT NULL,
    wayOut BOOL NOT NULL,
    PRIMARY KEY (idFloorEquipments)
);

SET FOREIGN_KEY_CHECKS = 1;
