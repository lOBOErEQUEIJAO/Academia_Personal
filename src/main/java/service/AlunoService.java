package service;

import entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import config.HibernateUtil;
import repository.AlunoRepository;
import java.util.List;
import java.util.Optional;

public class AlunoService {

    private final AlunoRepository alunoRepository = new AlunoRepository();

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
                // Define o status inicial do aluno como ATIVO ao cadastrar
                aluno.setStatus(StatusAluno.ATIVO);
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

    public void salvar(AlunoEntity aluno) {
        alunoRepository.atualizar(aluno);
    }

    // --- MÉTODOS DE CANCELAMENTO E EXCLUSÃO (ARRUMADOS) ---

    /**
     * Faz a exclusão lógica do aluno e desativa seu usuário de acesso.
     */
    public void excluir(Long id) {
        // 1. Desativa o login/usuário do aluno no banco de dados primeiro
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            // Localiza o usuário vinculado ao aluno para também inativá-lo
            session.createQuery("UPDATE UserEntity u SET u.status = :statusInativo WHERE u.aluno.id = :idAluno")
                    .setParameter("statusInativo", StatusUser.INATIVO) // Ajuste o enum se o seu for diferente de INATIVO
                    .setParameter("idAluno", id)
                    .executeUpdate();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println("Aviso: Não foi possível atualizar o status do usuário vinculado: " + e.getMessage());
        }

        // 2. Encaminha para o repositório fazer a inativação do registro do aluno com segurança
        alunoRepository.deletar(id);
    }

    /**
     * Redireciona para a mesma lógica centralizada de exclusão segura.
     */
    public void deletar(Long id) {
        this.excluir(id);
    }

    /**
     * Redireciona para a mesma lógica centralizada de exclusão segura.
     */
    public void cancelar(Long id) {
        this.excluir(id);
    }
}