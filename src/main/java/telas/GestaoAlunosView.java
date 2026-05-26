package telas;

import entity.AlunoEntity;
import service.AlunoService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GestaoAlunosView extends JFrame {

    private final DefaultListModel<String> modeloLista =
            new DefaultListModel<>();

    private final List<AlunoEntity> todosOsAlunos =
            new ArrayList<>();

    private JTextField txtBusca;

    private JList<String> jListAlunos;

    private final AlunoService alunoService =
            new AlunoService();

    private final DateTimeFormatter formatador =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public GestaoAlunosView() {

        setTitle("Painel de Controle - Personal Trainer");

        setSize(850, 750);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        // =========================
        // TÍTULO
        // =========================

        JLabel lblTitulo = new JLabel(
                "Registro de Alunos Matriculados",
                SwingConstants.CENTER
        );

        lblTitulo.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        lblTitulo.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        0,
                        10,
                        0
                )
        );

        add(lblTitulo, BorderLayout.NORTH);

        // =========================
        // PAINEL CENTRAL
        // =========================

        JPanel pnlCentral =
                new JPanel(new GridBagLayout());

        pnlCentral.setBackground(
                new Color(245, 245, 245)
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets = new Insets(8, 5, 8, 5);

        gbc.fill = GridBagConstraints.HORIZONTAL;

        // =========================
        // CAMPO BUSCA
        // =========================

        txtBusca = new JTextField();

        txtBusca.setPreferredSize(
                new Dimension(450, 45)
        );

        txtBusca.setBorder(
                BorderFactory.createTitledBorder(
                        "Procurar Aluno por Nome:"
                )
        );

        gbc.gridx = 0;
        gbc.gridy = 0;

        pnlCentral.add(txtBusca, gbc);

        // =========================
        // LISTA DE ALUNOS
        // =========================

        jListAlunos = new JList<>(modeloLista);

        jListAlunos.setFont(
                new Font("SansSerif", Font.PLAIN, 15)
        );

        jListAlunos.setFixedCellHeight(35);

        atualizarListaNaTela();

        // DUPLO CLIQUE PARA EDITAR

        jListAlunos.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {

                        if (e.getClickCount() == 2) {

                            int index =
                                    jListAlunos.getSelectedIndex();

                            if (index >= 0) {

                                AlunoEntity aluno =
                                        obterAlunoCorrespondente(index);

                                if (aluno != null) {

                                    abrirJanelaAgendamento(aluno);
                                }
                            }
                        }
                    }
                }
        );

        JScrollPane scrollPane =
                new JScrollPane(jListAlunos);

        scrollPane.setPreferredSize(
                new Dimension(450, 250)
        );

        gbc.gridy = 1;

        pnlCentral.add(scrollPane, gbc);

        // =========================
        // FILTRO
        // =========================

        txtBusca.getDocument().addDocumentListener(
                new DocumentListener() {

                    public void insertUpdate(DocumentEvent e) {
                        filtrar();
                    }

                    public void removeUpdate(DocumentEvent e) {
                        filtrar();
                    }

                    public void changedUpdate(DocumentEvent e) {
                        filtrar();
                    }
                }
        );

        // =========================
        // BOTÃO CADASTRAR
        // =========================

        JButton btnCadastrar =
                new JButton(" + Cadastrar Novo Aluno ");

        btnCadastrar.setBackground(
                new Color(34, 139, 34)
        );

        btnCadastrar.setForeground(Color.WHITE);

        btnCadastrar.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        btnCadastrar.setPreferredSize(
                new Dimension(450, 45)
        );

        btnCadastrar.addActionListener(e ->
                new CadastroView().setVisible(true)
        );

        gbc.gridy = 2;

        gbc.insets =
                new Insets(20, 5, 5, 5);

        pnlCentral.add(btnCadastrar, gbc);

        // =========================
        // BOTÃO REMOVER
        // =========================

        JButton btnRemover =
                new JButton(" - Remover Aluno ");

        btnRemover.setBackground(
                new Color(220, 20, 60)
        );

        btnRemover.setForeground(Color.WHITE);

        btnRemover.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        btnRemover.setPreferredSize(
                new Dimension(450, 45)
        );

        // REMOVE O ITEM SELECIONADO

        btnRemover.addActionListener(e -> removerAlunoSelecionado());

        gbc.gridy = 3;

        gbc.insets =
                new Insets(5, 5, 5, 5);

        pnlCentral.add(btnRemover, gbc);

        add(pnlCentral, BorderLayout.CENTER);

        // =========================
        // BOTÃO VOLTAR
        // =========================

        JButton btnVoltarMenu =
                new JButton("Voltar ao Menu Principal");

        btnVoltarMenu.setPreferredSize(
                new Dimension(220, 38)
        );

        btnVoltarMenu.addActionListener(e -> {

            new MenuPrincipalView().setVisible(true);

            dispose();
        });

        JPanel pnlSul = new JPanel();

        pnlSul.setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        0,
                        15,
                        0
                )
        );

        pnlSul.add(btnVoltarMenu);

        add(pnlSul, BorderLayout.SOUTH);
    }

    // =========================
    // JANELA DE EDIÇÃO
    // =========================

    private void abrirJanelaAgendamento(
            AlunoEntity aluno
    ) {

        JDialog popup =
                new JDialog(
                        this,
                        "Editar Aluno: " + aluno.getNome(),
                        true
                );

        popup.setSize(450, 280);

        popup.setLocationRelativeTo(this);

        popup.setLayout(new GridBagLayout());

        GridBagConstraints c =
                new GridBagConstraints();

        c.insets =
                new Insets(8, 12, 8, 12);

        c.fill =
                GridBagConstraints.HORIZONTAL;

        // DATA

        c.gridx = 0;
        c.gridy = 0;

        popup.add(
                new JLabel("Data:"),
                c
        );

        JTextField txtData =
                new JTextField(12);

        if (aluno.getDataMatricula() != null) {

            txtData.setText(
                    aluno.getDataMatricula()
                            .format(formatador)
            );
        }

        c.gridx = 1;

        popup.add(txtData, c);

        // HORÁRIO

        c.gridx = 0;
        c.gridy = 1;

        popup.add(
                new JLabel("Horário:"),
                c
        );

        JTextField txtHorario =
                new JTextField(
                        aluno.getHorarioTreino() != null
                                ? aluno.getHorarioTreino()
                                : ""
                );

        c.gridx = 1;

        popup.add(txtHorario, c);

        // PERSONAL

        c.gridx = 0;
        c.gridy = 2;

        popup.add(
                new JLabel("Personal:"),
                c
        );

        JTextField txtPersonal =
                new JTextField(
                        aluno.getPersonalTrainer() != null
                                ? aluno.getPersonalTrainer()
                                : ""
                );

        c.gridx = 1;

        popup.add(txtPersonal, c);

        // BOTÃO SALVAR

        JButton btnSalvar =
                new JButton("Salvar");

        btnSalvar.setBackground(
                new Color(30, 144, 255)
        );

        btnSalvar.setForeground(Color.WHITE);

        btnSalvar.addActionListener(evt -> {

            try {

                aluno.setDataMatricula(
                        LocalDate.parse(
                                txtData.getText().trim(),
                                formatador
                        )
                );

                aluno.setHorarioTreino(
                        txtHorario.getText().trim()
                );

                aluno.setPersonalTrainer(
                        txtPersonal.getText()
                                .trim()
                                .toUpperCase()
                );

                // SALVA NO BANCO

                alunoService.salvar(aluno);

                JOptionPane.showMessageDialog(
                        popup,
                        "Dados salvos com sucesso!"
                );

                atualizarListaNaTela();

                popup.dispose();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        popup,
                        "Erro ao salvar:\n"
                                + ex.getMessage()
                );
            }
        });

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;

        popup.add(btnSalvar, c);

        popup.setVisible(true);
    }

    // =========================
    // FILTRO
    // =========================

    private void filtrar() {

        String texto =
                txtBusca.getText()
                        .toLowerCase();

        modeloLista.clear();

        for (AlunoEntity aluno : todosOsAlunos) {

            if (aluno.getNome()
                    .toLowerCase()
                    .contains(texto)) {

                modeloLista.addElement(
                        aluno.getNome()
                );
            }
        }
    }

    // =========================
    // OBTÉM ALUNO
    // =========================

    private AlunoEntity obterAlunoCorrespondente(
            int indexSelecionado
    ) {

        String nomeExibido =
                jListAlunos.getModel()
                        .getElementAt(indexSelecionado);

        for (AlunoEntity aluno : todosOsAlunos) {

            if (aluno.getNome()
                    .equals(nomeExibido)) {

                return aluno;
            }
        }

        return null;
    }

    // =========================
    // REMOVE ALUNO
    // =========================

    private void removerAlunoSelecionado() {

        int index =
                jListAlunos.getSelectedIndex();

        if (index == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um aluno na lista."
            );

            return;
        }

        AlunoEntity aluno =
                obterAlunoCorrespondente(index);

        if (aluno == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Aluno não encontrado."
            );

            return;
        }

        int confirm =
                JOptionPane.showConfirmDialog(
                        this,
                        "Deseja excluir "
                                + aluno.getNome()
                                + " permanentemente?",
                        "Confirmar Exclusão",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirm == JOptionPane.YES_OPTION) {

            try {

                // REMOVE DO BANCO

                alunoService.excluir(aluno.getId());

                JOptionPane.showMessageDialog(
                        this,
                        "Aluno removido com sucesso!"
                );

                atualizarListaNaTela();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Erro ao excluir:\n"
                                + ex.getMessage()
                );
            }
        }
    }

    // =========================
    // ATUALIZA LISTA
    // =========================

    public void atualizarListaNaTela() {

        modeloLista.clear();

        todosOsAlunos.clear();

        try {

            List<AlunoEntity> alunos =
                    alunoService.listarTodos();

            for (AlunoEntity aluno : alunos) {

                todosOsAlunos.add(aluno);

                modeloLista.addElement(
                        aluno.getNome()
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao conectar com o banco."
            );
        }
    }
}