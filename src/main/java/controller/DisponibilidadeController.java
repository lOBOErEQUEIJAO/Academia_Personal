package controller;

import entity.DisponibilidadeEntity;
import service.DisponibilidadeService;
import java.util.List;

public class DisponibilidadeController {

    private final DisponibilidadeService disponibilidadeService;

    public DisponibilidadeController() {
        this.disponibilidadeService = new DisponibilidadeService();
    }

    /**
     * Recebe os dados de disponibilidade da View, valida as regras básicas
     * e encaminha para o Service salvar no banco.
     */
    public void cadastrarNovaDisponibilidade(DisponibilidadeEntity disponibilidade) throws Exception {
        if (disponibilidade == null) {
            throw new Exception("Os dados da disponibilidade não podem estar vazios.");
        }

        // Repassa para a camada de serviço onde rodam as regras de data retroativa e horários
        disponibilidadeService.cadastrar(disponibilidade);
    }

    /**
     * Retorna todas as disponibilidades cadastradas.
     * Útil para carregar tabelas de consulta ou preencher filtros.
     */
    public List<DisponibilidadeEntity> listarTodasDisponibilidades() {
        return disponibilidadeService.listarTodos();
    }

    /**
     * Retorna as disponibilidades específicas de um Personal.
     * NOTA: Ficará cinza até ser chamado na tela de agendamento de aulas!
     */
    public List<DisponibilidadeEntity> listarDisponibilidadesPorPersonal(Long idPersonal) throws Exception {
        if (idPersonal == null) {
            throw new Exception("O ID do personal é inválido.");
        }
        return disponibilidadeService.listarPorPersonal(idPersonal);
    }
}