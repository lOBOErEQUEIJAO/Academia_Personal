package telas;

import service.LoginService;
import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JButton btnEntrar;

    public LoginView() {
        setTitle("Sistema Academia - Acesso Personal");
        setSize(580, 240);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel painelPrincipal = new JPanel(new BorderLayout(20, 0));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel painelFormulario = new JPanel(new GridLayout(3, 2, 10, 10));

        painelFormulario.add(new JLabel("CPF do Personal:", SwingConstants.RIGHT));
        txtUsuario = new JTextField();
        txtUsuario.setHorizontalAlignment(JTextField.CENTER);
        painelFormulario.add(txtUsuario);

        painelFormulario.add(new JLabel("Senha:", SwingConstants.RIGHT));
        txtSenha = new JPasswordField();
        txtSenha.setHorizontalAlignment(JPasswordField.CENTER);
        painelFormulario.add(txtSenha);

        painelFormulario.add(new JLabel(""));
        btnEntrar = new JButton("Entrar");
        btnEntrar.setBackground(new Color(45, 120, 230));
        painelFormulario.add(btnEntrar);

        JLabel lblLogo = new JLabel();
        try {
            ImageIcon iconeOriginal = new ImageIcon("/home/alessandra/Imagens/Imagem musculação.jpg");
            Image imgRedimensionada = iconeOriginal.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(imgRedimensionada));
        } catch (Exception e) {
            System.out.println("Erro ao carregar imagem: " + e.getMessage());
        }

        JPanel painelDireitoDaTela = new JPanel(new BorderLayout());
        painelDireitoDaTela.add(lblLogo, BorderLayout.NORTH);

        painelPrincipal.add(painelFormulario, BorderLayout.CENTER);
        painelPrincipal.add(painelDireitoDaTela, BorderLayout.EAST);

        add(painelPrincipal);

        btnEntrar.addActionListener(e -> executarLogin());
        getRootPane().setDefaultButton(btnEntrar);

        setVisible(true);
    }

    private void executarLogin() {
        String cpf = txtUsuario.getText();
        String senha = new String(txtSenha.getPassword());

        if (cpf.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LoginService service = new LoginService();
        try {
            if (service.autenticar(cpf, senha)) {
                new MenuPrincipalView().setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "CPF ou Senha incorretos.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro de Conexão: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginView();
        });
    }
}