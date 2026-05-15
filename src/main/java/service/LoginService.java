package service;

import entity.UserEntity;

import java.util.Optional;

public class LoginService {

    private UserService userService = new UserService();

    public boolean autenticar(String login, String senha) {

        Optional<UserEntity> usuario =
                userService.buscarPorLogin(login);

        if (usuario.isPresent()) {
            return userService.autenticar(usuario.get(), senha);
        }

        return false;
    }
}