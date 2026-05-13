package telas;

import entity.AgendamentoEntity;
import service.AgendamentoService;
import javax.swing.*;
import java.util.List;

public class BtnAulasAgendadas {

    public void mostrarAulas(JFrame tela) {

        try {
            AgendamentoService agendamentoService = new AgendamentoService();

            List<AgendamentoEntity> lista = agendamentoService.listarTodos();

            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(tela, "Nenhuma aula agendada.");
                return;
            }

            StringBuilder texto = new StringBuilder();

            for (AgendamentoEntity ag : lista) {
                texto.append(  "Aluno: "
                                + ag.getAluno().getNome()
                                + "\nPersonal: "
                                + ag.getDisponibilidade()
                                .getPersonal()
                                .getNome()
                                + "\nData: "
                                + ag.getDisponibilidade()
                                .getData()
                                + "\nHora: "
                                + ag.getDisponibilidade()
                                .getHoraInicio()
                                + "\nStatus: "
                                + ag.getStatus()
                                + "\n");
            }

            JTextArea area = new JTextArea(texto.toString());
            area.setEditable(false);
            JScrollPane scroll = new JScrollPane(area);

            JOptionPane.showMessageDialog(tela, scroll,
                    "Aulas Agendadas",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(tela,"Erro: " + ex.getMessage());
        }
    }
}