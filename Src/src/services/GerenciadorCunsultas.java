package services;

import entites.Consulta;
import entites.Senior;
import entites.Estudante;
import dao.ConsultaDAO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GerenciadorCunsultas {
    private List<Consulta> consultas;

    public GerenciadorCunsultas() {
        consultas = new ArrayList<>();
    }

    public Consulta agendarConsulta(String id, Senior senior, Estudante estudante, LocalDateTime dataHora,
                                    String tipoConsulta) {
        Consulta consulta = new Consulta(id, senior, estudante, dataHora, tipoConsulta);
        consultas.add(consulta);

        // SALVAR NO BANCO TAMBÉM
        ConsultaDAO consultaDAO = new ConsultaDAO();
        consultaDAO.salvar(consulta);

        return consulta;
    }

    public List<Consulta> getConsultarSenior(String seniorId) {
        ConsultaDAO consultaDAO = new ConsultaDAO();
        return consultaDAO.listarSeniores(seniorId);
        //return consultas.stream()
        //        .filter(c -> c.getSenior().getId().equals(seniorId))
        //        .toList();
    }

    public List<Consulta> getConsultarEstudante(String estudanteId) {
        ConsultaDAO consultaDAO = new ConsultaDAO();
        return consultaDAO.listarEstudantes(estudanteId);
      //  return consultas.stream()
      //           .filter(c -> c.getEstudante().getId().equals(estudanteId))
      //          .toList();
    }

    public Optional<Consulta> getConsultaId(String id) {
        return consultas.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    public void cancelarConsulta(String id) {
        getConsultaId(id).ifPresent(consulta -> {
            consulta.setStatus("CANCELADA");
            System.out.println("Consulta cancelada com sucesso!");
        });
    }
}
