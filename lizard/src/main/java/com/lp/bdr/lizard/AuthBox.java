package com.lp.bdr.lizard;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.List;

public class AuthBox extends Form {
    private static int FIELD_USER = 0,
                       FIELD_PASSWORD = 1,
                       FIELD_HOST = 2,
                       FIELD_DATABASE = 3;

    private ConnectionProvider connectionProvider;
    private Label errorLabel;

    public AuthBox(ConnectionProvider connectionProvider) {
        super("Se connecter");
        this.connectionProvider = connectionProvider;

        addField(FIELD_HOST, "Adresse du serveur");
        addField(FIELD_DATABASE, "Nom de la base de données");
        addField(FIELD_USER, "Nom d'utilisateur");
        addPasswordField(FIELD_PASSWORD, "Mot de passe");

        errorLabel = new Label("La connexion a échoué.");
        errorLabel.setTextFill(Color.RED);

        setOnValidate(this::onValidate);
        setMaxWidth(400);
    }

    private void onValidate() {
        List<Node> childs = getChildren();
        childs.remove(errorLabel);

        boolean res = connectionProvider.connect(getFieldValue(FIELD_HOST),
            getFieldValue(FIELD_DATABASE), getFieldValue(FIELD_USER),
            getFieldValue(FIELD_PASSWORD));

        if(!res) {
            childs.add(childs.size() - 1, errorLabel);
        }
    }
}
