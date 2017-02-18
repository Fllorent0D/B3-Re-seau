create table factures_fournisseurs
(
  id_facture INTEGER,
  montant FLOAT,
  validee INTEGER,
  payee INTEGER,
  date_facturation DATE,
  id_fournisseur INTEGER,
  constraint fact_fourn$pk primary key (id_facture),
  constraint fact_fourn$fk$fournisseurs foreign key (id_fournisseur) references fournisseurs(id_fournisseur)
);


/* AI status */
CREATE SEQUENCE sequence_factures_fournisseurs INCREMENT BY 1 MAXVALUE 99999 NOCYCLE;

CREATE OR REPLACE TRIGGER factures_fournisseurs_id
  BEFORE INSERT ON factures_fournisseurs
  FOR EACH ROW
DECLARE
BEGIN
  IF(:new.id_facture IS NULL)
  THEN
    :new.id_facture := sequence_factures_fournisseurs.nextval;
  END IF;
END;
/

commit;