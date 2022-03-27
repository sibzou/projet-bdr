CREATE TABLE Compte (NumCompte NUMBER(3) PRIMARY KEY,
                     NomClient VARCHAR2(15) NOT NULL,
                     DateOuverture DATE NOT NULL,
                     Solde DECIMAL(7, 2) NOT NULL,
                     PMVR DECIMAL(7, 2) DEFAULT 0 NOT NULL);

CREATE TABLE Secteur (CodeSE CHAR(4) PRIMARY KEY,
                      SecteurEconomique VARCHAR2(15) NOT NULL);

CREATE TABLE Valeur (CodeValeur VARCHAR2(8) PRIMARY KEY,
                     Denomination VARCHAR2(10) NOT NULL,
                     CodeSE CHAR(4) REFERENCES Secteur(CodeSE),
                     Indice CHAR(8) NOT NULL,
                     Cours DECIMAL(4, 2) NOT NULL);

CREATE TABLE Operation (NumOp NUMBER(2) PRIMARY KEY,
                        NumCompte NUMBER(3) REFERENCES Compte(NumCompte),
                        CodeValeur VARCHAR2(8) REFERENCES Valeur(CodeValeur),
                        DateOp DATE NOT NULL,
                        Nature CHAR(1) NOT NULL,
                        QteOp NUMBER(3) NOT NULL,
                        Montant DECIMAL(7, 2) NOT NULL);

CREATE TABLE Portefeuille (NumCompte NUMBER(3) REFERENCES Compte(NumCompte),
                           CodeValeur VARCHAR2(8) REFERENCES Valeur(CodeValeur),
                           Quantite NUMBER(3) NOT NULL,
                           PAM DECIMAL(5, 2) NOT NULL,
                           PMVL DECIMAL(5, 2) NOT NULL,
                           PRIMARY KEY(NumCompte, CodeValeur));

-- Création de la procédure AjouterService
CREATE OR REPLACE PROCEDURE OuvrirCompte(Nom IN VARCHAR2, Montant IN NUMBER) 
IS
BEGIN
END;

-- Met à jour le PMVL quand le cours d'une valeur change
CREATE TRIGGER valeur_pmvl BEFORE UPDATE OF Cours ON Valeur FOR EACH ROW
BEGIN
    UPDATE Portefeuille SET PMVL = (:NEW.Cours - PAM) * Quantite WHERE CodeValeur = :NEW.CodeValeur;
END;
/

-- Met à jour le PMVL quand le PAM ou la quantité change
CREATE TRIGGER portefeuille_pmvl BEFORE INSERT OR UPDATE OF PAM, Quantite ON Portefeuille FOR EACH ROW
DECLARE
    valeur_cours DECIMAL(4, 2);
BEGIN
    SELECT Cours into valeur_cours FROM Valeur WHERE CodeValeur = :NEW.CodeValeur;
    :NEW.PMVL := (valeur_cours - :NEW.PAM) * :NEW.Quantite;
END;
/

-- Création de la procédure MAJValeur
CREATE PROCEDURE MAJValeur(CodeValeurIn IN VARCHAR2, CoursIn IN NUMBER) IS
    code_count NUMBER;
    cours_incorrect EXCEPTION;
    code_inconnu EXCEPTION;
BEGIN
    IF CoursIn < 0
    THEN
        RAISE cours_incorrect;
    END IF;

    SELECT COUNT(*) INTO code_count FROM Valeur WHERE CodeValeur = CodeValeurIn;

    IF code_count = 0
    THEN
        RAISE code_inconnu;
    END IF;

    UPDATE Valeur SET Cours = CoursIn WHERE CodeValeur = CodeValeurIn;
END;

-- Création de la procédure Acheter
CREATE OR REPLACE PROCEDURE Acheter((NumCpte IN NUMBER, Code IN VARCHAR2, DateA IN DATE, Quant IN NUMBER,
MA IN NUMBER) 
IS
BEGIN
END;

-- Création de la procédure Vendre
CREATE OR REPLACE PROCEDURE Vendre(NumCpte IN NUMBER, Code IN VARCHAR2, DateV IN DATE, Quant IN NUMBER,
MV IN NUMBER) 
IS
BEGIN
END;

-- Création de la procédure RepartitionPortefeuille
CREATE OR REPLACE PROCEDURE RepartitionPortefeuille(NumCpte IN NUMBER, Critere IN CHAR) 
IS
BEGIN
END;

-- Création de la fonctionnalité TotalPortefeuille
CREATE FUNCTION TotalPortefeuille((Nom IN VARCHAR2)
RETURNS NUMBER
    [ WITH <function_option> [ ,...n ] ]
    [ AS ]
    BEGIN
        function_body
        RETURN scalar_expression
    END
[ ; ]

DROP PROCEDURE MAJValeur;
DROP TRIGGER valeur_pmvl;
DROP TRIGGER portefeuille_pmvl;
DROP TABLE Portefeuille;
DROP TABLE Operation;
DROP TABLE Valeur;
DROP TABLE Secteur;
DROP TABLE Compte;
