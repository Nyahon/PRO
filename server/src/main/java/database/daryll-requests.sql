USE daryll;

/* Script SQL that allow us to create required procedures and functions
to add, update or find some data */

/* drop procedures */
DROP PROCEDURE IF EXISTS addPeriod;
DROP PROCEDURE IF EXISTS addFloor;
DROP PROCEDURE IF EXISTS addClassroom;
DROP PROCEDURE IF EXISTS addTakePlace;
DROP PROCEDURE IF EXISTS addFloorEquipment;
DROP PROCEDURE IF EXISTS addClassroomEquipment;
DROP PROCEDURE IF EXISTS updateClassroomLock;
DROP PROCEDURE IF EXISTS updateClassroomEquipment;
DROP PROCEDURE IF EXISTS updateFloorEquipment;
DROP PROCEDURE IF EXISTS fullTimeTableFromRoom;
DROP PROCEDURE IF EXISTS occupiedRoomsAtGivenSchedule;

/* Create a new period */
DELIMITER //
	CREATE PROCEDURE addPeriod(IN idPeriod TINYINT UNSIGNED, IN startingTime TIME, IN finishingTime TIME)
	BEGIN
		INSERT INTO Period(idPeriod, startingTime, finishingTime) VALUES 
        (idPeriod, startingTime, finishingTime);
	END //
    
/* Create a new floor */
DELIMITER //
	CREATE PROCEDURE addFloor(IN floorName VARCHAR(10), IN place TINYINT UNSIGNED)
	BEGIN
		INSERT INTO Floor(floorName, place) VALUES (floorName, place);
	END //
    
/* Create a new classroom */
DELIMITER //
	CREATE PROCEDURE addClassroom(IN classroomName VARCHAR(10), IN isLocked BOOL, 
    IN floorName VARCHAR(10), idClassroomEquipment TINYINT UNSIGNED)
    BEGIN
		INSERT INTO Classroom(classroomName, isLocked, floorName, idClassroomEquipment) VALUES
        (classroomName, isLocked, floorName, idClassroomEquipment);
	END //

/* Create a new couple of room and period (TakePlace) */
DELIMITER //
	CREATE PROCEDURE addTakePlace(IN date DATE, IN idPeriod TINYINT UNSIGNED, IN classroomName VARCHAR(10))
    BEGIN
		INSERT INTO TakePlace(date, idPeriod, classroomName) VALUES (date, idPeriod, classroomName);
	END //
    
/* Create a new equipment */
DELIMITER //
	CREATE PROCEDURE addClassroomEquipment(IN beamer BOOL, IN electricalSocket BOOL,
    IN computers BOOL, IN board BOOL)
    BEGIN
		INSERT INTO ClassroomEquipment(beamer, electricalSocket, computers, board) VALUES
        (beamer, electricalSocket, computers, board);
	END //
    
    
/* Create a new equipment */
DELIMITER //
	CREATE PROCEDURE addFloorEquipment(IN toiletM BOOL, IN toiletF BOOL, IN coffeeMachine BOOL, IN selecta BOOL, IN wayOut BOOL)
    BEGIN
		INSERT INTO FloorEquipment(toiletM, toiletF, coffeeMachine, selecta, wayOut) VALUES
        (toiletM, toiletF, coffeeMachine, selecta, wayOut);
	END //
    
/* Update the classroom lock state */
DELIMITER //
	CREATE PROCEDURE updateClassroomLock(IN classroomName VARCHAR(10), IN isLocked BOOL)
    BEGIN
		UPDATE Classroom SET isLocked=isLocked WHERE classroomName=classroomName;
	END //

/* Update the classroom equipment */
DELIMITER //
	CREATE PROCEDURE updateClassroomEquipment(IN idClassroomEquipment TINYINT UNSIGNED, IN beamer BOOL, IN electricalSocket BOOL, IN computers BOOL, IN board BOOL)
    BEGIN
		UPDATE ClassroomEquipment 
		SET beamer=beamer, electricalSocket=electricalSocket, computers=computers, beamer=board 
		WHERE idClassroomEquipment=idClassroomEquipment;
	END //

/* Update the floor equipment */
DELIMITER //
	CREATE PROCEDURE updateFloorEquipment(IN idFloorEquipment TINYINT UNSIGNED, IN toiletM BOOL, IN toiletF BOOL, IN coffeeMachine BOOL, IN selecta BOOL, IN wayOut BOOL)
    BEGIN
		UPDATE FloorEquipment 
		SET toiletM=toiletM, toiletF=toiletF, coffeeMachine=coffeeMachine, selecta=selecta, wayOut=wayOut 
		WHERE idFloorEquipment=idFloorEquipment;
	END //
    
DELIMITER //
CREATE PROCEDURE fullTimeTableFromRoom(IN classroomName varchar(10))
    BEGIN
		SELECT *
        FROM TakePlace
        INNER JOIN Period
			ON TakePlace.idPeriod = Period.idPeriod
        WHERE TakePlace.classroomName = classroomName;
	END //

DELIMITER //
CREATE PROCEDURE occupiedRoomsAtGivenSchedule(IN floorName varchar(10), IN date date, IN idPeriod tinyint(3))
	BEGIN
		SELECT * 
		FROM TakePlace
		INNER JOIN  Period
			ON TakePlace.idPeriod = Period.idPeriod
		INNER JOIN Classroom
			ON TakePlace.classroomName = Classroom.classroomName
		WHERE  Classroom.floorName = floorname AND TakePlace.date = date AND Period.idPeriod >= idPeriod;
	END //
