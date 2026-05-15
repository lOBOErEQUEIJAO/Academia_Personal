package telas;

import entity.DisponibilidadeEntity;
import entity.PersonalEntity;
import service.DisponibilidadeService;
import service.PersonalService;
import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public class BtnDisponibilidade {

    public void cadastrar(JFrame tela) {

        try {
            String idPersonal = JOptionPane.showInputDialog(tela,"ID do personal:");

            String data =JOptionPane.showInputDialog(tela, "Data (AAAA-MM-DD):");
            String inicio = JOptionPane.showInputDialog(tela,"Hora início (HH:mm):");
            String fim = JOptionPane.showInputDialog(tela,"Hora fim (HH:mm):");

            PersonalService personalService = new PersonalService();

            DisponibilidadeService disponibilidadeService = new DisponibilidadeService();

            Optional<PersonalEntity> personal = personalService.buscarPorId(Long.parseLong(idPersonal));
            if (personal.isEmpty()) {
                JOptionPane.showMessageDialog( tela,"Personal não encontrado.");
                return;
            }
            DisponibilidadeEntity disponibilidade = new DisponibilidadeEntity(personal.get(), LocalDate.parse(data), LocalTime.parse(inicio), LocalTime.parse(fim));

            disponibilidadeService.cadastrar(disponibilidade);

            JOptionPane.showMessageDialog(tela, "Disponibilidade cadastrada!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(tela,"Erro: " + ex.getMessage()
            );
        }
    }
}