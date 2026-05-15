package service;

import entity.StatusUser;
import entity.UserEntity;
import repository.UserRepository;

import java.util.Optional;

public class UserService {

    UserRepository userRepository = new UserRepository();

    public void cadastrar(UserEntity user) {
        userRepository.salvar(user);
    }

    public Optional<UserEntity> buscarPorIdAluno(Long idAluno) {
        return userRepository.buscarPorIdAluno(idAluno);
    }

    public Optional<UserEntity> buscarPorLogin(String login) {
        return userRepository.buscarPorLogin(login);
    }

    public Optional<UserEntity> buscarPorIdPersonal(Long idPersonal) {
        return userRepository.buscarPorIdPersonal(idPersonal);
    }

    public boolean autenticar(UserEntity user, String senha) {
        if (user.getStatus() != StatusUser.ATIVO) {
            throw new IllegalArgumentException("Acesso bloqueado.");
        }
        return user.getSenha().equals(senha);
    }
}
