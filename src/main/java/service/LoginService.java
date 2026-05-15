package service;

import entity.PersonalEntity; // Alterado de Aluno para Personal
import entity.UserEntity;
import java.util.Optional;

public class LoginService {

    // Agora usa o serviço do Personal
    private PersonalService personalService = new PersonalService();
    private UserService userService = new UserService();

    public boolean autenticar(String cpf, String senha) {

        // 1. Busca pelo CPF na tabela de Personal
        Optional<PersonalEntity> resultado = personalService.buscarPorCpf(cpf);

        if (resultado.isPresent()) {
            PersonalEntity personal = resultado.get();

            // 2. Busca o usuário vinculado ao ID do Personal
            // Certifique-se que o método na UserService se chama buscarPorIdPersonal
            Optional<UserEntity> usuario = userService.buscarPorIdPersonal(personal.getId());

            if (usuario.isPresent()) {
                // 3. Autentica a senha
                return userService.autenticar(usuario.get(), senha);
            }
        }

        return false;
    }
}