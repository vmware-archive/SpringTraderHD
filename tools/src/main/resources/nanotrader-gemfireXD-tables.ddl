ALTER TABLE ORDERS
    DROP CONSTRAINT FKC3DF62E518A618B8;

ALTER TABLE ORDERS
    DROP CONSTRAINT FKC3DF62E5D2E54D7A;

ALTER TABLE ACCOUNT
    DROP CONSTRAINT FKE49F160D2BA34895;

-- ----------------------------------------------------------------------- 
-- QUOTE 
-- ----------------------------------------------------------------------- 

DROP TABLE QUOTE;

-- ----------------------------------------------------------------------- 
-- ORDERS 
-- ----------------------------------------------------------------------- 

DROP TABLE ORDERS;

-- ----------------------------------------------------------------------- 
-- HOLDING 
-- ----------------------------------------------------------------------- 

DROP TABLE HOLDING;

-- ----------------------------------------------------------------------- 
-- HIBERNATE_SEQUENCES 
-- ----------------------------------------------------------------------- 

DROP TABLE HIBERNATE_SEQUENCES;

-- ----------------------------------------------------------------------- 
-- ACCOUNT 
-- ----------------------------------------------------------------------- 

DROP TABLE ACCOUNT;

-- ----------------------------------------------------------------------- 
-- ACCOUNTPROFILE 
-- ----------------------------------------------------------------------- 

DROP TABLE ACCOUNTPROFILE;

-- -----------------------------------------------------------------------
-- BUSY_STOCK
-- -----------------------------------------------------------------------

DROP TABLE BUSY_STOCK;

-- ----------------------------------------------------------------------- 
-- PROCEDURE CHAOSFUNCTION 
-- ----------------------------------------------------------------------- 

DROP PROCEDURE CHAOSFUNCTION;

-- -----------------------------------------------------------------------
-- HDFS STORE
-- -----------------------------------------------------------------------

CREATE HDFSSTORE st_datastore
    NameNode 'hdfs://localhost:9000'
    HomeDir 'st-tables'
    BatchSize 100
    BatchTimeInterval 60000
    QueuePersistent true;

-- ----------------------------------------------------------------------- 
-- ACCOUNTPROFILE 
-- ----------------------------------------------------------------------- 

CREATE TABLE ACCOUNTPROFILE
(
    PROFILEID INTEGER NOT NULL,
    ADDRESS VARCHAR(250),
    AUTHTOKEN VARCHAR(100),
    CREDITCARD VARCHAR(250),
    EMAIL VARCHAR(250),
    FULLNAME VARCHAR(250),
    PASSWD VARCHAR(250),
    USERID VARCHAR(250) NOT NULL,
    PRIMARY KEY (PROFILEID)
)
PERSISTENT
REPLICATE;

CREATE UNIQUE INDEX ACCOUNTPROFILE_USERID_KEY ON ACCOUNTPROFILE (USERID);
CREATE INDEX ACCOUNTPROFILE_AUTHTOKEN_KEY ON ACCOUNTPROFILE (AUTHTOKEN);


-- ----------------------------------------------------------------------- 
-- ACCOUNT 
-- ----------------------------------------------------------------------- 

CREATE TABLE ACCOUNT
(
    ACCOUNTID INTEGER NOT NULL,
    BALANCE NUMERIC(14,2),
    CREATIONDATE DATE,
    LASTLOGIN DATE,
    LOGINCOUNT INTEGER NOT NULL,
    LOGOUTCOUNT INTEGER NOT NULL,
    OPENBALANCE NUMERIC(14,2),
    PROFILE_PROFILEID INTEGER,
    VERSION INTEGER,
    PRIMARY KEY (ACCOUNTID)
)
PERSISTENT
BUCKETS 12
HDFSSTORE (st_datastore)
PARTITION BY PRIMARY KEY;

CREATE UNIQUE INDEX ACCOUNT_PROFILE_PROFILEID_KEY ON ACCOUNT (PROFILE_PROFILEID);
-- ----------------------------------------------------------------------- 
-- HIBERNATE_SEQUENCES 
-- ----------------------------------------------------------------------- 

CREATE TABLE HIBERNATE_SEQUENCES
(
    SEQUENCE_NAME VARCHAR(255),
    SEQUENCE_NEXT_HI_VALUE INTEGER
)
REPLICATE
PERSISTENT;

-- ----------------------------------------------------------------------- 
-- HOLDING 
-- ----------------------------------------------------------------------- 

CREATE TABLE HOLDING
(
    HOLDINGID INTEGER NOT NULL,
    ACCOUNT_ACCOUNTID INTEGER,
    PURCHASEDATE DATE,
    PURCHASEPRICE NUMERIC(14,2),
    QUANTITY NUMERIC(14,0) NOT NULL,
    QUOTE_SYMBOL VARCHAR(250),
    PRIMARY KEY (HOLDINGID)
)
PERSISTENT
PARTITION BY COLUMN (ACCOUNT_ACCOUNTID)
COLOCATE WITH (ACCOUNT);

CREATE INDEX HOLDING_ACCOUNT_ACCOUNTID_KEY ON HOLDING (ACCOUNT_ACCOUNTID);
-- ----------------------------------------------------------------------- 
-- ORDERS 
-- ----------------------------------------------------------------------- 

CREATE TABLE ORDERS
(
    ORDERID INTEGER NOT NULL,
    COMPLETIONDATE DATE,
    OPENDATE DATE,
    ORDERFEE NUMERIC(14,2),
    ORDERSTATUS VARCHAR(250),
    ORDERTYPE VARCHAR(250),
    PRICE NUMERIC(14,2),
    QUANTITY NUMERIC(19,2) NOT NULL,
    ACCOUNT_ACCOUNTID INTEGER,
    HOLDING_HOLDINGID INTEGER,
    QUOTE_SYMBOL VARCHAR(250),
    PRIMARY KEY (ORDERID)
)
PARTITION BY COLUMN (ACCOUNT_ACCOUNTID)
COLOCATE WITH (ACCOUNT)
BUCKETS 12
HDFSSTORE (st_datastore);

CREATE INDEX ORDERS_HOLDING_HOLDINGID_KEY ON ORDERS (HOLDING_HOLDINGID);

-- ----------------------------------------------------------------------- 
-- QUOTE 
-- ----------------------------------------------------------------------- 

CREATE TABLE QUOTE
(
    QUOTEID INTEGER NOT NULL,
    LOW NUMERIC(14,2),
    OPEN1 NUMERIC(14,2),
    VOLUME NUMERIC(19,2) NOT NULL,
    PRICE NUMERIC(14,2),
    HIGH NUMERIC(14,2),
    COMPANYNAME VARCHAR(250),
    SYMBOL VARCHAR(250) NOT NULL,
    CHANGE1 NUMERIC(19,2) NOT NULL,
    VERSION INTEGER,
    PRIMARY KEY (QUOTEID)
)
PERSISTENT
REPLICATE;
--PARTITION BY COLUMN (SYMBOL);
--PARTITION BY RANGE (SYMBOL)
--(
--  VALUES BETWEEN 'A' AND 'H',
--  VALUES BETWEEN 'I' AND 'P',
--  VALUES BETWEEN 'Q' AND 'Z'
--);

CREATE UNIQUE INDEX QUOTE_SYMBOL_KEY ON QUOTE (SYMBOL);

-- ------------------------------------------------------------------------
-- BUSY_STOCK
-- ------------------------------------------------------------------------

CREATE TABLE BUSY_STOCK
(
    QUOTESYMBOL VARCHAR(250) NOT NULL,
    COUNT INTEGER
)
PERSISTENT;


ALTER TABLE ACCOUNT
    ADD CONSTRAINT FKE49F160D2BA34895 FOREIGN KEY (PROFILE_PROFILEID) REFERENCES ACCOUNTPROFILE (PROFILEID);

ALTER TABLE ORDERS
    ADD CONSTRAINT FKC3DF62E518A618B8 FOREIGN KEY (ACCOUNT_ACCOUNTID) REFERENCES ACCOUNT (ACCOUNTID);

    
    
CREATE PROCEDURE CHAOSFUNCTION ()
    
    LANGUAGE JAVA 
    PARAMETER STYLE JAVA 
    READS SQL DATA 
EXTERNAL NAME 'org.springframework.nanotrader.chaos.sqlfire.ChaosFunction.killProcess';
