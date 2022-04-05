

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

CREATE SEQUENCE seqCompte START WITH 101 INCREMENT BY 1;

-- Création de la procédure OuvrirCompte
CREATE OR REPLACE PROCEDURE OuvrirCompte(Nom IN VARCHAR2, Montant IN NUMBER)
IS
BEGIN
  IF Montant <= 0
  THEN
    RAISE_APPLICATION_ERROR (-20002, 'le montant doit être supérieur à 0');
  END IF;
  IF Nom is NULL
  THEN
    RAISE_APPLICATION_ERROR (-20002, 'le nom ne doit pas être null');
  END IF;
  IF LTRIM(Nom) is NULL
  THEN
    RAISE_APPLICATION_ERROR (-20002, 'le nom ne doit pas être vide');
  END IF;

  INSERT INTO Compte (NumCompte, NomClient, DateOuverture, Solde, PMVR)
  VALUES (seqCompte.NEXTVAL, Nom, TRUNC(SYSDATE), Montant, 0);
END;
/

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

CREATE SEQUENCE numop_seq;

-- Création de la procédure Acheter
CREATE OR REPLACE PROCEDURE Acheter(NumCpte IN NUMBER, Code IN VARCHAR2, DateA IN DATE, Quant IN NUMBER, MA IN NUMBER)
IS
v_solde Compte.Solde%TYPE;
v_codevaleur Valeur.CodeValeur%TYPE;
valeur_cours Valeur.Cours%TYPE;
nbPortefeuille NUMBER;
sum_op NUMBER;
count_op NUMBER;
BEGIN
    BEGIN
    SELECT Solde INTO v_solde FROM Compte WHERE NumCompte = NumCpte;
    EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('Erreur : Compte inexistant');
    END;
    BEGIN
    SELECT CodeValeur INTO v_codevaleur FROM Valeur WHERE CodeValeur = Code;
    EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('Erreur : Valeur inexistante');
    END;
    IF  Quant <= 0
    THEN
        RAISE_APPLICATION_ERROR (-20002, 'le quantité doit être supérieur à 0');
    END IF;
    IF MA <= 0 OR MA > v_solde
    THEN
        RAISE_APPLICATION_ERROR (-20002, 'le montant doit être supérieur à 0 et doit être inférieur ou égal au solde du compte');
    END IF;
    SELECT COUNT (*) INTO nbPortefeuille FROM Portefeuille WHERE CodeValeur = Code AND NumCompte = NumCpte;
    SELECT Cours INTO valeur_cours FROM Valeur WHERE CodeValeur = Code;
    IF nbPortefeuille = 0
    THEN
        INSERT INTO Portefeuille VALUES(NumCpte,Code,Quant,valeur_cours,0);
    ELSE
        SELECT SUM(Montant/QteOp) INTO sum_op FROM Operation WHERE NumCompte = NumCpte AND CodeValeur = Code AND Nature='A';
        SELECT COUNT(*) INTO count_op FROM Operation WHERE NumCompte = NumCpte AND CodeValeur = Code AND Nature='A';
        UPDATE Portefeuille SET Quantite = Quantite + Quant, PAM = (sum_op+valeur_cours)/(count_op+1), PMVL = (valeur_cours-PAM)*Quantite;
    END IF;
    INSERT INTO Operation VALUES(numop_seq.NEXTVAL,NumCpte,Code,DateA,'A',Quant,MA);
END;
/

CREATE TRIGGER solde_trig BEFORE INSERT ON Operation FOR EACH ROW
DECLARE
    compte_pam DECIMAL(5, 2);
BEGIN
    IF :NEW.Nature = 'V'
    THEN
        SELECT PAM INTO compte_pam FROM Portefeuille WHERE NumCompte = :NEW.NumCompte AND CodeValeur = :NEW.CodeValeur;
        UPDATE Compte SET Solde = Solde + :NEW.Montant, PMVR = PMVR + (:NEW.Montant - :NEW.QteOp * compte_pam) WHERE NumCompte = :NEW.NumCompte;
    ELSIF :NEW.Nature = 'A'
    THEN
        UPDATE Compte SET Solde = Solde - :NEW.Montant WHERE NumCompte = :NEW.NumCompte;
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
CREATE OR REPLACE PROCEDURE RepartitionPortefeuille (NumCpte in number, Critere in char)
IS
v_numcompte Portefeuille.NumCompte%TYPE;
v_secteur Secteur.SecteurEconomique%TYPE;
v_codevaleur Valeur.CodeValeur%TYPE;
v_indice Valeur.Indice%TYPE;
v_repartition number(10,2);
BEGIN
  BEGIN
  SELECT NumCompte INTO v_numcompte FROM Compte WHERE NumCompte = NumCpte;
  EXCEPTION
  WHEN NO_DATA_FOUND THEN
    DBMS_OUTPUT.PUT_LINE('Erreur : Compte inexistant');
  END;
  IF (Critere = 'se') THEN
    FOR rec IN (
    SELECT SecteurEconomique, ROUND(SUM(Quantite*PAM)/(SELECT SUM(Quantite*PAM) FROM Portefeuille WHERE NumCompte = v_numcompte)*100,2) as repartition INTO v_secteur, v_repartition
    FROM Portefeuille, Valeur, Secteur
    WHERE Portefeuille.CodeValeur = Valeur.CodeValeur
    AND Valeur.CodeSE = Secteur.CodeSE
    AND Portefeuille.NumCompte = v_numcompte
    GROUP BY SecteurEconomique
    ORDER BY repartition DESC)
    LOOP
      DBMS_OUTPUT.PUT_LINE(rec.SecteurEconomique || ' : ' || rec.repartition || '%');
    END LOOP;
  ELSE
    IF (Critere = 'ib') THEN
      FOR rec IN (
      SELECT Indice, ROUND(SUM(Quantite*PAM)/(SELECT SUM(Quantite*PAM) FROM Portefeuille WHERE NumCompte = v_numcompte)*100,2) as repartition INTO v_indice, v_repartition
      FROM Portefeuille, Valeur
      WHERE Portefeuille.CodeValeur = Valeur.CodeValeur
      AND Portefeuille.NumCompte = v_numcompte
      GROUP BY Indice
      ORDER BY repartition DESC)
      LOOP
        DBMS_OUTPUT.PUT_LINE(rec.Indice || ' : ' || rec.repartition || '%');
      END LOOP;
    ELSE
      IF (Critere IS NULL) THEN
        FOR rec IN (
        SELECT SecteurEconomique,Indice, ROUND(SUM(Quantite*PAM)/(SELECT SUM(Quantite*PAM) FROM Portefeuille WHERE NumCompte = v_numcompte)*100,2) as repartition INTO v_secteur, v_indice, v_repartition
        FROM Portefeuille, Valeur, Secteur
        WHERE Portefeuille.CodeValeur = Valeur.CodeValeur
        AND Valeur.CodeSE = Secteur.CodeSE
        AND Portefeuille.NumCompte = v_numcompte
        GROUP BY SecteurEconomique,Indice
        ORDER BY repartition DESC)
        LOOP
          DBMS_OUTPUT.PUT_LINE(rec.SecteurEconomique || ' : ' || rec.Indice || ' : ' || rec.repartition || '%');
        END LOOP;
      ELSE
        RAISE_APPLICATION_ERROR(-20001, 'Critère non reconnu');
      END IF;
    END IF;
  END IF;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      DBMS_OUTPUT.PUT_LINE('Erreur : Portefeuille inexistant');
    WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('Erreur : '||sqlerrm);
END;
/

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

    SELECT SUM(Quantite * Cours) INTO total FROM Valeur, Portefeuille, Compte
        WHERE Valeur.CodeValeur = Portefeuille.CodeValeur
        AND Portefeuille.NumCompte = Compte.NumCompte
        AND NomClient = Nom;

    RETURN total;
END;
/

DROP FUNCTION TotalPortefeuille;
DROP PROCEDURE Vendre;
DROP TRIGGER solde_trig;
DROP SEQUENCE numop_seq;
DROP PROCEDURE MAJValeur;
DROP PROCEDURE Acheter;
DROP PROCEDURE OuvrirCompte;
DROP PROCEDURE RepartitionPortefeuille;
DROP SEQUENCE seqCompte;
DROP TRIGGER valeur_pmvl;
DROP TABLE Portefeuille;
DROP TABLE Operation;
DROP TABLE Valeur;
DROP TABLE Secteur;
DROP TABLE Compte;
