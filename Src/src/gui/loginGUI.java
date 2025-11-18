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
        inicializarComponentes();
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

    private void inicializarComponentes() {
        //Painel principal com borda e padding OKOKOK
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        mainPanel.setBackground(Color.WHITE);

        // titulo label bebes - Set de fonte calibri 20 bold e centralização
        JLabel tituloLabel = new JLabel("Nosso Canto");
        tituloLabel.setFont(new Font("Calibri", Font.BOLD, 24));
        tituloLabel.setForeground(new Color(0, 102, 204));
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tituloLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        //SUBTITULO
        JLabel subtituloLabel = new JLabel("Faça seu login");
        subtituloLabel.setFont(new Font("Calibri", Font.PLAIN, 14));
        subtituloLabel.setForeground(Color.GRAY);
        subtituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtituloLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        //Painel formulario
        JPanel formularioPanel = new JPanel(new GridBagLayout());
        formularioPanel.setBackground(Color.WHITE);
        GridBagConstraints c = new GridBagConstraints(); //grid Bag - restrições de posicionamento
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        //Email
        c.gridx = 0; c.gridy = 0; //define posição do gride na primeira coluna
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Calibri", Font.BOLD, 12));
        formularioPanel.add(emailLabel, c);

        c.gridx = 1; c.gridy = 0;
        c.gridwidth = 2;
        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(200, 30));
        formularioPanel.add(emailField, c);

        //SENHA
        c.gridx = 0; c.gridy = 1;
        c.gridwidth = 1;
        JLabel senhaLabel = new JLabel("Senha: ");
        senhaLabel.setFont(new Font("Calibri", Font.BOLD, 12));
        formularioPanel.add(senhaLabel, c);

        c.gridx = 1; c.gridy = 1;
        c.gridwidth = 2;
        senhaField = new JPasswordField();
        senhaField.setPreferredSize(new Dimension(200, 30));
        formularioPanel.add(senhaField, c);

        //Painel dos botoes click click (na vertical agora)
        JPanel botaoPanel = new JPanel();
        botaoPanel.setLayout(new BoxLayout(botaoPanel, BoxLayout.Y_AXIS));
        botaoPanel.setBackground(Color.WHITE);
        botaoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        //botao de login
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Calibri", Font.BOLD, 14));
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(200, 40));

        //Botao de cadastrar senior
        cadastrarSeniorButton = new JButton("Cadastrar Senior");
        cadastrarSeniorButton.setFont(new Font("Calibri", Font.PLAIN, 12));
        cadastrarSeniorButton.setBackground(new Color(240, 240, 240));
        cadastrarSeniorButton.setFocusPainted(false);
        cadastrarSeniorButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cadastrarSeniorButton.setMaximumSize(new Dimension(200, 35));

        //Botao cadastrar estudante
        cadastrarEstudanteButton = new JButton("Cadastrar Estudante");
        cadastrarEstudanteButton.setFont(new Font("Calibri", Font.PLAIN, 12));
        cadastrarEstudanteButton.setBackground(new Color(240, 240, 240));
        cadastrarEstudanteButton.setFocusPainted(false);
        cadastrarEstudanteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cadastrarEstudanteButton.setMaximumSize(new Dimension(200, 35));

        //Adicionar os boteos + espaçamento
        botaoPanel.add(loginButton);
        botaoPanel.add(Box.createRigidArea(new Dimension(0, 10))); //espaço entre os bonitinhos
        botaoPanel.add(cadastrarSeniorButton);
        botaoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        botaoPanel.add(cadastrarEstudanteButton);

        //Adicionar componentes ao main painel
        mainPanel.add(tituloLabel);
        mainPanel.add(subtituloLabel);
        mainPanel.add(formularioPanel);
        mainPanel.add(botaoPanel);

        //Conteudo da janela toda
        getContentPane().setBackground(Color.WHITE);
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
        setTitle("Nosso canto");
        setSize(400, 500);
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