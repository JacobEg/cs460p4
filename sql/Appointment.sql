DROP TABLE Appointment;

CREATE TABLE Appointment (
    ApptNo integer NOT NULL,
    CheckinTime timestamp,
    InPerson char(1) CONSTRAINT NO_FAIL CHECK (InPerson='Y' OR InPerson='N'), -- 'Y' = in person, 'N' = not in person
    Service varchar2(30),
    EmployeeID integer,
    PatientID integer,
    PRIMARY KEY(ApptNo)
);

INSERT INTO Appointment VALUES (1, TIMESTAMP '2022-05-14 08:30:00', 'N', 'CAPS', 1, 3);
INSERT INTO Appointment VALUES (2, TIMESTAMP '2022-05-13 10:15:00', 'Y', 'Immunization', 2, 1);
INSERT INTO Appointment VALUES (3, TIMESTAMP '2022-05-10 07:30:00', 'Y', 'Laboratory and Testing', 3, 5);
INSERT INTO Appointment VALUES (4, TIMESTAMP '2022-04-09 18:30:00', 'Y', 'General Medicine', 4, 7);

GRANT ALL PRIVILEGES ON Appointment TO PUBLIC;