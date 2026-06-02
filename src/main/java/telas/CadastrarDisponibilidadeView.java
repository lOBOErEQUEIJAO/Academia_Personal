package telas;

import controller.DisponibilidadeController;
import controller.PersonalController;
import entity.DisponibilidadeEntity;
import entity.PersonalEntity;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CadastrarDisponibilidadeView extends JFrame {
    private JTextField txtNomePersonal, txtDiaSemana, txtHoraInicio, txtHoraFim;
    private JButton btnSalvar, btnCancelar;

    // CONEXÕES MVC: Injetando os controllers correspondentes
    private final DisponibilidadeController disponibilidadeController = new DisponibilidadeController();
    private final PersonalController personalController = new PersonalController();

    public CadastrarDisponibilidadeView() {
        setTitle("Sistema Academia - Cadastrar Disponibilidade");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Painel de formulário estruturado
        JPanel painelCampos = new JPanel(new GridLayout(4, 2, 10, 15));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(25, 25, 10, 25));

        painelCampos.add(new JLabel("Nome do Personal:"));
        txtNomePersonal = new JTextField();
        painelCampos.add(txtNomePersonal);

        painelCampos.add(new JLabel("Data (DD/MM/AAAA):"));
        txtDiaSemana = new JTextField();
        painelCampos.add(txtDiaSemana);

        painelCampos.add(new JLabel("Hora Início (HH:MM):"));
        txtHoraInicio = new JTextField();
        painelCampos.add(txtHoraInicio);

        painelCampos.add(new JLabel("Hora Fim (HH:MM):"));
        txtHoraFim = new JTextField();
        painelCampos.add(txtHoraFim);

        // Painel inferior de botões
        JPanel painelBotoes = new JPanel(new GridLayout(2, 1, 5, 5));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(0, 25, 20, 25));

        btnCancelar = new JButton("Cancelar e Voltar");
        btnCancelar.setBackground(new Color(180, 180, 180));
        btnCancelar.addActionListener(e -> {
            this.dispose();
            new MenuPrincipalView().setVisible(true);
        });

        btnSalvar = new JButton("Salvar Horário");
        btnSalvar.setBackground(new Color(34, 139, 34));
        btnSalvar.setForeground(Color.WHITE);

        btnSalvar.addActionListener(e -> executarSalvamento());

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnSalvar);

        add(painelCampos, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        // Aciona o botão salvar ao pressionar ENTER em qualquer campo
        getRootPane().setDefaultButton(btnSalvar);
    }

    /** Coleta as strings da tela, realiza o parse para objetos de data/hora do JPA */
    private void executarSalvamento() {
        try {
            String nomePersonal = txtNomePersonal.getText().trim();
            String dataDigitada = txtDiaSemana.getText().trim();
            String horaInicioDigitada = txtHoraInicio.getText().trim();
            String horaFimDigitada = txtHoraFim.getText().trim();

            if (nomePersonal.isEmpty() || dataDigitada.isEmpty() || horaInicioDigitada.isEmpty() || horaFimDigitada.isEmpty()) {
                throw new IllegalArgumentException("Todos os campos são obrigatórios!");
            }

            // 1. Chamada segura usando a camada de Controller para localizar o Personal pelo padrão MVC
            PersonalEntity personalEncontrado = personalController.buscarPorNome(nomePersonal);

            // Validação de segurança: se o usuário digitar um nome que não existe no banco
            if (personalEncontrado == null) {
                throw new IllegalArgumentException("O Personal '" + nomePersonal + "' não foi encontrado no sistema!\nCadastre o Personal primeiro antes de definir horários.");
            }

            // 2. CONVERSÃO: Formatadores para converter o texto da interface gráfica
            DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter formatadorHora = DateTimeFormatter.ofPattern("HH:mm");

            LocalDate data = LocalDate.parse(dataDigitada, formatadorData);
            LocalTime horaInicio = LocalTime.parse(horaInicioDigitada, formatadorHora);
            LocalTime horaFim = LocalTime.parse(horaFimDigitada, formatadorHora);

            // 3. ENTIDADE: Instancia e popula o objeto de banco de dados
            DisponibilidadeEntity disp = new DisponibilidadeEntity();
            disp.setPersonal(personalEncontrado);
            disp.setData(data);
            disp.setHoraInicio(horaInicio);
            disp.setHoraFim(horaFim);
            disp.setStatus(entity.StatusDisponibilidade.ATIVO);

            // 4. CONTROLLER: Envia os dados encapsulados respeitando a arquitetura MVC
            disponibilidadeController.cadastrarNovaDisponibilidade(disp);

            // Feedback visual de sucesso
            JOptionPane.showMessageDialog(this, "Horários salvos no banco de dados com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            new MenuPrincipalView().setVisible(true);

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Atenção: " + ex.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        } catch (java.time.format.DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato incorreto de data ou hora!\nUse: Data (DD/MM/AAAA) e Horários (HH:MM)", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro técnico ao gravar dados: " + ex.getMessage(), "Erro no Banco", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}