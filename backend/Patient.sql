DROP TABLE Patient;

CREATE TABLE Patient (
    PatientID integer NOT NULL,
    FName varchar2(30),
    LName varchar2(30),
    BursarAcct integer,
    InsuranceProvider varchar2(50), -- NULL = no insurance
    Birthday date,
    PRIMARY KEY(PatientID)
);

INSERT INTO Patient VALUES (1, 'John', 'Smith', 51201, NULL, DATE '1999-12-19');
INSERT INTO Patient VALUES (2, 'Jane', 'Doe', 45379, 'Blue Cross Blue Shield', DATE '1998-07-28');
INSERT INTO Patient VALUES (3, 'George', 'Washington', 34527, NULL, DATE '1960-10-11');
INSERT INTO Patient VALUES (4, 'Raul', 'Suarez', 16290, 'UA', DATE '1984-06-12');
INSERT INTO Patient VALUES (5, 'Kyle', 'Wu', 71452, 'UnitedHealthcare', DATE '2000-04-07');
INSERT INTO Patient VALUES (6, 'Debora', 'Tannen', 10257, 'UA', DATE '1971-02-21');
INSERT INTO Patient VALUES (7, 'Amon', 'Gus', 99103, 'Oranges US', DATE '1999-03-12');
INSERT INTO Patient VALUES (8, 'Flynn', 'White', 71032, 'UA', DATE '1988-12-31');
INSERT INTO Patient VALUES (9, 'James', 'McGill', 29501, 'UA', DATE '1964-05-05');

GRANT ALL PRIVILEGES ON Patient TO PUBLIC;