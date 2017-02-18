/* TypeGeneral */
#define TG_APE 1
#define TG_APEAGE 2
#define TG_APEAAV 3
#define TG_APEOTHER 4 
/* End TypeGeneral */

/* EtatPaiement */
#define ET_DISPONIBLE 1
#define ET_RESERVEPAYE 2
#define ET_PAYECASH 3
#define ET_PAYECARTE 4
#define ET_PAYEVIREMENT 5
/* End EtatPaiement */

/* TypeAchat */
#define TA_SA 1
#define TA_INTERNET 2
/* End TypeAchat */

struct _Appareils{
	string NumSerie;
	int TypeGeneral;
	string TypePrecis;
	float PrixVente;
	string EtatSituation;
	int EtatPaiement;
	int TypeAchat;
};
typedef struct _Appareils Appareils;


struct _TypeAppareil{
	string TypePrecis;
	string Marque;
	string Libelle;
	float prixAchat;
	float prixVente;
	int IdFournisseur;
};
typedef struct _TypeAppareil TypeAppareil;

struct _Fournisseurs{
	int id;
	string nom;
}
typedef struct _Fournisseurs Fournisseurs;

