package service;

import config.HibernateUtil;
import entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repository.AlunoRepository;
import java.util.List;
import java.util.Optional;

public class AlunoService {
    private AlunoRepository alunoRepository = new AlunoRepository();

    // --- MÉTODOS DE BUSCA ---

    public Optional<AlunoEntity> buscarPorId(Long id) {
        return alunoRepository.buscarPorId(id);
    }

    public Optional<AlunoEntity> buscarPorCpf(String cpf) {
        return alunoRepository.buscarPorCpf(cpf);
    }

    public AlunoEntity buscarPorNome(String nome) {
        return alunoRepository.listarTodos().stream()
                .filter(a -> a.getNome().equalsIgnoreCase(nome.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado: " + nome));
    }

    public List<AlunoEntity> listarTodos() {
        return alunoRepository.listarTodos();
    }

    // --- MÉTODOS DE PERSISTÊNCIA ---

    public void cadastrar(AlunoEntity aluno, String senha) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.save(aluno);

                UserEntity user = new UserEntity();
                user.setSenha(senha);
                user.setTipo(TipoUsuario.ALUNO);
                user.setAluno(aluno);
                user.setLogin(aluno.getCpf());
                user.setStatus(StatusUser.ATIVO);

                session.save(user);
                tx.commit();
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                throw e;
            }
        }
    }

    public void atualizar(AlunoEntity aluno) {
        alunoRepository.atualizar(aluno);
    }

    // --- MÉTODOS DE CANCELAMENTO E EXCLUSÃO ---

    /**
     * Resolve o erro: cannot find symbol cancelar
     * Apenas desativa o aluno (muda o status)
     */
    public void cancelar(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            AlunoEntity aluno = session.get(AlunoEntity.class, id);
            if (aluno != null) {
                aluno.setStatus(StatusAluno.INATIVO); // Certifique-se que StatusAluno.INATIVO existe
                session.update(aluno);
                tx.commit();
            } else {
                throw new IllegalArgumentException("Aluno não encontrado.");
            }
        }
    }

    /**
     * Resolve o erro: cannot find symbol deletar
     * Chamado pela Visualizacao para exclusão lógica ou total
     */
    public void deletar(Long id) {
        // Aqui você pode decidir se deleta do banco ou apenas desativa.
        // Vou chamar o excluir que você já tem para apagar do banco:
        this.excluir(id);
    }

    /**
     * Exclui o aluno e o usuário vinculado do banco de dados.
     */
    public void excluir(Long id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            AlunoEntity aluno = session.get(AlunoEntity.class, id);

            if (aluno != null) {
                session.createQuery("DELETE FROM UserEntity u WHERE u.aluno = :aluno")
                        .setParameter("aluno", aluno)
                        .executeUpdate();

                session.createQuery("DELETE FROM AlunoEntity a WHERE a.id = :id")
                        .setParameter("id", id)
                        .executeUpdate();

                tx.commit();
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Erro ao excluir: " + e.getMessage());
        }
    }

    public void salvar(AlunoEntity aluno) {
        alunoRepository.atualizar(aluno);
    }
}