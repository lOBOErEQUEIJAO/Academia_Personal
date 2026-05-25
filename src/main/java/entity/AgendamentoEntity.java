package entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "agendamentos")
public class AgendamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agendamento")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_aluno")
    private AlunoEntity aluno;

    @ManyToOne
    @JoinColumn(name = "id_disponibilidade")
    private DisponibilidadeEntity disponibilidade;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private StatusAgendamento status;

    @Column(name = "obs", columnDefinition = "TEXT")
    private String obs;

    @OneToOne(mappedBy = "agendamento")
    private CheckinEntity checkin;

    // Construtor padrão obrigatório pelo JPA
    public AgendamentoEntity() {}

    // Construtor completo corrigido
    public AgendamentoEntity(AlunoEntity aluno, DisponibilidadeEntity disponibilidade, LocalDateTime dataHora) {
        this.aluno = aluno;
        this.disponibilidade = disponibilidade;
        this.dataHora = dataHora;
        this.status = StatusAgendamento.AGENDADO;
    }

    // --- GETTERS E SETTERS PADRÃO ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public AlunoEntity getAluno() { return aluno; }
    public void setAluno(AlunoEntity aluno) { this.aluno = aluno; }

    public DisponibilidadeEntity getDisponibilidade() { return disponibilidade; }
    public void setDisponibilidade(DisponibilidadeEntity disponibilidade) { this.disponibilidade = disponibilidade; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public StatusAgendamento getStatus() { return status; }
    public void setStatus(StatusAgendamento status) { this.status = status; }

    public String getObs() { return obs; }
    public void setObs(String obs) { this.obs = obs; }

    public CheckinEntity getCheckin() { return checkin; }
    public void setCheckin(CheckinEntity checkin) { this.checkin = checkin; }

    // --- MÉTODOS AUXILIARES CORRIGIDOS (Para compatibilidade com os campos da View) ---

    // Este método vai permitir que passe o Aluno já existente vindo do Banco/Service
    public void vincularAlunoExistente(AlunoEntity alunoDoBanco) {
        this.aluno = alunoDoBanco;
    }

    public String getNomeAluno() {
        return (this.aluno != null) ? this.aluno.getNome() : "";
    }

    public void setNomeAluno(String nomeAluno) {
        // Se a entidade aluno não existir, cria a instância
        if (this.aluno == null) {
            this.aluno = new AlunoEntity();
        }
        this.aluno.setNome(nomeAluno != null ? nomeAluno.trim() : null);
    }

    public String getData() {
        if (this.dataHora != null) {
            return this.dataHora.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        return "";
    }

    public void setData(String data) {
        this.obs = (this.obs != null ? this.obs : "") + " [Data digitada: " + (data != null ? data.trim() : "") + "]";
    }

    public String getHorario() {
        if (this.dataHora != null) {
            return this.dataHora.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return "";
    }

    public void setHorario(String horario) {
        this.obs = (this.obs != null ? this.obs : "") + " [Horário digitado: " + (horario != null ? horario.trim() : "") + "]";
    }

    public void setPersonal(String personal) {
        this.obs = (this.obs != null ? this.obs : "") + " [Personal digitado: " + (personal != null ? personal.trim() : "") + "]";
    }

    //Retorna o nome do personal amarrado à disponibilidade de forma segura
    public String getPersonal() {
        if (this.disponibilidade != null && this.disponibilidade.getPersonal() != null) {
            return this.disponibilidade.getPersonal().getNome();
        }
        return "";
    }
}