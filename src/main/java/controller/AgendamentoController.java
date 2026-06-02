package controller;

import entity.AgendamentoEntity;
import entity.StatusAluno;
import service.AgendamentoService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AgendamentoController {

    private final AgendamentoService agendamentoService;
    private final List<AgendamentoEntity> listaAgendamentosFiltrados;

    // JUNTANDO O VALIDADOR: O controller agora possui uma instância dele nos bastidores
    private final ValidadorAgendamento validador;

    public AgendamentoController() {
        this.agendamentoService = new AgendamentoService();
        this.listaAgendamentosFiltrados = new ArrayList<>();
        this.validador = new ValidadorAgendamento(); // Inicializa o validador
    }

    /**Busca os dados do banco, aplica os filtros de negócio e retorna para a View.
     */
    public List<Object[]> obterDadosTabela() throws Exception {
        List<AgendamentoEntity> todosAgendamentos = agendamentoService.listarTodos();
        listaAgendamentosFiltrados.clear();

        DateTimeFormatter dataFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter horaFormat = DateTimeFormatter.ofPattern("HH:mm");

        List<Object[]> linhas = new ArrayList<>();

        if (todosAgendamentos != null) {
            for (AgendamentoEntity agendamento : todosAgendamentos) {
                if (agendamento.getAluno() != null && agendamento.getAluno().getStatus() == StatusAluno.INATIVO) {
                    continue;
                }

                listaAgendamentosFiltrados.add(agendamento);

                String aluno = (agendamento.getAluno() != null) ? agendamento.getAluno().getNome() : "";
                String data = "";
                String hora = "";

                if (agendamento.getDataHora() != null) {
                    data = agendamento.getDataHora().toLocalDate().format(dataFormat);
                    hora = agendamento.getDataHora().toLocalTime().format(horaFormat);
                }

                String personal = (agendamento.getPersonal() != null) ? agendamento.getPersonal() : "";

                linhas.add(new Object[]{aluno, data, hora, personal});
            }
        }
        return linhas;
    }

    /** Retorna o agendamento correto baseado na linha real selecionada.
     */
    public AgendamentoEntity obterAgendamentoPorLinha(int linhaReal) {
        if (linhaReal >= 0 && linhaReal < listaAgendamentosFiltrados.size()) {
            return listaAgendamentosFiltrados.get(linhaReal);
        }
        return null;
    }

    /** Junta a validação antes de repassar para a camada de serviço salvar.
     */
    public void salvarAgendamento(AgendamentoEntity agendamento) throws Exception {
        // Primeiro, o validador confere as regras. Se falhar, ele lança a exceção aqui mesmo!
        System.out.printf("o controller foi acionado");
        validador.validar(agendamento);

        // Se passar por todas as regras sem erros, o serviço salva no banco normalmente
        agendamentoService.salvar(agendamento);
    }
    // Adicione este método dentro da sua classe AgendamentoController existente

    public void agendarAula(AgendamentoEntity agendamento) throws Exception {
        // Aqui você pode acionar seu validador se necessário:
         validador.validar(agendamento);

        agendamentoService.agendar(agendamento);
    }
}