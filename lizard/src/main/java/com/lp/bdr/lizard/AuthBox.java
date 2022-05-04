package com.lp.bdr.lizard;

public class AuthBox extends Form {
    private static int FIELD_USER = 0,
                       FIELD_PASSWORD = 1,
                       FIELD_HOST = 2;

    private ConnectionProvider connectionProvider;

    public AuthBox(ConnectionProvider connectionProvider) {
        super("Se connecter");
        this.connectionProvider = connectionProvider;

        addField(FIELD_HOST, "Adresse du serveur");
        addField(FIELD_USER, "Nom d'utilisateur");
        addPasswordField(FIELD_PASSWORD, "Mot de passe");

        setOnValidate(this::onValidate);
        setMaxWidth(400);
    }

    private void onValidate() {
        connectionProvider.connect(getFieldValue(FIELD_HOST),
            getFieldValue(FIELD_USER), getFieldValue(FIELD_PASSWORD));
    }
}
