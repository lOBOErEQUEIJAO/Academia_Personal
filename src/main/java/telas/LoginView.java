package telas;

import javax.swing.*;
import java.awt.*;
import service.LoginService;
import repository.AlunoRepository; // Importando seu repositório
import entity.AlunoEntity;  // importando sua entidade

public class LoginView extends JFrame {
    // Componentes de tela
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JButton btnEntrar;


    public LoginView() {
        setTitle("Sistema Academia - Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        //Criando a Interface
        add(new JLabel(" Usuário (CPF):" , SwingConstants.RIGHT));
        txtUsuario = new JTextField();
        txtUsuario.setHorizontalAlignment(JTextField.CENTER);
        add(txtUsuario);

        add(new JLabel("  Senha:", SwingConstants.RIGHT));
        txtSenha = new JPasswordField();
        txtSenha.setHorizontalAlignment(JPasswordField.CENTER);
        add(txtSenha);

        add(new JLabel("")); // Espaço vazio
        btnEntrar = new JButton("Entrar");
        add(btnEntrar);

        // Acão do Botão
        btnEntrar.addActionListener(e -> {
            executarLogin();
        });
          // "borda" invisível p/ os componentes não ficarem colados da beirada da janela
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    }
    class MenuPrincipalView extends JFrame {
        public MenuPrincipalView() {
            setTitle("Sistema Academia - Menu Principal");
            setSize(350, 200);
        }
    }

    private void executarLogin() {
        // Captura os dados da interface ou seja pega os dados da tela
        String cpf = txtUsuario.getText();
        String senha = new String(txtSenha.getPassword());

        LoginService service = new LoginService(); //O Cérebro do sistema ou chama o Service  p/ validar

        boolean autenticado = service.autenticar(cpf, senha); // Faz a chamada ao serviço

        if (autenticado) {
            JOptionPane.showMessageDialog(this, "Login realizado com sucesso! Bem-vindo.");

            // Aqui você abriria a próxima tela do projeto
            // new MenuPrincipalView().setVisible(true);

            this.dispose(); // Fecha a telea de login
        } else {
            new MenuPrincipalView().setVisible(true);
            // Tratamento de Erros/Feedback
            /*JOptionPane.showMessageDialog(this, "CPF ou senha inválidos!",
                    "Erro de Auntenticação", JOptionPane.ERROR_MESSAGE);*/
        }
    }

    // Método principal para a tela
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}
