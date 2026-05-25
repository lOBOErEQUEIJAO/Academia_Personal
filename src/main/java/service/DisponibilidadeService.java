package service;

import entity.DisponibilidadeEntity;
import repository.DisponibilidadeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class DisponibilidadeService {

    // Mantido o repositório correto para persistência via JPA/Hibernate
    private DisponibilidadeRepository disponibilidadeRepository = new DisponibilidadeRepository();

    //Valida as regras de negócio e cadastra a disponibilidade do personal no banco de dados.
    public void cadastrar(DisponibilidadeEntity disponibilidade) throws Exception {

        // 1. VALIDAÇÕES: Segurança contra campos nulos (Evita NullPointerException)
        if (disponibilidade.getPersonal() == null || disponibilidade.getPersonal().getNome() == null || disponibilidade.getPersonal().getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do personal é obrigatório!");
        }
        if (disponibilidade.getData() == null) {
            throw new IllegalArgumentException("A data da disponibilidade é obrigatória!");
        }
        if (disponibilidade.getHoraInicio() == null || disponibilidade.getHoraFim() == null) {
            throw new IllegalArgumentException("Os horários de início e fim são obrigatórios!");
        }

        // 2. REGRAS DE NEGÓCIO: Datas e Horários válidos
        if (disponibilidade.getData().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data da disponibilidade não pode ser no passado.");
        }
        if (disponibilidade.getHoraInicio().isAfter(disponibilidade.getHoraFim())) {
            throw new IllegalArgumentException("A hora de início não pode ser depois da hora de fim.");
        }

        // 3. PERSISTÊNCIA: Grava permanentemente no banco de dados
        disponibilidadeRepository.salvar(disponibilidade);
        System.out.println("Disponibilidade cadastrada com sucesso via Hibernate!");
    }

    //  MÉTODOS DE BUSCA E CONSULTA

    public Optional<DisponibilidadeEntity> buscarPorId(Long id) {
        return disponibilidadeRepository.buscarPorId(id);
    }

    public List<DisponibilidadeEntity> listarTodos() {
        return disponibilidadeRepository.listarTodos();
    }

    public List<DisponibilidadeEntity> listarPorPersonal(Long idPersonal) {
        return disponibilidadeRepository.listarPorPersonal(idPersonal);
    }
}