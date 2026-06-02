package telas;

import controller.AgendamentoController;
import controller.DisponibilidadeController;
import entity.AgendamentoEntity;
import entity.DisponibilidadeEntity;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AulasAgendadasView extends JFrame {

    private JTable tabelaAulas;
    private DefaultTableModel modeloTabela;

    // CONEXÃO MVC: Controladores injetados com segurança
    private final AgendamentoController agendamentoController = new AgendamentoController();
    private final DisponibilidadeController disponibilidadeController = new DisponibilidadeController();

    public AulasAgendadasView() {
        setTitle("Sistema Academia - Relatório de Aulas");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Cronograma de Aulas Marcadas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // COLUNAS
        String[] colunas = {"Aluno", "Data", "Horário", "Personal"};

        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaAulas = new JTable(modeloTabela);
        tabelaAulas.setRowHeight(25);
        tabelaAulas.setFont(new Font("SansSerif", Font.PLAIN, 14));

        carregarDadosDoBanco();

        // DUPLO CLIQUE PARA EDITAR
        tabelaAulas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int rowVisual = tabelaAulas.getSelectedRow();
                    if (rowVisual >= 0) {
                        int rowReal = tabelaAulas.convertRowIndexToModel(rowVisual);

                        // Buscando o agendamento correspondente à linha selecionada
                        AgendamentoEntity agendamento = agendamentoController.obterAgendamentoPorLinha(rowReal);
                        if (agendamento != null) {
                            abrirTelaEdicao(agendamento);
                        }
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabelaAulas);
        add(scroll, BorderLayout.CENTER);

        // ==========================================
        // PAINEL DE BOTÕES (SUL)
        // ==========================================
        JPanel painelSul = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton btnAtualizar = new JButton("Atualizar Lista");
        btnAtualizar.setPreferredSize(new Dimension(180, 35));
        btnAtualizar.setBackground(new Color(60, 179, 113));
        btnAtualizar.setForeground(Color.WHITE);
        btnAtualizar.setFont(new Font("Arial", Font.BOLD, 13));
        btnAtualizar.addActionListener(e -> {
            carregarDadosDoBanco();
            JOptionPane.showMessageDialog(this, "Lista de cronogramas atualizada!");
        });

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setPreferredSize(new Dimension(180, 35));
        btnVoltar.addActionListener(e -> {
            dispose();
            new MenuPrincipalView().setVisible(true);
        });

        painelSul.add(btnAtualizar);
        painelSul.add(btnVoltar);
        add(painelSul, BorderLayout.SOUTH);
    }

    // ==========================================
    // CARREGA DADOS USANDO O CONTROLLER (MVC)
    // ==========================================
    private void carregarDadosDoBanco() {
        try {
            modeloTabela.setRowCount(0); // Limpa as linhas da tabela visual

            // A View pede as linhas prontas e atualizadas para o Controller
            List<Object[]> linhasTabela = agendamentoController.obterDadosTabela();

            if (linhasTabela != null) {
                for (Object[] linha : linhasTabela) {
                    modeloTabela.addRow(linha); // Adiciona na tabela do Swing
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados:\n" + ex.getMessage());
        }
    }

    // ==========================================
    // TELA AUXILIAR PARA ATUALIZAR AGENDAMENTO
    // ==========================================
    private void abrirTelaEdicao(AgendamentoEntity agendamento) {
        JDialog dialog = new JDialog(this, "Editar Horário", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;

        DateTimeFormatter dataFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter horaFormat = DateTimeFormatter.ofPattern("HH:mm");

        JTextField txtData = new JTextField();
        JTextField txtHora = new JTextField();
        JTextField txtPersonal = new JTextField();

        if (agendamento.getDataHora() != null) {
            txtData.setText(agendamento.getDataHora().toLocalDate().format(dataFormat));
            txtHora.setText(agendamento.getDataHora().toLocalTime().format(horaFormat));
        }

        if (agendamento.getPersonal() != null) {
            txtPersonal.setText(agendamento.getPersonal());
        }

        c.gridx = 0; c.gridy = 0;
        dialog.add(new JLabel("Data:"), c);
        c.gridx = 1;
        dialog.add(txtData, c);

        c.gridx = 0; c.gridy = 1;
        dialog.add(new JLabel("Horário:"), c);
        c.gridx = 1;
        dialog.add(txtHora, c);

        c.gridx = 0; c.gridy = 2;
        dialog.add(new JLabel("Personal:"), c);
        c.gridx = 1;
        dialog.add(txtPersonal, c);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBackground(new Color(30, 144, 255));
        btnSalvar.setForeground(Color.WHITE);

        btnSalvar.addActionListener(e -> {
            try {
                LocalDate data = LocalDate.parse(txtData.getText().trim(), dataFormat);
                LocalTime hora = LocalTime.parse(txtHora.getText().trim(), horaFormat);
                LocalDateTime dataHora = LocalDateTime.of(data, hora);

                agendamento.setDataHora(dataHora);
                agendamento.setPersonal(txtPersonal.getText().trim());

                // Passa pelo controller para acionar as regras de validação e persistência!
                agendamentoController.salvarAgendamento(agendamento);

                // Integração inteligente da busca de horários do Personal
                if (agendamento.getAluno() != null && agendamento.getAluno().getId() != null) {
                    carregarHorariosDisponiveisDoPersonal(agendamento.getAluno().getId());
                }

                JOptionPane.showMessageDialog(dialog, "Horário alterado com sucesso!");
                dialog.dispose();

                // Recarrega a tabela automaticamente após salvar para refletir as mudanças
                carregarDadosDoBanco();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro ao salvar alterações:\n" + ex.getMessage());
            }
        });

        c.gridx = 0; c.gridy = 3;
        c.gridwidth = 2;
        dialog.add(btnSalvar, c);

        dialog.setVisible(true);
    }

    /**
     * Método utilitário acionado internamente para carregar as disponibilidades em lote
     * do Personal Trainer associado, fazendo ponte direta com o DisponibilidadeController.
     */
    private List<DisponibilidadeEntity> carregarHorariosDisponiveisDoPersonal(Long idPersonal) {
        try {
            if (idPersonal != null) {
                return disponibilidadeController.listarDisponibilidadesPorPersonal(idPersonal);
            }
        } catch (Exception e) {
            System.err.println("Aviso técnico: Não foi possível obter as disponibilidades: " + e.getMessage());
        }
        return null;
    }
}