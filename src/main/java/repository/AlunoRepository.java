package repository;

import config.HibernateUtil;
import entity.AlunoEntity;
import entity.StatusAluno;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class AlunoRepository implements Repositorio<AlunoEntity, Long> {

    public void salvar(AlunoEntity aluno) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(aluno);
            tx.commit();
        }
    }

    public Optional<AlunoEntity> buscarPorId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(AlunoEntity.class, id));
        }
    }

    public Optional<AlunoEntity> buscarPorCpf(String cpf) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM AlunoEntity WHERE cpf = :cpf", AlunoEntity.class)
                    .setParameter("cpf", cpf)
                    .uniqueResultOptional();
        }
    }

    // ARRUMADO: Agora traz apenas os alunos ATIVOS para a tabela da tela
    public List<AlunoEntity> listarTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM AlunoEntity WHERE status = :status", AlunoEntity.class)
                    .setParameter("status", StatusAluno.ATIVO)
                    .list();
        }
    }

    // ARRUMADO: Protegido contra sessões fechadas usando uma transação isolada e limpa
    public void atualizar(AlunoEntity aluno) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            // Faz o merge e commita na mesma sessão aberta
            session.merge(aluno);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Erro ao atualizar dados do aluno: " + e.getMessage());
        }
    }

    // ARRUMADO: Em vez de quebrar o banco com os agendamentos, fazemos a exclusão lógica perfeita
    public void deletar(Long id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            // 1. Busca o aluno dentro desta sessão ativa
            AlunoEntity aluno = session.get(AlunoEntity.class, id);

            if (aluno != null) {
                // 2. Altera o status dele para INATIVO
                aluno.setStatus(StatusAluno.INATIVO);

                // 3. Salva a alteração
                session.merge(aluno);

                tx.commit();
                System.out.println("Aluno inativado com sucesso.");
            } else {
                System.out.println("Aluno não encontrado para exclusão.");
                if (tx != null) tx.rollback();
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Erro ao tentar excluir o aluno:\n" + e.getMessage());
        }
    }
}