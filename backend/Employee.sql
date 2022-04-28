CREATE TABLE Employee (
    EmployeeID integer,
    PatientID integer NOT NULL,
    --Position varchar2(30),
    PRIMARY KEY(EmployeeID)
);

INSERT INTO Employee VALUES (1, 4);
INSERT INTO Employee VALUES (2, 6);
INSERT INTO Employee VALUES (3, 8);
INSERT INTO Employee VALUES (4, 9);
