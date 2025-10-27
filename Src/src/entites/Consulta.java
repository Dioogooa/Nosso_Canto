package entites;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Consulta {
    private String id;
    private Senior senior;
    private Estudante estudante;
    private LocalDate dataHora;
    private String tipoConsulta;
    private String status; //AGENDADA - CONCLUIDA - CANCELADA
    private String observacao;

    public Consulta(String id, Senior senior, Estudante estudante, LocalDate dataHora,
                    String tipoConsulta, String status, String observacao) {
        this.id = id;
        this.senior = senior;
        this.estudante = estudante;
        this.dataHora = dataHora;
        this.tipoConsulta = tipoConsulta;
        this.status = "Agendada";
        this.observacao = " ";
    }

    public Consulta(String id, Senior senior, Estudante estudante, LocalDateTime dataHora, String tipoConsulta) {
    }

    public void exibirDetalhes() {
        System.out.println("=== Consulta ===");
        System.out.println("ID: " + id);
        System.out.println("Senior: " + senior.getNome());
        System.out.println("Estudante: " + estudante.getNome());
        System.out.println("Data/Hora: " + dataHora);
        System.out.println("Tipo Consulta: " + tipoConsulta);
        System.out.println("Status: " + status);
    }

    public String getId() { return id; }
    public Senior getSenior() { return senior; }
    public Estudante getEstudante() { return estudante; }
    public LocalDate getDataHora() { return dataHora; }
    public String getTipoConsulta() { return tipoConsulta; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getObservacao() { return observacao; }
}
