package telas;

import entity.AlunoEntity;
import service.AlunoService;
import service.LoginService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class LoginView extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JButton btnEntrar;

    public LoginView() {
        setTitle("Sistema Academia - Acesso Personal");
        setSize(380, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel painelPrincipal = new JPanel(new GridLayout(3, 2, 10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        painelPrincipal.add(new JLabel("CPF do Personal:", SwingConstants.RIGHT));
        txtUsuario = new JTextField();
        txtUsuario.setHorizontalAlignment(JTextField.CENTER);
        painelPrincipal.add(txtUsuario);

        painelPrincipal.add(new JLabel("Senha:", SwingConstants.RIGHT));
        txtSenha = new JPasswordField();
        txtSenha.setHorizontalAlignment(JPasswordField.CENTER);
        painelPrincipal.add(txtSenha);

        painelPrincipal.add(new JLabel(""));
        btnEntrar = new JButton("Entrar");
        btnEntrar.setBackground(new Color(45, 120, 230));
        painelPrincipal.add(btnEntrar);

        add(painelPrincipal);

        btnEntrar.addActionListener(e -> executarLogin());
        getRootPane().setDefaultButton(btnEntrar);
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
            // Verifica se o login existe no banco
            if (service.autenticar(cpf, senha)) {
                new MenuPersonalView().setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "CPF ou Senha incorretos.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro de Conexão: " + e.getMessage());
        }
    }

    // --- TELA DO PERSONAL (DENTRO DA LOGINVIEW) ---
    static class MenuPersonalView extends JFrame {
        private DefaultListModel<String> modeloLista;
        private List<String> todosOsNomes;
        private JTextField txtBusca;
        private AlunoService alunoService = new AlunoService();

        public MenuPersonalView() {
            setTitle("Painel de Controle - Personal Trainer");
            setSize(800, 750);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            JLabel lblTitulo = new JLabel("Gestão de Alunos", SwingConstants.CENTER);
            lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
            lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
            add(lblTitulo, BorderLayout.NORTH);

            JPanel pnlCentral = new JPanel(new GridBagLayout());
            pnlCentral.setBackground(new Color(245, 245, 245));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Busca
            txtBusca = new JTextField();
            txtBusca.setPreferredSize(new Dimension(300, 45));
            txtBusca.setBorder(BorderFactory.createTitledBorder("Procurar Aluno por Nome:"));
            gbc.gridx = 0; gbc.gridy = 0;
            pnlCentral.add(txtBusca, gbc);

            // Lista
            modeloLista = new DefaultListModel<>();
            todosOsNomes = new ArrayList<>();
            atualizarListaNaTela();

            JList<String> jListAlunos = new JList<>(modeloLista);
            jListAlunos.setFont(new Font("SansSerif", Font.PLAIN, 14));
            jListAlunos.setFixedCellHeight(30);

            // Clique Duplo para Editar
            jListAlunos.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        String selecionado = jListAlunos.getSelectedValue();
                        if (selecionado != null) new EditarAlunoView(selecionado).setVisible(true);
                    }
                }
            });

            JScrollPane scrollPane = new JScrollPane(jListAlunos);
            scrollPane.setPreferredSize(new Dimension(300, 300));
            gbc.gridy = 1;
            pnlCentral.add(scrollPane, gbc);

            // Filtro de Busca dinâmico
            txtBusca.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e) { filtrar(); }
                public void removeUpdate(DocumentEvent e) { filtrar(); }
                public void changedUpdate(DocumentEvent e) { filtrar(); }
                private void filtrar() {
                    String texto = txtBusca.getText().toLowerCase();
                    modeloLista.clear();
                    for (String nome : todosOsNomes) {
                        if (nome.toLowerCase().contains(texto)) modeloLista.addElement(nome);
                    }
                }
            });

            // Botão Cadastrar
            JButton btnCadastrar = new JButton(" + Cadastrar Novo Aluno ");
            btnCadastrar.setBackground(new Color(34, 139, 34));
            btnCadastrar.setPreferredSize(new Dimension(300, 40));
            btnCadastrar.addActionListener(e -> new CadastroView().setVisible(true));
            gbc.gridy = 2; gbc.insets = new Insets(15, 5, 5, 5);
            pnlCentral.add(btnCadastrar, gbc);

            // Botão Remover (Usa o ID para deletar com segurança)
            JButton btnRemover = new JButton(" - Remover Aluno Permanentemente ");
            btnRemover.setBackground(new Color(220, 20, 60));
            btnRemover.setPreferredSize(new Dimension(300, 40));
            btnRemover.addActionListener(e -> {
                String nomeInserido = JOptionPane.showInputDialog(this, "Digite o nome exato para REMOVER:");
                if (nomeInserido != null && !nomeInserido.trim().isEmpty()) {
                    executarRemocao(nomeInserido.trim());
                }
            });
            gbc.gridy = 3; gbc.insets = new Insets(5, 5, 5, 5);
            pnlCentral.add(btnRemover, gbc);

            add(pnlCentral, BorderLayout.CENTER);

            // Sair / Logoff
            JButton btnSair = new JButton("Sair do Sistema");
            btnSair.addActionListener(e -> { new LoginView().setVisible(true); this.dispose(); });
            JPanel pnlSul = new JPanel();
            pnlSul.add(btnSair);
            add(pnlSul, BorderLayout.SOUTH);
        }

        private void executarRemocao(String nome) {
            try {
                // Busca o aluno pelo nome para pegar o ID dele
                AlunoEntity encontrado = alunoService.buscarPorNome(nome);

                if (encontrado != null) {
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "ATENÇÃO: Deseja apagar " + encontrado.getNome() + " e seu login permanentemente?",
                            "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                    if (confirm == JOptionPane.YES_OPTION) {
                        // Chama o excluir do Service passando o ID
                        alunoService.excluir(encontrado.getId());
                        JOptionPane.showMessageDialog(this, "Registro excluído do banco de dados!");
                        atualizarListaNaTela();
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        }

        private void atualizarListaNaTela() {
            modeloLista.clear();
            todosOsNomes.clear();
            try {
                List<AlunoEntity> alunos = alunoService.listarTodos();
                for (AlunoEntity a : alunos) {
                    modeloLista.addElement(a.getNome());
                    todosOsNomes.add(a.getNome());
                }
            } catch (Exception e) {
                modeloLista.addElement("Erro ao conectar com o banco.");
            }
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }
}