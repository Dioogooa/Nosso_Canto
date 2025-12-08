package services;

import entites.Usuario;
import entites.Senior;
import entites.Estudante;
import java.util.ArrayList;
import dao.usuarioDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GerenciadorUsuarios {
    private List<Usuario> usuarios;
    private usuarioDAO usuarioDAO;

    public GerenciadorUsuarios() {
        this.usuarios = new ArrayList<>();
        this.usuarioDAO = new usuarioDAO();

        try {
            carregarUsuariosDoBD();
        } catch (Exception e) {
            System.out.println("Modo offline - lista local (deu red)");
        }
    }

    private void carregarUsuariosDoBD() {
        try {
            this.usuarios = usuarioDAO.listarTodosUsuarios();
            System.out.println(usuarios.size() + " usuarios encontrados no BD");

        } catch (Exception e) {
            System.out.println("Erro ao carregar usuarios do BD" + e.getMessage());
            System.out.println("Continyando com lista vazia :/");
            this.usuarios = new ArrayList<>();
        }
    }

    public void listarUsuarios() {
        System.out.println("== Usuarios cadastrados ==");
        for (Usuario usuario : usuarios) {
            System.out.println("email: " + usuario.getEmail() + " senha: " + usuario.getSenha()+
                    "Tipo: "+usuario.getTipoUsuario());
        }
        System.out.println("Total: "+usuarios.size()+ " usuarios");
    }

    public void cadastrarUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
        usuarioDAO.salvar(usuario);
        System.out.println("UsuÃ¡rio "+ usuario.getNome() + " cadastrado com sucesso!");
    }

    //OPTIONAL (usuarios.strem().filter(u->u.getEmail.equals(email) && u.getSenha().equals(senha).findFirts();
    //Verifica se os dados batem e pega o primeiro da list - Optional permite valores nulos!
    public Optional<Usuario> fazerLogin(String email, String senha) {

        Optional<Usuario> usuarioMemoria = usuarios.stream()
                .filter(u -> u.getEmail().equals(email) && u.getSenha().equals(senha))
                .findFirst();

        if (usuarioMemoria.isPresent()) {
            return usuarioMemoria;
        }

        //Caso o usuario nao esteja na memoria, tenta no banco!
        try {
            Optional<Usuario> usuarioBanco = usuarioDAO.buscarPorEmail(email);
            if (usuarioBanco.isPresent() && usuarioBanco.get().getSenha().equals(senha)) {
                //adc memoria para proximas consultas ---!!!
                usuarios.add(usuarioBanco.get());
                return usuarioBanco;
            }

        } catch (Exception e) {
            System.out.println("Erro ao buscar por email no banco: " + e.getMessage());
        }

        return Optional.empty();
    }

    public List<Senior> listarSeniores() {
        List<Senior> seniores = usuarios.stream()
                .filter(u -> u instanceof Senior)
                .map(u -> (Senior) u)
                .toList();

        for (Senior senior : seniores) {
            try {
                usuarioDAO.carregarCondicoesSaude(senior);
                usuarioDAO.carregarMedicamentos(senior);
            } catch (Exception e) {
                System.out.println("Erro ao carregar dados do senior " + senior.getNome() + ": " + e.getMessage());
            }
        }

        return seniores;
    }

    public List<Estudante> listarEstudantes() {
        List<Estudante> estudantes = usuarios.stream()
                .filter(u -> u instanceof Estudante)
                .map(u -> (Estudante) u)
                .toList();

        for (Estudante estudante : estudantes) {
            try {
                usuarioDAO.carregarEspecialidades(estudante);
            } catch (Exception e) {
                System.out.println("Erro ao carregar especialidades do estudante " + estudante.getNome() + ": " + e.getMessage());
            }
        }

        return estudantes;
    }

    public Optional<Usuario> buscarPorId(String id) { //Buscar por ID
        // mesmo esquema, tenta na memoria, caso nÃ£o ache nada, vai ao bd
        Optional<Usuario> usuarioMemoria = usuarios.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();

        if (usuarioMemoria.isPresent()) {
            return usuarioMemoria;
        }

        try {
            Optional<Usuario> usuarioBanco = usuarioDAO.buscarPorId(id);
            if (usuarioBanco.isPresent()) {
                usuarios.add(usuarioBanco.get());
                return usuarioBanco;
            }

        } catch (Exception e) {
            System.out.println("Erro ao buscar por ID no banco: " + e.getMessage());
        }

        return Optional.empty();
    }
}
