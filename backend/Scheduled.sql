DROP TABLE Scheduled;

CREATE TABLE Scheduled (
    BookTime datetime,
    ApptNo integer NOT NULL
);

INSERT INTO Scheduled VALUES ('2022-05-14 08:30:00', 1);
INSERT INTO Scheduled VALUES ('2022-05-13 10:15:00', 2);
INSERT INTO Scheduled VALUES ('2022-05-10 07:30:00', 3);