import entites.*;
import services.*;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    private static GerenciadorUsuarios gerenciadorUsuarios = new GerenciadorUsuarios();
    private static GerenciadorCunsultas gerenciadorCunsulta = new GerenciadorCunsultas();
    private static GerenciadorMsg gerenciadorMsg = new GerenciadorMsg();
    private static Scanner input = new Scanner(System.in);
    private static Usuario usuarioIsLogado = null;

    public static void main(String[] args) {
        System.out.println("=== BEM-VINDO AO NOSSO CANTO ===");
        cadastrarExemplos();

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
        System.out.println("Usuarios de exemplo, cadastrados.");
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
        System.out.println("5. Gerenciar Condição de Saúde");
        System.out.println("6. Gerenciar Medicamentos");
        System.out.println("6. Ver meu perfil");
        System.out.println("7. Sair da conta");
        System.out.print("Escolha -> ");

        int op = input.nextInt();
        input.nextLine();

        switch (op) {
            case 1 -> buscarEstudantes();
            case 2 -> agendarConsulta();
            case 3 -> minhasConsultasSenior();
            case 4 -> chatComEstudantes();
            case 5 -> gerenciarCondicoesSaude();
            case 6 -> gerenciarMedicamentos();
            case 7 -> usuarioIsLogado.exibirPerfil();
            case 8 -> usuarioIsLogado = null;
            default -> System.out.println("Opção invalida :/");
        }
    }

    public static void exibirMenuEstudante() {
        System.out.println("\n=== MENU ESTUDANTE ===");
        System.out.println("1. Buscar Seniors");
        System.out.println("2. Minhas consultas");
        System.out.println("3. Chat com Seniors");
        System.out.println("4. Gerenciar minhas especialidades");
        System.out.println("5. Atualizar disponibilidade");
        System.out.println("6. Ver Perfil");
        System.out.println("7. Sair da conta");
        System.out.print("Escolha -> ");

        int op = input.nextInt();
        input.nextLine();

        switch (op) {
            case 1 -> buscarSenior();
            case 2 -> minhasConsultasEstudante();
            case 3 -> chatComSeniors();
            case 4 -> gerenciarEspecialidade();
            case 5 -> atualizarDisponibilidade();
            case 6 -> usuarioIsLogado.exibirPerfil();
            case 7 -> usuarioIsLogado = null;
            default -> System.out.println("Opação invalida :/");
        }
    }

    private static void fazerLogin() {
        gerenciadorUsuarios.listarUsuarios();
        System.out.println("\n=== LOGIN ===");
        System.out.print("Email: ");
        String email = input.nextLine().trim();
        System.out.print("Senha: ");
        String senha = input.nextLine().trim();

        var usuario = gerenciadorUsuarios.fazerLogin(email, senha); //Var é varivael temporaria
        if (usuario.isPresent()) {
            usuarioIsLogado = usuario.get();
            System.out.println("Login realizado com sucesso!");
            System.out.println("Bem vindo: "+usuarioIsLogado.getNome()+" !");
        } else {
            System.out.println("Email ou senha invalidos");
        }
    }

    public static void cadastrarSenior() { //Cadastro completo p Senior
        System.out.println("\n=== CADASTRAR SENIOR ===");

        String id = "S" + (gerenciadorUsuarios.listarSeniores().size() + 1);

        System.out.print("Nome Completo: ");
        String nome = input.nextLine();

        System.out.print("Email: "); //aqui tambem p precisar estar certo o email "pelo menos @algo kkkk"
        String email = input.nextLine();

        System.out.print("Senha: "); //da pra polir aq tbm
        String senha = input.nextLine();

        System.out.print("Telefone: ");
        String telefone = input.nextLine();

        System.out.println("Data de nascimento (AAAA-MM-DD): ");
        LocalDate dataNascimento = LocalDate.parse(input.nextLine());

        System.out.print("Cpf: ");
        String cpf = input.nextLine();
        while (cpf.length() != 11) { //valida cpf
            System.out.println("CPF invalido. Tente novamente!");
            System.out.print("CPF: ");
            cpf = input.nextLine();
        }

        System.out.println("Endereco: ");
        String endereco = input.nextLine();

        System.out.println("Contato de Emergencia: ");
        String contato = input.nextLine();

        System.out.println("Tem acompanhante (sim / não) - ");
        String resposta = input.nextLine().toLowerCase(); //valida resposta, pro usuario n ter que digitar true/false
        boolean temAcompanhante = resposta.equals("sim") || resposta.equals("s") || resposta.equals("si");
        //coloca true caso for "sim" , "s" ou "si".. Caso contrario, vira false.

        Senior senior = new Senior(id, nome, email, senha, telefone, dataNascimento, cpf,
                endereco, contato, temAcompanhante);

        gerenciadorUsuarios.cadastrarUsuario(senior);
        System.out.println("Senior cadastrado com sucesso!");

        //ADICIONAR REMEDIOS E CONDIÇÃO DE SAÚDE COMO METODO NO MENU SENIOR, AQUI NO CADASTRO NÃO!!!!!! ----- LEMBRAR!!
    }

    public static void cadastrarEstudante() { //fazer aqui, precisa cadastrar infos além de email e senha
        System.out.println("\n=== CADASTRAR ESTUDANTE ===");

        String id = "E" + (gerenciadorUsuarios.listarEstudantes().size() + 1);

        System.out.print("Nome Completo: ");
        String nome = input.nextLine();

        System.out.print("Email: ");
        String email = input.nextLine();

        System.out.print("Senha: ");
        String senha = input.nextLine();

        System.out.print("Telefone: ");
        String telefone = input.nextLine();

        System.out.print("Data de nascimento (AAAA-MM-DD): ");
        LocalDate dataNascimento = LocalDate.parse(input.nextLine());

        System.out.print("Cpf: ");
        String cpf = input.nextLine();
        while (cpf.length() != 11) { //valida cpf
            System.out.println("CPF invalido. Tente novamente!");
            System.out.print("CPF: ");
            cpf = input.nextLine();
        }

        System.out.print("Endereco: ");
        String endereco = input.nextLine();

        System.out.print("Instituição estudantil: ");
        String instituicao = input.nextLine();

        System.out.print("Curso: ");
        String curso = input.nextLine();

        System.out.println("Periodo: ");
        int semestre = Integer.parseInt(input.nextLine());
        input.nextLine();

        boolean disponivel = false;

        Estudante estudante = new Estudante(id, nome, email, senha, telefone, dataNascimento, cpf,
                endereco, instituicao, curso, semestre, disponivel);

        gerenciadorUsuarios.cadastrarUsuario(estudante);
        System.out.println("Estudante cadastrado com sucesso!");

        //Lembrar de colocar no Menu ESTUDANTE, ficar disponivel!!! Também cadastro de especialidades como metodo
    }

    private static void buscarEstudantes() { //ATUALIZEI
        System.out.println("\n=== ESTUDANTES DISPONIVEIS ===");
        var estudantes = gerenciadorUsuarios.listarEstudantes().stream()
                .filter(Estudante::isDisponivel)
                .toList();

        if (estudantes.isEmpty()) {
            System.out.println("Nenhum estudante encontrado");
        } else {
            estudantes.forEach(Estudante::exibirPerfil);
        }
    }

    private static void buscarSenior() {
        System.out.println("\n=== SERNIORS DISPONIVEIS ===");
        var seniors = gerenciadorUsuarios.listarSeniores();

        if (seniors.isEmpty()) {
            System.out.println("Nenhum senior encontrado");
        } else {
            seniors.forEach(Senior::exibirPerfil);
        }
    } //

    private static void gerenciarCondicoesSaude() {
        Senior senior = (Senior) usuarioIsLogado;

        System.out.println("\n=== GERENCIAR CONDIÇÕES DE SAÚDE ==="); //aqui teria que ser um vetor..
        System.out.println("Suas condições atuais: "+senior.getCondicaoSaude());

        System.out.println("1. Adicionar condição");
        System.out.println("2. Remover condição");
        System.out.println("3. Voltar");
        System.out.println("Escolha: ");

        int op = input.nextInt();
        input.nextLine();

        switch (op) {
            case 1 -> {
                System.out.println("Nova Condição de saúde: ");
                String condicao = input.nextLine();
                senior.addCondicaoSaude(condicao);
                System.out.println("Condição adicionada com sucesso!");
            }
            case 2 -> {
                System.out.println("Condição a remover: ");
                String condicao = input.nextLine();
                senior.getCondicaoSaude().remove(condicao);
                System.out.println("Condição removida com sucesso");
            }
            case 3 -> {return;}
            default -> System.out.println("Opção invalida");
        }
    }

    private static void gerenciarMedicamentos() {
        Senior senior = (Senior) usuarioIsLogado;

        System.out.println("\n=== GERENCIAR MEDICAMENTOS ==="); //aqui teria que ser um vetor..
        System.out.println("Suas condições atuais: "+senior.getMedicamentos());

        System.out.println("1. Adicionar Medicamento");
        System.out.println("2. Remover Medicamento");
        System.out.println("3. Voltar");
        System.out.println("Escolha: ");

        int op = input.nextInt();
        input.nextLine();

        switch (op) {
            case 1 -> {
                System.out.println("Nova Medicamento: ");
                String medicamento = input.nextLine();
                senior.addMedicamento(medicamento);
                System.out.println("Medicamento adicionada com sucesso!");
            }
            case 2 -> {
                System.out.println("Medicamento a remover: ");
                String medicamento = input.nextLine();
                senior.getMedicamentos().remove(medicamento);
                System.out.println("Medicamento removida com sucesso");
            }
            case 3 -> {return;}
            default -> System.out.println("Opção invalida");
        }
    }

    private static void gerenciarEspecialidade() {
        Estudante estudante = (Estudante) usuarioIsLogado;

        System.out.println("\n=== GERENCIAR ESPECIALIDADE ===");

        System.out.println("Suas especialidades atuais: "+estudante.getEspecialidades());

        System.out.println("1. Adicionar Especialidade");
        System.out.println("2. Remover Especialidade");
        System.out.println("3. Voltar");
        System.out.println("Escolha: ");

        int op = input.nextInt();
        input.nextLine();

        switch (op) {
            case 1 -> {
                System.out.println("Nova Especialidade: ");
                String especialidade = input.nextLine();
                estudante.adcEspecialidade(especialidade);
                System.out.println("Especialidade adicionada com sucesso!");
            }
            case 2 -> {
                System.out.println("Especialidade a remover: ");
                String especialidade = input.nextLine();
                estudante.getEspecialidades().remove(especialidade);
                System.out.println("Especialidade removida com sucesso");
            }
            case 3 -> {return;}
            default -> System.out.println("Opção invalida");
        }
    }

    private static void atualizarDisponibilidade() {
        Estudante estudante = (Estudante) usuarioIsLogado;

        System.out.println("Sua disponibilidade atual: "+(estudante.isDisponivel() ? "SIM" : "NÃO"));

        System.out.println("1. Mudar disponibilidade");
        System.out.println("2. Voltar");
        int op = input.nextInt();
        input.nextLine();

        while (op != 1 || op != 2) {
            System.out.println("Opção invalida");
            System.out.println("1. Ficar disponivel");
            System.out.println("2. Ficar indiponivel");
            System.out.println("3. Voltar");
            op = input.nextInt();
            input.nextLine();

            switch (op) {
                case 1 -> {
                    estudante.setDisponivel(true);
                    System.out.println("Disponibilidade atualizada com sucesso!");
                    System.out.println("Sua disponibilidade: "+estudante.isDisponivel());
                }
                case 2 -> {
                    estudante.setDisponivel(false);
                    System.out.println("Disponibilidade atualizada com sucesso!");
                    System.out.println("Sua disponibilidade: "+estudante.isDisponivel());
                }
                case 3 -> {return;}
                default -> System.out.println("Opção invalida");
            }
        }
    }

    private static void agendarConsulta() {
        System.out.println("\n=== AGENDAR CONSULTA ===");

        var estudantesDiponiveis = gerenciadorUsuarios.listarEstudantes().stream()
                .filter(Estudante::isDisponivel)
                .toList();

        if(estudantesDiponiveis.isEmpty()) {
            System.out.println("Nenhum estudante disponivel para agendamento ");
            return;
        }

        estudantesDiponiveis.forEach(Estudante::exibirPerfil);

        System.out.println("ID do estudante: ");
        String estudanteId = input.nextLine();

        var estudanteOpt = gerenciadorUsuarios.buscarPorId(estudanteId);
        if(estudanteOpt.isEmpty() || !(estudanteOpt.get() instanceof Estudante)) {
            System.out.println("Estudante não encotrado");
            return;
        }

        Estudante estudante = (Estudante) estudanteOpt.get();

        if(!estudante.isDisponivel()) {
            System.out.println("Este estudante não está disponivel no momento");
            return;
        }

        System.out.print("Data e Hora (AAAA-MM-DDTHH:MM): ");
        LocalDateTime dataHora = LocalDateTime.parse(input.nextLine());

        System.out.print("Tipo da consulta: ");
        String tipoConsulta = input.nextLine();

        String consultaId = "C" + System.currentTimeMillis();
        Consulta consulta = gerenciadorCunsulta.agendarConsulta(consultaId,
                (Senior) usuarioIsLogado, estudante, dataHora, tipoConsulta);

        System.out.println("Consulta agendada com sucesso!");
        consulta.exibirDetalhes();
    } //Atualizei

    private static void minhasConsultasSenior() {
        System.out.println("\n=== MINHAS CONSULTAS ===");
        var consultas = gerenciadorCunsulta.getConsultarSenior(usuarioIsLogado.getId());

        if (consultas.isEmpty()) {
            System.out.println("Nenhum consulta encontrado");
        } else {
            consultas.forEach(Consulta::exibirDetalhes);
        }
    }

    private static void minhasConsultasEstudante() {
        System.out.println("\n=== MINHAS CONSULTAS ===");
        var consultas = gerenciadorCunsulta.getConsultarEstudante(usuarioIsLogado.getId());

        if (consultas.isEmpty()) {
            System.out.println("Nenhum consulta encontrado");
        } else {
            consultas.forEach(Consulta::exibirDetalhes);
        }
    }

    private static void chatComEstudantes() {
        System.out.println("\n=== CHAT ESTUDANTES ===");
        var estudantes = gerenciadorUsuarios.listarEstudantes();

        if (estudantes.isEmpty()) {
            System.out.println("Nenhum estudante encontrado");
            return;
        }

        estudantes.forEach(Estudante::exibirPerfil);

        System.out.println("ID do estudante para conversa:");
        String estudanteId = input.nextLine();

        var estudanteOpt = gerenciadorUsuarios.buscarPorId(estudanteId);
        if (estudanteOpt.isEmpty() || !(estudanteOpt.get() instanceof Estudante)) {
            System.out.println("Estudante não encotrado");
            return;
        }

        Estudante estudante = (Estudante) estudanteOpt.get();
        iniciarChat(estudante, " Estudante");
    } //precisa de mudanças (seus devidos chats no devidos menus)

    private static void chatComSeniors() {
        System.out.println("=== CHAT SENIORS ===");
        var seniors = gerenciadorUsuarios.listarSeniores().stream()
                .filter(s -> s.getId().equals(usuarioIsLogado.getId()))
                .toList();

        if (seniors.isEmpty()) {
            System.out.println("Nenhum senior encontrado");
            return;
        }

        seniors.forEach(Usuario::exibirPerfil);

        System.out.println("ID do senior para conversa:");
        String seniorId = input.nextLine();

        var seniorOpt = gerenciadorUsuarios.buscarPorId(seniorId);
        if (seniorOpt.isEmpty() || !(seniorOpt.get() instanceof Senior)) {
            System.out.println("Senior não encontrado");
            return;
        }

        Senior senior = (Senior) seniorOpt.get();
        iniciarChat(senior, "Senior");
    } //precisa polir tambem

    private static void iniciarChat(Usuario destinatario, String tipoDestinatario) {
        System.out.println("\n=== CHAT com " + destinatario.getNome() + " (" + tipoDestinatario + ") ===");
        System.out.println("Digite 'sair1' para encerrar o chat");

        // Carregar histórico
        var historico = gerenciadorMsg.getChat(usuarioIsLogado.getId(), destinatario.getId());
        if (!historico.isEmpty()) {
            System.out.println("\n--- Histórico de Mensagens ---");
            historico.forEach(Mensagem::exibirTexto);
            System.out.println("------------\n");
        } else {
            System.out.println("Nenhuma mensagem anterior. Inicie a conversa!\n");
        }

        while (true) {
            System.out.print(usuarioIsLogado.getNome() + ": ");
            String texto = input.nextLine();

            if (texto.equalsIgnoreCase("sair1")) break;
            if (texto.trim().isEmpty()) continue;

            String mensagemId = "MSG" + System.currentTimeMillis();
            gerenciadorMsg.enviarMensagem(mensagemId, usuarioIsLogado, destinatario, texto);
            System.out.println("✓ Mensagem enviada");
        }

        System.out.println("Chat encerrado.");
    }
}