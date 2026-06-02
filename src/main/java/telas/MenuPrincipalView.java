package telas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.imageio.ImageIO;

public class MenuPrincipalView extends JFrame {

    public MenuPrincipalView() {
        setTitle("Sistema Academia - Menu Principal");
        setSize(580, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // --- PAINEL DE FUNDO COM IMAGEM (Substituindo o antigo painelConteudo) ---
        // Altere o caminho abaixo para a sua imagem no computador!
        String caminhoDaImagem = "/home/alessandra/Imagens/nuvem.jpg";
        PainelComFundo painelConteudo = new PainelComFundo(caminhoDaImagem);
        painelConteudo.setLayout(new BorderLayout());

        // --- TOPO: Título Centralizado ---
        JLabel lblTitulo = new JLabel("Painel de Controle Administrativo", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(1, 5, 28)); // Azul escuro profundo
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(25, 0, 15, 0));
        painelConteudo.add(lblTitulo, BorderLayout.NORTH);

        // --- CENTRO: Painel de Botões em Grade (3 linhas, 2 colunas) ---
        JPanel painelBotoes = new JPanel(new GridLayout(3, 2, 18, 18));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        painelBotoes.setOpaque(false); // Mantém transparente para o fundo aparecer

        // Instanciando os botões com o visual Azul Reluzente Customizado
        JButton btnCadastro = new BotaoNeonReal("Cadastrar Aluno", new Color(0, 55, 145, 255), new Color(0, 115, 230));
        JButton btnAgendar = new BotaoNeonReal("Agendar Aula", new Color(0, 55, 145), new Color(0, 115, 230));
        JButton btnDisponibilidade = new BotaoNeonReal("Cadastrar Disponibilidade", new Color(0, 55, 145), new Color(0, 115, 230));
        JButton btnCadastrarPersonal = new BotaoNeonReal("Cadastrar Personal", new Color(0, 55, 145), new Color(0, 115, 230));
        JButton btnVerAgenda = new BotaoNeonReal("Gestão de Alunos", new Color(0, 55, 145), new Color(0, 115, 230));
        JButton btnAulasAgendadas = new BotaoNeonReal("Aulas Agendadas", new Color(0, 55, 145), new Color(0, 115, 230));

        // --- Lógica de Ação dos Botões ---
        btnCadastro.addActionListener(e -> { this.dispose(); new CadastroView().setVisible(true); });
        btnAgendar.addActionListener(e -> { this.dispose(); new AgendarAulaView().setVisible(true); });
        btnDisponibilidade.addActionListener(e -> { this.dispose(); new CadastrarDisponibilidadeView().setVisible(true); });
        btnCadastrarPersonal.addActionListener(e -> { this.dispose(); new CadastrarPersonalView().setVisible(true); });
        btnVerAgenda.addActionListener(e -> { this.dispose(); new GestaoAlunosView().setVisible(true); });
        btnAulasAgendadas.addActionListener(e -> { this.dispose(); new AulasAgendadasView().setVisible(true); });

        // Adiciona os botões ao painel em grade
        painelBotoes.add(btnCadastro);
        painelBotoes.add(btnAgendar);
        painelBotoes.add(btnDisponibilidade);
        painelBotoes.add(btnCadastrarPersonal);
        painelBotoes.add(btnVerAgenda);
        painelBotoes.add(btnAulasAgendadas);

        painelConteudo.add(painelBotoes, BorderLayout.CENTER);

        // --- SUL: Botão Vermelho Reluzente "Sair do Sistema" ---
        JPanel painelSul = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelSul.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));
        painelSul.setOpaque(false);

        JButton btnSair = new BotaoNeonReal("Sair do Sistema", new Color(145, 20, 20), new Color(220, 45, 45));
        btnSair.setPreferredSize(new Dimension(190, 38));
        btnSair.addActionListener(e -> System.exit(0));

        painelSul.add(btnSair);
        painelConteudo.add(painelSul, BorderLayout.SOUTH);

        add(painelConteudo);
    }

    // --- SUBCLASSE: Painel customizado que renderiza a imagem de fundo ---
    private static class PainelComFundo extends JPanel {
        private Image imagemDeFundo;

        public PainelComFundo(String caminhoImagem) {
            try {
                File arquivo = new File(caminhoImagem);
                if (arquivo.exists()) {
                    this.imagemDeFundo = ImageIO.read(arquivo);
                }
            } catch (Exception e) {
                System.out.println("Não foi possível carregar a imagem de fundo: " + e.getMessage());
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imagemDeFundo != null) {
                // Desenha a imagem esticando-a para preencher todo o tamanho atual do painel
                g.drawImage(imagemDeFundo, 0, 0, getWidth(), getHeight(), this);
            } else {
                // Caso a imagem não seja encontrada, mantém o fundo padrão cinza azulado original
                g.setColor(new Color(235, 238, 243));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    // --- SUBCLASSE: Mecanismo de Renderização de Brilho de Vidro e Luz Ativa (Hover) ---
    private static class BotaoNeonReal extends JButton {
        private final Color corFundo;
        private final Color corBrilho;
        private boolean mousePorCima = false;

        public BotaoNeonReal(String texto, Color corFundo, Color corBrilho) {
            super(texto);
            this.corFundo = corFundo;
            this.corBrilho = corBrilho;

            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    mousePorCima = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    mousePorCima = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int largura = getWidth();
            int altura = getHeight();

            Color baseAtual = mousePorCima ? corBrilho : corFundo;
            Color topoBrilho = mousePorCima ? corBrilho.brighter() : corBrilho;

            // 1. Fundo do botão com degradê de profundidade tridimensional
            GradientPaint gradienteFundo = new GradientPaint(0, 0, topoBrilho, 0, altura, baseAtual.darker().darker());
            g2.setPaint(gradienteFundo);
            g2.fillRoundRect(0, 0, largura, altura, 12, 12);

            // 2. Reflexo de vidro em elipse (efeito glossy brilhante na metade superior)
            GradientPaint reflexoVidro = new GradientPaint(0, 0, new Color(255, 255, 255, 130), 0, altura / 2, new Color(255, 255, 255, 0));
            g2.setPaint(reflexoVidro);
            g2.fillRoundRect(2, 2, largura - 4, altura / 2, 10, 10);

            // 3. Moldura reluzente nas bordas externas
            g2.setColor(mousePorCima ? Color.WHITE : corBrilho);
            g2.setStroke(new BasicStroke(mousePorCima ? 2.0f : 1.2f));
            g2.drawRoundRect(1, 1, largura - 2, altura - 2, 12, 12);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipalView().setVisible(true));
    }
}