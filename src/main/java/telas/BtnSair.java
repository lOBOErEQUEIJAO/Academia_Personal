package telas;

import javax.swing.*;

public class BtnSair {

    public void sair(JFrame telaAtual) {

        new LoginView().setVisible(true);

        telaAtual.dispose();
    }
}