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
