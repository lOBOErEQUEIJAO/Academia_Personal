package controller;

import entity.AgendamentoEntity;
import java.time.LocalDateTime;

public class ValidadorAgendamento {

    /**Valida todas as regras de negócio de um agendamento.
     */
    public void validar(AgendamentoEntity agendamento) throws Exception {
        if (agendamento == null) {
            throw new Exception("O agendamento não pode ser nulo.");
        }

        // 1. Validação de Data e Hora
        if (agendamento.getDataHora() == null) {
            throw new Exception("A data e o horário do agendamento são obrigatórios.");
        }

        if (agendamento.getDataHora().isBefore(LocalDateTime.now())) {
            throw new Exception("Não é possível agendar uma aula em uma data ou horário passado.");
        }

        // 2. Validação do Aluno
        if (agendamento.getAluno() == null) {
            throw new Exception("É necessário vincular um aluno ao agendamento.");
        }

        // 3. Validação do Personal Trainer
        if (agendamento.getPersonal() == null || agendamento.getPersonal().trim().isEmpty()) {
            throw new Exception("O nome do Personal Trainer é obrigatório.");
        }

        if (agendamento.getPersonal().trim().length() < 3) {
            throw new Exception("O nome do Personal Trainer deve ter pelo menos 3 caracteres.");
        }
    }
}