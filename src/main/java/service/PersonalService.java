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

    PersonalRepository personalRepository = new PersonalRepository();

    public Optional<PersonalEntity> buscarPorCpf(String cpf) {
        return personalRepository.buscarPorCpf(cpf);
    }

    public Optional<PersonalEntity> buscarPorId(Long id) {
        return personalRepository.buscarPorId(id);
    }

    public List<PersonalEntity> listarTodos() {
        return personalRepository.listarTodos();
    }

    public void cadastrar(PersonalEntity personal, String senha) {
        Validador.validarNome(personal.getNome());
        Validador.validarCpf(personal.getCpf());
        Validador.validarCref(personal.getCref());
        Validador.validarTelefone(personal.getTelefone());
        Validador.validarEmail(personal.getEmail());
        Validador.validarSenha(senha);
        if (personalRepository.buscarPorCpf(personal.getCpf()).isPresent()) {
            throw new IllegalArgumentException("CPF " + personal.getCpf() + " ja esta cadastrado.");
        }
        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            Transaction tx = session.beginTransaction();

            session.save(personal);

            UserEntity user =
                    new UserEntity(senha, TipoUsuario.PERSONAL);

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
