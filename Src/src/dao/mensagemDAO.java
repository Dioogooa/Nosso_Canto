package dao;

import utils.*;
import entites.Mensagem;
import entites.Usuario;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class mensagemDAO {

    public void salvar (Mensagem mensagem) {
        String sql = "INSERT INTO mensagens (id, remetente_id, destinatario_id, texto, data_hora, visto) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, mensagem.getId());
            stmt.setString(2, mensagem.getRemetente().getId());
            stmt.setString(3, mensagem.getDestinatario().getId());
            stmt.setString(4, mensagem.getTexto());
            stmt.setTimestamp(5, Timestamp.valueOf(mensagem.getDataHora()));
            stmt.setBoolean(6, mensagem.isVisto());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar mensagem"+ e.getMessage(), e);
        }
    }

    public List<Mensagem> buscarConversa(String usuario1Id, String usuario2Id) {
        List<Mensagem> mensagens = new ArrayList<>();
        String sql = "SELECT m.*, r.nome as remetente_nome, d.nome as destinatario_nome " +
                "FROM mensagens m " +
                "JOIN usuarios r ON m.remetente_id = r.id " +
                "JOIN usuarios d ON m.destinatario_id = d.id " +
                "WHERE (m.remetente_id = ? AND m.destinatario_id = ?) OR " +
                "(m.remetente_id = ? AND m.destinatario_id = ?) " +
                "ORDER BY m.data_hora ASC";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario1Id);
            stmt.setString(2, usuario2Id);
            stmt.setString(3, usuario2Id);
            stmt.setString(4, usuario1Id);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                mensagens.add(criarMensagemFromResultSet(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar mensagem", e);
        }

        return mensagens;
    }

    private Mensagem criarMensagemFromResultSet(ResultSet rs) throws SQLException {
        Usuario remetente = new Usuario(rs.getString("remetente_id"), rs.getString("remetente_nome"),
                "", "", "", null, "", "") {

            @Override public void exibirPerfil() {}
            @Override public void exibirPerfilReduzido() {}
            @Override public String getTipoUsuario() {return "";}
        };

        Usuario destinatario = new Usuario(rs.getString("destinatario_id"), rs.getString("destinatario_nome"),
                "", "", "", null, "", "") {

            @Override public void exibirPerfil() {}
            @Override public void exibirPerfilReduzido() {}
            @Override public String getTipoUsuario() {return "";}
        };

        Mensagem mensagem = new Mensagem(
                rs.getString("id"),
                remetente,
                destinatario,
                rs.getString("texto")
        );

        //Cfg data/hora
        mensagem.getDataHora();

        if (rs.getBoolean("visto")) {
            mensagem.isVisto();
        }

        return mensagem;
    }
}