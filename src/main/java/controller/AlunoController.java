package controller;

import entity.AlunoEntity;
import service.AlunoService;
import java.util.List;

public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController() {
        this.alunoService = new AlunoService();
    }

    /**
     * Recebe um novo aluno da tela de cadastro e repassa para salvar.
     */
    public void cadastrarNovoAluno(AlunoEntity aluno) throws Exception {
        if (aluno.getNome() == null || aluno.getNome().trim().isEmpty()) {
            throw new Exception("O nome do aluno é obrigatório.");
        }
        alunoService.salvar(aluno);
    }

    /**
     * Busca um aluno específico pelo nome.
     */
    public AlunoEntity buscarPorNome(String nome) throws Exception {
        if (nome == null || nome.trim().isEmpty()) {
            throw new Exception("O nome para busca não pode estar vazio.");
        }
        return alunoService.buscarPorNome(nome);
    }

    /**
     * Retorna a lista pura de entidades de alunos para preenchimento de componentes JList.
     * UTILIZADO POR: GestaoAlunosView para carregar a lista na tela.
     */
    public List<AlunoEntity> listarTodosAlunos() throws Exception {
        return alunoService.listarTodos();
    }

    /**
     * Remove permanentemente um aluno do sistema pelo ID.
     * Faz a ponte segura entre a View de Edição e a camada de Service.
     */
    public void excluirAluno(Long id) throws Exception {
        if (id == null) {
            throw new Exception("O ID do aluno fornecido é inválido para exclusão.");
        }
        alunoService.excluir(id);
    }
}