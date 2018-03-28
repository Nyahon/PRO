USE daryll;

/* Script SQL that allow us to create required procedures and functions
to add, update or find some data */

/* drop procedures */
DROP PROCEDURE IF EXISTS addPeriod;
DROP PROCEDURE IF EXISTS addFloor;
DROP PROCEDURE IF EXISTS addClassroom;
DROP PROCEDURE IF EXISTS addTakePlace;
DROP PROCEDURE IF EXISTS addEquipments;
DROP PROCEDURE IF EXISTS addFloorEquipment;
DROP PROCEDURE IF EXISTS addClassroomEquipment;
DROP PROCEDURE IF EXISTS addTakePlaceClassroomFloor;
DROP FUNCTION  IF EXISTS nb_medias_by_genre;

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
	CREATE PROCEDURE addRoom(IN roomName VARCHAR(10), IN isLocked BOOL, 
    IN floorName VARCHAR(10), idEquipments TINYINT UNSIGNED)
    BEGIN
		INSERT INTO Classroom(roomName, isLocked, floorName, idEquipments) VALUES
        (roomName, isLocked, floorName, idEquipments);
	END //

/* Create a new time room period (TakePlace) */
DELIMITER //
	CREATE PROCEDURE addTakePlace(IN roomName VARCHAR(10), IN idPeriod TINYINT UNSIGNED)
    BEGIN
		INSERT INTO TakePlace(roomName, idPeriod) VALUES (roomName, idPeriod);
	END //
    
/* Create a new equipment */
DELIMITER //
	CREATE PROCEDURE addEquipments(IN beamer BOOL, IN electricalSocket BOOL,
    IN computers BOOL, IN board BOOL)
    BEGIN
		INSERT INTO Equipments(beamer, electricalSocket, computers, board) VALUES
        (beamer, electricalSocket, computers, board);
	END //
    
    
/* Create a new equipment */
DELIMITER //
	CREATE PROCEDURE addEquipments(IN toiletM BOOL, IN toiletF BOOL, IN coffeeMachine BOOL,
    IN selecta BOOL, IN wayOut BOOL)
    BEGIN
		INSERT INTO Equipments(toiletM, toiletF, coffeeMachine, selecta, wayOut) VALUES
        (toiletM, toiletF, coffeeMachine, selecta, wayOut);
	END //

