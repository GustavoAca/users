CREATE TABLE USUARIOS (
	ID                   UUID                       NOT NULL,
	USERNAME             VARCHAR(250)               NOT NULL,
	PASSWORD             VARCHAR(250)               NOT NULL,
	CREATED_DATE         timestamp with time zone   NULL,
	MODIFIED_DATE        timestamp with time zone   NULL,
	CREATED_BY           VARCHAR(100)               NULL,
	MODIFIED_BY          VARCHAR(100)               NULL,
	CONSTRAINT USERS_PK PRIMARY KEY ( ID )
 );

 CREATE table LOCAIS (
   ID                   UUID                       NOT NULL,
   NOME                 VARCHAR(250)               NOT NULL,
   ENDERECO             VARCHAR(250)               NOT NULL,
   CEP                  VARCHAR(13)                NOT NULL,
   LOGRADOURO           VARCHAR(250)               NOT NULL,
   BAIRRO               VARCHAR(200)               NOT NULL,
   NUMERO               VARCHAR(30)                NOT NULL,
   ESTADO               VARCHAR(80)                NOT NULL,
   CREATED_DATE         timestamp with time zone   NULL,
   MODIFIED_DATE        timestamp with time zone   NULL,
   CREATED_BY           VARCHAR(100)               NULL,
   MODIFIED_BY          VARCHAR(100)               NULL,
   CONSTRAINT LOCAIS_PK PRIMARY KEY ( ID )
 );