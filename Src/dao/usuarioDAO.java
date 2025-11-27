package dao;

import utils.*;
import entities.Usuario;
import entities.Senior;
import entities.Estudante;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class usuarioDAO {

    public void salvar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (id, nome, email, senha, telefone, data_nascimento, cpf, endereco, tipo, " +
                "contato_emergencia, tem_acompanhante, instituicao, curso, semestre, disponivel) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = dataBaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, usuario.getId());
            stmt.setString(2, usuario.getNome());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getSenha());
            stmt.setString(5, usuario.getTelefone());
            stmt.setString(6, Date.valueOf(usuario.getNascimento()));
            stmt.setString(7, usuario.getCpf());
            stmt.setString(8, usuario.getEndereco());

            if (usuario instanceof Senior) {
                Senior senior = (Senior) usuario;
                stmt.setString(9, "SENIOR");
                stmt.setString(10, senior.getContatoEmergencia());
                stmt.setString(11, senior.isTemAcompanhante());
                stmt.setNull(12, Types.VARCHAR);
                stmt.setNull(13, Types.VARCHAR);
                stmt.setNull(14, Types.INTEGER);
                stmt.setNull(15, Types.BOOLEAN);

                salvarCondicoesSaude(senior);
                salvarMedicamentos(senior);

            } else {
                Estudante estudante = (Estudante) usuario;
                stmt.setString(9, "ESTUDANTE");
                stmt.setNull(10, Types.BOOLEAN);
                stmt.setNull(11, Types.VARCHAR);
                stmt.setNull(12, estudante.getInstuicao());
                stmt.setNull(13, estudante.getCurso());
                stmt.setNull(14, estudante.getSemestre());
                stmt.setNull(15, estudante.isDisponivel());

                salvarEspecialidades();
            }

            stmt.executeUpdate();
            System.out.println("Usuario cadastrado com sucesso!" + usuario.getNome());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar usuario!" + e.getMessage(), e);
        }
    }

    private void salvarCondicoesSaude(Senior senior) {
        String sql = "INSERT INTO condicoes_saude (senior_id, condicao) VALUES (?, ?)";

        try (Connection connection = dataBaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){

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

    private void salvarMedicamentos(Senior senior) {
        String sql = "INSERT INTO medicamentos (senior_id, medicamento) VALUES (?, ?)";

        try (Connection connection = dataBaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {

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

    private void salvarEspecialidades(Estudante estudante) {
        String sql = "INSERT INTO especialidades (estudante_id, especialidade) VALUES (?, ?)";

        try (Connection connection = dataBaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {

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

        try (Connection connection = dataBaseConnection.getConnection();
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

        try (Connection connection = dataBaseConnection.getConnection();
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

        try (Connection connection = dataBaseConnection.getConnection();
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

        try (Connection connection = dataBaseConnection.getConnection();
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

        try (Connection connection = dataBaseConnection.getConnection();
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

        try (Connection connection = dataBaseConnection.getConnection();
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

        try (Connection connection = dataBaseConnection.getConnection();
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

        try (Connection connection = dataBaseConnection.getConnection();
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

