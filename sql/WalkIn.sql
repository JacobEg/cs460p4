DROP TABLE WalkIn;

CREATE TABLE WalkIn (
    IsEmergency char(1) CONSTRAINT NO_FAIL2 CHECK (IsEmergency='Y' OR IsEmergency='N'),
    ApptNo integer NOT NULL
);

INSERT INTO WalkIn VALUES ('Y', 4);

GRANT ALL PRIVILEGES ON WalkIn TO PUBLIC;