package service;

import config.HibernateUtil;
import entity.PersonalEntity;
import entity.TipoUsuario;
import entity.UserEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repository.PersonalRepository;

import java.util.List;
import java.util.Optional;

public class PersonalService {

    private PersonalRepository personalRepository = new PersonalRepository();

    public Optional<PersonalEntity> buscarPorCpf(String cpf) {
        return personalRepository.buscarPorCpf(cpf);
    }

    public Optional<PersonalEntity> buscarPorId(Long id) {
        return personalRepository.buscarPorId(id);
    }

    public List<PersonalEntity> listarTodos() {
        return personalRepository.listarTodos();
    }

    // Busca por nome para alimentar a View de Disponibilidade
    public PersonalEntity buscarPorNome(String nome) {
        return personalRepository.listarTodos().stream()
                .filter(p -> p.getNome().equalsIgnoreCase(nome.trim()))
                .findFirst()
                .orElse(null);
    }

    public void cadastrar(PersonalEntity personal, String senha) {
        // Validações
        Validador.validarNome(personal.getNome());
        Validador.validarCpf(personal.getCpf());
        Validador.validarCref(personal.getCref());
        Validador.validarTelefone(personal.getTelefone());
        Validador.validarEmail(personal.getEmail());
        Validador.validarSenha(senha);

        if (personalRepository.buscarPorCpf(personal.getCpf()).isPresent()) {
            throw new IllegalArgumentException("CPF " + personal.getCpf() + " ja esta cadastrado.");
        }

        // ✨ GARANTIA DE SEGURANÇA: Injeta a senha no personal antes de abrir a sessão do banco
        personal.setSenha(senha);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            // Agora sim! O Hibernate sabe qual é a senha e aceita salvar sem reclamar
            session.save(personal);

            UserEntity user = new UserEntity(senha, TipoUsuario.PERSONAL);
            user.setLogin(personal.getCpf());
            user.setPersonal(personal);

            session.save(user);

            tx.commit();
        }
    }

    public boolean existeAlgumPersonal() {
        return personalRepository.existeAlgumPersonal();
    }
}