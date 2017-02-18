--------------------------------------------------------
--  Fichier créé - mardi-janvier-17-2017   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Sequence SEQUENCE_CLIENT
--------------------------------------------------------

   CREATE SEQUENCE  "SEQUENCE_CLIENT"  MINVALUE 1 MAXVALUE 99999 INCREMENT BY 1 START WITH 81 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQUENCE_FACTURE
--------------------------------------------------------

   CREATE SEQUENCE  "SEQUENCE_FACTURE"  MINVALUE 1 MAXVALUE 99999 INCREMENT BY 1 START WITH 41 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Table APPAREILS
--------------------------------------------------------

  CREATE TABLE "APPAREILS" 
   (	"NUMERO_SERIE" VARCHAR2(20 BYTE), 
	"TYPE_GENERAL" VARCHAR2(5 BYTE) DEFAULT NULL, 
	"TYPE_PRECIS" NUMBER(10,0), 
	"PRIX_VENTE_EFFECTIF" NUMBER(10,2), 
	"POS_X" NUMBER(10,0), 
	"POS_Y" NUMBER(10,0), 
	"ETAT_SITUATION" NUMBER(10,0), 
	"ETAT_PAIEMENT" VARCHAR2(10 BYTE), 
	"TYPE_ACHAT" VARCHAR2(3 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table CLIENTS
--------------------------------------------------------

  CREATE TABLE "CLIENTS" 
   (	"ID_CLIENT" NUMBER(10,0), 
	"NOM" VARCHAR2(255 BYTE), 
	"PRENOM" VARCHAR2(255 BYTE), 
	"LOGIN" VARCHAR2(255 BYTE), 
	"PASSWORD" VARCHAR2(255 BYTE), 
	"ID_ZONE" NUMBER(10,0)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table ETATS_SITUATION
--------------------------------------------------------

  CREATE TABLE "ETATS_SITUATION" 
   (	"ID_SITUATION" NUMBER(10,0), 
	"DESCRIPTION" VARCHAR2(30 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table FACTURES
--------------------------------------------------------

  CREATE TABLE "FACTURES" 
   (	"ID_FACTURE" NUMBER(10,0), 
	"ID_CLIENT" NUMBER(10,0), 
	"DATE_FACTURATION" DATE, 
	"TYPE_ACHAT" VARCHAR2(2 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table FOURNISSEURS
--------------------------------------------------------

  CREATE TABLE "FOURNISSEURS" 
   (	"ID_FOURNISSEUR" NUMBER(10,0), 
	"NOM" VARCHAR2(255 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table ITEMS_FACTURE
--------------------------------------------------------

  CREATE TABLE "ITEMS_FACTURE" 
   (	"ID_FACTURE" NUMBER(10,0), 
	"NUMERO_SERIE" VARCHAR2(20 BYTE), 
	"PRIX" NUMBER(10,2)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table PERSONNEL
--------------------------------------------------------

  CREATE TABLE "PERSONNEL" 
   (	"ID_PERSONNEL" NUMBER(10,0), 
	"NOM" VARCHAR2(255 BYTE), 
	"PRENOM" VARCHAR2(255 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table RESERVATIONS
--------------------------------------------------------

  CREATE TABLE "RESERVATIONS" 
   (	"ID_CLIENT" NUMBER(10,0), 
	"NUMERO_SERIE" VARCHAR2(20 BYTE), 
	"DATE_RESERVATION" DATE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table SALAIRES
--------------------------------------------------------

  CREATE TABLE "SALAIRES" 
   (	"ID_SALAIRE" NUMBER(10,0), 
	"MONTANT" NUMBER(10,2), 
	"ID_PERSONNEL" NUMBER(10,0)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table TYPE_APPAREILS
--------------------------------------------------------

  CREATE TABLE "TYPE_APPAREILS" 
   (	"ID_TYPE_APPAREIL" NUMBER(10,0), 
	"TYPE" VARCHAR2(255 BYTE), 
	"MARQUE" VARCHAR2(255 BYTE), 
	"LIBELLE" VARCHAR2(255 BYTE), 
	"PRIX_ACHAT" NUMBER(10,2), 
	"PRIX_VENTE_BASE" NUMBER(10,2), 
	"ID_FOURNISSEUR" NUMBER(10,0)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table VEHICULES
--------------------------------------------------------

  CREATE TABLE "VEHICULES" 
   (	"MATRICULE" VARCHAR2(255 BYTE), 
	"SOCIETE" VARCHAR2(255 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table ZONES
--------------------------------------------------------

  CREATE TABLE "ZONES" 
   (	"ID_ZONE" NUMBER(10,0), 
	"NOM" VARCHAR2(255 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
REM INSERTING into APPAREILS
SET DEFINE OFF;
Insert into APPAREILS (NUMERO_SERIE,TYPE_GENERAL,TYPE_PRECIS,PRIX_VENTE_EFFECTIF,POS_X,POS_Y,ETAT_SITUATION,ETAT_PAIEMENT,TYPE_ACHAT) values ('123123','APE','1','1100','1','1','5',null,null);
Insert into APPAREILS (NUMERO_SERIE,TYPE_GENERAL,TYPE_PRECIS,PRIX_VENTE_EFFECTIF,POS_X,POS_Y,ETAT_SITUATION,ETAT_PAIEMENT,TYPE_ACHAT) values ('aze5465','OTHER','2','421','1','2','5',null,null);
Insert into APPAREILS (NUMERO_SERIE,TYPE_GENERAL,TYPE_PRECIS,PRIX_VENTE_EFFECTIF,POS_X,POS_Y,ETAT_SITUATION,ETAT_PAIEMENT,TYPE_ACHAT) values ('23213','OTHER','3','500','1','3','1',null,null);
Insert into APPAREILS (NUMERO_SERIE,TYPE_GENERAL,TYPE_PRECIS,PRIX_VENTE_EFFECTIF,POS_X,POS_Y,ETAT_SITUATION,ETAT_PAIEMENT,TYPE_ACHAT) values ('shdflhfs','OTHER','2','5435','1','4','5',null,null);
Insert into APPAREILS (NUMERO_SERIE,TYPE_GENERAL,TYPE_PRECIS,PRIX_VENTE_EFFECTIF,POS_X,POS_Y,ETAT_SITUATION,ETAT_PAIEMENT,TYPE_ACHAT) values ('dsfsd-dsf','OTHER','4','100','1','5','5',null,null);
Insert into APPAREILS (NUMERO_SERIE,TYPE_GENERAL,TYPE_PRECIS,PRIX_VENTE_EFFECTIF,POS_X,POS_Y,ETAT_SITUATION,ETAT_PAIEMENT,TYPE_ACHAT) values ('proute','APE','5','900','1','6','1',null,null);
Insert into APPAREILS (NUMERO_SERIE,TYPE_GENERAL,TYPE_PRECIS,PRIX_VENTE_EFFECTIF,POS_X,POS_Y,ETAT_SITUATION,ETAT_PAIEMENT,TYPE_ACHAT) values ('caca','APE','6','10','2','1','1',null,null);
Insert into APPAREILS (NUMERO_SERIE,TYPE_GENERAL,TYPE_PRECIS,PRIX_VENTE_EFFECTIF,POS_X,POS_Y,ETAT_SITUATION,ETAT_PAIEMENT,TYPE_ACHAT) values ('popo','OTHER','4','100','2','2','1',null,null);
Insert into APPAREILS (NUMERO_SERIE,TYPE_GENERAL,TYPE_PRECIS,PRIX_VENTE_EFFECTIF,POS_X,POS_Y,ETAT_SITUATION,ETAT_PAIEMENT,TYPE_ACHAT) values ('maaam','OTHER','3','432','2','3','1',null,null);
Insert into APPAREILS (NUMERO_SERIE,TYPE_GENERAL,TYPE_PRECIS,PRIX_VENTE_EFFECTIF,POS_X,POS_Y,ETAT_SITUATION,ETAT_PAIEMENT,TYPE_ACHAT) values ('jeremflo','OTHER','6','222','2','4','1',null,null);
REM INSERTING into CLIENTS
SET DEFINE OFF;
Insert into CLIENTS (ID_CLIENT,NOM,PRENOM,LOGIN,PASSWORD,ID_ZONE) values ('7',null,null,'sdf','sdfdsf',null);
Insert into CLIENTS (ID_CLIENT,NOM,PRENOM,LOGIN,PASSWORD,ID_ZONE) values ('9',null,null,'fcaaaaa','789987',null);
Insert into CLIENTS (ID_CLIENT,NOM,PRENOM,LOGIN,PASSWORD,ID_ZONE) values ('6',null,null,'seerz','ezerzze',null);
Insert into CLIENTS (ID_CLIENT,NOM,PRENOM,LOGIN,PASSWORD,ID_ZONE) values ('1','Cardoen','Florent','fca','fca',null);
Insert into CLIENTS (ID_CLIENT,NOM,PRENOM,LOGIN,PASSWORD,ID_ZONE) values ('2',null,null,'Test@me.com','test',null);
Insert into CLIENTS (ID_CLIENT,NOM,PRENOM,LOGIN,PASSWORD,ID_ZONE) values ('3',null,null,'test','test',null);
Insert into CLIENTS (ID_CLIENT,NOM,PRENOM,LOGIN,PASSWORD,ID_ZONE) values ('8',null,null,'hdht','dfghdfghdfgh',null);
Insert into CLIENTS (ID_CLIENT,NOM,PRENOM,LOGIN,PASSWORD,ID_ZONE) values ('5',null,null,'gdgs','dsfsdf',null);
Insert into CLIENTS (ID_CLIENT,NOM,PRENOM,LOGIN,PASSWORD,ID_ZONE) values ('63',null,null,'coucou','azerty',null);
Insert into CLIENTS (ID_CLIENT,NOM,PRENOM,LOGIN,PASSWORD,ID_ZONE) values ('41',null,null,'usertest','test',null);
Insert into CLIENTS (ID_CLIENT,NOM,PRENOM,LOGIN,PASSWORD,ID_ZONE) values ('64',null,null,'prouteslkjfdnglksjdf','kfdjsq',null);
Insert into CLIENTS (ID_CLIENT,NOM,PRENOM,LOGIN,PASSWORD,ID_ZONE) values ('35',null,null,'bastin','bastin',null);
Insert into CLIENTS (ID_CLIENT,NOM,PRENOM,LOGIN,PASSWORD,ID_ZONE) values ('61',null,null,'prouteproute','azerty',null);
REM INSERTING into ETATS_SITUATION
SET DEFINE OFF;
Insert into ETATS_SITUATION (ID_SITUATION,DESCRIPTION) values ('1','WA');
Insert into ETATS_SITUATION (ID_SITUATION,DESCRIPTION) values ('2','SA');
Insert into ETATS_SITUATION (ID_SITUATION,DESCRIPTION) values ('3','Livré');
Insert into ETATS_SITUATION (ID_SITUATION,DESCRIPTION) values ('4','Réservé');
Insert into ETATS_SITUATION (ID_SITUATION,DESCRIPTION) values ('5','Vendu Web');
REM INSERTING into FACTURES
SET DEFINE OFF;
Insert into FACTURES (ID_FACTURE,ID_CLIENT,DATE_FACTURATION,TYPE_ACHAT) values ('22','41',to_date('10/11/16','DD/MM/RR'),'SA');
Insert into FACTURES (ID_FACTURE,ID_CLIENT,DATE_FACTURATION,TYPE_ACHAT) values ('21','1',to_date('10/11/16','DD/MM/RR'),'SA');
REM INSERTING into FOURNISSEURS
SET DEFINE OFF;
REM INSERTING into ITEMS_FACTURE
SET DEFINE OFF;
Insert into ITEMS_FACTURE (ID_FACTURE,NUMERO_SERIE,PRIX) values ('22','aze5465','421');
Insert into ITEMS_FACTURE (ID_FACTURE,NUMERO_SERIE,PRIX) values ('22','dsfsd-dsf','100');
Insert into ITEMS_FACTURE (ID_FACTURE,NUMERO_SERIE,PRIX) values ('22','shdflhfs','5435');
Insert into ITEMS_FACTURE (ID_FACTURE,NUMERO_SERIE,PRIX) values ('21','123123','1100');
REM INSERTING into PERSONNEL
SET DEFINE OFF;
REM INSERTING into RESERVATIONS
SET DEFINE OFF;
REM INSERTING into SALAIRES
SET DEFINE OFF;
REM INSERTING into TYPE_APPAREILS
SET DEFINE OFF;
Insert into TYPE_APPAREILS (ID_TYPE_APPAREIL,TYPE,MARQUE,LIBELLE,PRIX_ACHAT,PRIX_VENTE_BASE,ID_FOURNISSEUR) values ('1','Télévision','Sony','Superbe télé','1000','1100',null);
Insert into TYPE_APPAREILS (ID_TYPE_APPAREIL,TYPE,MARQUE,LIBELLE,PRIX_ACHAT,PRIX_VENTE_BASE,ID_FOURNISSEUR) values ('2','OnePlus3','Oneplus','Trop cool','400','441',null);
Insert into TYPE_APPAREILS (ID_TYPE_APPAREIL,TYPE,MARQUE,LIBELLE,PRIX_ACHAT,PRIX_VENTE_BASE,ID_FOURNISSEUR) values ('3','PC portabke','Apple','Beaucoup trop classe','2000','3000',null);
Insert into TYPE_APPAREILS (ID_TYPE_APPAREIL,TYPE,MARQUE,LIBELLE,PRIX_ACHAT,PRIX_VENTE_BASE,ID_FOURNISSEUR) values ('4','Ecouteurs sans fils','Hocco','Mouais','10','12',null);
Insert into TYPE_APPAREILS (ID_TYPE_APPAREIL,TYPE,MARQUE,LIBELLE,PRIX_ACHAT,PRIX_VENTE_BASE,ID_FOURNISSEUR) values ('5','iPhone','Apple','Les photos sont pas moches','800','1000',null);
Insert into TYPE_APPAREILS (ID_TYPE_APPAREIL,TYPE,MARQUE,LIBELLE,PRIX_ACHAT,PRIX_VENTE_BASE,ID_FOURNISSEUR) values ('6','Laptop XPS13','Dell','Stylé','1300','1900',null);
REM INSERTING into VEHICULES
SET DEFINE OFF;
REM INSERTING into ZONES
SET DEFINE OFF;
--------------------------------------------------------
--  DDL for Index PK_VEHICULES
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_VEHICULES" ON "VEHICULES" ("MATRICULE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index PK_ETATS_SITUATION
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_ETATS_SITUATION" ON "ETATS_SITUATION" ("ID_SITUATION") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index PK_FACTURES
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_FACTURES" ON "FACTURES" ("ID_FACTURE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index PK_RESERVATION
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_RESERVATION" ON "RESERVATIONS" ("ID_CLIENT", "NUMERO_SERIE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index PK_ITEMS_FACTURE
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_ITEMS_FACTURE" ON "ITEMS_FACTURE" ("ID_FACTURE", "NUMERO_SERIE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index PK_APPAREILS
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_APPAREILS" ON "APPAREILS" ("NUMERO_SERIE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index PK_CLIENTS
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_CLIENTS" ON "CLIENTS" ("ID_CLIENT") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index PK_PERSONNEL
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_PERSONNEL" ON "PERSONNEL" ("ID_PERSONNEL") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index PK_FOURNISSEURS
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_FOURNISSEURS" ON "FOURNISSEURS" ("ID_FOURNISSEUR") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index PK_SALAIRES
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_SALAIRES" ON "SALAIRES" ("ID_SALAIRE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index PK_TYPE_APPAREILS
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_TYPE_APPAREILS" ON "TYPE_APPAREILS" ("ID_TYPE_APPAREIL") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index PK_ZONES
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_ZONES" ON "ZONES" ("ID_ZONE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index CK_CLIENTS$LOGIN
--------------------------------------------------------

  CREATE UNIQUE INDEX "CK_CLIENTS$LOGIN" ON "CLIENTS" ("LOGIN") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  Constraints for Table VEHICULES
--------------------------------------------------------

  ALTER TABLE "VEHICULES" ADD CONSTRAINT "PK_VEHICULES" PRIMARY KEY ("MATRICULE")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Constraints for Table SALAIRES
--------------------------------------------------------

  ALTER TABLE "SALAIRES" ADD CONSTRAINT "PK_SALAIRES" PRIMARY KEY ("ID_SALAIRE")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Constraints for Table APPAREILS
--------------------------------------------------------

  ALTER TABLE "APPAREILS" ADD CONSTRAINT "CK_APPAREILS$TYPE_GENERAL" CHECK (TYPE_GENERAL IN('APE', 'AFE', 'AAV', 'OTHER')) ENABLE;
  ALTER TABLE "APPAREILS" ADD CONSTRAINT "CK_APPAREILS$TYPE_ACHAT" CHECK (TYPE_ACHAT IN ('SA', 'WEB')) ENABLE;
  ALTER TABLE "APPAREILS" ADD CONSTRAINT "CK_APPAREILS$TYPE_PRECIS" CHECK (TYPE_PRECIS IS NOT NULL) ENABLE;
  ALTER TABLE "APPAREILS" ADD CONSTRAINT "PK_APPAREILS" PRIMARY KEY ("NUMERO_SERIE")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Constraints for Table RESERVATIONS
--------------------------------------------------------

  ALTER TABLE "RESERVATIONS" ADD CONSTRAINT "PK_RESERVATION" PRIMARY KEY ("ID_CLIENT", "NUMERO_SERIE")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Constraints for Table CLIENTS
--------------------------------------------------------

  ALTER TABLE "CLIENTS" ADD CONSTRAINT "PK_CLIENTS" PRIMARY KEY ("ID_CLIENT")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
  ALTER TABLE "CLIENTS" ADD CONSTRAINT "CK_CLIENTS$LOGIN" UNIQUE ("LOGIN")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Constraints for Table TYPE_APPAREILS
--------------------------------------------------------

  ALTER TABLE "TYPE_APPAREILS" ADD CONSTRAINT "PK_TYPE_APPAREILS" PRIMARY KEY ("ID_TYPE_APPAREIL")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Constraints for Table FACTURES
--------------------------------------------------------

  ALTER TABLE "FACTURES" ADD CONSTRAINT "CK_FACTURES$TYPE_ACHAT" CHECK (TYPE_ACHAT IN ('SA', 'WEB')) ENABLE;
  ALTER TABLE "FACTURES" ADD CONSTRAINT "PK_FACTURES" PRIMARY KEY ("ID_FACTURE")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Constraints for Table ETATS_SITUATION
--------------------------------------------------------

  ALTER TABLE "ETATS_SITUATION" ADD CONSTRAINT "PK_ETATS_SITUATION" PRIMARY KEY ("ID_SITUATION")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Constraints for Table FOURNISSEURS
--------------------------------------------------------

  ALTER TABLE "FOURNISSEURS" ADD CONSTRAINT "PK_FOURNISSEURS" PRIMARY KEY ("ID_FOURNISSEUR")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Constraints for Table ITEMS_FACTURE
--------------------------------------------------------

  ALTER TABLE "ITEMS_FACTURE" ADD CONSTRAINT "PK_ITEMS_FACTURE" PRIMARY KEY ("ID_FACTURE", "NUMERO_SERIE")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Constraints for Table ZONES
--------------------------------------------------------

  ALTER TABLE "ZONES" ADD CONSTRAINT "PK_ZONES" PRIMARY KEY ("ID_ZONE")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Constraints for Table PERSONNEL
--------------------------------------------------------

  ALTER TABLE "PERSONNEL" ADD CONSTRAINT "PK_PERSONNEL" PRIMARY KEY ("ID_PERSONNEL")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table APPAREILS
--------------------------------------------------------

  ALTER TABLE "APPAREILS" ADD CONSTRAINT "FK_APPAREILS$ETATSITUATION" FOREIGN KEY ("ETAT_SITUATION")
	  REFERENCES "ETATS_SITUATION" ("ID_SITUATION") ENABLE;
  ALTER TABLE "APPAREILS" ADD CONSTRAINT "FK_APPAREILS$TYPE_PRECIS" FOREIGN KEY ("TYPE_PRECIS")
	  REFERENCES "TYPE_APPAREILS" ("ID_TYPE_APPAREIL") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table CLIENTS
--------------------------------------------------------

  ALTER TABLE "CLIENTS" ADD CONSTRAINT "FK_CLIENTS$ZONE" FOREIGN KEY ("ID_ZONE")
	  REFERENCES "ZONES" ("ID_ZONE") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table FACTURES
--------------------------------------------------------

  ALTER TABLE "FACTURES" ADD CONSTRAINT "FK_FACTURES$CLIENT" FOREIGN KEY ("ID_CLIENT")
	  REFERENCES "CLIENTS" ("ID_CLIENT") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ITEMS_FACTURE
--------------------------------------------------------

  ALTER TABLE "ITEMS_FACTURE" ADD CONSTRAINT "FK_ITEMS_FACTURES$FACTURE" FOREIGN KEY ("ID_FACTURE")
	  REFERENCES "FACTURES" ("ID_FACTURE") ENABLE;
  ALTER TABLE "ITEMS_FACTURE" ADD CONSTRAINT "FK_ITEMS_FACTURES$NUMERO_SERIE" FOREIGN KEY ("NUMERO_SERIE")
	  REFERENCES "APPAREILS" ("NUMERO_SERIE") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table RESERVATIONS
--------------------------------------------------------

  ALTER TABLE "RESERVATIONS" ADD CONSTRAINT "FK_RESERVATION$CLIENT" FOREIGN KEY ("ID_CLIENT")
	  REFERENCES "CLIENTS" ("ID_CLIENT") ENABLE;
  ALTER TABLE "RESERVATIONS" ADD CONSTRAINT "FK_RESERVATION$NUMERO_SERIE" FOREIGN KEY ("NUMERO_SERIE")
	  REFERENCES "APPAREILS" ("NUMERO_SERIE") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table SALAIRES
--------------------------------------------------------

  ALTER TABLE "SALAIRES" ADD CONSTRAINT "FK_SALAIRES$PERSONNEL" FOREIGN KEY ("ID_PERSONNEL")
	  REFERENCES "PERSONNEL" ("ID_PERSONNEL") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TYPE_APPAREILS
--------------------------------------------------------

  ALTER TABLE "TYPE_APPAREILS" ADD CONSTRAINT "FK_TYPE_APP$ID_FOURNISSEUR" FOREIGN KEY ("ID_FOURNISSEUR")
	  REFERENCES "FOURNISSEURS" ("ID_FOURNISSEUR") ENABLE;
--------------------------------------------------------
--  DDL for Trigger CLIENTSID
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "CLIENTSID" 
  BEFORE INSERT ON clients
  FOR EACH ROW
DECLARE
BEGIN
  IF(:new.id_client IS NULL)
  THEN
    :new.id_client := sequence_client.nextval;
  END IF;
END;
/
ALTER TRIGGER "CLIENTSID" ENABLE;
--------------------------------------------------------
--  DDL for Trigger FACTUREID
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "FACTUREID" 
  BEFORE INSERT ON factures
  FOR EACH ROW
DECLARE
BEGIN
  IF(:new.id_facture IS NULL)
  THEN
    :new.id_facture := sequence_facture.nextval;
  END IF;
END;
/
ALTER TRIGGER "FACTUREID" ENABLE;
--------------------------------------------------------
--  DDL for Procedure CLEAN_RESERVATIONS
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "CLEAN_RESERVATIONS" AS 
BEGIN
  DBMS_OUTPUT.PUT_LINE('Test Job');
END CLEAN_RESERVATIONS;

/
