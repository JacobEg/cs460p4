CREATE TABLE Shift (
    EmployeeID integer,
    StartTime datetime,
    EndTime datetime,
    SName varchar2(30)
);

INSERT INTO Shift VALUES (1, '2022-05-14 08:00:00', '2022-05-14 18:00:00', 'CAPS');
INSERT INTO Shift VALUES (2, '2022-05-13 07:30:00', '2022-05-13 16:45:00', 'Immunization');
INSERT INTO Shift VALUES (3, '2022-05-10 06:50:00', '2022-05-10 11:00:00', 'Laboratory & Testing');
INSERT INTO Shift VALUES (4, '2022-05-09 09:00:00', '2022-05-09 20:00:00', 'General Medicine');
