package repository;

import config.HibernateUtil;
import entity.UserEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class UserRepository implements Repositorio<UserEntity, Long> {

    public void salvar(UserEntity user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(user);
            tx.commit();
        }
    }

    public Optional<UserEntity> buscarPorId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(UserEntity.class, id));
        }
    }

    public List<UserEntity> listarTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM UserEntity", UserEntity.class).list();
        }
    }

    public Optional<UserEntity> buscarPorIdAluno(Long idAluno) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM UserEntity WHERE aluno.id = :idAluno", UserEntity.class)
                    .setParameter("idAluno", idAluno)
                    .uniqueResultOptional();
        }
    }

    public Optional<UserEntity> buscarPorIdPersonal(Long idPersonal) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM UserEntity WHERE personal.id = :idPersonal",
                            UserEntity.class)
                    .setParameter("idPersonal", idPersonal)
                    .uniqueResultOptional();
        }
    }

    public Optional<UserEntity> buscarPorLogin(String login) {

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                            "FROM UserEntity WHERE login = :login",
                            UserEntity.class
                    )
                    .setParameter("login", login)
                    .uniqueResultOptional();
        }
    }

    public void atualizar(UserEntity user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(user);
            tx.commit();
        }
    }

    public void deletar(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            UserEntity user = session.get(UserEntity.class, id);
            if (user != null) session.delete(user);
            tx.commit();
        }
    }
}
