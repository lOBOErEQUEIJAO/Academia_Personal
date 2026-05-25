package entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "disponibilidades")
public class DisponibilidadeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_disponibilidade")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_personal", nullable = false)
    private PersonalEntity personal;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fim", nullable = false)
    private LocalTime horaFim;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusDisponibilidade status;

    // Construtor padrão obrigatório pelo JPA
    public DisponibilidadeEntity() {}

    // Construtor completo
    public DisponibilidadeEntity(PersonalEntity personal, LocalDate data, LocalTime horaInicio, LocalTime horaFim) {
        this.personal = personal;
        this.data = data;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.status = StatusDisponibilidade.ATIVO;
    }

    // GETTERS E SETTERS PADRÃO (JPA)

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PersonalEntity getPersonal() { return personal; }
    public void setPersonal(PersonalEntity personal) { this.personal = personal; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalTime getHoraFim() { return horaFim; }
    public void setHoraFim(LocalTime horaFim) { this.horaFim = horaFim; }

    public StatusDisponibilidade getStatus() { return status; }
    public void setStatus(StatusDisponibilidade status) { this.status = status; }

    // --- MÉTODOS AUXILIARES ACOPLADOS (Para compatibilidade com os campos da View) ---

    public void setNomePersonal(String nomePersonal) {
        if (this.personal == null) {
            this.personal = new PersonalEntity();
        }
        this.personal.setNome(nomePersonal != null ? nomePersonal.trim() : null);
    }

    public void setDiaSemana(String diaSemana) {
        // Método exigido pela View. Como seu banco usa LocalDate, guardamos uma cópia textual temporária se necessário,
        // ou você pode tratar a conversão de dia de semana diretamente na View/Service.
    }

    public void setHoraInicio(String horaInicio) {
        if (horaInicio != null && !horaInicio.trim().isEmpty()) {
            try {
                this.horaInicio = LocalTime.parse(horaInicio.trim());
            } catch (Exception e) {
                // Evita quebra imediata na digitação incorreta
            }
        }
    }

    public void setHoraFim(String horaFim) {
        if (horaFim != null && !horaFim.trim().isEmpty()) {
            try {
                this.horaFim = LocalTime.parse(horaFim.trim());
            } catch (Exception e) {
                // Evita quebra imediata na digitação incorreta
            }
        }
    }
}