package telas;

import service.LoginService;

import javax.swing.*;
import java.awt.*;

public class WebView extends JFrame {

    // Componentes da tela
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JButton btnEntrar;
    private JButton btnSair;

    public WebView() {

        setTitle("Sistema Academia - Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout principal da janela
        setLayout(new BorderLayout());

        // Painel para organizar os componentes
        JPanel painel = new JPanel(new GridLayout(3, 2, 10, 10));

        // Usuário
        painel.add(new JLabel("Usuário (CPF):", SwingConstants.RIGHT));

        txtUsuario = new JTextField();
        txtUsuario.setHorizontalAlignment(JTextField.CENTER);
        painel.add(txtUsuario);

        // Senha
        painel.add(new JLabel("Senha:", SwingConstants.RIGHT));

        txtSenha = new JPasswordField();
        txtSenha.setHorizontalAlignment(JTextField.CENTER);
        painel.add(txtSenha);

        // Espaço vazio
        painel.add(new JLabel(""));

        // Botão entrar
        btnEntrar = new JButton("Entrar");
        painel.add(btnEntrar);

        // Adiciona o painel na janela
        add(painel, BorderLayout.CENTER);

        // Margem interna
        ((JPanel) getContentPane()).setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        // Evento do botão
        btnEntrar.addActionListener(e -> executarLogin());
    }

    private void executarLogin() {

        // Captura os dados da tela
        String cpf = txtUsuario.getText();
        String senha = new String(txtSenha.getPassword());

        // Chama o service
        LoginService service = new LoginService();

        boolean autenticado = service.autenticar(cpf, senha);

        // Verifica login
        if (autenticado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Login realizado com sucesso!"
            );

            new MenuPrincipalView().setVisible(true);

            this.dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "CPF ou senha inválidos!",
                    "Erro de Autenticação",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Main
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new WebView().setVisible(true);
        });
    }
}