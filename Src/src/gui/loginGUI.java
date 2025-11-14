package gui;

import entites.Estudante;
import entites.Senior;
import entites.Usuario;
import services.GerenciadorUsuarios;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Optional;

public class loginGUI extends JFrame {
    private GerenciadorUsuarios gerenciadorUsuarios;
    private JTextField emailField;
    private JPasswordField senhaField;
    private JButton loginButton;
    private JButton cadastrarSeniorButton;
    private JButton cadastrarEstudanteButton;

    public loginGUI() {
        this.gerenciadorUsuarios = new GerenciadorUsuarios();
        cadastrarExemplos();
        inicilizarComponentes();
        configurarJanela();
    }

    private void cadastrarExemplos() {
        try {
            Senior senior = new Senior("S1", "Antônio Fagundes", "ser@", "Sen123",
                    "(62)98165-9834", LocalDate.of(1955, 2, 18), "901.785.901-31",
                    "Rua A, 900, Jardim Luz", "(72)95678-3190", false);
            senior.addCondicaoSaude("Alzheimer ");
            senior.addMedicamento("Kisunla (donanemabe)");
            senior.addMedicamento("Óleo de canabidiol");

            Estudante estudante = new Estudante("E1", "Pedro Santiago", "est@",
                    "est123", "(21)98990-1254", LocalDate.of(2003, 9, 19),
                    "900-800-700-65", "Rua flamengo, 177, Flamengo", "UFRJ", "Psicologia",
                    7, true);
            estudante.adcEspecialidade("Psicologo humanista");

            gerenciadorUsuarios.cadastrarUsuario(senior);
            gerenciadorUsuarios.cadastrarUsuario(estudante);

            System.out.println("Usuarios de exemplo cadastrados!");
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar exemplos: " + e.getMessage());
        }
    }

    private void inicilizarComponentes() {
        //Painel principal com borda e padding OKOKOK
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // titulo label bebes - Set de fonte calibri 20 bold e centralização
        JLabel tituloLabel = new JLabel("Nosso Canto - Login");
        tituloLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        tituloLabel.setHorizontalAlignment(SwingConstants.CENTER);

        //Painel formulario
        JPanel formularioPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        JLabel emailLabel = new JLabel("Email: ");
        emailField = new JTextField();

        JLabel senhaLabel = new JLabel("Senha: ");
        senhaField = new JPasswordField();

        formularioPanel.add(emailLabel);
        formularioPanel.add(emailField);
        formularioPanel.add(senhaLabel);
        formularioPanel.add(senhaField);

        //Painel dos botoes click click
        JPanel botaoPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        loginButton = new JButton("Login");
        cadastrarSeniorButton = new JButton("Cadastrar como Senior");
        cadastrarEstudanteButton = new JButton("Cadastrar como Estudante");

        botaoPanel.add(loginButton);
        botaoPanel.add(cadastrarSeniorButton);
        botaoPanel.add(cadastrarEstudanteButton);

        //colocar os componetes no painel main
        mainPanel.add(tituloLabel, BorderLayout.NORTH); //titulo encima
        mainPanel.add(formularioPanel, BorderLayout.CENTER); //formulario no mei
        mainPanel.add(botaoPanel, BorderLayout.SOUTH); //botoes abaixo

        add(mainPanel);
        configAcoes();
    }

    private void configAcoes() {
        loginButton.addActionListener(new ActionListener() { //adiciona lista (no caso so uma aq rs) de acoes ao botao de login
            @Override
            public void actionPerformed(ActionEvent e) {
                fazerLogin();
            }
        });

        cadastrarSeniorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastroSenior();
            }
        });

        cadastrarEstudanteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastroEstudante();
            }
        });

        senhaField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fazerLogin();
            }
        });
    }

    private void fazerLogin() {
        try {
            String email = emailField.getText().trim(); //so p lembrar .trim() remove espaços em branco da String (melhor qualidade de entrada qnd n pode espaço)
            String senha = new String(senhaField.getPassword()).trim();

            //debugs

            //VALIDA E VALIDA
            if (email.isEmpty() || senha.isEmpty()) { //se não preencher os campos da erro
                JOptionPane.showMessageDialog(this,
                        "Preencha todos os campos",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            //erro se o email n conter @ (pode melhorar) - (quem ler isso, '!' é negação de algo tmj)
            if (!email.contains("@")) {
                JOptionPane.showMessageDialog(this,
                        "Email invalido!",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            //Validar no backend bebes, services.GerenciadorUsuarios - validação de front BASICA, campos preenchidos e @
            Optional<Usuario> usuarioOpt = gerenciadorUsuarios.fazerLogin(email, senha);

            if (usuarioOpt.isPresent()) { //Verifica se a strem é nula
                Usuario usuarioLogado = usuarioOpt.get();
                JOptionPane.showMessageDialog(this,
                        "Login realizado com sucesso!\nBem-vindo" + usuarioLogado.getNome() + " !",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);

                abrirMainMenu(usuarioLogado);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Email ou senha incorretos",
                        "Erro no login",
                        JOptionPane.ERROR_MESSAGE);

                //Limpa campos em caso de erro no front
                senhaField.setText("");
                emailField.requestFocus();
            }

        } catch (Exception exc) {
            JOptionPane.showMessageDialog(this,
                    "Erro: " + exc.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            exc.printStackTrace(); //detalha o erro
        }
    }

    private void abrirMainMenu(Usuario usuario) {
        //PRIMEIRO DE TUDO, fechar tela de login
        this.dispose(); //metodo swing para fechar janelas!!!!!

        //Logica para abrir os menus
        SwingUtilities.invokeLater(new Runnable() { //invoker later coloca a execução na fila, coisa pos a outra, organiza ações
            @Override
            public void run() {
                if (usuario.getTipoUsuario().equals("Senior")) {
                    //new MenuSeniorGUI(usuario, gerenciadorUsuarios).setVisible(true);
                    JOptionPane.showMessageDialog(null,
                            "Menu senior ainda em desenvolvimento");
                } else {
                    //new MenuEstudanteGUI(usuario, gerenciadorUsuarios).setVisible(true);
                    JOptionPane.showMessageDialog(null,
                            "Menu estudante ainda em desenvolvimento");
                }
            }
        });
    }

    private void cadastroEstudante() {
        //fecha login e abre cadastro
        this.dispose();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //new CadastroEstudanteGUI(gerenciadorUsuarios).setVisible(true);
                JOptionPane.showMessageDialog(null,
                        "Cadastro de estudante ainda em desenvolvimento");
            }
        });
    }

    private void cadastroSenior() {
        //fecha login e abre cadastro senior
        this.dispose();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //new CadastroSeniorGUI(gerenciadorUsuarios).setVisible(true);
                JOptionPane.showMessageDialog(null,
                        "Cadastro de senior ainda em desenvolvimento");
            }
        });
    }

    private void configurarJanela() {
        setTitle("Nosso canto - Login");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new loginGUI().setVisible(true);
            }
        });
    }
}