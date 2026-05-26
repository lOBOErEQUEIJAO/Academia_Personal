package telas;

import entity.AlunoEntity;
import entity.StatusAluno;
import service.AlunoService;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.time.LocalDate;

public class CadastroView extends JFrame {
    private JTextField txtNome, txtCpf, txtTelefone, txtEmail;
    private JPasswordField txtSenha;
    private JButton btnSalvar, btnCancelar;

    public CadastroView() {
        setTitle("Sistema Academia - Cadastrar Novo Aluno");
        setSize(450, 600); // Aumentado um pouco a altura para acomodar a imagem no topo
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // --- PAINEL DO TOPO (Para a Imagem) ---
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 10));
        JLabel lblFotoPerfil = new JLabel();

        try {
            // EXEMPLO: Mude esse caminho para o arquivo correto do seu computador depois!
            File arquivoImg = new File("/home/alessandra/Imagens/icone.png");

            if (arquivoImg.exists()) {
                BufferedImage imgOriginal = ImageIO.read(arquivoImg);
                ImageIcon iconeRedondo = criarImagemRedonda(imgOriginal, 100); // Tamanho ajustado para o topo
                lblFotoPerfil.setIcon(iconeRedondo);
            } else {
                lblFotoPerfil.setIcon(criarCirculoPadrao(100));
            }
        } catch (Exception e) {
            lblFotoPerfil.setIcon(criarCirculoPadrao(100));
        }
        painelTopo.add(lblFotoPerfil);

        // --- PAINEL DE CAMPOS (CENTRO) ---
        JPanel painelCampos = new JPanel(new GridLayout(6, 2, 10, 15));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(15, 25, 10, 25));

        painelCampos.add(new JLabel("Nome do Aluno:"));
        txtNome = new JTextField();
        painelCampos.add(txtNome);

        painelCampos.add(new JLabel("CPF:"));
        txtCpf = new JTextField();
        painelCampos.add(txtCpf);

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
        btnSalvar.setForeground(Color.WHITE); // Texto branco para destacar no verde
        btnSalvar.setFont(new Font("Arial", Font.BOLD, 14));

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnSalvar);

        // Adicionando os painéis na janela seguindo a estrutura original
        add(painelTopo, BorderLayout.NORTH); // Imagem adicionada na parte de cima (Norte)
        add(painelCampos, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        // --- EVENTOS DE INTERLIGAÇÃO ---

        // Ação do Botão Cancelar: Fecha a ficha e volta direto para o Menu Principal
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
            AlunoEntity aluno = new AlunoEntity();
            aluno.setNome(txtNome.getText().trim());
            aluno.setCpf(txtCpf.getText().trim());
            aluno.setTelefone(txtTelefone.getText().trim());
            aluno.setEmail(txtEmail.getText().trim());

            String senha = new String(txtSenha.getPassword());
            aluno.setSenha(senha);

            aluno.setDataNascimento(LocalDate.of(2000, 1, 1));
            aluno.setDataMatricula(LocalDate.now());
            aluno.setStatus(StatusAluno.ATIVO);

            AlunoService service = new AlunoService();
            service.cadastrar(aluno, senha);

            JOptionPane.showMessageDialog(this, "Aluno cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            this.dispose();
            new MenuPrincipalView().setVisible(true);

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Atenção: " + ex.getMessage(), "Erro de Validação", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro técnico ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CadastroView().setVisible(true));
    }
}