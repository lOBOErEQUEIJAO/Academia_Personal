package telas;

import controller.AgendamentoController;
import controller.AlunoController;
import controller.DisponibilidadeController;
import entity.AgendamentoEntity;
import entity.AlunoEntity;
import entity.DisponibilidadeEntity;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AgendarAulaView extends javax.swing.JFrame {
    private JTextField txtNomeAluno, txtData, txtHorario, txtPersonal;
    private JButton btnSalvar, btnCancelar;

    // CONEXÕES MVC: Injetando os controllers responsáveis
    private final AlunoController alunoController = new AlunoController();
    private final AgendamentoController agendamentoController = new AgendamentoController();
    private final DisponibilidadeController disponibilidadeController = new DisponibilidadeController();

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
            String nomeDigitado = txtNomeAluno.getText().trim();
            String dataDigitada = txtData.getText().trim();
            String horaDigitada = txtHorario.getText().trim();
            String personalDigitado = txtPersonal.getText().trim();

            if (nomeDigitado.isEmpty() || dataDigitada.isEmpty() || horaDigitada.isEmpty() || personalDigitado.isEmpty()) {
                throw new IllegalArgumentException("Por favor, preencha todos os campos do formulário!");
            }

            // 1. Chamada via AlunoController (Camada MVC respeitada)
            AlunoEntity alunoEncontrado = alunoController.buscarPorNome(nomeDigitado);
            if (alunoEncontrado == null) {
                throw new IllegalArgumentException("O aluno '" + nomeDigitado + "' não foi encontrado!");
            }

            DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter formatadorHora = DateTimeFormatter.ofPattern("HH:mm");

            LocalDate data = LocalDate.parse(dataDigitada, formatadorData);
            LocalTime hora = LocalTime.parse(horaDigitada, formatadorHora);
            LocalDateTime dataHoraDoAgendamento = LocalDateTime.of(data, hora);

            // 2. Localiza a disponibilidade correspondente de forma geral
            DisponibilidadeEntity disponibilidadeEncontrada = disponibilidadeController.listarTodasDisponibilidades().stream()
                    .filter(d -> d.getData().equals(data)
                            && d.getPersonal() != null
                            && d.getPersonal().getNome().equalsIgnoreCase(personalDigitado)
                            && !hora.isBefore(d.getHoraInicio()) && !hora.isAfter(d.getHoraFim()))
                    .findFirst()
                    .orElse(null);

            if (disponibilidadeEncontrada == null) {
                throw new IllegalArgumentException("Não há disponibilidade cadastrada para o personal " + personalDigitado + " nesta data/horário!");
            }

            // =========================================================================
            // INTEGRAÇÃO EXIGIDA: Busca filtrada usando o ID extraído dinamicamente
            // =========================================================================
            Long idPersonalDinamico = disponibilidadeEncontrada.getPersonal().getId();
            List<DisponibilidadeEntity> horarios = disponibilidadeController.listarDisponibilidadesPorPersonal(idPersonalDinamico);

            // Validação de segurança opcional usando a lista que você pediu para buscar
            if (horarios == null || horarios.isEmpty()) {
                throw new IllegalArgumentException("Erro de consistência: Nenhuma agenda ativa encontrada para este Personal.");
            }

            // 3. Monta a entidade com os dados da tela
            AgendamentoEntity agendamento = new AgendamentoEntity();
            agendamento.setAluno(alunoEncontrado);
            agendamento.setDisponibilidade(disponibilidadeEncontrada);
            agendamento.setDataHora(dataHoraDoAgendamento);
            agendamento.setStatus(entity.StatusAgendamento.AGENDADO);

            agendamento.setNomeAluno(alunoEncontrado.getNome());
            agendamento.setData(dataDigitada);
            agendamento.setHorario(horaDigitada);
            agendamento.setPersonal(disponibilidadeEncontrada.getPersonal().getNome());

            // 4. Chamada via AgendamentoController (Camada MVC respeitada)
            agendamentoController.agendarAula(agendamento);

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