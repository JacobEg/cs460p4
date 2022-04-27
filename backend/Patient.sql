CREATE TABLE Patient (
    PatientID integer,
    FName varchar2(30),
    LName varchar2(30),
    BursarAcct integer,
    InsuranceProvider varchar2(50),
    PRIMARY KEY PatientID
);

INSERT INTO Patient VALUES (1, 'John', 'Smith', 51201, NULL);
INSERT INTO Patient VALUES (2, 'Jane', 'Doe', 45379, 'Blue Cross Blue Shield');
INSERT INTO Patient VALUES (3, 'George', 'Washington', 34527, 'Aetna');
INSERT INTO Patient VALUES (4, 'Raul', 'Suarez', 16290, NULL);
INSERT INTO Patient VALUES (5, 'Kyle', 'Wu', 71452, 'UnitedHealthcare');
