package services;

import entites.Usuario;
import entites.Senior;
import entites.Estudante;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GerenciadorUsuarios {
    private List<Usuario> usuarios;

    public GerenciadorUsuarios() {
        this.usuarios = new ArrayList<>();
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
        System.out.println("Usuário "+ usuario.getNome() + " cadastrado com sucesso!");
    }

    //OPTIONAL (usuarios.strem().filter(u->u.getEmail.equals(email) && u.getSenha().equals(senha).findFirts();
    //Verifica se os dados batem e pega o primeiro da list - Optional permite valores nulos!
    public Optional<Usuario> fazerLogin(String email, String senha) {
        return usuarios.stream() //Cria a stream da list Usuarios
                .filter(u -> u.getEmail().equals(email) && u.getSenha().equals(senha))
                .findFirst();
    }

    public List<Senior> listarSeniores() {
        return usuarios.stream()
                .filter(u -> u instanceof Senior) //Filtra apenas instancia de Senior
                .map(u -> (Senior) u).toList(); //Converte Usuario p Senior e .toList coleta em list
    }

    public List<Estudante> listarEstudantes() {
        return usuarios.stream()
                .filter(u -> u instanceof Estudante)
                .map (u -> (Estudante) u).toList();
    }

    public Optional<Usuario> buscarPorId(String id) { //Buscar por ID
        return usuarios.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }
}
