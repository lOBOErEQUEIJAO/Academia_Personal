package Camilaronzzani.com.github;

import telas.LoginView;

public class Main {

    public static void main(String[] args) {

        FlywayConfig.migrate();

        new LoginView();
    }
}