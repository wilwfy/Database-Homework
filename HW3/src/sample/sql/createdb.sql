
--SET LINESIZE 150;

CREATE TABLE Movies (
  mid            INTEGER,
  title          VARCHAR2(60),
  imdbID         INTEGER,
  spanishTitle   VARCHAR2(60),
  imdbPictureURL VARCHAR2(150),
  year           INTEGER,
  rtID           VARCHAR2(60),
  rtAllCriticsRating      NUMBER,
  rtAllCriticsNumReviews  INTEGER,
  rtAllCriticsNumFresh    INTEGER,
  rtAllCriticsNumRotten   INTEGER,
  rtAllCriticsScore       INTEGER,
  rtTopCriticsRating      NUMBER,
  rtTopCriticsNumReviews  INTEGER,
  rtTopCriticsNumFresh    INTEGER,
  rtTopCriticsNumRotten   INTEGER,
  rtTopCriticsScore       INTEGER,
  rtAudienceRating        NUMBER,
  rtAudienceNumRatings    INTEGER,
  rtAudienceScore         INTEGER,
  rtPictureURL            VARCHAR2(150),
  PRIMARY KEY (mid)
)


CREATE TABLE Mcountry (
  mid      INTEGER,
  country  VARCHAR2(50) CONSTRAINT country_nn NOT NULL,
  PRIMARY KEY (mid)
)


CREATE TABLE Genres (
  mid      INTEGER,
  genre    VARCHAR2(20) CONSTRAINT genre_nn NOT NULL,
  PRIMARY KEY (mid, genre)
  --FOREIGN KEY (mid)
  --REFERENCES Movies (mid)
  --ON DELETE CASCADE
)


CREATE TABLE Locations (
  lid        INTEGER,
  mid        INTEGER,
  location1  VARCHAR2(50),
  location2  VARCHAR2(50),
  location3  VARCHAR2(100),
  location4  VARCHAR2(150),
  PRIMARY KEY (lid)
  --FOREIGN KEY (mid)
  --REFERENCES Movies (mid)
  --ON DELETE CASCADE
)


CREATE TABLE Tags (
  tid    INTEGER,
  value  VARCHAR2(50) CONSTRAINT value_nn NOT NULL,
  PRIMARY KEY (tid)
)


CREATE TABLE Movietag (
  mid      INTEGER,
  tid      INTEGER,
  tweight  INTEGER CONSTRAINT tweight_nn NOT NULL,
  PRIMARY KEY (mid, tid)
  --FOREIGN KEY (mid)
  --REFERENCES Movies (mid)
  --ON DELETE CASCADE,
  --FOREIGN KEY (tid)
  --REFERENCES Tags (tid)
  --ON DELETE CASCADE
)