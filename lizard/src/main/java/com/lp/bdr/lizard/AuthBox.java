package com.lp.bdr.lizard;

public class AuthBox extends Form {
    private static int FIELD_USER = 0,
                       FIELD_PASSWORD = 1;

    public AuthBox() {
        super("Se connecter");
        addField(FIELD_USER, "Nom d'utilisateur");
        addField(FIELD_PASSWORD, "Mot de passe");

        setMaxWidth(400);
    }
}
