package controller;

import service.LoginService;
import request.LoginRequest;

public class LoginController {

    private final LoginService loginService;

    public LoginController() {
        this.loginService = new LoginService();
    }

    //Valida os campos usando o objeto LoginRequest e tenta realizar o login.

    public boolean autenticar(LoginRequest request) throws Exception {
        if (request == null) {
            throw new Exception("Dados de autenticação inválidos.");
        }

        // Buscando os dados de dentro do pacote "request"
        if (request.getUsuario() == null || request.getUsuario().trim().isEmpty()) {
            throw new Exception("O campo de usuário/CPF é obrigatório.");
        }
        if (request.getSenha() == null || request.getSenha().trim().isEmpty()) {
            throw new Exception("O campo de senha é obrigatório.");
        }

        // Passa os dados desembrulhados para o seu Service buscar no banco
        boolean logado = loginService.autenticar(request.getUsuario(), request.getSenha());

        if (!logado) {
            throw new Exception("Usuário ou senha incorretos.");
        }

        return true;
    }
}