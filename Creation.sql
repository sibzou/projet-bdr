-- Suppression des objets
-- Suppression des tables et des contraintes associées…
DROP TABLE Compte CASCADE CONSTRAINTS;
DROP TABLE Operation CASCADE CONSTRAINTS;
DROP TABLE Portefeuille CASCADE CONSTRAINTS;
DROP TABLE Secteur CASCADE CONSTRAINTS;
DROP TABLE Valeur CASCADE CONSTRAINTS;

-- a) Création des tables...
CREATE TABLE Compte (   numCompte NUMBER(3) PRIMARY KEY, 
            nomClient VARCHAR2(15) NOT NULL,
            DateOuverture DATE NOT NULL,
            Solde DECIMAL(7,2),
            PMVR DECIMAL(7,2) DEFAULT 0
            );
             
CREATE TABLE Secteur ( CodeSE CHAR(4) PRIMARY KEY, 
            SecteurEconomique VARCHAR2(15) NOT NULL
            );
             
CREATE TABLE Valeur ( codeValeur VARCHAR2(8) PRIMARY KEY,
        Denomination VARCHAR2(10) NOT NULL, 
        CodeSecteur CHAR(8) NOT NULL,
        Indice CHAR(8) NOT NULL,
        Cours DECIMAL(4,2) NOT NULL
        );

CREATE TABLE Operation ( NumOp NUMBER(2) PRIMARY KEY,
            numCompte NUMBER(3) REFERENCES Compte(numCompte),
            CodeValeur CHAR(8) REFERENCES Valeur(codeValeur),
            DateOp DATE NOT NULL, 
            Nature CHAR(1) NOT NULL,
            QteOp NUMBER(3) NOT NULL,
            Montant DECIMAL(7,2) NOT NULL
             );
             
CREATE TABLE Portefeuille ( NumCompte NUMBER(3) REFERENCES Compte(numCompte),
            CodeValeur CHAR(8) REFERENCES Valeur(codeValeur),
            Quantite NUMBER(3),
            PAM DECIMAL(5,2), 
            PMVL DECIMAL(5,2),
            PRIMARY KEY(NumCompte, CodeValeur)
             );
             
-- Création de la procédure AjouterService
CREATE OR REPLACE PROCEDURE OuvrirCompte(Nom IN VARCHAR2, Montant IN NUMBER) 
IS
BEGIN
END;

-- Création de la procédure MAJValeur
CREATE OR REPLACE PROCEDURE MAJValeur(Code IN VARCHAR2, Cours IN NUMBER) 
IS
BEGIN
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