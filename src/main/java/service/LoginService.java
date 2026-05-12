package service;

import entity.AlunoEntity;
import entity.UserEntity;

import java.util.Optional;

public class LoginService {

    private AlunoService alunoService =
            new AlunoService();

    private UserService userService =
            new UserService();

    public boolean autenticar(String cpf, String senha) {

        Optional<AlunoEntity> resultado =
                alunoService.buscarPorCpf(cpf);

        if (resultado.isPresent()) {

            AlunoEntity aluno =
                    resultado.get();

            Optional<UserEntity> usuario =
                    userService.buscarPorIdAluno(
                            aluno.getId()
                    );

            if (usuario.isPresent()) {

                return userService.autenticar(
                        usuario.get(),
                        senha
                );
            }
        }

        return false;
    }
}