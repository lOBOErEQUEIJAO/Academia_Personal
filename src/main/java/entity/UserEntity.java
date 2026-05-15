package entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    // ADICIONADO: O campo login que estava faltando
    @Column(name = "login", nullable = false, length = 100, unique = true)
    private String login;

    @Column(name = "senha", nullable = false, length = 255)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoUsuario tipo;

    @Column(name = "criado_em", nullable = false)
    private LocalDate criadoEm;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusUser status;

    @OneToOne
    @JoinColumn(name = "id_aluno", unique = true)
    private AlunoEntity aluno;

    @OneToOne
    @JoinColumn(name = "id_personal", unique = true)
    private PersonalEntity personal;

    public UserEntity() {
        this.criadoEm = LocalDate.now();
        this.status = StatusUser.ATIVO;
    }

    public UserEntity(String senha, TipoUsuario tipo) {
        this.senha = senha;
        this.tipo = tipo;
        this.criadoEm = LocalDate.now();
        this.status = StatusUser.ATIVO;
    }

    // --- GETTERS E SETTERS ---

    public Long getId() { return id; }

    // Getter e Setter para o Login (O que o Java não estava achando)
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public TipoUsuario getTipo() { return tipo; }
    public void setTipo(TipoUsuario tipo) { this.tipo = tipo; }

    public LocalDate getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDate criadoEm) { this.criadoEm = criadoEm; }

    public StatusUser getStatus() { return status; }
    public void setStatus(StatusUser status) { this.status = status; }

    public AlunoEntity getAluno() { return aluno; }
    public void setAluno(AlunoEntity aluno) { this.aluno = aluno; }

    public PersonalEntity getPersonal() { return personal; }
    public void setPersonal(PersonalEntity personal) { this.personal = personal; }
}