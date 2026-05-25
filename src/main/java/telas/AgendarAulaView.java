package telas;

import entity.AgendamentoEntity;
import entity.AlunoEntity;
import entity.DisponibilidadeEntity;
import service.AgendamentoService;
import service.AlunoService;
import service.DisponibilidadeService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AgendarAulaView extends javax.swing.JFrame {
    private JTextField txtNomeAluno, txtData, txtHorario, txtPersonal;
    private JButton btnSalvar, btnCancelar;

    public AgendarAulaView() {
        setTitle("Sistema Academia - Agendar Aula");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel painelCampos = new JPanel(new GridLayout(4, 2, 10, 15));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(25, 25, 10, 25));

        painelCampos.add(new JLabel("Nome do Aluno:"));
        txtNomeAluno = new JTextField();
        painelCampos.add(txtNomeAluno);

        painelCampos.add(new JLabel("Data (DD/MM/AAAA):"));
        txtData = new JTextField();
        painelCampos.add(txtData);

        painelCampos.add(new JLabel("Horário (HH:MM):"));
        txtHorario = new JTextField();
        painelCampos.add(txtHorario);

        painelCampos.add(new JLabel("Personal Trainer:"));
        txtPersonal = new JTextField();
        painelCampos.add(txtPersonal);

        JPanel painelBotoes = new JPanel(new GridLayout(2, 1, 5, 5));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(0, 25, 20, 25));

        btnCancelar = new JButton("Cancelar e Voltar");
        btnCancelar.setBackground(new Color(180, 180, 180));
        btnCancelar.addActionListener(e -> {
            this.dispose();
            new MenuPrincipalView().setVisible(true);
        });

        btnSalvar = new JButton("Confirmar Agendamento");
        btnSalvar.setBackground(new Color(34, 139, 34));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.addActionListener(e -> executarAgendamento());

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnSalvar);

        add(painelCampos, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(btnSalvar);
    }

    private void executarAgendamento() {
        try {
            // Captura dinâmica dos dados digitados na tela pelo usuário
            String nomeDigitado = txtNomeAluno.getText().trim();
            String dataDigitada = txtData.getText().trim();
            String horaDigitada = txtHorario.getText().trim();
            String personalDigitado = txtPersonal.getText().trim();

            if (nomeDigitado.isEmpty() || dataDigitada.isEmpty() || horaDigitada.isEmpty() || personalDigitado.isEmpty()) {
                throw new IllegalArgumentException("Por favor, preencha todos os campos do formulário!");
            }

            // 1. Busca o aluno de maneira segura pelo service do Banco
            AlunoService alunoService = new AlunoService();
            AlunoEntity alunoEncontrado = alunoService.buscarPorNome(nomeDigitado);
            if (alunoEncontrado == null) {
                throw new IllegalArgumentException("O aluno '" + nomeDigitado + "' não foi encontrado!");
            }

            // Formatadores para ler o texto da tela
            DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter formatadorHora = DateTimeFormatter.ofPattern("HH:mm");

            // CONVERSÃO DINÂMICA: Transforma o que foi digitado em objetos de tempo do Java
            LocalDate data = LocalDate.parse(dataDigitada, formatadorData);
            LocalTime hora = LocalTime.parse(horaDigitada, formatadorHora);
            LocalDateTime dataHoraDoAgendamento = LocalDateTime.of(data, hora);

            // 2. Localiza a disponibilidade correspondente do Personal cadastrado
            DisponibilidadeService dispService = new DisponibilidadeService();
            DisponibilidadeEntity disponibilidadeEncontrada = dispService.listarTodos().stream()
                    .filter(d -> d.getData().equals(data)
                            && d.getPersonal() != null
                            && d.getPersonal().getNome().equalsIgnoreCase(personalDigitado)
                            && !hora.isBefore(d.getHoraInicio()) && !hora.isAfter(d.getHoraFim()))
                    .findFirst()
                    .orElse(null);

            if (disponibilidadeEncontrada == null) {
                throw new IllegalArgumentException("Não há disponibilidade cadastrada para o personal " + personalDigitado + " nesta data/horário!");
            }

            // 3. Cria a nova entidade mapeada que vai virar uma linha na tabela 'agendamentos'
            AgendamentoEntity agendamento = new AgendamentoEntity();

            // Sincroniza os objetos relacionais (Essencial para o Hibernate salvar as FKs id_aluno e id_disponibilidade)
            agendamento.setAluno(alunoEncontrado);
            agendamento.setDisponibilidade(disponibilidadeEncontrada);

            // GRAVAÇÃO DINÂMICA: Seta a data e hora exatas criadas a partir dos campos da tela
            agendamento.setDataHora(dataHoraDoAgendamento);
            agendamento.setStatus(entity.StatusAgendamento.AGENDADO);

            // Alimenta os campos auxiliares/observações sem destruir o objeto principal
            agendamento.setNomeAluno(alunoEncontrado.getNome());
            agendamento.setData(dataDigitada);
            agendamento.setHorario(horaDigitada);
            agendamento.setPersonal(disponibilidadeEncontrada.getPersonal().getNome());

            // 4. Salva de vez no banco de dados através do Service
            AgendamentoService service = new AgendamentoService();
            service.agendar(agendamento);

            JOptionPane.showMessageDialog(this, "Aula agendada com sucesso e sincronizada no banco!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            new MenuPrincipalView().setVisible(true);

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Atenção: " + ex.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        } catch (java.time.format.DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato inválido! Use: Data (DD/MM/AAAA) e Horário (HH:MM)", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro técnico ao salvar agendamento: " + ex.getMessage(), "Erro no Banco", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}