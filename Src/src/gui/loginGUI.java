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
        inicializarComponentes();
        configurarJanela();
    }

    private void inicializarComponentes() {
        //Painel principal com borda e padding OKOKOK
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        mainPanel.setBackground(Color.WHITE);

        //Imagem
        ImageIcon logoIcon =  carregarImagem("/resources/logo.png");
        if (logoIcon != null) {
            Image logoRedimensionada = logoIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(logoRedimensionada));
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            logoLabel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
            mainPanel.add(logoLabel);
        }

        // titulo label bebes - Set de fonte calibri 20 bold e centralizaÃ§Ã£o
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
        GridBagConstraints c = new GridBagConstraints(); //grid Bag - restriÃ§Ãµes de posicionamento
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        //Email
        c.gridx = 0; c.gridy = 0; //define posiÃ§Ã£o do gride na primeira coluna
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Calibri", Font.BOLD, 16));
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
        senhaLabel.setFont(new Font("Calibri", Font.BOLD, 16));
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
        loginButton.setFont(new Font("Calibri", Font.BOLD, 18));
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

        //Adicionar os boteos + espaÃ§amento
        botaoPanel.add(loginButton);
        botaoPanel.add(Box.createRigidArea(new Dimension(0, 10))); //espaÃ§o entre os bonitinhos
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
            String email = emailField.getText().trim(); //so p lembrar .trim() remove espaÃ§os em branco da String (melhor qualidade de entrada qnd n pode espaÃ§o)
            String senha = new String(senhaField.getPassword()).trim();

            //debugs

            //VALIDA E VALIDA
            if (email.isEmpty() || senha.isEmpty()) { //se nÃ£o preencher os campos da erro
                JOptionPane.showMessageDialog(this,
                        "Preencha todos os campos",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            //erro se o email n conter @ (pode melhorar) - (quem ler isso, '!' Ã© negaÃ§Ã£o de algo tmj)
            if (!email.contains("@")) {
                JOptionPane.showMessageDialog(this,
                        "Email invalido!",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            //Validar no backend bebes, services.GerenciadorUsuarios - validação de front BASICA, campos preenchidos e @
            Optional<Usuario> usuarioOpt = gerenciadorUsuarios.fazerLogin(email, senha);

            if (usuarioOpt.isPresent()) { //Verifica se a strem é nao nula
                Usuario usuarioLogado = usuarioOpt.get();
                JOptionPane.showMessageDialog(this,
                        "Login realizado com sucesso!\nBem-vindo, " + usuarioLogado.getNome() + " !",
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
        SwingUtilities.invokeLater(new Runnable() { //invoker later coloca a execuÃ§Ã£o na fila, coisa pos a outra, organiza aÃ§Ãµes
            @Override
            public void run() {
                if (usuario.getTipoUsuario().equals("Senior")) {
                    new menuSeniorGUI(usuario, gerenciadorUsuarios).setVisible(true);
                } else {
                    new MenuEstudanteGUI(usuario, gerenciadorUsuarios).setVisible(true);
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
                new CadastroEstudanteGUI(gerenciadorUsuarios).setVisible(true);
            }
        });
    }

    private void cadastroSenior() {
        //fecha login e abre cadastro senior
        this.dispose();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CadastroSeniorGUI(gerenciadorUsuarios).setVisible(true);
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

    private ImageIcon carregarImagem(String caminho) {
        try {
            System.out.println("Procurando imagem em: " + caminho);

            // Tenta vários caminhos possíveis
            java.net.URL imgURL = getClass().getResource(caminho);

            if (imgURL == null) {
                // Tenta caminho alternativo (sem a barra inicial)
                imgURL = getClass().getResource(caminho.substring(1));
            }

            if (imgURL == null) {
                // Tenta como arquivo local
                try {
                    java.io.File file = new java.io.File("src" + caminho);
                    if (file.exists()) {
                        imgURL = file.toURI().toURL();
                        System.out.println("Imagem encontrada como arquivo: " + file.getAbsolutePath());
                    }
                } catch (Exception e) {
                    System.out.println("Arquivo local não encontrado");
                }
            }

            if (imgURL != null) {
                System.out.println("Imagem carregada: " + imgURL);
                return new ImageIcon(imgURL);
            } else {
                System.out.println("Imagem NÃO encontrada em nenhum caminho: " + caminho);
                // Retorna um ícone vazio para evitar NullPointerException
                return criarIconeVazio();
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar imagem: " + e.getMessage());
            return criarIconeVazio();
        }
    }

    private ImageIcon criarIconeVazio() {
        // Cria um ícone vazio para evitar NullPointerException
        java.awt.Image imagemVazia = new java.awt.image.BufferedImage(60, 60, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        return new ImageIcon(imagemVazia);
    }
}