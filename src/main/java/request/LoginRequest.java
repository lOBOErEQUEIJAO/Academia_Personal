package request;

//Classe responsável por transportar os dados de login da View para o Controller.

public class LoginRequest {

    private String usuario;
    private String senha;

    // Construtor cheio para facilitar a criação na View
    public LoginRequest(String usuario, String senha) {
        this.usuario = usuario;
        this.senha = senha;
    }

    // Getters e Setters (O IntelliJ pode gerar automaticamente com Alt+Insert)
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
