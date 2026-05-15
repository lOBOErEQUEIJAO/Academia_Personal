package telas;

import entity.AlunoEntity;
import service.AlunoService;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EditarAlunoView extends JFrame {
    private JComboBox<String> comboHorario;
    private JButton btnSalvar, btnExcluir;
    private AlunoEntity alunoAtual;
    private AlunoService service = new AlunoService();

    public EditarAlunoView(String nomeBusca) {
        try {
            this.alunoAtual = service.buscarPorNome(nomeBusca);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
            this.dispose();
            return;
        }

        setTitle("Agendamento: " + alunoAtual.getNome());
        setSize(400, 300);
        setLocationRelativeTo(null);
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


        btnExcluir = new JButton("Remover Aluno");
        btnExcluir.setBackground(new Color(220, 20, 60));


        btnSalvar.addActionListener(e -> {
            try {
                alunoAtual.setHorarioTreino((String) comboHorario.getSelectedItem());
                service.atualizar(alunoAtual);
                JOptionPane.showMessageDialog(this, "Salvo!");
                this.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        btnExcluir.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Excluir?") == JOptionPane.YES_OPTION) {
                service.excluir(alunoAtual.getId());
                this.dispose();
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