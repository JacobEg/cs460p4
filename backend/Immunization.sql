DROP TABLE Immunization;

CREATE TABLE Immunization (
    INo integer NOT NULL,
    ApptNo integer NOT NULL,
    IType varchar2(20) NOT NULL,
    PRIMARY KEY (INo)
);

INSERT INTO Immunization VALUES (1, 2, 'COVID-19');

GRANT ALL PRIVILEGES ON Immunization TO PUBLIC;