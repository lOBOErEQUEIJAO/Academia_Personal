package telas;

import entity.PersonalEntity;
import service.PersonalService;
import javax.swing.*;

public class BtnCadastrarPersonal {

    public void cadastrar(JFrame tela) {

        try {
            String nome = JOptionPane.showInputDialog(tela,"Nome:");

            String cpf = JOptionPane.showInputDialog( tela,"CPF:");

            String cref = JOptionPane.showInputDialog(tela,"CREF:");

            String senha = JOptionPane.showInputDialog(tela,"Senha:");
            PersonalEntity personal = new PersonalEntity(nome,cpf, cref);

            PersonalService personalService = new PersonalService();
            personalService.cadastrar( personal, senha);

            JOptionPane.showMessageDialog(tela,"Personal cadastrado!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(tela,"Erro: " + ex.getMessage());
        }
    }
}