--------------------------------------------------------
--  DDL for Table APPAREILS
--------------------------------------------------------

  CREATE TABLE "SHOP"."APPAREILS" 
   (	"NUMERO_SERIE" VARCHAR2(20 BYTE), 
	"TYPE_GENERAL" VARCHAR2(5 BYTE) DEFAULT NULL, 
	"TYPE_APPAREIL" NUMBER(10,0), 
	"PRIX_VENTE_EFFECTIF" NUMBER(10,2), 
	"POS_X" NUMBER(10,0), 
	"POS_Y" NUMBER(10,0), 
	"ETAT_SITUATION" NUMBER(10,0), 
	"ETAT_PAIEMENT" VARCHAR2(10 BYTE), 
	"TYPE_ACHAT" VARCHAR2(3 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table CLIENTS
--------------------------------------------------------

  CREATE TABLE "SHOP"."CLIENTS" 
   (	"ID_CLIENT" NUMBER(10,0), 
	"NOM" VARCHAR2(255 BYTE), 
	"PRENOM" VARCHAR2(255 BYTE), 
	"LOGIN" VARCHAR2(255 BYTE), 
	"PASSWORD" VARCHAR2(255 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table ETATS_SITUATION
--------------------------------------------------------

  CREATE TABLE "SHOP"."ETATS_SITUATION" 
   (	"ID_SITUATION" NUMBER(10,0), 
	"DESCRIPTION" VARCHAR2(30 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table FACTURES
--------------------------------------------------------

  CREATE TABLE "SHOP"."FACTURES" 
   (	"ID_FACTURE" NUMBER(10,0), 
	"ID_CLIENT" NUMBER(10,0), 
	"DATE_FACTURATION" DATE, 
	"TYPE_ACHAT" VARCHAR2(3 BYTE), 
	"MODE_PAIEMENT" VARCHAR2(10 BYTE), 
	"ADRESSE_LIVRAISON" VARCHAR2(1000 BYTE), 
	"ID_VILLE" NUMBER(10,0)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table FOURNISSEURS
--------------------------------------------------------

  CREATE TABLE "SHOP"."FOURNISSEURS" 
   (	"ID_FOURNISSEUR" NUMBER(10,0), 
	"NOM" VARCHAR2(255 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table ITEMS_FACTURE
--------------------------------------------------------

  CREATE TABLE "SHOP"."ITEMS_FACTURE" 
   (	"ID_FACTURE" NUMBER(10,0), 
	"NUMERO_SERIE" VARCHAR2(20 BYTE), 
	"PRIX" NUMBER(10,2)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table PERSONNEL
--------------------------------------------------------

  CREATE TABLE "SHOP"."PERSONNEL" 
   (	"ID_PERSONNEL" NUMBER(10,0), 
	"NOM" VARCHAR2(255 BYTE), 
	"PRENOM" VARCHAR2(255 BYTE), 
	"LOGIN" VARCHAR2(255 BYTE), 
	"PASSWORD" VARCHAR2(255 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table RESERVATIONS
--------------------------------------------------------

  CREATE TABLE "SHOP"."RESERVATIONS" 
   (	"ID_CLIENT" NUMBER(10,0), 
	"NUMERO_SERIE" VARCHAR2(20 BYTE), 
	"DATE_RESERVATION" DATE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table SALAIRES
--------------------------------------------------------

  CREATE TABLE "SHOP"."SALAIRES" 
   (	"ID_SALAIRE" NUMBER(10,0), 
	"MONTANT" NUMBER(10,2), 
	"ID_PERSONNEL" NUMBER(10,0)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table TYPE_APPAREILS
--------------------------------------------------------

  CREATE TABLE "SHOP"."TYPE_APPAREILS" 
   (	"ID_TYPE_APPAREIL" NUMBER(10,0), 
	"MARQUE" VARCHAR2(255 BYTE), 
	"LIBELLE" VARCHAR2(255 BYTE), 
	"PRIX_ACHAT" NUMBER(10,2), 
	"PRIX_VENTE_BASE" NUMBER(10,2), 
	"ID_FOURNISSEUR" NUMBER(10,0), 
	"TYPE_PRECIS" NUMBER(10,0)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table TYPE_PRECIS
--------------------------------------------------------

  CREATE TABLE "SHOP"."TYPE_PRECIS" 
   (	"ID_TYPE_PRECIS" NUMBER(10,0), 
	"TYPE" VARCHAR2(255 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table VEHICULES
--------------------------------------------------------

  CREATE TABLE "SHOP"."VEHICULES" 
   (	"MATRICULE" VARCHAR2(255 BYTE), 
	"SOCIETE" VARCHAR2(255 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table VENTES_RATEES
--------------------------------------------------------

  CREATE TABLE "SHOP"."VENTES_RATEES" 
   (	"ID_VENTE_RATEE" NUMBER(10,0), 
	"NUMERO_SERIE" VARCHAR2(20 BYTE), 
	"ID_PERSONNEL" NUMBER(10,0), 
	"TEMPS" NUMBER(4,0), 
	"ARGUMENT" VARCHAR2(4000 BYTE), 
	"REMARQUE" VARCHAR2(4000 BYTE)
   ) SEGMENT CREATION DEFERRED 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table VENTES_REUSSIES
--------------------------------------------------------

  CREATE TABLE "SHOP"."VENTES_REUSSIES" 
   (	"ID_PERSONNEL" NUMBER(10,0), 
	"ID_FACTURE" NUMBER(10,0), 
	"TEMPS" NUMBER(3,0), 
	"ARGUMENT" VARCHAR2(4000 BYTE), 
	"REMARQUE" VARCHAR2(4000 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table VILLES
--------------------------------------------------------

  CREATE TABLE "SHOP"."VILLES" 
   (	"ID_VILLE" NUMBER(10,0), 
	"NOM" VARCHAR2(255 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Sequence SEQUENCE_CLIENT
--------------------------------------------------------

   CREATE SEQUENCE  "SHOP"."SEQUENCE_CLIENT"  MINVALUE 0 MAXVALUE 99999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE  NOPARTITION ;
--------------------------------------------------------
--  DDL for Sequence SEQUENCE_FACTURE
--------------------------------------------------------

   CREATE SEQUENCE  "SHOP"."SEQUENCE_FACTURE"  MINVALUE 0 MAXVALUE 99999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE  NOPARTITION ;
REM INSERTING into SHOP.APPAREILS
SET DEFINE OFF;
REM INSERTING into SHOP.CLIENTS
SET DEFINE OFF;
REM INSERTING into SHOP.ETATS_SITUATION
SET DEFINE OFF;
Insert into SHOP.ETATS_SITUATION (ID_SITUATION,DESCRIPTION) values ('1','WA');
Insert into SHOP.ETATS_SITUATION (ID_SITUATION,DESCRIPTION) values ('2','SA');
Insert into SHOP.ETATS_SITUATION (ID_SITUATION,DESCRIPTION) values ('3','Livré');
Insert into SHOP.ETATS_SITUATION (ID_SITUATION,DESCRIPTION) values ('4','Réservé');
Insert into SHOP.ETATS_SITUATION (ID_SITUATION,DESCRIPTION) values ('5','Vendu Web');
Insert into SHOP.ETATS_SITUATION (ID_SITUATION,DESCRIPTION) values ('6','Vendu SA');
REM INSERTING into SHOP.FACTURES
SET DEFINE OFF;
REM INSERTING into SHOP.FOURNISSEURS
SET DEFINE OFF;
REM INSERTING into SHOP.ITEMS_FACTURE
SET DEFINE OFF;
REM INSERTING into SHOP.PERSONNEL
SET DEFINE OFF;
Insert into SHOP.PERSONNEL (ID_PERSONNEL,NOM,PRENOM,LOGIN,PASSWORD) values ('2','Cardoen','Florent','fca','fca');
Insert into SHOP.PERSONNEL (ID_PERSONNEL,NOM,PRENOM,LOGIN,PASSWORD) values ('3','Bastin','Jérémy','jba','jba');
Insert into SHOP.PERSONNEL (ID_PERSONNEL,NOM,PRENOM,LOGIN,PASSWORD) values ('4','Ploc','Ploc','Plic','plic');
Insert into SHOP.PERSONNEL (ID_PERSONNEL,NOM,PRENOM,LOGIN,PASSWORD) values ('5','Pipi','popo','PIPI','POPO');
Insert into SHOP.PERSONNEL (ID_PERSONNEL,NOM,PRENOM,LOGIN,PASSWORD) values ('6','COCO','popo','coco','motdepasse');
Insert into SHOP.PERSONNEL (ID_PERSONNEL,NOM,PRENOM,LOGIN,PASSWORD) values ('7','Hiho','Yo','Hiho','azerty');
Insert into SHOP.PERSONNEL (ID_PERSONNEL,NOM,PRENOM,LOGIN,PASSWORD) values ('8','Lolo','Planete','Lolo','tttt');
Insert into SHOP.PERSONNEL (ID_PERSONNEL,NOM,PRENOM,LOGIN,PASSWORD) values ('9','poule','Swag','Swag','depoule');
Insert into SHOP.PERSONNEL (ID_PERSONNEL,NOM,PRENOM,LOGIN,PASSWORD) values ('10','Poulet','Tuto','Poulet','Spaghettis');
Insert into SHOP.PERSONNEL (ID_PERSONNEL,NOM,PRENOM,LOGIN,PASSWORD) values ('1','SHOP','Jérémy','SHOP','SHOP');
REM INSERTING into SHOP.RESERVATIONS
SET DEFINE OFF;
REM INSERTING into SHOP.SALAIRES
SET DEFINE OFF;
REM INSERTING into SHOP.TYPE_APPAREILS
SET DEFINE OFF;
Insert into SHOP.TYPE_APPAREILS (ID_TYPE_APPAREIL,MARQUE,LIBELLE,PRIX_ACHAT,PRIX_VENTE_BASE,ID_FOURNISSEUR,TYPE_PRECIS) values ('1','Sony','TV HD 2116','1000','1100',null,'2');
Insert into SHOP.TYPE_APPAREILS (ID_TYPE_APPAREIL,MARQUE,LIBELLE,PRIX_ACHAT,PRIX_VENTE_BASE,ID_FOURNISSEUR,TYPE_PRECIS) values ('2','Oneplus','OnePlusThree','400','441',null,'1');
Insert into SHOP.TYPE_APPAREILS (ID_TYPE_APPAREIL,MARQUE,LIBELLE,PRIX_ACHAT,PRIX_VENTE_BASE,ID_FOURNISSEUR,TYPE_PRECIS) values ('3','Apple','Iphone 6','2000','3000',null,'1');
Insert into SHOP.TYPE_APPAREILS (ID_TYPE_APPAREIL,MARQUE,LIBELLE,PRIX_ACHAT,PRIX_VENTE_BASE,ID_FOURNISSEUR,TYPE_PRECIS) values ('4','Hocco','Je sais pas ce que c''est','10','12',null,'3');
Insert into SHOP.TYPE_APPAREILS (ID_TYPE_APPAREIL,MARQUE,LIBELLE,PRIX_ACHAT,PRIX_VENTE_BASE,ID_FOURNISSEUR,TYPE_PRECIS) values ('5','Apple','Iphone 5','800','1000',null,'1');
Insert into SHOP.TYPE_APPAREILS (ID_TYPE_APPAREIL,MARQUE,LIBELLE,PRIX_ACHAT,PRIX_VENTE_BASE,ID_FOURNISSEUR,TYPE_PRECIS) values ('6','Dell','Dell Ovion','1300','1900',null,'4');
Insert into SHOP.TYPE_APPAREILS (ID_TYPE_APPAREIL,MARQUE,LIBELLE,PRIX_ACHAT,PRIX_VENTE_BASE,ID_FOURNISSEUR,TYPE_PRECIS) values ('9','Iiyama','PL-MD56027','400','450',null,'8');
Insert into SHOP.TYPE_APPAREILS (ID_TYPE_APPAREIL,MARQUE,LIBELLE,PRIX_ACHAT,PRIX_VENTE_BASE,ID_FOURNISSEUR,TYPE_PRECIS) values ('10','Medion','Radio de cuisine','10','50',null,'9');
Insert into SHOP.TYPE_APPAREILS (ID_TYPE_APPAREIL,MARQUE,LIBELLE,PRIX_ACHAT,PRIX_VENTE_BASE,ID_FOURNISSEUR,TYPE_PRECIS) values ('11','Beko','Lave vaisselle de base','500','530',null,'10');
Insert into SHOP.TYPE_APPAREILS (ID_TYPE_APPAREIL,MARQUE,LIBELLE,PRIX_ACHAT,PRIX_VENTE_BASE,ID_FOURNISSEUR,TYPE_PRECIS) values ('12','UE','UE BOOM','200','220',null,'5');
Insert into SHOP.TYPE_APPAREILS (ID_TYPE_APPAREIL,MARQUE,LIBELLE,PRIX_ACHAT,PRIX_VENTE_BASE,ID_FOURNISSEUR,TYPE_PRECIS) values ('13','Bose','MiniPlayer','400','420',null,'5');
Insert into SHOP.TYPE_APPAREILS (ID_TYPE_APPAREIL,MARQUE,LIBELLE,PRIX_ACHAT,PRIX_VENTE_BASE,ID_FOURNISSEUR,TYPE_PRECIS) values ('14','Apple','Macbook 13"','1300','1400',null,'4');
Insert into SHOP.TYPE_APPAREILS (ID_TYPE_APPAREIL,MARQUE,LIBELLE,PRIX_ACHAT,PRIX_VENTE_BASE,ID_FOURNISSEUR,TYPE_PRECIS) values ('15','Samsung','Tv de chez Samsung pas cher','200','220',null,'2');
Insert into SHOP.TYPE_APPAREILS (ID_TYPE_APPAREIL,MARQUE,LIBELLE,PRIX_ACHAT,PRIX_VENTE_BASE,ID_FOURNISSEUR,TYPE_PRECIS) values ('16','Dell','Clavier de chez Dell','30','35',null,'3');
Insert into SHOP.TYPE_APPAREILS (ID_TYPE_APPAREIL,MARQUE,LIBELLE,PRIX_ACHAT,PRIX_VENTE_BASE,ID_FOURNISSEUR,TYPE_PRECIS) values ('17','Antec','Boitier ordinateur','70','80',null,'6');
Insert into SHOP.TYPE_APPAREILS (ID_TYPE_APPAREIL,MARQUE,LIBELLE,PRIX_ACHAT,PRIX_VENTE_BASE,ID_FOURNISSEUR,TYPE_PRECIS) values ('18','JVC','Camera','300','320',null,'11');
Insert into SHOP.TYPE_APPAREILS (ID_TYPE_APPAREIL,MARQUE,LIBELLE,PRIX_ACHAT,PRIX_VENTE_BASE,ID_FOURNISSEUR,TYPE_PRECIS) values ('7','Dell','SDS-5554','850','900',null,'6');
Insert into SHOP.TYPE_APPAREILS (ID_TYPE_APPAREIL,MARQUE,LIBELLE,PRIX_ACHAT,PRIX_VENTE_BASE,ID_FOURNISSEUR,TYPE_PRECIS) values ('8','WD','Dique dur 1To','300','330',null,'7');
Insert into SHOP.TYPE_APPAREILS (ID_TYPE_APPAREIL,MARQUE,LIBELLE,PRIX_ACHAT,PRIX_VENTE_BASE,ID_FOURNISSEUR,TYPE_PRECIS) values ('19','Samsung','SAMS-5454','200','270',null,'6');
Insert into SHOP.TYPE_APPAREILS (ID_TYPE_APPAREIL,MARQUE,LIBELLE,PRIX_ACHAT,PRIX_VENTE_BASE,ID_FOURNISSEUR,TYPE_PRECIS) values ('20','Apple','iPod','150','170',null,'3');
REM INSERTING into SHOP.TYPE_PRECIS
SET DEFINE OFF;
Insert into SHOP.TYPE_PRECIS (ID_TYPE_PRECIS,TYPE) values ('1','Smartphone');
Insert into SHOP.TYPE_PRECIS (ID_TYPE_PRECIS,TYPE) values ('2','TV');
Insert into SHOP.TYPE_PRECIS (ID_TYPE_PRECIS,TYPE) values ('3','Accessoire');
Insert into SHOP.TYPE_PRECIS (ID_TYPE_PRECIS,TYPE) values ('4','Ordi portable');
Insert into SHOP.TYPE_PRECIS (ID_TYPE_PRECIS,TYPE) values ('5','Haut parleur');
Insert into SHOP.TYPE_PRECIS (ID_TYPE_PRECIS,TYPE) values ('6','Ordi bureau');
Insert into SHOP.TYPE_PRECIS (ID_TYPE_PRECIS,TYPE) values ('7','Disque dur');
Insert into SHOP.TYPE_PRECIS (ID_TYPE_PRECIS,TYPE) values ('8','Ecran ordinateur');
Insert into SHOP.TYPE_PRECIS (ID_TYPE_PRECIS,TYPE) values ('9','Radio');
Insert into SHOP.TYPE_PRECIS (ID_TYPE_PRECIS,TYPE) values ('10','Lave vaiselle');
Insert into SHOP.TYPE_PRECIS (ID_TYPE_PRECIS,TYPE) values ('11','Camera');
REM INSERTING into SHOP.VEHICULES
SET DEFINE OFF;
REM INSERTING into SHOP.VENTES_RATEES
SET DEFINE OFF;
REM INSERTING into SHOP.VENTES_REUSSIES
SET DEFINE OFF;
REM INSERTING into SHOP.VILLES
SET DEFINE OFF;
Insert into SHOP.VILLES (ID_VILLE,NOM) values ('1','Verviers');
Insert into SHOP.VILLES (ID_VILLE,NOM) values ('2','Liege');
Insert into SHOP.VILLES (ID_VILLE,NOM) values ('3','Namur');
Insert into SHOP.VILLES (ID_VILLE,NOM) values ('4','Charleroi');
Insert into SHOP.VILLES (ID_VILLE,NOM) values ('5','Bruxelles');
Insert into SHOP.VILLES (ID_VILLE,NOM) values ('6','Ostende');
Insert into SHOP.VILLES (ID_VILLE,NOM) values ('7','Provedroux');
Insert into SHOP.VILLES (ID_VILLE,NOM) values ('8','Louvain');
Insert into SHOP.VILLES (ID_VILLE,NOM) values ('9','Anderlecht');
Insert into SHOP.VILLES (ID_VILLE,NOM) values ('10','Seraing');
Insert into SHOP.VILLES (ID_VILLE,NOM) values ('11','Ennal');
Insert into SHOP.VILLES (ID_VILLE,NOM) values ('12','Heusy');
Insert into SHOP.VILLES (ID_VILLE,NOM) values ('13','Libramont');
--------------------------------------------------------
--  DDL for Index PK_VEHICULES
--------------------------------------------------------

  CREATE UNIQUE INDEX "SHOP"."PK_VEHICULES" ON "SHOP"."VEHICULES" ("MATRICULE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index PK_ETATS_SITUATION
--------------------------------------------------------

  CREATE UNIQUE INDEX "SHOP"."PK_ETATS_SITUATION" ON "SHOP"."ETATS_SITUATION" ("ID_SITUATION") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index PK_FACTURES
--------------------------------------------------------

  CREATE UNIQUE INDEX "SHOP"."PK_FACTURES" ON "SHOP"."FACTURES" ("ID_FACTURE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index PK_RESERVATION
--------------------------------------------------------

  CREATE UNIQUE INDEX "SHOP"."PK_RESERVATION" ON "SHOP"."RESERVATIONS" ("ID_CLIENT", "NUMERO_SERIE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index PK_TYPE_PRECIS
--------------------------------------------------------

  CREATE UNIQUE INDEX "SHOP"."PK_TYPE_PRECIS" ON "SHOP"."TYPE_PRECIS" ("ID_TYPE_PRECIS") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index PK_ITEMS_FACTURE
--------------------------------------------------------

  CREATE UNIQUE INDEX "SHOP"."PK_ITEMS_FACTURE" ON "SHOP"."ITEMS_FACTURE" ("ID_FACTURE", "NUMERO_SERIE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index PK_APPAREILS
--------------------------------------------------------

  CREATE UNIQUE INDEX "SHOP"."PK_APPAREILS" ON "SHOP"."APPAREILS" ("NUMERO_SERIE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index PK_CLIENTS
--------------------------------------------------------

  CREATE UNIQUE INDEX "SHOP"."PK_CLIENTS" ON "SHOP"."CLIENTS" ("ID_CLIENT") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index PK_VILLES
--------------------------------------------------------

  CREATE UNIQUE INDEX "SHOP"."PK_VILLES" ON "SHOP"."VILLES" ("ID_VILLE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index PK_PERSONNEL
--------------------------------------------------------

  CREATE UNIQUE INDEX "SHOP"."PK_PERSONNEL" ON "SHOP"."PERSONNEL" ("ID_PERSONNEL") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index PK_FOURNISSEURS
--------------------------------------------------------

  CREATE UNIQUE INDEX "SHOP"."PK_FOURNISSEURS" ON "SHOP"."FOURNISSEURS" ("ID_FOURNISSEUR") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index PK_SALAIRES
--------------------------------------------------------

  CREATE UNIQUE INDEX "SHOP"."PK_SALAIRES" ON "SHOP"."SALAIRES" ("ID_SALAIRE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index PK_VENTESRATEES
--------------------------------------------------------

  CREATE UNIQUE INDEX "SHOP"."PK_VENTESRATEES" ON "SHOP"."VENTES_RATEES" ("ID_VENTE_RATEE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index PK_TYPE_APPAREILS
--------------------------------------------------------

  CREATE UNIQUE INDEX "SHOP"."PK_TYPE_APPAREILS" ON "SHOP"."TYPE_APPAREILS" ("ID_TYPE_APPAREIL") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index PK_VENTESREUSSIES
--------------------------------------------------------

  CREATE UNIQUE INDEX "SHOP"."PK_VENTESREUSSIES" ON "SHOP"."VENTES_REUSSIES" ("ID_PERSONNEL", "ID_FACTURE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index CK_CLIENTS$LOGIN
--------------------------------------------------------

  CREATE UNIQUE INDEX "SHOP"."CK_CLIENTS$LOGIN" ON "SHOP"."CLIENTS" ("LOGIN") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Trigger CLIENTSID
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TRIGGER "SHOP"."CLIENTSID" 
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
ALTER TRIGGER "SHOP"."CLIENTSID" ENABLE;
--------------------------------------------------------
--  DDL for Trigger FACTUREID
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TRIGGER "SHOP"."FACTUREID" 
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
ALTER TRIGGER "SHOP"."FACTUREID" ENABLE;
--------------------------------------------------------
--  DDL for Procedure CLEAN_RESERVATIONS
--------------------------------------------------------
set define off;

  CREATE OR REPLACE EDITIONABLE PROCEDURE "SHOP"."CLEAN_RESERVATIONS" AS 
BEGIN
  DBMS_OUTPUT.PUT_LINE('Test Job');
END CLEAN_RESERVATIONS;

/
--------------------------------------------------------
--  DDL for Procedure RESERVATION
--------------------------------------------------------
set define off;

  CREATE OR REPLACE EDITIONABLE PROCEDURE "SHOP"."RESERVATION" (ic clients.id_client%type, ta type_appareils.id_type_appareil%type, quantite number) AS 
E_PARAM       EXCEPTION;
E_CLINOTFOUND EXCEPTION;
E_APPNOTFOUND EXCEPTION;
E_NOSTOCK     EXCEPTION;
E_NOTENOUGH   EXCEPTION;
appareilReserved  appareils%rowtype;
nbrStock Number;

BEGIN
  IF ic IS NULL OR ta IS NULL OR quantite IS NULL THEN 
    RAISE E_PARAM;
  END IF;
  
  SELECT count(*)
  INTO nbrStock
  from appareils ap
  RIGHT JOIN (select * from etats_situation where description IN ('WA', 'SA')) et 
    ON ap.etat_situation = et.id_situation
  where ap.type_appareil = ta;
  
  if nbrStock = 0 THEN 
    RAISE E_NOSTOCK;
  END IF;
  
  if nbrStock < quantite THEN
    RAISE E_NOTENOUGH;
  END IF;
  
  FOR i IN 1 .. quantite LOOP
    NULL;
    BEGIN
      select ap.*
      into appareilReserved
      from appareils ap
      INNER JOIN ETATS_SITUATION et ON ap.etat_situation = et.id_situation
      where et.description IN ('WA', 'SA')
      and type_appareil = ta  
      and  ROWNUM = 1
      FOR UPDATE;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        RAISE E_APPNOTFOUND;
    END;
    
    INSERT INTO reservations (id_client, numero_serie, date_reservation) 
    values (ic, appareilReserved.numero_serie, sysdate);
    
    UPDATE appareils 
    set etat_situation = 
      (select id_situation 
       from etats_situation 
       where Description = 'Réservé')
    where numero_serie = appareilReserved.numero_serie;
  
    
  END LOOP;
  commit;

  
EXCEPTION
  WHEN E_APPNOTFOUND THEN
    rollback;
    RAISE_APPLICATION_ERROR(-20103, 'Un appareil de ce type n''est disponible dans le stock.');
    
  WHEN E_PARAM THEN
    rollback;
    RAISE_APPLICATION_ERROR(-20102, 'Les paramètres ne sont pas bons recommence...');
    
  WHEN E_NOTENOUGH THEN
    RAISE_APPLICATION_ERROR(-20103, 'Il n''y a plus assez d''éléments dans le stock. Il en reste ' || to_char(nbrStock));
  
  WHEN E_NOSTOCK THEN
    RAISE_APPLICATION_ERROR(-20104, 'Il n''y a plus disponible dans le stock.');
    
  WHEN OTHERS THEN rollback; RAISE;
  
END RESERVATION;

/
--------------------------------------------------------
--  DDL for Procedure RESET_SEQ
--------------------------------------------------------
set define off;

  CREATE OR REPLACE EDITIONABLE PROCEDURE "SHOP"."RESET_SEQ" ( p_seq_name in varchar2 )
is
    l_val number;
begin
    execute immediate
    'select ' || p_seq_name || '.nextval from dual' INTO l_val;

    execute immediate
    'alter sequence ' || p_seq_name || ' increment by -' || l_val || 
                                                          ' minvalue 0';

    execute immediate
    'select ' || p_seq_name || '.nextval from dual' INTO l_val;

    execute immediate
    'alter sequence ' || p_seq_name || ' increment by 1 minvalue 0';
end;

/
--------------------------------------------------------
--  DDL for Procedure SEED
--------------------------------------------------------
set define off;

  CREATE OR REPLACE EDITIONABLE PROCEDURE "SHOP"."SEED" AS 
Factu factures%rowtype;
Appa appareils%rowtype;
item_fac items_facture%rowtype;
type_app type_appareils%rowtype;
cli   clients%rowtype;
tmpint  number;
vend    VENTES_REUSSIES%rowtype;


BEGIN

  for i in 1..500 LOOP
    cli.prenom := 'Jean';
    cli.nom :='Nexistepas';
    
    cli.login := DBMS_RANDOM.string('L',TRUNC(DBMS_RANDOM.value(10,21)));
    cli.password := DBMS_RANDOM.string('L',TRUNC(DBMS_RANDOM.value(10,21)));
    
    insert into clients values cli;  
  END LOOP;


  /* Crée des factures terminées */
  FOR i IN 1..35000 LOOP
    /* Crée la facture */
    --factu.id_facture := i;
    factu.id_facture := null;
    factu.id_client :=  FLOOR(DBMS_RANDOM.value(low => 1, high => 500));
    factu.date_facturation := TO_DATE(TRUNC(DBMS_RANDOM.VALUE(TO_CHAR(DATE '2016-11-03','J'),TO_CHAR(DATE '1996-10-31','J'))),'J');
    factu.adresse_livraison := 'Adresse générée';
    factu.id_ville := FLOOR(DBMS_RANDOM.value(low => 1, high => 13));
    factu.mode_paiement := 'LIQUIDE';
    tmpint := FLOOR(DBMS_RANDOM.value(low => 1, high => 6));
    IF tmpint = 1 THEN
      factu.type_achat := 'WEB';
    ELSE
      factu.type_achat := 'SA';
    END IF;
    
    INSERT INTO Factures values Factu returning id_facture, id_client, date_facturation, type_achat, '', '', 0 into Factu;
    
    IF factu.type_achat = 'SA' THEN
      
      /* Choisis un vendeur au hazard et lui attribue la vente */
      vend.id_personnel := FLOOR(DBMS_RANDOM.value(low => 1, high => 11));
      vend.id_facture := factu.id_facture;
      vend.temps := FLOOR(DBMS_RANDOM.value(low => 5, high => 60));
      vend.argument := 'Vente générée';
      
      INSERT INTO VENTES_REUSSIES values vend;
    END IF;
    
    
    
    /* Créer les appareils de la facture */
    tmpint := FLOOR(DBMS_RANDOM.value(low => 1, high => 10));
    FOR j IN 0..tmpint LOOP
      Appa.numero_serie := dbms_random.string('x', 10);
      Appa.TYPE_general := 'APE';
      Appa.type_appareil := FLOOR(DBMS_RANDOM.value(low => 1, high => 21));
      select * into type_app from type_appareils where ID_TYPE_APPAREIL = appa.type_appareil;
      appa.prix_vente_effectif := type_app.prix_vente_base;
      Appa.pos_x := null;
      Appa.pos_y := null;
      IF factu.type_achat = 'WEB' THEN
        appa.etat_situation := 5;
      ELSE 
        appa.etat_situation := 6;
      END IF;
      appa.etat_paiement := 'Payé';
      appa.type_achat := factu.type_achat;
      
      INSERT INTO Appareils values appa;
      
      item_fac.id_facture := factu.id_facture;
      item_fac.numero_serie := appa.numero_serie;
      item_fac.prix := appa.prix_vente_effectif;
      
      insert into items_facture values item_fac;
    
    
    END LOOP;
    
    
    
    
  END LOOP;
  
  /* Crée du stock */
  FOR i IN 1..200 LOOP
      Appa.numero_serie := dbms_random.string('x', 10);
      Appa.TYPE_general := 'APE';
      Appa.type_appareil := FLOOR(DBMS_RANDOM.value(low => 1, high => 21));
      select * into type_app from type_appareils where ID_TYPE_APPAREIL = appa.type_appareil;
      appa.prix_vente_effectif := type_app.prix_vente_base;
      
      -- 1 sur 4 ira dans la SA le reste dans WA
      tmpint := FLOOR(DBMS_RANDOM.value(low => 1, high => 4));
      IF tmpint = 1 THEN
        --SA
        appa.etat_situation := 2;
        Appa.pos_x := null;
        Appa.pos_y := null;
      ELSE
        --WA
        appa.etat_situation := 1;
        Appa.pos_x := FLOOR(DBMS_RANDOM.value(low => 1, high => 50));
        Appa.pos_y := FLOOR(DBMS_RANDOM.value(low => 1, high => 50));
      END IF;
      
      factu.type_achat := null;  
      appa.etat_paiement := null;
      appa.type_achat := null;
    
    INSERT INTO Appareils values appa;
    

  END LOOP;

END SEED;

/
--------------------------------------------------------
--  DDL for Function FACTURATION
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE FUNCTION "SHOP"."FACTURATION" (ic clients.id_client%type, typeach factures.type_achat%type) 
RETURN factures.id_facture%type AS 
  factureGenerated factures%rowtype;
  testr reservations%rowtype;
  e_noreser EXCEPTION;
BEGIN
  
  BEGIN
    SELECT * into testr from reservations where ID_CLIENT = ic AND ROWNUM = 1;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RAISE E_NORESER;
  END;
  
  insert into factures (id_client, date_facturation, type_achat) 
  values (ic, sysdate, typeach)
  return id_facture, id_client, date_facturation, type_achat, '', '', 0 INTO factureGenerated;
  
  
  insert into items_facture (id_facture, numero_serie, prix) 
  select factureGenerated.id_facture, re.numero_serie, ap.prix_vente_effectif
  from reservations re
  inner join appareils ap on re.NUMERO_SERIE = ap.NUMERO_SERIE 
  where re.ID_CLIENT = ic;
  
  UPDATE appareils ap 
  set etat_situation = (select et.id_situation 
                        from etats_situation et
                        where et.Description = '%'|| UPPER(typeach) ||'%') 
  where ap.numero_serie IN (select it.numero_serie 
                            from items_facture it 
                            where it.id_facture = factureGenerated.id_facture);

  delete from reservations where id_client = ic;
  commit;
  return factureGenerated.id_facture;
EXCEPTION
  WHEN E_NORESER THEN
      rollback;
      RAISE_APPLICATION_ERROR(-20104, 'Le client n''a aucune réservation enregistrée.');
  WHEN OTHERS THEN rollback; RAISE;

END FACTURATION;

/
--------------------------------------------------------
--  Constraints for Table ITEMS_FACTURE
--------------------------------------------------------

  ALTER TABLE "SHOP"."ITEMS_FACTURE" ADD CONSTRAINT "PK_ITEMS_FACTURE" PRIMARY KEY ("ID_FACTURE", "NUMERO_SERIE")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Constraints for Table RESERVATIONS
--------------------------------------------------------

  ALTER TABLE "SHOP"."RESERVATIONS" ADD CONSTRAINT "PK_RESERVATION" PRIMARY KEY ("ID_CLIENT", "NUMERO_SERIE")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Constraints for Table VENTES_REUSSIES
--------------------------------------------------------

  ALTER TABLE "SHOP"."VENTES_REUSSIES" ADD CONSTRAINT "PK_VENTESREUSSIES" PRIMARY KEY ("ID_PERSONNEL", "ID_FACTURE")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Constraints for Table VILLES
--------------------------------------------------------

  ALTER TABLE "SHOP"."VILLES" ADD CONSTRAINT "PK_VILLES" PRIMARY KEY ("ID_VILLE")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Constraints for Table VEHICULES
--------------------------------------------------------

  ALTER TABLE "SHOP"."VEHICULES" ADD CONSTRAINT "PK_VEHICULES" PRIMARY KEY ("MATRICULE")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Constraints for Table FACTURES
--------------------------------------------------------

  ALTER TABLE "SHOP"."FACTURES" ADD CONSTRAINT "CK_FACTURES$TYPE_ACHAT" CHECK (TYPE_ACHAT IN ('SA', 'WEB')) DISABLE;
  ALTER TABLE "SHOP"."FACTURES" ADD CONSTRAINT "PK_FACTURES" PRIMARY KEY ("ID_FACTURE")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
  ALTER TABLE "SHOP"."FACTURES" ADD CONSTRAINT "CK_FACTURES$MODEPAIEMENT" CHECK (MODE_PAIEMENT IN('CHEQUE', 'LIQUIDE', 'VIREMENT', 'BANCONTACT')) ENABLE;
--------------------------------------------------------
--  Constraints for Table CLIENTS
--------------------------------------------------------

  ALTER TABLE "SHOP"."CLIENTS" ADD CONSTRAINT "PK_CLIENTS" PRIMARY KEY ("ID_CLIENT")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
  ALTER TABLE "SHOP"."CLIENTS" ADD CONSTRAINT "CK_CLIENTS$LOGIN" UNIQUE ("LOGIN")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Constraints for Table APPAREILS
--------------------------------------------------------

  ALTER TABLE "SHOP"."APPAREILS" ADD CONSTRAINT "CK_APPAREILS$TYPE_GENERAL" CHECK (TYPE_GENERAL IN('APE', 'AFE', 'AAV', 'OTHER')) ENABLE;
  ALTER TABLE "SHOP"."APPAREILS" ADD CONSTRAINT "CK_APPAREILS$TYPE_ACHAT" CHECK (TYPE_ACHAT IN ('SA', 'WEB')) ENABLE;
  ALTER TABLE "SHOP"."APPAREILS" ADD CONSTRAINT "CK_APPAREILS$TYPE_APPAREILS" CHECK (TYPE_APPAREIL IS NOT NULL) ENABLE;
  ALTER TABLE "SHOP"."APPAREILS" ADD CONSTRAINT "PK_APPAREILS" PRIMARY KEY ("NUMERO_SERIE")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Constraints for Table PERSONNEL
--------------------------------------------------------

  ALTER TABLE "SHOP"."PERSONNEL" ADD CONSTRAINT "PK_PERSONNEL" PRIMARY KEY ("ID_PERSONNEL")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Constraints for Table TYPE_APPAREILS
--------------------------------------------------------

  ALTER TABLE "SHOP"."TYPE_APPAREILS" ADD CONSTRAINT "PK_TYPE_APPAREILS" PRIMARY KEY ("ID_TYPE_APPAREIL")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Constraints for Table TYPE_PRECIS
--------------------------------------------------------

  ALTER TABLE "SHOP"."TYPE_PRECIS" ADD CONSTRAINT "PK_TYPE_PRECIS" PRIMARY KEY ("ID_TYPE_PRECIS")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Constraints for Table SALAIRES
--------------------------------------------------------

  ALTER TABLE "SHOP"."SALAIRES" ADD CONSTRAINT "PK_SALAIRES" PRIMARY KEY ("ID_SALAIRE")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Constraints for Table FOURNISSEURS
--------------------------------------------------------

  ALTER TABLE "SHOP"."FOURNISSEURS" ADD CONSTRAINT "PK_FOURNISSEURS" PRIMARY KEY ("ID_FOURNISSEUR")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Constraints for Table VENTES_RATEES
--------------------------------------------------------

  ALTER TABLE "SHOP"."VENTES_RATEES" ADD CONSTRAINT "PK_VENTESRATEES" PRIMARY KEY ("ID_VENTE_RATEE")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Constraints for Table ETATS_SITUATION
--------------------------------------------------------

  ALTER TABLE "SHOP"."ETATS_SITUATION" ADD CONSTRAINT "PK_ETATS_SITUATION" PRIMARY KEY ("ID_SITUATION")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table APPAREILS
--------------------------------------------------------

  ALTER TABLE "SHOP"."APPAREILS" ADD CONSTRAINT "FK_APPAREILS$ETATSITUATION" FOREIGN KEY ("ETAT_SITUATION")
	  REFERENCES "SHOP"."ETATS_SITUATION" ("ID_SITUATION") ENABLE;
  ALTER TABLE "SHOP"."APPAREILS" ADD CONSTRAINT "FK_APPAREILS$TYPE_APPAREIL" FOREIGN KEY ("TYPE_APPAREIL")
	  REFERENCES "SHOP"."TYPE_APPAREILS" ("ID_TYPE_APPAREIL") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table FACTURES
--------------------------------------------------------

  ALTER TABLE "SHOP"."FACTURES" ADD CONSTRAINT "FK_FACTURES$CLIENT" FOREIGN KEY ("ID_CLIENT")
	  REFERENCES "SHOP"."CLIENTS" ("ID_CLIENT") ENABLE;
  ALTER TABLE "SHOP"."FACTURES" ADD CONSTRAINT "FK_FACTURES$VILLE" FOREIGN KEY ("ID_VILLE")
	  REFERENCES "SHOP"."VILLES" ("ID_VILLE") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ITEMS_FACTURE
--------------------------------------------------------

  ALTER TABLE "SHOP"."ITEMS_FACTURE" ADD CONSTRAINT "FK_ITEMS_FACTURES$FACTURE" FOREIGN KEY ("ID_FACTURE")
	  REFERENCES "SHOP"."FACTURES" ("ID_FACTURE") ENABLE;
  ALTER TABLE "SHOP"."ITEMS_FACTURE" ADD CONSTRAINT "FK_ITEMS_FACTURES$NUMERO_SERIE" FOREIGN KEY ("NUMERO_SERIE")
	  REFERENCES "SHOP"."APPAREILS" ("NUMERO_SERIE") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table RESERVATIONS
--------------------------------------------------------

  ALTER TABLE "SHOP"."RESERVATIONS" ADD CONSTRAINT "FK_RESERVATION$CLIENT" FOREIGN KEY ("ID_CLIENT")
	  REFERENCES "SHOP"."CLIENTS" ("ID_CLIENT") ENABLE;
  ALTER TABLE "SHOP"."RESERVATIONS" ADD CONSTRAINT "FK_RESERVATION$NUMERO_SERIE" FOREIGN KEY ("NUMERO_SERIE")
	  REFERENCES "SHOP"."APPAREILS" ("NUMERO_SERIE") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table SALAIRES
--------------------------------------------------------

  ALTER TABLE "SHOP"."SALAIRES" ADD CONSTRAINT "FK_SALAIRES$PERSONNEL" FOREIGN KEY ("ID_PERSONNEL")
	  REFERENCES "SHOP"."PERSONNEL" ("ID_PERSONNEL") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TYPE_APPAREILS
--------------------------------------------------------

  ALTER TABLE "SHOP"."TYPE_APPAREILS" ADD CONSTRAINT "FK_APPAREILS$TYPE_PRECISS" FOREIGN KEY ("TYPE_PRECIS")
	  REFERENCES "SHOP"."TYPE_PRECIS" ("ID_TYPE_PRECIS") ENABLE;
  ALTER TABLE "SHOP"."TYPE_APPAREILS" ADD CONSTRAINT "FK_TYPE_APP$ID_FOURNISSEUR" FOREIGN KEY ("ID_FOURNISSEUR")
	  REFERENCES "SHOP"."FOURNISSEURS" ("ID_FOURNISSEUR") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table VENTES_REUSSIES
--------------------------------------------------------

  ALTER TABLE "SHOP"."VENTES_REUSSIES" ADD CONSTRAINT "FK_VENTESREUSSIES$FACTURE" FOREIGN KEY ("ID_FACTURE")
	  REFERENCES "SHOP"."FACTURES" ("ID_FACTURE") ENABLE;
  ALTER TABLE "SHOP"."VENTES_REUSSIES" ADD CONSTRAINT "FK_VENTESREUSSIES$PERSONNEL" FOREIGN KEY ("ID_PERSONNEL")
	  REFERENCES "SHOP"."PERSONNEL" ("ID_PERSONNEL") ENABLE;
