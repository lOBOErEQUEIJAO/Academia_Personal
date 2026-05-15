package Camilaronzzani.com.github;

import telas.LoginView;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        FlywayConfig.migrate();

        SwingUtilities.invokeLater(() -> {
            LoginView tela = new LoginView();
            tela.setVisible(true);
        });
    }
}