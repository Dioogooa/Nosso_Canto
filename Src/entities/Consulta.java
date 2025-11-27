package entities;

import java.time.LocalDateTime;

public class Consulta {
    private String id;
    private Senior senior;
    private Estudante estudante;
    private LocalDateTime dataHora;
    private String tipoConsulta;
    private String status; //AGENDADA - CONCLUIDA - CANCELADA

    public Consulta(String id, Senior senior, Estudante estudante, LocalDateTime dataHora,
                    String tipoConsulta) {
        this.id = id;
        this.senior = senior;
        this.estudante = estudante;
        this.dataHora = dataHora;
        this.tipoConsulta = tipoConsulta;
        this.status = "AGENDADA";
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
    public LocalDateTime getDataHora() { return dataHora; }
    public String getTipoConsulta() { return tipoConsulta; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

