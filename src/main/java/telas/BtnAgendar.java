package telas;

import entity.AlunoEntity;
import entity.AgendamentoEntity;
import entity.DisponibilidadeEntity;
import service.AlunoService;
import service.AgendamentoService;
import service.DisponibilidadeService;
import javax.swing.*;
import java.util.List;
import java.util.Optional;

public class BtnAgendar {

    public void agendar(JFrame tela) {
        try {
            AlunoService alunoService = new AlunoService();
            DisponibilidadeService disponibilidadeService = new DisponibilidadeService();
            AgendamentoService agendamentoService = new AgendamentoService();
            String cpf = JOptionPane.showInputDialog(tela,"CPF do aluno:");
            Optional<AlunoEntity> alunoBuscado = alunoService.buscarPorCpf(cpf);
            if (alunoBuscado.isEmpty()) {
                JOptionPane.showMessageDialog(tela,"Aluno não encontrado.");
                return;
            }

            AlunoEntity aluno = alunoBuscado.get();
            List<DisponibilidadeEntity> lista = disponibilidadeService.listarTodos();
            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(tela,"Nenhuma aula disponível.");
                return;
            }
            StringBuilder horarios = new StringBuilder();
            for (DisponibilidadeEntity d : lista) {
                horarios.append( "ID: "
                                + d.getId()
                                + " | "
                                + d.getData()
                                + " | "
                                + d.getHoraInicio());
            }

            String idTexto = JOptionPane.showInputDialog(tela,horarios + "Digite o ID da aula:");

            Long id = Long.parseLong(idTexto);

            Optional<DisponibilidadeEntity> disp = disponibilidadeService.buscarPorId(id);
            if (disp.isEmpty()) {
                JOptionPane.showMessageDialog(tela,"Horário não encontrado.");
                return;
            }
            AgendamentoEntity agendamento = new AgendamentoEntity(aluno, disp.get(),null);

            agendamentoService.agendar(agendamento);
            JOptionPane.showMessageDialog(tela,"Agendamento realizado!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(tela,"Erro: " + ex.getMessage()
            );
        }
    }
}