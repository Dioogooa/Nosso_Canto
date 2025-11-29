package dao;

import utils.DataBaseConnection;
import entites.Consulta;
import entites.Senior;
import entites.Estudante;
import entites.Usuario;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.sql.SQLException;

public class ConsultaDAO {

    public void salvar(Consulta consulta) {
        String sql = "INSERT INTO consultas (id, senior_id, estudante_id, data_hora, tipo_consulta, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, consulta.getId());
            stmt.setString(2, consulta.getSenior().getId());
            stmt.setString(3, consulta.getEstudante().getId());
            stmt.setTimestamp(4, Timestamp.valueOf(consulta.getDataHora()));
            stmt.setString(5, consulta.getTipoConsulta());
            stmt.setString(6, consulta.getStatus());

            stmt.executeUpdate();
            System.out.println("Consulta salva com sucesso!");

        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar consulta" + e.getMessage(), e);
        }
    }

    public List<Consulta> listarSeniores(String seniorId) {
        List<Consulta> consultas = new ArrayList<>();

        String sql = "SELECT c.*, s.nome as senior_nome, e.nome as estudante_nome " +
                "FROM consultas c " +
                "JOIN usuarios s ON c.senior_id = s.id " +
                "JOIN usuarios e ON c.estudante_id = e.id " +
                "WHERE c.senior_id = ? ORDER BY c.data_hora DESC";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, seniorId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                consultas.add(criarConsultasFromResultSet(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar consultas de senior", e);
        }

        return consultas;
    }

    public List<Consulta> listarEstudantes(String estudanteId) {
        List<Consulta> consultas = new ArrayList<>();

        String sql = "SELECT c.*, s.nome as senior_nome, e.nome as estudante_nome " +
                "FROM consultas c " +
                "JOIN usuarios s ON c.senior_id = s.id " +
                "JOIN usuarios e ON c.estudante_id = e.id " +
                "WHERE c.estudante_id = ? ORDER BY c.data_hora DESC";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, estudanteId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("DEBUG - Buscando consultas para estudante: " + estudanteId);

            int count = 0;
            while (rs.next()) {
                count++;
                System.out.println("DEBUG - Consulta encontrada #" + count + ":");
                System.out.println("  ID: " + rs.getString("id"));
                System.out.println("  Senior: " + rs.getString("senior_nome"));
                System.out.println("  Estudante: " + rs.getString("estudante_nome"));
                System.out.println("  Status: " + rs.getString("status"));
                System.out.println("  Data: " + rs.getTimestamp("data_hora"));

                consultas.add(criarConsultasFromResultSet(rs));
            }

            System.out.println("DEBUG - Total de consultas encontradas: " + count);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar consultas de estudante", e);
        }

        return consultas;
    }

    private Consulta criarConsultasFromResultSet(ResultSet rs) throws SQLException {
        // Usar dados já carregados do ResultSet, sem buscar novamente no banco
        Senior senior = new Senior(
                rs.getString("senior_id"),
                rs.getString("senior_nome"),
                "", "", "", null, "", "", "", false
        );

        Estudante estudante = new Estudante(
                rs.getString("estudante_id"),
                rs.getString("estudante_nome"),
                "", "", "", null, "", "", "", "", 0, false
        );

        Consulta consulta = new Consulta(
                rs.getString("id"),
                senior,
                estudante,
                rs.getTimestamp("data_hora").toLocalDateTime(),
                rs.getString("tipo_consulta")
        );

        consulta.setStatus(rs.getString("status"));
        return consulta;
    }

}