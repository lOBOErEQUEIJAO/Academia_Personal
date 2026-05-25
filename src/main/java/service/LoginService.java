package service;

import entity.AlunoEntity;
import entity.PersonalEntity;
import repository.AlunoRepository;
import repository.PersonalRepository;

import java.util.Optional;

public class LoginService {

    private AlunoRepository alunoRepository = new AlunoRepository();
    PersonalRepository personalRepository = new PersonalRepository();

    public boolean autenticar(String cpf, String senha) {
        // 1. Validação básica: evita chamadas desnecessárias ao banco
        if (cpf == null || cpf.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
            return false;
        }

        String cpfLimpo = cpf.trim();

        try {
            // 2. Busca o aluno pelo CPF
            Optional<AlunoEntity> alunoEncontrado = alunoRepository.buscarPorCpf(cpfLimpo);

            // 3. Verifica se o aluno existe
            if (alunoEncontrado.isPresent()) {
                AlunoEntity aluno = alunoEncontrado.get();

                // 4. IMPORTANTE: Comparar a senha digitada com a senha do banco
                if (aluno.getSenha().equals(senha)) {
                    return true; // Login autorizado
                }
            }
            Optional<PersonalEntity> personalEncontrado = personalRepository.buscarPorCpf(cpfLimpo);
            if (personalEncontrado.isPresent()) {
                PersonalEntity personal = personalEncontrado.get();
                if (personal.getSenha().equals(senha)) {
                    return true; // Login autorizado
                }
            }

        } catch (Exception e) {
            // Log de erro caso o banco falhe
            System.err.println("Erro técnico ao acessar o banco: " + e.getMessage());
        }

        return false; // Se chegou aqui, algo falhou (CPF não existe ou senha errada)
    }
}