package dao;

import utils.DataBaseConnection;
import entites.Usuario;
import entites.Senior;
import entites.Estudante;
import java.sql.*;
import java.time.LocalDate;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class usuarioDAO {

    public void salvar(Usuario usuario) {
        Connection connection = null;
        try {
            connection = DataBaseConnection.getConnection();
            connection.setAutoCommit(false);

            salvarUsarioPrincipal(connection, usuario);

            if (usuario instanceof Senior) {
                Senior senior = (Senior) usuario;
                salvarCondicoesSaude(connection, senior);
                salvarMedicamentos(connection, senior);

            } else {
                if (usuario instanceof Estudante) {
                    Estudante estudante = (Estudante) usuario;
                    salvarEspecialidades(connection, estudante);
                }
            }

            connection.commit();
            System.out.println("Usuario salvo no BD: "+ usuario.getNome());

        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    System.out.println("Erro no rollback: " + ex.getMessage());
                }
            }
            throw new RuntimeException("Erro ao salvar usuario: " + e.getMessage());

        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();

                } catch (SQLException e) {
                    System.out.println("Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }
    }

    private void salvarUsarioPrincipal(Connection connection, Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (id, nome, email, senha, telefone, data_nascimento, cpf, endereco, tipo, " +
                "contato_emergencia, tem_acompanhante, instituicao, curso, semestre, disponivel) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario.getId());
            stmt.setString(2, usuario.getNome());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getSenha());
            stmt.setString(5, usuario.getTelefone());
            stmt.setDate(6, Date.valueOf(usuario.getDataNascimento()));
            stmt.setString(7, usuario.getCpf());
            stmt.setString(8, usuario.getEndereco());

            if (usuario instanceof Senior) {
                Senior senior = (Senior) usuario;
                stmt.setString(9, "SENIOR");
                stmt.setString(10, senior.getContatoEmergencia());
                stmt.setBoolean(11, senior.isTemAcompanhante());
                stmt.setNull(12, Types.VARCHAR);
                stmt.setNull(13, Types.VARCHAR);
                stmt.setNull(14, Types.INTEGER);
                stmt.setNull(15, Types.BOOLEAN);
            } else {
                Estudante estudante = (Estudante) usuario;
                stmt.setString(9, "ESTUDANTE");
                stmt.setNull(10, Types.VARCHAR);
                stmt.setNull(11, Types.BOOLEAN);
                stmt.setString(12, estudante.getInstuicao());
                stmt.setString(13, estudante.getCurso());
                stmt.setInt(14, estudante.getSemestre());
                stmt.setBoolean(15, estudante.isDisponivel());
            }

            stmt.executeUpdate();
        }
    }

    private void salvarCondicoesSaude(Connection connection ,Senior senior) {
        String sql = "INSERT INTO condicoes_saude (senior_id, condicao) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)){

            for (String condicao : senior.getCondicaoSaude()) {
                stmt.setString(1, senior.getId());
                stmt.setString(2, condicao);
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            System.out.println("Erro ao salvar condicoes saude!" + e.getMessage());
        }
    }

    private void salvarMedicamentos(Connection connection, Senior senior) {
        String sql = "INSERT INTO medicamentos (senior_id, medicamento) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            for (String medicamento : senior.getMedicamentos()) {
                stmt.setString(1, senior.getId());
                stmt.setString(2, medicamento);
                stmt.addBatch();
            }
            stmt.executeBatch();

        } catch (SQLException e) {
            System.out.println("Erro ao salvar condicoes medicamentos!" + e.getMessage());
        }
    }

    private void salvarEspecialidades(Connection connection, Estudante estudante) {
        String sql = "INSERT INTO especialidades (estudante_id, especialidade) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            for (String especialidade : estudante.getEspecialidades()) {
                stmt.setString(1, estudante.getId());
                stmt.setString(2, especialidade);
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch ( SQLException e) {
            System.out.println("Erro ao salvar especialidades!" + e.getMessage());
        }
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(criarUsuarioFromResultSet(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar usario por email", e);
        }

        return Optional.empty();
    }

    public Optional<Usuario> buscarPorId(String id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(criarUsuarioFromResultSet(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar usario por id", e);
        }

        return Optional.empty();
    }

    private Usuario criarUsuarioFromResultSet(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo");
        String id = rs.getString("id");
        String nome = rs.getString("nome");
        String email = rs.getString("email");
        String senha = rs.getString("senha");
        String telefone = rs.getString("telefone");
        LocalDate dataNascimento = rs.getDate("data_nascimento").toLocalDate();
        String cpf = rs.getString("cpf");
        String endereco = rs.getString("endereco");

        if ("SENIOR".equals(tipo)) {
            Senior senior = new Senior(id, nome, email, senha, telefone, dataNascimento, cpf, endereco,
                    rs.getString("contato_emergencia"), rs.getBoolean("tem_acompanhante"));

            carregarCondicoesSaude(senior);
            carregarMedicamentos(senior);

            return senior;
        } else {
            Estudante estudante = new Estudante(id, nome, email, senha, telefone, dataNascimento, cpf, endereco,
                    rs.getString("instituicao"), rs.getString("curso"), rs.getInt("semestre"),
                    rs.getBoolean("disponivel"));

            carregarEspecialidades(estudante);

            return estudante;
        }
    }

    private void carregarCondicoesSaude(Senior senior) {
        String sql = "SELECT condicao FROM condicoes_saude WHERE senior_id = ?";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, senior.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                senior.addCondicaoSaude(rs.getString("condicao"));
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar condicao saude!" + e.getMessage());
        }
    }

    private void carregarMedicamentos(Senior senior) {
        String sql = "SELECT medicamento FROM medicamentos WHERE senior_id = ?";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, senior.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                senior.addMedicamento(rs.getString("medicamento"));
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar medicamentos!" + e.getMessage());
        }
    }

    private void carregarEspecialidades(Estudante estudante) {
        String sql = "SELECT especialidade FROM especialidades WHERE estudante_id = ?";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, estudante.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                estudante.adcEspecialidade(rs.getString("especialidade"));
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar especialidades!" + e.getMessage());
        }
    }

    public List<Usuario> listarTodosUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                usuarios.add(criarUsuarioFromResultSet(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar todos os usuarios!" + e);
        }

        return usuarios;
    }

    public List<Senior> listarTodosSeniors() {
        List<Senior> seniores = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE tipo = 'SENIOR' ";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Senior senior = (Senior) criarUsuarioFromResultSet(rs);
                seniores.add(senior);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar todos os seniores!" + e);
        }

        return seniores;
    }

    public List<Estudante> listarTodosEstudantes() {
        List<Estudante> estudantes = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE tipo = 'ESTUDANTE' ";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Estudante estudante = (Estudante) criarUsuarioFromResultSet(rs);
                estudantes.add(estudante);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar todos os estudantes!" + e);
        }

        return estudantes;
    }
}