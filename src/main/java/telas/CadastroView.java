package telas;

import entity.AlunoEntity;
import entity.StatusAluno;
import service.AlunoService;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class CadastroView extends JFrame {
    private JTextField txtNome, txtCpf, txtTelefone, txtEmail;
    private JPasswordField txtSenha;
    private JButton btnSalvar, btnCancelar;

    public CadastroView() {
        setTitle("Sistema Academia - Cadastrar Novo Aluno");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // --- PAINEL DE CAMPOS (CENTRO) ---
        JPanel painelCampos = new JPanel(new GridLayout(6, 2, 10, 15));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(25, 25, 10, 25));

        painelCampos.add(new JLabel("Nome do Aluno:"));
        txtNome = new JTextField();
        painelCampos.add(txtNome);

        painelCampos.add(new JLabel("CPF:"));
        txtCpf = new JTextField();
        painelCampos.add(txtCpf);

        painelCampos.add(new JLabel("Telefone:"));
        txtTelefone = new JTextField();
        painelCampos.add(txtTelefone);

        painelCampos.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        painelCampos.add(txtEmail);

        painelCampos.add(new JLabel("Senha Inicial:"));
        txtSenha = new JPasswordField();
        painelCampos.add(txtSenha);

        // --- PAINEL DE BOTÕES (SUL) ---
        JPanel painelBotoes = new JPanel(new GridLayout(2, 1, 5, 5));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(0, 25, 20, 25));

        // Botão Cancelar configurado para Voltar
        btnCancelar = new JButton("Cancelar e Voltar");
        btnCancelar.setBackground(new Color(180, 180, 180));
        btnCancelar.setFont(new Font("Arial", Font.PLAIN, 13));

        // Botão Salvar
        btnSalvar = new JButton("Finalizar Cadastro");
        btnSalvar.setBackground(new Color(34, 139, 34));
        btnSalvar.setForeground(Color.WHITE); // Texto branco para destacar no verde
        btnSalvar.setFont(new Font("Arial", Font.BOLD, 14));

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnSalvar);

        // Adicionando os painéis na janela
        add(painelCampos, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        // --- EVENTOS DE INTERLIGAÇÃO ---

        // Ação do Botão Cancelar: Fecha a ficha e volta direto para o Menu Principal
        btnCancelar.addActionListener(e -> {
            this.dispose();
            new MenuPrincipalView().setVisible(true);
        });

        // Ação do Botão Salvar
        btnSalvar.addActionListener(e -> executarCadastro());

        // Tecla ENTER aciona o botão de salvar automaticamente
        getRootPane().setDefaultButton(btnSalvar);
    }

    private void executarCadastro() {
        try {
            AlunoEntity aluno = new AlunoEntity();
            aluno.setNome(txtNome.getText().trim());
            aluno.setCpf(txtCpf.getText().trim());
            aluno.setTelefone(txtTelefone.getText().trim());
            aluno.setEmail(txtEmail.getText().trim());

            String senha = new String(txtSenha.getPassword());
            aluno.setSenha(senha);

            aluno.setDataNascimento(LocalDate.of(2000, 1, 1));
            aluno.setDataMatricula(LocalDate.now());
            aluno.setStatus(StatusAluno.ATIVO);

            AlunoService service = new AlunoService();
            service.cadastrar(aluno, senha);

            JOptionPane.showMessageDialog(this, "Aluno cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            // Redireciona de volta para o Menu Principal após salvar com sucesso
            this.dispose();
            new MenuPrincipalView().setVisible(true);

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Atenção: " + ex.getMessage(), "Erro de Validação", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro técnico ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new CadastroView().setVisible(true));
    }
}