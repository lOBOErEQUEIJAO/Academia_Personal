package telas;

import service.LoginService;
import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    private JTextField campoCpf;
    private JPasswordField campoSenha;
    private JButton botaoLogin;

    public LoginView() {

        setTitle("Login Academia");
        setSize(850, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel();
        painel.setLayout(new GridLayout(3, 2, 10, 10));

        painel.add(new JLabel("Usuário:"));
        campoCpf = new JTextField();
        painel.add(campoCpf);

        painel.add(new JLabel("Senha:"));
        campoSenha = new JPasswordField();
        painel.add(campoSenha);

        botaoLogin = new JButton("Entrar");
        painel.add(botaoLogin);
        add(painel);
        botaoLogin.addActionListener(e -> fazerLogin());
        setVisible(true);
    }

    private void fazerLogin() {
        String usuario = campoCpf.getText();
        String senha = new String(campoSenha.getPassword());
        LoginService loginService = new LoginService();

        if (loginService.autenticar(usuario, senha)) {
            JOptionPane.showMessageDialog(this,"Login realizado!");
            new MenuPrincipalView();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,"Usuário ou senha inválidos!");
        }
    }
}