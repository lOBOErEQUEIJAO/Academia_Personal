package telas;

import controller.LoginController;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import request.LoginRequest;

public class LoginView extends JFrame {
    private JTextField txtUsuario; // Este campo recebe o CPF do usuário
    private JPasswordField txtSenha;
    private JButton btnEntrar;

    // Injetando o Controller conforme as regras do MVC
    private final LoginController loginController;

    public LoginView() {
        // Inicializa o gerenciador de fluxo da tela
        this.loginController = new LoginController();

        setTitle("Sistema Academia - Acesso Personal");
        setSize(750, 530); // Ajustado para dar o espaçamento confortável do protótipo
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Painel Principal com fundo cinza bem claro (estilo Figma)
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setBackground(new Color(240, 240, 240));
        painelPrincipal.setLayout(null); // Permite posicionar os componentes nos pixels exatos

        // 1. TÍTULO PRINCIPAL (LOGIN PERSONAL)
        JLabel lblTitulo = new JLabel("LOGIN PERSONAL");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 36));
        lblTitulo.setForeground(Color.BLACK);
        lblTitulo.setBounds(80, 150, 350, 50);
        painelPrincipal.add(lblTitulo);

        // 2. RÓTULO CPF
        JLabel lblCpf = new JLabel("CPF:");
        lblCpf.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblCpf.setBounds(80, 230, 100, 20);
        painelPrincipal.add(lblCpf);

        // 3. CAMPO DE TEXTO CPF (Alinhado à esquerda com borda fina)
        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtUsuario.setHorizontalAlignment(JTextField.LEFT);
        txtUsuario.setBounds(80, 255, 300, 35);
        txtUsuario.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        painelPrincipal.add(txtUsuario);

        // 4. RÓTULO SENHA
        JLabel lblSenha = new JLabel("SENHA:");
        lblSenha.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblSenha.setBounds(80, 310, 100, 20);
        painelPrincipal.add(lblSenha);

        // 5. CAMPO DE TEXTO SENHA (Alinhado à esquerda com borda fina)
        txtSenha = new JPasswordField();
        txtSenha.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtSenha.setHorizontalAlignment(JPasswordField.LEFT);
        txtSenha.setBounds(80, 335, 300, 35);
        txtSenha.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        painelPrincipal.add(txtSenha);

        // 6. BOTÃO ENTRAR (Abaixo dos campos de texto, estilizado em azul)
        btnEntrar = new JButton("Entrar");
        btnEntrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnEntrar.setBackground(new Color(45, 120, 230));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFocusPainted(false);
        btnEntrar.setBounds(80, 390, 300, 45); // Centralizado logo abaixo das caixas
        painelPrincipal.add(btnEntrar);

        // 7. LINK DE CADASTRO (Parte inferior centralizada)
        JLabel lblCadastro = new JLabel("Não tem login? Cadastre-se", SwingConstants.CENTER);
        lblCadastro.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblCadastro.setForeground(new Color(30, 144, 255)); // Azul de link
        lblCadastro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblCadastro.setBounds(80, 455, 300, 20);
        painelPrincipal.add(lblCadastro);

        // 8. LOGO REDONDA (Posicionada à direita do formulário)
        JLabel lblLogo = new JLabel();
        lblLogo.setBounds(440, 140, 200, 200); // Espaço reservado para a logo à direita

        try {
            File arquivoImg = new File("/home/alessandra/Imagens/Imagem musculação.jpg");
            BufferedImage imgOriginal = ImageIO.read(arquivoImg);

            if (imgOriginal != null) {
                // Cria a imagem perfeitamente circular de 180x180 pixels com borda
                ImageIcon iconeRedondo = criarImagemRedondaComBorda(imgOriginal, 180);
                lblLogo.setIcon(iconeRedondo);
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar imagem: " + e.getMessage());
        }
        painelPrincipal.add(lblLogo);

        // Inicialização e Eventos da Janela
        add(painelPrincipal);

        // JUNÇÃO: Evento do clique usando o LoginController e LoginRequest
        btnEntrar.addActionListener(e -> {
            try {
                String cpfDigitado = txtUsuario.getText(); // Captura o CPF digitado
                String senhaDigitada = new String(txtSenha.getPassword()); // Captura a senha

                // 1. Empacota os dados capturados na classe Request criada
                LoginRequest dadosLogin = new LoginRequest(cpfDigitado, senhaDigitada);

                // 2. Transfere a responsabilidade de autenticar para o controller
                boolean sucesso = loginController.autenticar(dadosLogin);

                // 3. Se retornar true, direciona o fluxo para a tela do Menu
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Login realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                    new MenuPrincipalView().setVisible(true);
                }

            } catch (Exception ex) {
                // Captura as mensagens de validação lançadas pelo controller
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Autenticação", JOptionPane.ERROR_MESSAGE);
            }
        });

        getRootPane().setDefaultButton(btnEntrar);
        setVisible(true);
    }

    /*Método auxiliar que gera o recorte redondo adicionando a linha preta fina de contorno do desenho
     */
    private static ImageIcon criarImagemRedondaComBorda(BufferedImage imagemOriginal, int tamanho) {
        BufferedImage imagemRedonda = new BufferedImage(tamanho, tamanho, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = imagemRedonda.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Recorta a imagem em círculo
        g2.setClip(new Ellipse2D.Float(0, 0, tamanho, tamanho));
        g2.drawImage(imagemOriginal, 0, 0, tamanho, tamanho, null);

        // Remove o clip para desenhar o contorno por cima da borda da imagem
        g2.setClip(null);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1));
        g2.draw(new Ellipse2D.Float(0, 0, tamanho - 1, tamanho - 1));

        g2.dispose();
        return new ImageIcon(imagemRedonda);
    }
}