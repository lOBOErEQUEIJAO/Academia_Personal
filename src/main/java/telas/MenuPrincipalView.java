package telas;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipalView extends JFrame {

    private JButton btnSair;
    private JButton btnCadastro;
    private JButton btnAgendar;
    private JButton btnDisponibilidade;
    private JButton btnCadastrarPersonal;
    private JButton btnVerAgenda;
    private JButton btnAulasAgendadas;

    public MenuPrincipalView() {

        setTitle("Sistema Academia - Menu Principal");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        btnCadastro = new JButton("Cadastrar Aluno");
        btnAgendar = new JButton("Agendar Aula");
        btnDisponibilidade = new JButton("Cadastrar Disponibilidade");
        btnCadastrarPersonal = new JButton("Cadastrar Personal");
        btnVerAgenda = new JButton("Ver Agenda");
        btnAulasAgendadas = new JButton("Aulas Agendadas");
        btnSair = new JButton("Sair");

        add(btnCadastro);
        add(btnAgendar);
        add(btnDisponibilidade);
        add(btnCadastrarPersonal);
        add(btnVerAgenda);
        add(btnAulasAgendadas);
        add(btnSair);

        btnCadastro.addActionListener(e -> {
            BtnCadastro cadastro = new BtnCadastro();
            cadastro.cadastrar(this);
        });

        btnAgendar.addActionListener(e -> {
            BtnAgendar agendar =new BtnAgendar();
            agendar.agendar(this);
        });

        btnDisponibilidade.addActionListener(e -> {
            BtnDisponibilidade disponibilidade =  new BtnDisponibilidade();
            disponibilidade.cadastrar(this);
        });

        btnCadastrarPersonal.addActionListener(e -> {
            BtnCadastrarPersonal personal =new BtnCadastrarPersonal();
            personal.cadastrar(this);
        });

        btnVerAgenda.addActionListener(e -> {
            BtnVerAgenda agenda = new BtnVerAgenda();
            agenda.visualizar(this);
        });

        btnAulasAgendadas.addActionListener(e -> {
            BtnAulasAgendadas aulas = new BtnAulasAgendadas();
            aulas.mostrarAulas(this);
        });

        btnSair.addActionListener(e -> {
            new LoginView().setVisible(true);
            dispose();
        });

        setVisible(true);
    }
}