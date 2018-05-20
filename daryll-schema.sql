
/* Script for the creation of the database */
DROP SCHEMA IF EXISTS daryll;
CREATE SCHEMA daryll;

USE daryll;

/* Unset foreign key checks for creation of the different tables */
SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE Period (
    idPeriod TINYINT UNSIGNED NOT NULL,
    startingTime TIME NOT NULL,
    finishingTime TIME NOT NULL,
    PRIMARY KEY (idPeriod)
);

CREATE TABLE Classroom (
    classroomName VARCHAR(10) NOT NULL,
    isLocked BOOL NOT NULL,
    floorName VARCHAR(10) NOT NULL,
    idClassroomEquipment TINYINT UNSIGNED NOT NULL,
    FOREIGN KEY (floorName)
        REFERENCES Floor (floorName),
    FOREIGN KEY (idClassroomEquipment)
        REFERENCES ClassroomEquipment (idClassroomEquipment),
    PRIMARY KEY (classroomName)
);

/* Creation of the classroom equipment table (a classroom or more 
can reference an element of this table as this is just to know
if a given equipment is provided in the classroom*/
CREATE TABLE ClassroomEquipment (
    idClassroomEquipment TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    beamer BOOL NOT NULL,
    electricalSocket BOOL NOT NULL,
    computers BOOL NOT NULL,
    board BOOL NOT NULL,
    PRIMARY KEY (idClassroomEquipment)
);

/* Creation of table TakePlace, 
   it links a classroom with a period at a certain date */
CREATE TABLE TakePlace (
    date DATE NOT NULL,
    idPeriod TINYINT UNSIGNED NOT NULL,
    classroomName VARCHAR(10) NOT NULL,
    FOREIGN KEY (idPeriod)
        REFERENCES Period (idPeriod),
    FOREIGN KEY (classroomName)
        REFERENCES Classroom (classroomName),
    PRIMARY KEY (date, idPeriod, classroomName)
);

CREATE TABLE Floor (
    floorName VARCHAR(10) NOT NULL,
    building TINYINT UNSIGNED NOT NULL,
    idFloorEquipment TINYINT UNSIGNED,
    FOREIGN KEY (idFloorEquipment)
        REFERENCES FloorEquipment (idFloorEquipment),
    PRIMARY KEY (floorName)
);

/* Creation of the floor equipment (a floor or more 
can reference an element of this table as this is just to know
if a given equipment is provided on the floor)*/
CREATE TABLE FloorEquipment (
    idFloorEquipment TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    toiletM BOOL NOT NULL,
    toiletF BOOL NOT NULL,
    coffeeMachine BOOL NOT NULL,
    selecta BOOL NOT NULL,
    wayOut BOOL NOT NULL,
    PRIMARY KEY (idFloorEquipment)
);

/* Set foreign key checks after creation of all the required tables */
SET FOREIGN_KEY_CHECKS = 1;
