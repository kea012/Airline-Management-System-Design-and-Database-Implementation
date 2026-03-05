DROP INDEX IF EXISTS FlightInstance_FN_index;
DROP INDEX IF EXISTS FlightInstance_FD_index;
DROP INDEX IF EXISTS FlightInstance_TC_index;
DROP INDEX IF EXISTS FlightInstance_NOS_index;
DROP INDEX IF EXISTS Schedule_FN_index;
DROP INDEX IF EXISTS Schedule_DOW_index;
DROP INDEX IF EXISTS Schedule_DT_index;
DROP INDEX IF EXISTS Schedule_AT_index;
DROP INDEX IF EXISTS Reservation_S_index;
DROP INDEX IF EXISTS Reservation_FIID_index;
DROP INDEX IF EXISTS Repair_TID_index;
DROP INDEX IF EXISTS Repair_PID_index;
DROP INDEX IF EXISTS Repair_RD_index;
DROP INDEX IF EXISTS Flight_DC_index;
DROP INDEX IF EXISTS Flight_AC_index;
DROP INDEX IF EXISTS CustomerCredentials_CID_index;
DROP INDEX IF EXISTS MaintenanceRequest_RD_index;
DROP INDEX IF EXISTS MaintenanceRequest_PTID_index;
DROP INDEX IF EXISTS MaintenanceRequest_RC_index;
DROP INDEX IF EXISTS MaintenanceRequest_PID_index;


CREATE INDEX FlightInstance_FN_index
ON FlightInstance
USING BTREE
(FLightNumber);

CREATE INDEX FlightInstance_FD_index
ON FlightInstance
USING BTREE
(FLightDate);

CREATE INDEX FlightInstance_TC_index
ON FlightInstance
USING BTREE
(TicketCost);

CREATE INDEX FlightInstance_NOS_index
ON FlightInstance
USING BTREE
(NumOfStops);

CREATE INDEX Schedule_FN_index
ON Schedule
USING BTREE
(FlightNumber);

CREATE INDEX Schedule_DOW_index
ON Schedule
USING BTREE
(DayOfWeek);

CREATE INDEX Reservation_S_index
ON Reservation
USING BTREE
(Status);

CREATE INDEX Reservation_FIID_index
ON Reservation
USING BTREE
(FlightInstacneID);

CREATE INDEX Repair_TID_index
ON Repair
USING BTREE
(TechnicianID);

CREATE INDEX Repair_PID_index
ON Repair
USING BTREE
(PlaneID);

CREATE INDEX Repair_RD_index
ON Repair
USING BTREE
(RepairDate);

CREATE INDEX Flight_DC_index
ON Flight
USING BTREE
(DepartureCity);

CREATE INDEX Flight_AC_index
ON Flight
USING BTREE
(ArrivalCity);


CREATE INDEX Schedule_DT_index
ON Schedule
USING BTREE
(DepartureTime);

CREATE INDEX Schedule_AT_index
ON Schedule
USING BTREE
(ArrivalTime);

CREATE INDEX CustomerCredentials_CID_index
ON CustomerCredentials
USING BTREE
(custID);


CREATE INDEX MaintenanceRequest_RD_index
ON MaintenanceRequest
USING BTREE
(RequestDate);

CREATE INDEX MaintenanceRequest_PTID_index
ON MaintenanceRequest
USING BTREE
(PilotID);

CREATE INDEX MaintenanceRequest_PID_index
ON MaintenanceRequest
USING BTREE
(PlaneID);

CREATE INDEX MaintenanceRequest_RC_index
ON MaintenanceRequest
USING BTREE
(RepairCode);