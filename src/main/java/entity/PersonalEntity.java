package entity;

import javax.persistence.*;
import java.util.List;
import Camilaronzzani.com.github.Identificavel;

@Entity
@Table(name = "personais")
public class PersonalEntity implements Identificavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_personal")
    private Long id;

    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    @Column(name = "cpf", nullable = false, length = 14, unique = true)
    private String cpf;

    @Column(name = "cref", nullable = false, length = 20, unique = true)
    private String cref;

    // Esta anotação gera a coluna 'senha' no seu banco de dados
    @Column(name = "senha", nullable = false, length = 255)
    private String senha;

    @Column(name = "especialidade", length = 100)
    private String especialidade;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "email", length = 100)
    private String email;

    @OneToMany(mappedBy = "personal", cascade = CascadeType.ALL)
    private List<DisponibilidadeEntity> disponibilidades;

    public PersonalEntity() {}

    public PersonalEntity(String nome, String cpf, String cref) {
        this.nome = nome;
        this.cpf = cpf;
        this.cref = cref;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getCref() { return cref; }
    public void setCref(String cref) { this.cref = cref; }
    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<DisponibilidadeEntity> getDisponibilidades() { return disponibilidades; }
    public void setDisponibilidades(List<DisponibilidadeEntity> disponibilidades) { this.disponibilidades = disponibilidades; }
}