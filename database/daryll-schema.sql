
DROP SCHEMA IF EXISTS daryll;
CREATE SCHEMA daryll;

USE daryll;

SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE Period(
	idPeriod TINYINT UNSIGNED NOT NULL,
    startingTime TIME NOT NULL,
    finishingTime TIME NOT NULL,
    PRIMARY KEY (idPeriod)
);

CREATE TABLE TakePlace(
    date DATE NOT NULL,
    idPeriod TINYINT UNSIGNED NOT NULL,
    roomName VARCHAR(10) NOT NULL,
    FOREIGN KEY (idPeriod) REFERENCES Period (idPeriod),
    FOREIGN KEY (roomName) REFERENCES Classroom (roomName),
    PRIMARY KEY (roomName, idPeriod)
);

CREATE TABLE Classroom(
	roomName VARCHAR(10) NOT NULL,
    isLocked BOOL NOT NULL,
    floorName VARCHAR(10) NOT NULL,
    idClassroomEquipment TINYINT UNSIGNED NOT NULL,
    FOREIGN KEY (floorName) REFERENCES Floor (floorName),
    FOREIGN KEY (idClassroomEquipment) REFERENCES ClassroomEquipment (idClassroomEquipment),
    PRIMARY KEY (roomName)
);


CREATE TABLE ClassroomEquipment(
	idClassroomEquipment TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    beamer BOOL NOT NULL,
    electricalSocket BOOL NOT NULL,
    computers BOOL NOT NULL,
    board BOOL NOT NULL,
    PRIMARY KEY (idClassroomEquipment)
);

CREATE TABLE FloorEquipment(
	idFloorEquipment TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    toiletM BOOL NOT NULL,
    toiletF BOOL NOT NULL,
    coffeeMachine BOOL NOT NULL,
    selecta BOOL NOT NULL,
    wayOut BOOL NOT NULL,
    PRIMARY KEY (idFloorEquipment)
);

CREATE TABLE Floor(
	floorName VARCHAR(10) NOT NULL,
    place TINYINT UNSIGNED NOT NULL,
    idFloorEquipment TINYINT UNSIGNED NOT NULL,
    FOREIGN KEY (idFloorEquipment) REFERENCES FloorEquipment (idFloorEquipment),
    PRIMARY KEY (floorName)
);

SET FOREIGN_KEY_CHECKS = 1;
