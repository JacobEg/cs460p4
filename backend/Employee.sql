DROP TABLE Employee;

CREATE TABLE Employee (
    EmployeeID integer,
    PatientID integer NOT NULL,
    AccountNum integer,
    RoutingNum integer,
    --Position varchar2(30),
    PRIMARY KEY(EmployeeID)
);

INSERT INTO Employee VALUES (1, 4, 213746, 1097421);
INSERT INTO Employee VALUES (2, 6, 2374012743, 172381);
INSERT INTO Employee VALUES (3, 8, 991272, 263718);
INSERT INTO Employee VALUES (4, 9, 61289476, 1987246);

GRANT ALL PRIVILEGES ON Employee to PUBLIC;