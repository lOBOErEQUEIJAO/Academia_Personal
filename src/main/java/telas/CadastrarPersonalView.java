package telas;

import entity.PersonalEntity;
import service.PersonalService;
import javax.swing.*;
import java.awt.*;

public class CadastrarPersonalView extends JFrame {
    private JTextField txtNome, txtCpf, txtCref, txtTelefone;
    private JPasswordField txtSenha;
    private JButton btnSalvar, btnCancelar;

    public CadastrarPersonalView() {
        setTitle("Sistema Academia - Cadastrar Novo Personal");
        setSize(450, 460);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel painelCampos = new JPanel(new GridLayout(5, 2, 10, 15));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(25, 25, 10, 25));

        painelCampos.add(new JLabel("Nome Completo:"));
        txtNome = new JTextField();
        painelCampos.add(txtNome);

        painelCampos.add(new JLabel("CPF:"));
        txtCpf = new JTextField();
        painelCampos.add(txtCpf);

        painelCampos.add(new JLabel("CREF (Registro):"));
        txtCref = new JTextField();
        painelCampos.add(txtCref);

        painelCampos.add(new JLabel("Telefone:"));
        txtTelefone = new JTextField();
        painelCampos.add(txtTelefone);

        painelCampos.add(new JLabel("Senha de Acesso:"));
        txtSenha = new JPasswordField();
        painelCampos.add(txtSenha);

        JPanel painelBotoes = new JPanel(new GridLayout(2, 1, 5, 5));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(0, 25, 20, 25));

        btnCancelar = new JButton("Cancelar e Voltar");
        btnCancelar.setBackground(new Color(180, 180, 180));
        btnCancelar.addActionListener(e -> {
            this.dispose();
            new MenuPrincipalView().setVisible(true);
        });

        btnSalvar = new JButton("Finalizar Cadastro do Personal");
        btnSalvar.setBackground(new Color(34, 139, 34));
        btnSalvar.setForeground(Color.WHITE);

        // O botão salvar chama a rotina que registra no banco de dados
        btnSalvar.addActionListener(e -> executarCadastro());

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnSalvar);

        add(painelCampos, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        // Tecla ENTER aciona o botão de salvar automaticamente
        getRootPane().setDefaultButton(btnSalvar);
    }

    /**
     * Captura os dados da interface gráfica, popula a Entity,
     * e aciona a camada de negócios (Service) para salvar no banco de dados.
     */
    private void executarCadastro() {
        try {
            // 1. Instancia e popula a entidade com os dados capturados da tela
            PersonalEntity personal = new PersonalEntity();
            personal.setNome(txtNome.getText().trim());
            personal.setCpf(txtCpf.getText().trim());
            personal.setCref(txtCref.getText().trim());
            personal.setTelefone(txtTelefone.getText().trim());

            // Captura a senha de forma segura e garante a injeção direta no objeto
            String senha = new String(txtSenha.getPassword());
            personal.setSenha(senha);

            // 2. Aciona o Service que contém as regras e chamará o DAO responsável pelo banco
            PersonalService service = new PersonalService();
            service.cadastrar(personal, senha);

            // 3. Feedback visual em caso de sucesso e redirecionamento de tela
            JOptionPane.showMessageDialog(this, "Personal Trainer cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            new MenuPrincipalView().setVisible(true);

        } catch (IllegalArgumentException ex) {
            // Captura erros de validação da sua camada Service (ex: campo vazio)
            JOptionPane.showMessageDialog(this, "Atenção: " + ex.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            // Captura falhas técnicas como erro de propriedade nula ou banco de dados
            JOptionPane.showMessageDialog(this, "Erro técnico ao registrar no banco: " + ex.getMessage(), "Erro no Sistema", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}