package gui;

import entities.Senior;
import entities.Usuario;
import services.GerenciadorUsuarios;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuSeniorGUI extends JFrame{
    private Senior usuario;
    private GerenciadorUsuarios gerenciador;

    public MenuSeniorGUI(Usuario usuario, GerenciadorUsuarios gerenciador) {
        this.usuario = (Senior) usuario;
        this.gerenciador = gerenciador;
        inicializarComponentes();
        configurarJanela();
    }

    private void inicializarComponentes() {
        //Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30 , 40));
        mainPanel.setBackground(Color.WHITE);

        //Titulo
        ImageIcon logoIcon =  carregarImagem("/resources/logo.png");
        if (logoIcon != null) {
            Image logoRedimensionada = logoIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(logoRedimensionada));
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            logoLabel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
            mainPanel.add(logoLabel);
        }
        JLabel tituloLabel = new JLabel("Nosso canto");
        tituloLabel.setFont(new Font("Calibri", Font.BOLD, 24));
        tituloLabel.setForeground(new Color(0, 102, 204));
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tituloLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10 ,0));

        //Saudation
        JLabel saudacaoLabel = new JLabel("Ola, " + usuario.getNome() + "!");
        saudacaoLabel.setFont(new Font("Calibri", Font.BOLD, 16));
        saudacaoLabel.setForeground(Color.DARK_GRAY);
        saudacaoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        saudacaoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30 ,0));

        //Botton Panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0 , 0));


        //Bottons
        JButton buscarEstudantesButton = criarBotaoMenu(" Buscar Estudantes ");
        JButton agendarConsultaButoon = criarBotaoMenu(" Agendar Consulta ");
        JButton minhasConsultasButton = criarBotaoMenu (" Minha Consultas ");
        JButton chatButton = criarBotaoMenu(" Chat com Estudantes ");
        JButton condiciesSaudeButton = criarBotaoMenu(" Gerenciar Saude ");
        JButton medicamentosButton  = criarBotaoMenu(" Gerenciar Medicamentos ");
        JButton perfilButton = criarBotaoMenu(" Ver meu perfil ");
        JButton sairButton = criarBotaoSair(" Sair da Conta ");

        //Adicionar botoes ao menu + espacamento
        menuPanel.add(buscarEstudantesButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(agendarConsultaButoon);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(minhasConsultasButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(chatButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(condiciesSaudeButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(medicamentosButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(perfilButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        menuPanel.add(sairButton);

        //Adicionar componentes ao main panel
        mainPanel.add(tituloLabel);
        mainPanel.add(saudacaoLabel);
        mainPanel.add(menuPanel);

        getContentPane().setBackground(Color.WHITE);
        add(mainPanel);

        configurarAct(buscarEstudantesButton, agendarConsultaButoon, minhasConsultasButton,
                chatButton, condiciesSaudeButton, medicamentosButton, perfilButton, sairButton);

    }

    private JButton criarBotaoMenu(String texto) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Calibri", Font.PLAIN, 14));
        botao.setBackground(new Color(240, 240, 240));
        botao.setForeground(Color.DARK_GRAY);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setMaximumSize(new Dimension(250, 45));
        botao.setCursor( new Cursor(Cursor.HAND_CURSOR));
        return botao;
    }

    private JButton criarBotaoSair(String texto) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Calibri", Font.BOLD, 14));
        botao.setBackground(new Color(220, 53, 69));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setMaximumSize(new Dimension(200, 40));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return botao;
    }

    private void configurarAct(JButton buscarEstudantesButton, JButton agendarConsultaButoon,
                               JButton minhasConsultasButton, JButton chatButton,
                               JButton condiciesSaudeButton, JButton medicamentosButton,
                               JButton perfilButton, JButton sairButton) {
        buscarEstudantesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MenuSeniorGUI.this,
                        "Buscar Estuntes - Em Desenvolvimento");
            }
        });

        agendarConsultaButoon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MenuSeniorGUI.this,
                        "Agendar Consulta - Em Desenvolvimento");
            }
        });

        minhasConsultasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MenuSeniorGUI.this,
                        "Minhas Consultas - Em Desenvolvimento");
            }
        });

        chatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MenuSeniorGUI.this,
                        "Chat - Em Desenvolvimento");
            }
        });

        condiciesSaudeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MenuSeniorGUI.this,
                        "Modificar Saude - Em Desenvolvimento");
            }
        });

        medicamentosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MenuSeniorGUI.this,
                        "Modificar Medicamentos - Em Desenvolvimento");
            }
        });

        perfilButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mostrar perfil do usuÃ¡rio
                StringBuilder perfil = new StringBuilder();
                perfil.append("=== Meu Perfil ===\n");
                perfil.append("Nome: ").append(usuario.getNome()).append("\n");
                perfil.append("Email: ").append(usuario.getEmail()).append("\n");
                perfil.append("Telefone: ").append(usuario.getTelefone()).append("\n");
                perfil.append("CondiÃ§Ãµes de SaÃºde: ").append(usuario.getCondicaoSaude()).append("\n");
                perfil.append("Medicamentos: ").append(usuario.getMedicamentos());

                JOptionPane.showMessageDialog(MenuSeniorGUI.this,
                        perfil.toString(), "Meu Perfil", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        sairButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(MenuSeniorGUI.this,
                        "Deseja realmente sair da conta?",
                        "Confirmar saida",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    dispose();
                    new loginGUI().setVisible(true);
                }
            }
        });
    }

    private void configurarJanela() {
        setTitle("Nosso Canto - Menu Senior");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private ImageIcon carregarImagem(String caminho) {
        try {
            System.out.println("Tentando carregar: " + caminho);

            // Mostra o diretÃ³rio atual
            java.io.File dir = new java.io.File(".");
            System.out.println("DiretÃ³rio atual: " + dir.getAbsolutePath());

            // Lista arquivos no diretÃ³rio
            System.out.println("Arquivos no diretÃ³rio:");
            for (String file : dir.list()) {
                System.out.println(" - " + file);
            }

            java.net.URL imgURL = getClass().getResource(caminho);
            System.out.println("URL encontrada: " + imgURL);

            if (imgURL != null) {
                return new ImageIcon(imgURL);
            } else {
                System.out.println("IMAGEM NÃƒO ENCONTRADA!");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            return null;
        }
    }
}

