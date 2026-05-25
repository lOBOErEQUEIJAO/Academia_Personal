package entity;

import javax.persistence.*;
import java.time.LocalDate;
import Camilaronzzani.com.github.Identificavel;

@Entity
@Table(name = "alunos")
public class AlunoEntity implements Identificavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alunos")
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "horario_treino")
    private String horarioTreino;

    @Column(name = "cpf", nullable = false, length = 14, unique = true)
    private String cpf;

    @Column(name = "senha", nullable = false, length = 100)
    private String senha;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "telefone", length = 100, unique = true)
    private String telefone;

    @Column(name = "data_matricula", nullable = false)
    private LocalDate dataMatricula;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "personal_trainer", length = 100)
    private String personalTrainer;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = true)
    private StatusAluno status;

    // 1. Construtor Vazio (Obrigatório para o Hibernate)
    public AlunoEntity() {
        this.status = StatusAluno.ATIVO;
    }

    // 2. Construtor com 4 parâmetros
    public AlunoEntity(String nome, String cpf, LocalDate dataNascimento, LocalDate dataMatricula) {
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.dataMatricula = dataMatricula;
        this.status = StatusAluno.ATIVO;
        this.senha = "123456";
    }

    // Getters e Setters
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getHorarioTreino() { return horarioTreino; }
    public void setHorarioTreino(String horarioTreino) { this.horarioTreino = horarioTreino; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public LocalDate getDataMatricula() { return dataMatricula; }
    public void setDataMatricula(LocalDate dataMatricula) { this.dataMatricula = dataMatricula; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPersonalTrainer() { return personalTrainer; }

    @SuppressWarnings("unused")
    public void setPersonalTrainer(String personalTrainer) { this.personalTrainer = personalTrainer; }

    public StatusAluno getStatus() { return status; }
    public void setStatus(StatusAluno status) { this.status = status; }
}