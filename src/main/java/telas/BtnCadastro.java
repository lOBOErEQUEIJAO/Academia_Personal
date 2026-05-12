package telas;

import entity.AlunoEntity;
import service.AlunoService;
import javax.swing.*;

public class BtnCadastro {
    public void cadastrar(JFrame tela) {
        try {
            String nome = JOptionPane.showInputDialog(tela,"Nome:");
            String cpf = JOptionPane.showInputDialog(tela,"CPF:");
            String dataNascimento = JOptionPane.showInputDialog(tela,"Data nascimento (AAAA-MM-DD):");
            String dataMatricula = JOptionPane.showInputDialog(tela,"Data matrícula (AAAA-MM-DD):");
            String senha =JOptionPane.showInputDialog(tela,"Senha:");
            AlunoEntity aluno = new AlunoEntity(nome, cpf, java.time.LocalDate.parse(dataNascimento), java.time.LocalDate.parse(dataMatricula));
            AlunoService alunoService = new AlunoService();
            alunoService.cadastrar(aluno, senha);

            JOptionPane.showMessageDialog(tela,"Aluno cadastrado!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog( tela,"Erro: " + ex.getMessage());
        }
    }
}