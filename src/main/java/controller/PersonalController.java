package controller;

import entity.PersonalEntity;
import service.PersonalService;

public class PersonalController {

    private final PersonalService personalService;

    public PersonalController() {
        this.personalService = new PersonalService();
    }

    /**
     * Valida os dados da tela e repassa para o modo 'cadastrar' no Service.
     * Recebemos a senha que veio do campo de texto da View.
     */
    public void cadastrarNovoPersonal(PersonalEntity personal, String senha) throws Exception {
        // Validações básicas antes de mandar para o Service
        if (personal.getNome() == null || personal.getNome().trim().isEmpty()) {
            throw new Exception("O nome do personal é obrigatório.");
        }

        if (personal.getCref() == null || personal.getCref().trim().isEmpty()) {
            throw new Exception("O número do CREF é obrigatório.");
        }

        if (senha == null || senha.trim().isEmpty()) {
            throw new Exception("A senha de acesso do personal é obrigatória.");
        }

        // Agora chama 'cadastrar' (que é o nome real no seu Service) e passa a senha junto!
        personalService.cadastrar(personal, senha);
    }

    /**
     * Faz a ponte do MVC para buscar o Personal pelo nome com segurança.
     * Utilizado para vincular horários de disponibilidade a um personal existente.
     */
    public PersonalEntity buscarPorNome(String nome) throws Exception {
        if (nome == null || nome.trim().isEmpty()) {
            throw new Exception("O nome do personal não pode estar vazio.");
        }
        return personalService.buscarPorNome(nome);
    }
}