package telas;

import controller.PersonalController;
import entity.PersonalEntity;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class CadastrarPersonalView extends JFrame {
    private JTextField txtNome, txtCpf, txtTelefone, txtEmail, txtCref;
    private JPasswordField txtSenha;
    private JButton btnSalvar, btnCancelar;

    // Injetando o Controller conforme as regras do MVC
    private final PersonalController personalController;

    public CadastrarPersonalView() {
        // Inicializa o controlador da tela
        this.personalController = new PersonalController();

        setTitle("Sistema Academia - Cadastrar Novo Personal");
        setSize(450, 600); // Mantida a altura para acomodar a imagem no topo
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // --- PAINEL DO TOPO (Para a Imagem) ---
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 10));
        JLabel lblFotoPerfil = new JLabel();

        try {
            // Caminho para o arquivo correto do computador
            File arquivoImg = new File("/home/alessandra/Imagens/Personal.jpg");

            if (arquivoImg.exists()) {
                BufferedImage imgOriginal = ImageIO.read(arquivoImg);
                ImageIcon iconeRedondo = criarImagemRedonda(imgOriginal, 100);
                lblFotoPerfil.setIcon(iconeRedondo);
            } else {
                lblFotoPerfil.setIcon(criarCirculoPadrao(100));
            }
        } catch (Exception e) {
            lblFotoPerfil.setIcon(criarCirculoPadrao(100));
        }
        painelTopo.add(lblFotoPerfil);

        // --- PAINEL DE CAMPOS (CENTRO) ---
        // Alterado de (6, 2...) para (7, 2...) para acomodar o CREF perfeitamente
        JPanel painelCampos = new JPanel(new GridLayout(7, 2, 10, 15));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(15, 25, 10, 25));

        painelCampos.add(new JLabel("Nome do Personal:"));
        txtNome = new JTextField();
        painelCampos.add(txtNome);

        painelCampos.add(new JLabel("CPF:"));
        txtCpf = new JTextField();
        painelCampos.add(txtCpf);

        painelCampos.add(new JLabel("CREF:"));
        txtCref = new JTextField();
        painelCampos.add(txtCref);

        painelCampos.add(new JLabel("Telefone:"));
        txtTelefone = new JTextField();
        painelCampos.add(txtTelefone);

        painelCampos.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        painelCampos.add(txtEmail);

        painelCampos.add(new JLabel("Senha Inicial:"));
        txtSenha = new JPasswordField();
        painelCampos.add(txtSenha);

        // --- PAINEL DE BOTÕES (SUL) ---
        JPanel painelBotoes = new JPanel(new GridLayout(2, 1, 5, 5));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(0, 25, 20, 25));

        // Botão Cancelar configurado para Voltar
        btnCancelar = new JButton("Cancelar e Voltar");
        btnCancelar.setBackground(new Color(180, 180, 180));
        btnCancelar.setFont(new Font("Arial", Font.PLAIN, 13));

        // Botão Salvar
        btnSalvar = new JButton("Finalizar Cadastro");
        btnSalvar.setBackground(new Color(34, 139, 34));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("Arial", Font.BOLD, 14));

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnSalvar);

        // Adicionando os painéis na janela seguindo estritamente a estrutura original
        add(painelTopo, BorderLayout.NORTH);
        add(painelCampos, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        // --- EVENTOS DE INTERLIGAÇÃO ---

        // Ação do Botão Cancelar
        btnCancelar.addActionListener(e -> {
            this.dispose();
            new MenuPrincipalView().setVisible(true);
        });

        // Ação do Botão Salvar
        btnSalvar.addActionListener(e -> executarCadastro());

        // Tecla ENTER aciona o botão de salvar automaticamente
        getRootPane().setDefaultButton(btnSalvar);
    }

    // Método auxiliar para recortar a imagem em formato circular
    private static ImageIcon criarImagemRedonda(BufferedImage imagemOriginal, int tamanho) {
        BufferedImage imagemRedonda = new BufferedImage(tamanho, tamanho, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = imagemRedonda.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g2.setClip(new Ellipse2D.Float(0, 0, tamanho, tamanho));
        g2.drawImage(imagemOriginal, 0, 0, tamanho, tamanho, null);

        g2.setClip(null);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1));
        g2.draw(new Ellipse2D.Float(0, 0, tamanho - 1, tamanho - 1));

        g2.dispose();
        return new ImageIcon(imagemRedonda);
    }

    // Gera um círculo cinza caso o arquivo não seja encontrado
    private static ImageIcon criarCirculoPadrao(int tamanho) {
        BufferedImage imagemBkp = new BufferedImage(tamanho, tamanho, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = imagemBkp.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.LIGHT_GRAY);
        g2.fill(new Ellipse2D.Float(0, 0, tamanho, tamanho));
        g2.setColor(Color.BLACK);
        g2.draw(new Ellipse2D.Float(0, 0, tamanho - 1, tamanho - 1));

        g2.dispose();
        return new ImageIcon(imagemBkp);
    }

    private void executarCadastro() {
        try {
            // 1. Instancia a entidade e preenche com os dados da tela
            PersonalEntity personal = new PersonalEntity();
            personal.setNome(txtNome.getText().trim());
            personal.setCpf(txtCpf.getText().trim());
            personal.setCref(txtCref.getText().trim());
            personal.setTelefone(txtTelefone.getText().trim());
            personal.setEmail(txtEmail.getText().trim());

            // Captura a senha digitada na tela
            String senha = new String(txtSenha.getPassword());

            // CORREÇÃO AQUI: Define a senha diretamente no objeto da entidade para o Hibernate não dar erro de not-null
            personal.setSenha(senha);

            // 2. Transfere a responsabilidade para o Controller validar e salvar
            personalController.cadastrarNovoPersonal(personal, senha);

            // 3. Exibe mensagem de sucesso se passar por todas as validações do controller
            JOptionPane.showMessageDialog(this, "Personal cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            this.dispose();
            new MenuPrincipalView().setVisible(true);

        } catch (Exception ex) {
            // Captura as mensagens de validação lançadas pelo controller
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Validação", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CadastrarPersonalView().setVisible(true));
    }
}