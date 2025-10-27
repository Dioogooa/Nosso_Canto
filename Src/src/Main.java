import entites.*;
import services.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {
    private static GerenciadorUsuarios gerenciadorUsuarios = new GerenciadorUsuarios();
    private static GerenciadorCunsultas gerenciadorCunsulta = new GerenciadorCunsultas();
    private static GerenciadorMsg gerenciadorMsg = new GerenciadorMsg();
    private static Scanner input = new Scanner(System.in);
    private static Usuario usuarioIsLogado = null;

    public static void main(String[] args) {
        System.out.println("=== BEM-VINDO AO NOSSO CANTO ===");

       // cadastrarExemplos();

        while (true) {
            if (usuarioIsLogado == null) {
                exibirMainMenu();
            } else {
                exibirMenuUsuario();
            }
        }
    }

    private static void cadastrarExemplos() {
        //Exemplo p senior
        Senior senior = new Senior("S1", "Antônio Fagundes", "antonio1955@gmail.com", "1995Anto@",
                "(62)98165-9834", LocalDate.of(1955, 2, 18), "901.785.901-31",
                "Rua A, 900, Jardim Luz", "(72)95678-3190", false);
        senior.addCondicaoSaude("Alzheimer ");
        senior.addMedicamento("Kisunla (donanemabe)");
        senior.addMedicamento("Óleo de canabidiol");

        Estudante estudante = new Estudante("E1", "Pedro Santiago", "pedrinho2003@gmail.com",
                "ppTrem109040@", "(21)98990-1254", LocalDate.of(2003, 9, 19),
                "900-800-700-65", "Rua flamengo, 177, Flamengo", "UFRJ", "Psicologia",
                7, true);
        estudante.adcEspecialidade("Psicologo humanista");

        gerenciadorUsuarios.cadastrarUsuario(senior);
        gerenciadorUsuarios.cadastrarUsuario(estudante);
    }

    private static void exibirMainMenu() {
        int op;
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1. Login");
        System.out.println("2. Cadastrar Sênior");
        System.out.println("3. Cadastrar Estudante");
        System.out.println("4. Sair");
        System.out.print("Escolha uma opção: ");

        op = input.nextInt();
        input.nextLine();

        switch (op) {
            case 1 -> fazerLogin();
            case 2 -> cadastrarSenior();
            case 3 -> cadastrarEstudante();
            case 4 -> {
                System.out.println("Obrigado por usar o Nosso Canto!");
                System.exit(0);
            }
            default -> System.out.println("Opção invalida :/");
        }
    }

    private static void exibirMenuUsuario() {
        System.out.println("Bem vindo: "+usuarioIsLogado.getNome()+" !");

        if (usuarioIsLogado instanceof Senior) {
            exibirMenuSenior();
        } else {
            if (usuarioIsLogado instanceof Estudante) {
                exibirMenuEstudante();
            }
        }
    }

    private static void exibirMenuSenior() {
        System.out.println("\n=== MENU SENIOR ===");
        System.out.println("1. Buscar estudante");
        System.out.println("2. Agendar Consulta");
        System.out.println("3. Minhas Consultas");
        System.out.println("4. Chat com Estudantes");
        System.out.println("5. Chat com outro Senior");
        System.out.println("6. Ver perfil");
        System.out.println("7. Sair da conta");
        System.out.print("Escolha -> ");

        int op = input.nextInt();
        input.nextLine();

        switch (op) {
            case 1 -> buscarEstudantes();
            case 2 -> agendarConsulta();
            case 3 -> minhasConsultas();
            case 4 -> chatEstudantes();
            case 5 -> chatSeniors();
            case 6 -> usuarioIsLogado.exibirPerfil();
            case 7 -> usuarioIsLogado = null;
            default -> System.out.println("Opção invalida :/");
        }
    }

    public static void exibirMenuEstudante() {
        System.out.println("\n=== MENU ESTUDANTE ===");
        System.out.println("1. Buscar Seniors");
        System.out.println("2. Minhas consultas");
        System.out.println("3. Chat com Seniors");
        System.out.println("4. Ver Perfil");
        System.out.println("5. Sair da conta");
        System.out.print("Escolha -> ");

        int op = input.nextInt();
        input.nextLine();

        switch (op) {
            case 1 -> buscarSenior();
            case 2 -> minhasConsultas();
            case 3 -> chatSeniors();
            case 4 -> usuarioIsLogado.exibirPerfil();
            case 5 -> usuarioIsLogado = null;
            default -> System.out.println("Opação invalida :/");
        }
    }

    private static void fazerLogin() {
        gerenciadorUsuarios.listarUsuarios();
        System.out.println("\n=== LOGIN ===");
        System.out.print("Email: ");
        String email = input.nextLine();
        System.out.print("Senha: ");
        String senha = input.nextLine();

        var usuario = gerenciadorUsuarios.fazerLogin(email, senha); //Var é varivael temporaria
        if (usuario.isPresent()) {
            usuarioIsLogado = usuario.get();
            System.out.println("Login realizado com sucesso!");
        } else {
            System.out.println("Email ou senha invalidos");
        }
    }

    public static void cadastrarSenior() { //fazer aqui, precisa cadastrar infos além de email e senha
        System.out.println("\n=== CADASTRAR SENIOR ===");
        System.out.println("Ainda em desenvolvimento...");
    }

    public static void cadastrarEstudante() { //fazer aqui, precisa cadastrar infos além de email e senha
        System.out.println("\n=== CADASTRAR ESTUDANTE ===");
        System.out.println("Ainda em desenvolvimento...");
    }

    private static void buscarEstudantes() {
        System.out.println("\n=== ESTUDANTES DISPONIVEIS ===");
        gerenciadorUsuarios.listarEstudantes().forEach(Estudante::exibirPerfil);
    }

    private static void buscarSenior() {
        System.out.println("\n=== SERNIORS DISPONIVEIS ===");
        gerenciadorUsuarios.listarSeniores().forEach(Senior::exibirPerfil);
    }

    private static void agendarConsulta() {
        // Implementar agendamento
        System.out.println("Funcionalidade de agendamento em desenvolvimento...");
    }

    private static void minhasConsultas() {
        // Implementar listagem de consultas
        System.out.println("Funcionalidade de consultas em desenvolvimento...");
    }

    private static void chatEstudantes() {
        // Implementar chat
        System.out.println("Funcionalidade de chat em desenvolvimento...");
    }

    private static void chatSeniors() {
        // Implementar chat entre seniors
        System.out.println("Funcionalidade de chat entre seniors em desenvolvimento...");
    }
}