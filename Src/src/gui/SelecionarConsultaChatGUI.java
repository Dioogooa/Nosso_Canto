package gui;

import entites.Consulta;
import entites.Usuario;
import services.GerenciadorCunsultas;
import services.GerenciadorUsuarios;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SelecionarConsultaChatGUI extends JFrame {
    private Usuario usuarioLogado;
    private GerenciadorUsuarios gerenciadorUsuarios;
    private GerenciadorCunsultas gerenciadorConsultas;
    private String tipoUsuario;
    private JPanel consultasPanel;

    public SelecionarConsultaChatGUI(Usuario usuario, GerenciadorUsuarios gerenciador, String tipoUsuario) {
        this.usuarioLogado = usuario;
        this.gerenciadorUsuarios = gerenciador;
        this.gerenciadorConsultas = new GerenciadorCunsultas();
        this.tipoUsuario = tipoUsuario;
        inicializarComponentes();
        configurarJanela();
        carregarConsultasParaChat();
    }

    private void inicializarComponentes() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Cabeçalho
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);

        JLabel tituloLabel = new JLabel("Selecionar Consulta para Chat");
        tituloLabel.setFont(new Font("Calibri", Font.BOLD, 24));
        tituloLabel.setForeground(new Color(0, 102, 204));
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtituloLabel = new JLabel("Escolha uma consulta para iniciar a conversa");
        subtituloLabel.setFont(new Font("Calibri", Font.PLAIN, 14));
        subtituloLabel.setForeground(Color.GRAY);
        subtituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(tituloLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(subtituloLabel);

        // Painel de consultas (com scroll)
        consultasPanel = new JPanel();
        consultasPanel.setLayout(new BoxLayout(consultasPanel, BoxLayout.Y_AXIS));
        consultasPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(consultasPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Consultas Disponíveis para Chat"));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        botoesPanel.setBackground(Color.WHITE);

        JButton voltarButton = new JButton("Voltar ao Menu");
        voltarButton.setFont(new Font("Calibri", Font.BOLD, 14));
        voltarButton.setBackground(new Color(240, 240, 240));
        voltarButton.setFocusPainted(false);
        voltarButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        voltarButton.addActionListener(e -> voltarMenu());

        botoesPanel.add(voltarButton);

        // Adicionar componentes
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(botoesPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void carregarConsultasParaChat() {
        // LIMPAR O PAINEL ANTES DE RECARREGAR
        consultasPanel.removeAll();

        // Buscar consultas do usuário
        List<Consulta> consultas;
        if ("SENIOR".equals(tipoUsuario)) {
            consultas = gerenciadorConsultas.getConsultarSenior(usuarioLogado.getId());
        } else {
            consultas = gerenciadorConsultas.getConsultarEstudante(usuarioLogado.getId());
        }

        System.out.println("DEBUG - Total de consultas encontradas para chat: " + consultas.size());

        // Filtrar apenas consultas que podem ter chat (SOLICITADA, AGENDADA)
        List<Consulta> consultasParaChat = consultas.stream()
                .filter(c -> "SOLICITADA".equals(c.getStatus()) || "AGENDADA".equals(c.getStatus()))
                .toList();

        if (consultasParaChat.isEmpty()) {
            JLabel semConsultasLabel = new JLabel("Nenhuma consulta disponível para chat");
            semConsultasLabel.setFont(new Font("Calibri", Font.ITALIC, 14));
            semConsultasLabel.setForeground(Color.GRAY);
            semConsultasLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            consultasPanel.add(semConsultasLabel);

            JLabel instrucaoLabel = new JLabel("Aguarde confirmação ou agende uma consulta primeiro.");
            instrucaoLabel.setFont(new Font("Calibri", Font.PLAIN, 12));
            instrucaoLabel.setForeground(Color.GRAY);
            instrucaoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            consultasPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            consultasPanel.add(instrucaoLabel);
        } else {
            for (Consulta consulta : consultasParaChat) {
                consultasPanel.add(criarCardConsultaChat(consulta));
                consultasPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        consultasPanel.revalidate();
        consultasPanel.repaint();
    }

    private JPanel criarCardConsultaChat(Consulta consulta) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout(10, 5));

        // Cor baseada no status
        Color corBorda = "AGENDADA".equals(consulta.getStatus()) ?
                new Color(0, 150, 0) : new Color(255, 165, 0);
        Color corFundo = "AGENDADA".equals(consulta.getStatus()) ?
                new Color(240, 255, 240) : new Color(255, 250, 240);

        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(corBorda, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        cardPanel.setBackground(corFundo);
        cardPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Informações da consulta
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(corFundo);

        // Definir o outro usuário baseado no tipo
        String outroUsuarioNome;
        String outroUsuarioInfo;

        if ("SENIOR".equals(tipoUsuario)) {
            outroUsuarioNome = consulta.getEstudante().getNome();
            outroUsuarioInfo = consulta.getEstudante().getCurso();
        } else {
            outroUsuarioNome = consulta.getSenior().getNome();
            outroUsuarioInfo = "Senior";
        }

        JLabel usuarioLabel = new JLabel(outroUsuarioNome);
        usuarioLabel.setFont(new Font("Calibri", Font.BOLD, 16));
        usuarioLabel.setForeground(corBorda);

        JLabel infoLabel = new JLabel(outroUsuarioInfo);
        infoLabel.setFont(new Font("Calibri", Font.PLAIN, 14));

        JLabel dataLabel = new JLabel("Data: " + consulta.getDataHora().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")));
        dataLabel.setFont(new Font("Calibri", Font.PLAIN, 12));

        JLabel statusLabel = new JLabel("Status: " +
                ("AGENDADA".equals(consulta.getStatus()) ? "CONFIRMADA" : "AGUARDANDO CONFIRMAÇÃO"));
        statusLabel.setFont(new Font("Calibri", Font.BOLD, 12));
        statusLabel.setForeground(corBorda);

        infoPanel.add(usuarioLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(infoLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        infoPanel.add(dataLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(statusLabel);

        // Botão de abrir chat
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(corFundo);

        JButton chatButton = new JButton("Abrir Chat");
        chatButton.setFont(new Font("Calibri", Font.BOLD, 12));
        chatButton.setBackground(new Color(0, 102, 204));
        chatButton.setForeground(Color.WHITE);
        chatButton.setFocusPainted(false);
        chatButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        chatButton.setPreferredSize(new Dimension(100, 35));

        chatButton.addActionListener(e -> abrirChat(consulta));

        buttonPanel.add(chatButton);

        // Clicar no card também abre o chat
        cardPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                abrirChat(consulta);
            }
        });

        cardPanel.add(infoPanel, BorderLayout.CENTER);
        cardPanel.add(buttonPanel, BorderLayout.EAST);

        return cardPanel;
    }

    private void abrirChat(Consulta consulta) {
        try {
            // Determinar quem é o outro usuário
            Usuario outroUsuario;
            if ("SENIOR".equals(tipoUsuario)) {
                outroUsuario = consulta.getEstudante();
            } else {
                outroUsuario = consulta.getSenior();
            }

            new ChatGUI(usuarioLogado, outroUsuario, consulta).setVisible(true);
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao abrir chat: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void voltarMenu() {
        this.dispose();
        if ("SENIOR".equals(tipoUsuario)) {
            new menuSeniorGUI(usuarioLogado, gerenciadorUsuarios).setVisible(true);
        } else {
            new MenuEstudanteGUI(usuarioLogado, gerenciadorUsuarios).setVisible(true);
        }
    }

    private void configurarJanela() {
        setTitle("Nosso Canto - Selecionar Chat");
        setSize(550, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
    }
}