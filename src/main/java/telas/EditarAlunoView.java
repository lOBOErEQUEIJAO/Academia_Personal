package telas;

import controller.AlunoController;
import entity.AlunoEntity;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EditarAlunoView extends JFrame {
    private JComboBox<String> comboHorario;
    private JButton btnSalvar, btnExcluir;
    private AlunoEntity alunoAtual;

    // CONEXÃO MVC: Injetando apenas o AlunoController
    private final AlunoController alunoController = new AlunoController();

    public EditarAlunoView(String nomeBusca) {
        try {
            // Chamada segura via Controller
            this.alunoAtual = alunoController.buscarPorNome(nomeBusca);

            if (this.alunoAtual == null) {
                throw new Exception("Aluno não encontrado no sistema.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar dados do aluno: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            this.dispose();
            return;
        }

        setTitle("Agendamento: " + alunoAtual.getNome());
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel painelCentral = new JPanel(new GridBagLayout());
        painelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        comboHorario = new JComboBox<>(gerarHorarios());
        if (alunoAtual.getHorarioTreino() != null) {
            comboHorario.setSelectedItem(alunoAtual.getHorarioTreino());
        }

        painelCentral.add(new JLabel("Horário: "));
        painelCentral.add(comboHorario);

        JPanel painelBotoes = new JPanel(new GridLayout(2, 1, 5, 5));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(0, 50, 20, 50));

        btnSalvar = new JButton("Salvar Horário");
        btnSalvar.setBackground(new Color(30, 144, 255));
        btnSalvar.setForeground(Color.WHITE);

        btnExcluir = new JButton("Remover Aluno");
        btnExcluir.setBackground(new Color(220, 20, 60));
        btnExcluir.setForeground(Color.WHITE);

        // EVENTO: Salvar Alterações passando pelo Controller
        btnSalvar.addActionListener(e -> {
            try {
                alunoAtual.setHorarioTreino((String) comboHorario.getSelectedItem());

                // Repassa a entidade atualizada para o Controller validar e salvar
                alunoController.cadastrarNovoAluno(alunoAtual);

                JOptionPane.showMessageDialog(this, "Horário atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(), "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            }
        });

        // EVENTO: Excluir Aluno passando estritamente pelo Controller
        btnExcluir.addActionListener(e -> {
            int confirmacao = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja realmente remover permanentemente o aluno " + alunoAtual.getNome() + "?",
                    "Confirmar Exclusão",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirmacao == JOptionPane.YES_OPTION) {
                try {
                    // Chamada correta do método do Controller
                    alunoController.excluirAluno(alunoAtual.getId());
                    JOptionPane.showMessageDialog(this, "Aluno removido com sucesso!");
                    this.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro técnico ao excluir: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnExcluir);

        add(painelCentral, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private String[] gerarHorarios() {
        List<String> h = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            h.add(String.format("%02d:00", i));
            h.add(String.format("%02d:30", i));
        }
        return h.toArray(new String[0]);
    }
}