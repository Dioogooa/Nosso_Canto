import entities.*;
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
        Senior senior = new Senior("S1", "AntÃ´nio Fagundes", "ser@", "Sen123",
                "(62)98165-9834", LocalDate.of(1955, 2, 18), "901.785.901-31",
                "Rua A, 900, Jardim Luz", "(72)95678-3190", false);
        senior.addCondicaoSaude("Alzheimer ");
        senior.addMedicamento("Kisunla (donanemabe)");
        senior.addMedicamento("Ã“leo de canabidiol");

        Estudante estudante = new Estudante("E1", "Pedro Santiago", "est@",
                "est123", "(21)98990-1254", LocalDate.of(2003, 9, 19),
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
        System.out.println("2. Cadastrar SÃªnior");
        System.out.println("3. Cadastrar Estudante");
        System.out.println("4. Sair");
        System.out.print("Escolha uma opÃ§Ã£o: ");

        op = input.nextInt();
        input.nextLine();
        limparTela();

        switch (op) {
            case 1 -> fazerLogin();
            case 2 -> cadastrarSenior();
            case 3 -> cadastrarEstudante();
            case 4 -> {
                System.out.println("Obrigado por usar o Nosso Canto!");
                System.exit(0);
            }
            default -> System.out.println("OpÃ§Ã£o invalida :/");
        }
    }

    private static void exibirMenuUsuario() {

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
        System.out.println("5. Gerenciar CondiÃ§Ã£o de SaÃºde");
        System.out.println("6. Gerenciar Medicamentos");
        System.out.println("7. Ver meu perfil");
        System.out.println("8. Sair da conta");
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
            case 8 -> {
                limparTela();
                usuarioIsLogado = null;
            }
            default -> System.out.println("OpÃ§Ã£o invalida :/");
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
            case 7 -> {
                limparTela();
                usuarioIsLogado = null;
            }
            default -> System.out.println("OpaÃ§Ã£o invalida :/");
        }
    }

    private static void fazerLogin() {

        System.out.println("\n=== LOGIN ===");
        System.out.print("Email: ");
        String email = input.nextLine().trim();
        System.out.print("Senha: ");
        String senha = input.nextLine().trim();

        var usuario = gerenciadorUsuarios.fazerLogin(email, senha); //Var Ã© varivael temporaria
        if (usuario.isPresent()) {
            usuarioIsLogado = usuario.get();
            limparTela();
            System.out.println("\nLogin realizado com sucesso!");
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

        System.out.println("Tem acompanhante (sim / nÃ£o) - ");
        String resposta = input.nextLine().toLowerCase(); //valida resposta, pro usuario n ter que digitar true/false
        boolean temAcompanhante = resposta.equals("sim") || resposta.equals("s") || resposta.equals("si");
        //coloca true caso for "sim" , "s" ou "si".. Caso contrario, vira false.

        Senior senior = new Senior(id, nome, email, senha, telefone, dataNascimento, cpf,
                endereco, contato, temAcompanhante);

        gerenciadorUsuarios.cadastrarUsuario(senior);
        System.out.println("Senior cadastrado com sucesso!");

        //ADICIONAR REMEDIOS E CONDIÃ‡ÃƒO DE SAÃšDE COMO METODO NO MENU SENIOR, AQUI NO CADASTRO NÃƒO!!!!!! ----- LEMBRAR!!
    }

    public static void cadastrarEstudante() { //fazer aqui, precisa cadastrar infos alÃ©m de email e senha
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

        System.out.print("InstituiÃ§Ã£o estudantil: ");
        String instituicao = input.nextLine();

        System.out.print("Curso: ");
        String curso = input.nextLine();

        System.out.println("Periodo: ");
        int semestre = Integer.parseInt(input.nextLine());

        boolean disponivel = false;

        Estudante estudante = new Estudante(id, nome, email, senha, telefone, dataNascimento, cpf,
                endereco, instituicao, curso, semestre, disponivel);

        gerenciadorUsuarios.cadastrarUsuario(estudante);
        System.out.println("Estudante cadastrado com sucesso!");

        //Lembrar de colocar no Menu ESTUDANTE, ficar disponivel!!! TambÃ©m cadastro de especialidades como metodo
    }

    private static void buscarEstudantes() {
        limparTela();
        System.out.println("\n=== ESTUDANTES DISPONIVEIS ===");
        var estudantes = gerenciadorUsuarios.listarEstudantes().stream()
                .filter(Estudante::isDisponivel)
                .toList();

        if (estudantes.isEmpty()) {
            System.out.println("Nenhum estudante encontrado");
        } else {
            estudantes.forEach(Estudante::exibirPerfilReduzido);
        }
    }

    private static void buscarSenior() {

        limparTela();
        System.out.println("\n=== SENIORES DISPONIVEIS ===");
        var seniors = gerenciadorUsuarios.listarSeniores();

        if (seniors.isEmpty()) {
            System.out.println("Nenhum senior encontrado");
        } else {
            seniors.forEach(Senior::exibirPerfil);
        }
    } //

    private static void gerenciarCondicoesSaude() {
        Senior senior = (Senior) usuarioIsLogado;

        System.out.println("\n=== GERENCIAR CONDIÃ‡Ã•ES DE SAÃšDE ==="); //aqui teria que ser um vetor..
        System.out.println("Suas condiÃ§Ãµes atuais: "+senior.getCondicaoSaude());

        System.out.println("1. Adicionar condiÃ§Ã£o");
        System.out.println("2. Remover condiÃ§Ã£o");
        System.out.println("3. Voltar");
        System.out.println("Escolha: ");

        int op = input.nextInt();
        input.nextLine();

        switch (op) {
            case 1 -> {
                System.out.println("Nova CondiÃ§Ã£o de saÃºde: ");
                String condicao = input.nextLine();
                senior.addCondicaoSaude(condicao);
                limparTela();
                System.out.println("CondiÃ§Ã£o adicionada com sucesso!");
            }
            case 2 -> {
                System.out.println("CondiÃ§Ã£o a remover: ");
                String condicao = input.nextLine();
                senior.getCondicaoSaude().remove(condicao);
                limparTela();
                System.out.println("CondiÃ§Ã£o removida com sucesso");
            }
            case 3 -> {return;}
            default -> System.out.println("OpÃ§Ã£o invalida");
        }
    }

    private static void gerenciarMedicamentos() {
        Senior senior = (Senior) usuarioIsLogado;

        System.out.println("\n=== GERENCIAR MEDICAMENTOS ==="); //aqui teria que ser um vetor..
        System.out.println("Suas condiÃ§Ãµes atuais: "+senior.getMedicamentos());

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
            default -> System.out.println("OpÃ§Ã£o invalida");
        }
    }

    private static void gerenciarEspecialidade() {
        Estudante estudante = (Estudante) usuarioIsLogado;

        System.out.println("\n=== GERENCIAR ESPECIALIDADE ===");

        System.out.println("Suas especialidades atuais: "+estudante.getEspecialidades());

        System.out.println("1. Adicionar Especialidade");
        System.out.println("2. Remover Especialidade");
        System.out.println("3. Voltar");
        System.out.print("Escolha: ");

        int op = input.nextInt();
        input.nextLine();
        limparTela();

        switch (op) {
            case 1 -> {
                System.out.print("Nova Especialidade: ");
                String especialidade = input.nextLine();
                estudante.adcEspecialidade(especialidade);
                System.out.println("Especialidade adicionada com sucesso!");
            }
            case 2 -> {
                System.out.print("Especialidade a remover: ");
                String especialidade = input.nextLine();
                estudante.getEspecialidades().remove(especialidade);
                System.out.println("Especialidade removida com sucesso");
            }
            case 3 -> {return;}
            default -> System.out.println("OpÃ§Ã£o invalida");
        }
    }

    private static void atualizarDisponibilidade() {
        Estudante estudante = (Estudante) usuarioIsLogado;

        System.out.println("\n=== ATUALIZAR DISPONIBILIDADE ===");
        System.out.println("Sua disponibilidade atual: "+(estudante.isDisponivel() ? "SIM" : "NÃƒO"));

        System.out.println("1. Mudar disponibilidade");
        System.out.println("2. Voltar");
        System.out.print("Escolha -> ");
        int op = input.nextInt();
        input.nextLine();

        while (op < 1 || op > 2) {
            System.out.println("OpÃ§Ã£o invalida");
            System.out.println("1. Ficar disponivel");
            System.out.println("2. Ficar indiponivel");
            System.out.println("3. Voltar");
            System.out.println("Escolha: ");
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
                default -> System.out.println("OpÃ§Ã£o invalida");
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

        estudantesDiponiveis.forEach(Estudante::exibirPerfilReduzido);

        System.out.println("ID do estudante: ");
        String estudanteId = input.nextLine();

        var estudanteOpt = gerenciadorUsuarios.buscarPorId(estudanteId);
        if(estudanteOpt.isEmpty() || !(estudanteOpt.get() instanceof Estudante)) {
            System.out.println("Estudante nÃ£o encotrado");
            return;
        }

        Estudante estudante = (Estudante) estudanteOpt.get();

        if(!estudante.isDisponivel()) {
            System.out.println("Este estudante nÃ£o estÃ¡ disponivel no momento");
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
    }

    private static void minhasConsultasSenior() {
        System.out.println("\n=== MINHAS CONSULTAS ===");
        var consultas = gerenciadorCunsulta.getConsultarSenior(usuarioIsLogado.getId());

        if (consultas.isEmpty()) {
            System.out.println("Nenhuma consulta encontrada!");
        } else {
            consultas.forEach(Consulta::exibirDetalhes);
        }
    }

    private static void minhasConsultasEstudante() {
        System.out.println("\n=== MINHAS CONSULTAS ===");
        var consultas = gerenciadorCunsulta.getConsultarEstudante(usuarioIsLogado.getId());

        if (consultas.isEmpty()) {
            System.out.println("Nenhuma consulta encontrada!");
        } else {
            consultas.forEach(Consulta::exibirDetalhes);
        }
    }

    private static void chatComEstudantes() {
        limparTela();
        System.out.println("\n=== CHAT ESTUDANTES ===");
        var estudantes = gerenciadorUsuarios.listarEstudantes();

        if (estudantes.isEmpty()) {
            System.out.println("Nenhum estudante encontrado");
            return;
        }

        estudantes.forEach(Estudante::exibirPerfilReduzido);

        System.out.print("\nID do estudante para conversa:");
        String estudanteId = input.nextLine();

        var estudanteOpt = gerenciadorUsuarios.buscarPorId(estudanteId);
        if (estudanteOpt.isEmpty() || !(estudanteOpt.get() instanceof Estudante)) {
            System.out.println("Estudante nÃ£o encotrado");
            return;
        }

        Estudante estudante = (Estudante) estudanteOpt.get();
        iniciarChat(estudante, " Estudante");
    } //precisa de mudanÃ§as (seus devidos chats no devidos menus)

    private static void chatComSeniors() {

        limparTela();
        System.out.println("\n=== CHAT SENIORS ===");
        var seniors = gerenciadorUsuarios.listarSeniores().stream()
                .filter(s -> !s.getId().equals(usuarioIsLogado.getId()))
                .toList();

        if (seniors.isEmpty()) {
            System.out.println("Nenhum senior encontrado");
            return;
        }

        seniors.forEach(Senior::exibirPerfil);

        System.out.println("\n '1' para listar seniores!");
        System.out.print("ID do senior para conversa: ");
        String seniorId = input.nextLine();

        if (seniorId.equals("1")) { //testar se sai
            gerenciadorUsuarios.listarSeniores();
        }

        var seniorOpt = gerenciadorUsuarios.buscarPorId(seniorId);
        if (seniorOpt.isEmpty() || !(seniorOpt.get() instanceof Senior)) {
            System.out.println("Senior nÃ£o encontrado");
            return;
        }

        Senior senior = (Senior) seniorOpt.get();
        iniciarChat(senior, "Senior");
    }

    private static void iniciarChat(Usuario destinatario, String tipoDestinatario) {
        limparTela();
        System.out.println("\n=== CHAT com " + destinatario.getNome() + " (" + tipoDestinatario + ") ===");
        System.out.println("Digite 'sair1' para encerrar o chat");

        // Carregar histÃ³rico
        var historico = gerenciadorMsg.getChat(usuarioIsLogado.getId(), destinatario.getId());
        if (!historico.isEmpty()) {
            System.out.println("\n--- HistÃ³rico de Mensagens ---");
            historico.forEach(Mensagem::exibirTexto);
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
            System.out.println("Enviada!");
        }

        System.out.println("Chat encerrado.");
        limparTela();
    }

    //public static LocalDateTime entradaAjustadaDataHora() {
    //}

    public static void limparTela() {
        try {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                // Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Linux, macOS, Unix
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

