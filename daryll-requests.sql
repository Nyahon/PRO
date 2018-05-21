USE daryll;

/* Script SQL that allow us to create required procedures and functions
to add, update or find some data */

/* drop procedures */
DROP PROCEDURE IF EXISTS addPeriod;
DROP PROCEDURE IF EXISTS addFloor;
DROP PROCEDURE IF EXISTS addClassroom;
DROP PROCEDURE IF EXISTS addTakePlace;
DROP PROCEDURE IF EXISTS addClassroomEquipment;
DROP PROCEDURE IF EXISTS addFloorEquipment;
DROP PROCEDURE IF EXISTS updateClassroomLock;
DROP PROCEDURE IF EXISTS updateClassroomEquipment;
DROP PROCEDURE IF EXISTS updateFloorEquipment;
DROP PROCEDURE IF EXISTS fullTimeTableFromRoom;
DROP PROCEDURE IF EXISTS occupiedRoomsAtGivenSchedule;
DROP PROCEDURE IF EXISTS classroomAdvancedSchedule;
DROP PROCEDURE IF EXISTS floorAdvancedSchedule;
DROP PROCEDURE IF EXISTS buildingAdvancedSchedule;

/* Create a new period */
DELIMITER //
	CREATE PROCEDURE addPeriod(IN idPeriod TINYINT UNSIGNED, IN startingTime TIME, IN finishingTime TIME)
	BEGIN
		INSERT INTO Period(idPeriod, startingTime, finishingTime) VALUES 
        (idPeriod, startingTime, finishingTime);
	END //
    
/* Create a new floor */
DELIMITER //
	CREATE PROCEDURE addFloor(IN floorName VARCHAR(10), IN building TINYINT UNSIGNED)
	BEGIN
		INSERT INTO Floor(floorName, building) VALUES (floorName, building);
	END //
    
/* Create a new classroom */
DELIMITER //
	CREATE PROCEDURE addClassroom(IN classroomName VARCHAR(10), IN isLocked BOOL, IN floorName VARCHAR(10), idClassroomEquipment TINYINT UNSIGNED)
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
	CREATE PROCEDURE addClassroomEquipment(IN beamer BOOL, IN electricalSocket BOOL, IN computers BOOL, IN board BOOL)
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

# Query the database to return all rooms that are occupied during this schedule
DELIMITER //
CREATE PROCEDURE fullTimeTableFromRoom(IN classroomName varchar(10))
    BEGIN
		SELECT date, idPeriod, classroomName
        FROM TakePlace
        WHERE classroomName = classroomName AND date = current_date();
	END //

# Querry the database to return all rooms that are occupied at a given schedule
DELIMITER //
CREATE PROCEDURE occupiedRoomsAtGivenSchedule(IN floorName varchar(10), IN date date, IN idPeriod tinyint(3))
	BEGIN
		SELECT TakePlace.classroomName, TakePlace.date, MIN(TakePlace.idPeriod) AS 'idPeriod'
		FROM TakePlace
		INNER JOIN Classroom
			ON TakePlace.classroomName = Classroom.classroomName
		WHERE  Classroom.floorName = floorname AND TakePlace.date = date AND TakePlace.idPeriod >= idPeriod
		GROUP BY (Classroom.classroomName);
	END //

# Query the database to return all occupied periods during the given interval and in the given classroom
DELIMITER //
CREATE PROCEDURE classroomAdvancedSchedule(building tinyint(3), dateBegin date, dateEnd date, idPeriodBegin tinyint(3), idPeriodEnd tinyint(3), classroomName varchar(10))
	BEGIN
		SELECT TakePlace.classroomName, TakePlace.date, TakePlace.idPeriod
        FROM TakePlace
		INNER JOIN Classroom
			ON TakePlace.classroomName = Classroom.classroomName
		INNER JOIN Floor
			ON Classroom.floorName = Floor.floorName
		WHERE Floor.building = building AND TakePlace.date >= dateBegin AND TakePlace.date <= dateEnd AND TakePlace.idPeriod >= idPeriodBegin AND TakePlace.idPeriod <= idPeriodEnd AND Classroom.classroomName = classroomName
        ORDER BY TakePlace.date, TakePlace.classroomName, TakePlace.idPeriod;
		END //

# Query the database to return a TimeSlot array containing all occupied periods during the given interval and in the given floor
DELIMITER //
CREATE PROCEDURE floorAdvancedSchedule(building tinyint(3), dateBegin date, dateEnd date, idPeriodBegin tinyint(3), idPeriodEnd tinyint(3), floorName varchar(10))
	BEGIN
		SELECT TakePlace.classroomName, TakePlace.date, TakePlace.idPeriod
        FROM TakePlace
		INNER JOIN Classroom
			ON TakePlace.classroomName = Classroom.classroomName
		INNER JOIN Floor
			ON Classroom.floorName = Floor.floorName
		WHERE Floor.building = building AND TakePlace.date >= dateBegin AND TakePlace.date <= dateEnd AND TakePlace.idPeriod >= idPeriodBegin AND TakePlace.idPeriod <= idPeriodEnd AND Floor.floorName = floorName
        ORDER BY TakePlace.date, TakePlace.classroomName, TakePlace.idPeriod;
		END //

# Query the database to return a TimeSlot array containing all occupied periods during the given interval and in the given building
DELIMITER //
CREATE PROCEDURE buildingAdvancedSchedule(building tinyint(3), dateBegin date, dateEnd date, idPeriodBegin tinyint(3), idPeriodEnd tinyint(3))
	BEGIN
		SELECT TakePlace.classroomName, TakePlace.date, TakePlace.idPeriod
        FROM TakePlace
		INNER JOIN Classroom
			ON TakePlace.classroomName = Classroom.classroomName
		INNER JOIN Floor
			ON Classroom.floorName = Floor.floorName
		WHERE Floor.building = building AND TakePlace.date >= dateBegin AND TakePlace.date <= dateEnd AND TakePlace.idPeriod >= idPeriodBegin AND TakePlace.idPeriod <= idPeriodEnd
        ORDER BY TakePlace.date, TakePlace.classroomName, TakePlace.idPeriod ;
		END //
