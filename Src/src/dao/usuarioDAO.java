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

        // Tratar data_nascimento que pode ser null
        java.sql.Date dataNascSql = rs.getDate("data_nascimento");
        LocalDate dataNascimento = (dataNascSql != null) ? dataNascSql.toLocalDate() : null;

        String cpf = rs.getString("cpf");
        String endereco = rs.getString("endereco");

        System.out.println("DEBUG - Criando usuário: " + nome + " Tipo: " + tipo);

        if ("SENIOR".equals(tipo)) {
            Senior senior = new Senior(id, nome, email, senha, telefone, dataNascimento, cpf, endereco,
                    rs.getString("contato_emergencia"), rs.getBoolean("tem_acompanhante"));

            carregarCondicoesSaude(senior);
            carregarMedicamentos(senior);

            return senior;
        } else if ("ESTUDANTE".equals(tipo)) {
            // Tratar campos que podem ser null para Estudante ---
            String instituicao = rs.getString("instituicao");
            String curso = rs.getString("curso");
            int semestre = rs.getInt("semestre");
            boolean disponivel = rs.getBoolean("disponivel");

            // Se semestre for 0 no banco (SQL NULL), usar 1 como default!!!!!
            if (semestre == 0) semestre = 1;

            Estudante estudante = new Estudante(id, nome, email, senha, telefone, dataNascimento, cpf, endereco,
                    (instituicao != null) ? instituicao : "Não informada",
                    (curso != null) ? curso : "Não informado",
                    semestre,
                    disponivel);

            carregarEspecialidades(estudante);

            System.out.println("DEBUG - Estudante criado: " + estudante.getNome() +
                    ", Curso: " + estudante.getCurso() +
                    ", Semestre: " + estudante.getSemestre());

            return estudante;
        } else {
            System.out.println("Tipo de usuário desconhecido: " + tipo);
            return null;
        }
    }

    public void carregarCondicoesSaude(Senior senior) {
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

    public void carregarMedicamentos(Senior senior) {
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

    public void carregarEspecialidades(Estudante estudante) {
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

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM usuarios");
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("=== DEBUG listarTodosUsuarios ===");

            while (rs.next()) {
                String id = rs.getString("id");
                String nome = rs.getString("nome");
                String tipo = rs.getString("tipo");
                String email = rs.getString("email");
                String telefone = rs.getString("telefone");

                java.sql.Date dataNascSql = rs.getDate("data_nascimento");
                LocalDate dataNascimento = (dataNascSql != null) ? dataNascSql.toLocalDate() : null;

                String cpf = rs.getString("cpf");
                String endereco = rs.getString("endereco");

                System.out.println("Encontrado: " + nome + " (" + tipo + ") - ID: " + id);

                if ("SENIOR".equals(tipo)) {
                    String contatoEmergencia = rs.getString("contato_emergencia");
                    boolean temAcompanhante = rs.getBoolean("tem_acompanhante");

                    Senior senior = new Senior(id, nome, email, "", telefone, dataNascimento, cpf, endereco,
                            contatoEmergencia, temAcompanhante);
                    usuarios.add(senior);

                } else if ("ESTUDANTE".equals(tipo)) {
                    String instituicao = rs.getString("instituicao");
                    String curso = rs.getString("curso");
                    int semestre = rs.getInt("semestre");
                    boolean disponivel = rs.getBoolean("disponivel");

                    // Se semestre for 0 (NULL no banco), usar 1 como default
                    if (semestre == 0) semestre = 1;

                    Estudante estudante = new Estudante(id, nome, email, "", telefone, dataNascimento, cpf, endereco,
                            instituicao, curso, semestre, disponivel);
                    usuarios.add(estudante);
                }
            }

        } catch (Exception e) {
            System.out.println("Erro no listarTodosUsuarios: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Total de usuarios carregados: " + usuarios.size());
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

    public void carregarDadosCompletos(Usuario usuario) {
        if (usuario instanceof Senior) {
            carregarCondicoesSaude((Senior) usuario);
            carregarMedicamentos((Senior) usuario);
            // Também carrega dados básicos do senior
            carregarDadosSenior((Senior) usuario);
        } else if (usuario instanceof Estudante) {
            carregarEspecialidades((Estudante) usuario);
            // Também carrega dados básicos do estudante
            carregarDadosEstudante((Estudante) usuario);
        }
    }

    private void carregarDadosSenior(Senior senior) {
        String sql = "SELECT telefone, data_nascimento, cpf, endereco, contato_emergencia, tem_acompanhante " +
                "FROM usuarios WHERE id = ?";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, senior.getId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                senior.setTelefone(rs.getString("telefone"));

                java.sql.Date dataNascSql = rs.getDate("data_nascimento");
                if (dataNascSql != null) {
                    senior.setDataNascimento(dataNascSql.toLocalDate());
                }

                senior.setCpf(rs.getString("cpf"));
                senior.setEndereco(rs.getString("endereco"));
                senior.setContatoEmergencia(rs.getString("contato_emergencia"));
                senior.setTemAcompanhante(rs.getBoolean("tem_acompanhante"));
            }

        } catch (Exception e) {
            System.out.println("Erro ao carregar dados do senior: " + e.getMessage());
        }
    }

    private void carregarDadosEstudante(Estudante estudante) {
        String sql = "SELECT telefone, data_nascimento, cpf, endereco, instituicao, curso, semestre, disponivel " +
                "FROM usuarios WHERE id = ?";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, estudante.getId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                estudante.setTelefone(rs.getString("telefone"));

                java.sql.Date dataNascSql = rs.getDate("data_nascimento");
                if (dataNascSql != null) {
                    estudante.setDataNascimento(dataNascSql.toLocalDate());
                }

                estudante.setCpf(rs.getString("cpf"));
                estudante.setEndereco(rs.getString("endereco"));
                estudante.setInstuicao(rs.getString("instituicao"));
                estudante.setCurso(rs.getString("curso"));

                int semestre = rs.getInt("semestre");
                estudante.setSemestre(semestre == 0 ? 1 : semestre); // Default 1 se for 0

                estudante.setDisponivel(rs.getBoolean("disponivel"));
            }

        } catch (Exception e) {
            System.out.println("Erro ao carregar dados do estudante: " + e.getMessage());
        }
    }

    public void atualizarDisponibilidadeEstudante(String estudanteId, boolean disponivel) {
        String sql = "UPDATE usuarios SET disponivel = ? WHERE id = ?";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setBoolean(1, disponivel);
            stmt.setString(2, estudanteId);

            int rowsAffected = stmt.executeUpdate();
            System.out.println("DEBUG - Disponibilidade atualizada para " + estudanteId +
                    ": " + disponivel + " (linhas afetadas: " + rowsAffected + ")");

        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar disponibilidade: " + e.getMessage(), e);
        }
    }

    public void atualizarCondicoesSaude(Senior senior) { //deleta a antiga e coloca a nova no bd
        Connection connection = null;
        try {
            connection = DataBaseConnection.getConnection();
            connection.setAutoCommit(false);

            String deleteSQL = "DELETE FROM condicoes_saude WHERE senior_id = ?";
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSQL)) {
                deleteStmt.setString(1, senior.getId());
                deleteStmt.executeUpdate();
            }

            salvarCondicoesSaude(connection, senior);

            connection.commit();
            System.out.println("Condições de saúde atualizadas para: " + senior.getNome());

        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    System.out.println("Erro no rollback: " + ex.getMessage());
                }
            }
            throw new RuntimeException("Erro ao atualizar condições de saúde: " + e.getMessage(), e);
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

    public void atualizarMedicamentos(Senior senior) {
        Connection connection = null;
        try {
            connection = DataBaseConnection.getConnection();
            connection.setAutoCommit(false);

            String deleteSQL = "DELETE FROM medicamentos WHERE senior_id = ?";
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSQL)) {
                deleteStmt.setString(1, senior.getId());
                deleteStmt.executeUpdate();
            }

            salvarMedicamentos(connection, senior);

            connection.commit();
            System.out.println("Medicamentos atualizados para: " + senior.getNome());

        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    System.out.println("Erro no rollback: " + ex.getMessage());
                }
            }
            throw new RuntimeException("Erro ao atualizar medicamentos: " + e.getMessage(), e);
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

    public void atualizarEspecialidades(Estudante estudante) {
        Connection connection = null;
        try {
            connection = DataBaseConnection.getConnection();
            connection.setAutoCommit(false);

            String deleteSQL = "DELETE FROM especialidades WHERE estudante_id = ?";
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSQL)) {
                deleteStmt.setString(1, estudante.getId());
                deleteStmt.executeUpdate();
            }

            salvarEspecialidades(connection, estudante);

            connection.commit();
            System.out.println("Especialidades atualizadas para: " + estudante.getNome());

        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    System.out.println("Erro no rollback: " + ex.getMessage());
                }
            }
            throw new RuntimeException("Erro ao atualizar especialidades: " + e.getMessage(), e);
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
}