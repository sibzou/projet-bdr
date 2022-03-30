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
-- CREATE OR REPLACE PROCEDURE OuvrirCompte(Nom IN VARCHAR2, Montant IN NUMBER)
-- IS
-- BEGIN
-- END;

-- Met à jour le PMVL quand le cours d'une valeur change
CREATE TRIGGER valeur_pmvl BEFORE UPDATE OF Cours ON Valeur FOR EACH ROW
BEGIN
    UPDATE Portefeuille SET PMVL = (:NEW.Cours - PAM) * Quantite WHERE CodeValeur = :NEW.CodeValeur;
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

EXCEPTION
    WHEN cours_incorrect THEN
        DBMS_OUTPUT.PUT_LINE('Cours invalide');
    WHEN code_inconnu THEN
        DBMS_OUTPUT.PUT_LINE('Code valeur inconnu');
END;
/

-- Création de la procédure Acheter
-- CREATE OR REPLACE PROCEDURE Acheter((NumCpte IN NUMBER, Code IN VARCHAR2, DateA IN DATE, Quant IN NUMBER,
-- MA IN NUMBER)
-- IS
-- BEGIN
-- END;

CREATE SEQUENCE numop_seq;

CREATE TRIGGER solde_trig BEFORE INSERT ON Operation FOR EACH ROW
DECLARE
    compte_pam DECIMAL(5, 2);
BEGIN
    IF :NEW.Nature = 'V'
    THEN
        SELECT PAM INTO compte_pam FROM Portefeuille WHERE NumCompte = :NEW.NumCompte AND CodeValeur = :NEW.CodeValeur;
        UPDATE Compte SET Solde = Solde + :NEW.Montant, PMVR = PMVR + (:NEW.Montant - :NEW.QteOp * compte_pam) WHERE NumCompte = :NEW.NumCompte;
    END IF;
END;
/

-- Création de la procédure Vendre
CREATE PROCEDURE Vendre(NumCpte IN NUMBER, Code IN VARCHAR2, DateV IN DATE, Quant IN NUMBER, MV IN NUMBER) IS
    quant_incorrect EXCEPTION;
    mv_incorrect EXCEPTION;
    non_possede EXCEPTION;
    portefeuille_qte NUMBER;
    cours_valeur DECIMAL(4, 2);
BEGIN
    IF Quant <= 0
    THEN
        RAISE quant_incorrect;
    END IF;

    IF MV <= 0
    THEN
        RAISE mv_incorrect;
    END IF;

    SELECT SUM(Quantite) INTO portefeuille_qte FROM Portefeuille WHERE NumCompte = NumCpte AND CodeValeur = Code;

    IF portefeuille_qte IS NULL OR portefeuille_qte < Quant
    THEN
        RAISE non_possede;
    END IF;

    INSERT INTO Operation VALUES(numop_seq.NEXTVAL, NumCpte, Code, DateV, 'V', Quant, MV);

    IF portefeuille_qte = Quant
    THEN
        DELETE FROM Portefeuille WHERE NumCompte = NumCpte AND CodeValeur = Code;
    ELSE
        UPDATE Portefeuille SET Quantite = Quantite - Quant WHERE NumCompte = NumCpte AND CodeValeur = Code;

        SELECT Cours INTO cours_valeur FROM Valeur WHERE CodeValeur = Code;
        UPDATE Portefeuille SET PMVL = (cours_valeur - PAM) * Quantite WHERE NumCompte = NumCpte AND CodeValeur = Code;
    END IF;

EXCEPTION
    WHEN quant_incorrect THEN
        DBMS_OUTPUT.PUT_LINE('Quantité invalide');
    WHEN mv_incorrect THEN
        DBMS_OUTPUT.PUT_LINE('Montant invalide');
    WHEN non_possede THEN
        DBMS_OUTPUT.PUT_LINE('Quantité possédée insuffisante');
END;
/

-- Création de la procédure RepartitionPortefeuille
-- CREATE OR REPLACE PROCEDURE RepartitionPortefeuille(NumCpte IN NUMBER, Critere IN CHAR)
-- IS
-- BEGIN
-- END;

-- Création de la fonctionnalité TotalPortefeuille
CREATE FUNCTION TotalPortefeuille(Nom IN VARCHAR2) RETURN NUMBER IS
    client_count NUMBER;
    total NUMBER;
BEGIN
    SELECT COUNT(*) INTO client_count FROM Compte WHERE NomClient = Nom;

    IF client_count = 0
    THEN
        RETURN -1;
    END IF;

    SELECT SUM(Quantite * PAM) INTO total FROM Portefeuille, Compte
        WHERE Portefeuille.NumCompte = Compte.NumCompte AND NomClient = Nom;

    RETURN total;
END;
/

DROP FUNCTION TotalPortefeuille;
DROP PROCEDURE Vendre;
DROP TRIGGER solde_trig;
DROP SEQUENCE numop_seq;
DROP PROCEDURE MAJValeur;
DROP TRIGGER valeur_pmvl;
DROP TABLE Portefeuille;
DROP TABLE Operation;
DROP TABLE Valeur;
DROP TABLE Secteur;
DROP TABLE Compte;
