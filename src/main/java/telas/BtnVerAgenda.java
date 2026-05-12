package telas;

import entity.DisponibilidadeEntity;
import service.DisponibilidadeService;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BtnVerAgenda {
    public void visualizar(JFrame tela) {
        try {
            DisponibilidadeService disponibilidadeService = new DisponibilidadeService();

            List<DisponibilidadeEntity> lista = disponibilidadeService.listarTodos();
            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(tela,"Nenhuma disponibilidade cadastrada.");
                return;
            }

            StringBuilder agenda = new StringBuilder();
            for (DisponibilidadeEntity d : lista) {
                agenda.append( "ID: " + d.getId()
                                + "\nPersonal: "
                                + d.getPersonal().getNome()
                                + "\nData: "
                                + d.getData()
                                + "\nHorário: "
                                + d.getHoraInicio()
                                + " até "
                                + d.getHoraFim());
            }

            JTextArea area = new JTextArea(agenda.toString());
            area.setEditable(false);
            JScrollPane scroll = new JScrollPane(area);
            scroll.setPreferredSize(new Dimension(500, 300));
            JOptionPane.showMessageDialog( tela, scroll,"Agenda",JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(tela,"Erro: " + ex.getMessage());
        }
    }
}