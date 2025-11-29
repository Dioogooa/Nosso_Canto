package gui;

import entites.Consulta;
import entites.Estudante;
import entites.Usuario;
import services.GerenciadorCunsultas;
import services.GerenciadorUsuarios;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MinhasConsultasEstudanteGUI extends JFrame {
    private Estudante usuarioEstudante;
    private GerenciadorUsuarios gerenciadorUsuarios;
    private GerenciadorCunsultas gerenciadorConsultas;
    private JPanel consultasPanel;

    public MinhasConsultasEstudanteGUI(Usuario usuario, GerenciadorUsuarios gerenciador) {
        this.usuarioEstudante = (Estudante) usuario;
        this.gerenciadorUsuarios = gerenciador;
        this.gerenciadorConsultas = new GerenciadorCunsultas();
        inicializarComponentes();
        configurarJanela();
        carregarConsultas();
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

        JLabel tituloLabel = new JLabel("Minhas Consultas");
        tituloLabel.setFont(new Font("Calibri", Font.BOLD, 24));
        tituloLabel.setForeground(new Color(0, 102, 204));
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtituloLabel = new JLabel("Gerencie suas consultas e solicitações");
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
        scrollPane.setBorder(BorderFactory.createTitledBorder("Consultas"));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Botão voltar
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(Color.WHITE);

        JButton voltarButton = new JButton("Voltar ao Menu");
        voltarButton.setFont(new Font("Calibri", Font.BOLD, 14));
        voltarButton.setBackground(new Color(240, 240, 240));
        voltarButton.setFocusPainted(false);
        voltarButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        voltarButton.addActionListener(e -> voltarMenu());

        footerPanel.add(voltarButton);

        // Adicionar componentes
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void carregarConsultas() {

        System.out.println("DEBUG - Carregando consultas para estudantes:" +usuarioEstudante.getId());
        System.out.println("DEBUG - Nome do estudante: " + usuarioEstudante.getNome());

        // Buscar consultas do estudante (do banco)
        List<Consulta> consultas = gerenciadorConsultas.getConsultarEstudante(usuarioEstudante.getId());

        System.out.println("Debug - Total de consultas encontradas: " + consultas.size());

        if (consultas.isEmpty()) {
            JLabel semConsultasLabel = new JLabel("Nenhuma consulta encontrada");
            semConsultasLabel.setFont(new Font("Calibri", Font.ITALIC, 14));
            semConsultasLabel.setForeground(Color.GRAY);
            semConsultasLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            consultasPanel.add(semConsultasLabel);
        } else {
            // Separar por status
            List<Consulta> solicitadas = consultas.stream()
                    .filter(c -> "SOLICITADA".equals(c.getStatus()))
                    .toList();

            List<Consulta> agendadas = consultas.stream()
                    .filter(c -> "AGENDADA".equals(c.getStatus()))
                    .toList();

            List<Consulta> outras = consultas.stream()
                    .filter(c -> !"SOLICITADA".equals(c.getStatus()) && !"AGENDADA".equals(c.getStatus()))
                    .toList();

            // Adicionar solicitações pendentes primeiro
            if (!solicitadas.isEmpty()) {
                JLabel solicitacoesLabel = new JLabel("Solicitações Pendentes:");
                solicitacoesLabel.setFont(new Font("Calibri", Font.BOLD, 16));
                solicitacoesLabel.setForeground(new Color(255, 140, 0)); // Laranja
                solicitacoesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                consultasPanel.add(solicitacoesLabel);
                consultasPanel.add(Box.createRigidArea(new Dimension(0, 10)));

                for (Consulta consulta : solicitadas) {
                    consultasPanel.add(criarCardSolicitacao(consulta));
                    consultasPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }

                consultasPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            }

            // Consultas agendadas
            if (!agendadas.isEmpty()) {
                JLabel agendadasLabel = new JLabel("Consultas Agendadas:");
                agendadasLabel.setFont(new Font("Calibri", Font.BOLD, 16));
                agendadasLabel.setForeground(new Color(0, 150, 0)); // Verde
                agendadasLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                consultasPanel.add(agendadasLabel);
                consultasPanel.add(Box.createRigidArea(new Dimension(0, 10)));

                for (Consulta consulta : agendadas) {
                    consultasPanel.add(criarCardConsultaAgendada(consulta));
                    consultasPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }

            // Outras consultas (canceladas, concluídas)
            if (!outras.isEmpty()) {
                JLabel outrasLabel = new JLabel("Outras Consultas:");
                outrasLabel.setFont(new Font("Calibri", Font.BOLD, 16));
                outrasLabel.setForeground(Color.GRAY);
                outrasLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                consultasPanel.add(outrasLabel);
                consultasPanel.add(Box.createRigidArea(new Dimension(0, 10)));

                for (Consulta consulta : outras) {
                    consultasPanel.add(criarCardConsulta(consulta));
                    consultasPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
        }

        consultasPanel.revalidate();
        consultasPanel.repaint();
    }

    private JPanel criarCardSolicitacao(Consulta consulta) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout(10, 5));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 165, 0), 2), // Borda laranja
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        cardPanel.setBackground(new Color(255, 250, 240)); // Fundo laranja claro
        cardPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Informações da solicitação
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(255, 250, 240));

        JLabel seniorLabel = new JLabel("Senior: " + consulta.getSenior().getNome());
        seniorLabel.setFont(new Font("Calibri", Font.BOLD, 14));
        seniorLabel.setForeground(new Color(255, 140, 0));

        JLabel dataLabel = new JLabel("Data: " + consulta.getDataHora().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        dataLabel.setFont(new Font("Calibri", Font.PLAIN, 12));

        JLabel tipoLabel = new JLabel("Motivo: " + consulta.getTipoConsulta());
        tipoLabel.setFont(new Font("Calibri", Font.PLAIN, 12));

        // Limitar texto do motivo
        String motivo = consulta.getTipoConsulta();
        if (motivo.length() > 50) {
            motivo = motivo.substring(0, 47) + "...";
        }
        tipoLabel.setText("Motivo: " + motivo);

        JLabel statusLabel = new JLabel("Status: SOLICITAÇÃO PENDENTE");
        statusLabel.setFont(new Font("Calibri", Font.BOLD, 12));
        statusLabel.setForeground(new Color(255, 140, 0));

        infoPanel.add(seniorLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(dataLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        infoPanel.add(tipoLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(statusLabel);

        // Botões de ação
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        botoesPanel.setBackground(new Color(255, 250, 240));

        JButton verDetalhesButton = new JButton("Ver Detalhes");
        verDetalhesButton.setFont(new Font("Calibri", Font.BOLD, 11));
        verDetalhesButton.setBackground(new Color(0, 102, 204));
        verDetalhesButton.setForeground(Color.WHITE);
        verDetalhesButton.setFocusPainted(false);
        verDetalhesButton.setPreferredSize(new Dimension(100, 25));

        verDetalhesButton.addActionListener(e -> verDetalhesSolicitacao(consulta));

        botoesPanel.add(verDetalhesButton);

        // Clicar no card também abre os detalhes
        cardPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                verDetalhesSolicitacao(consulta);
            }
        });

        cardPanel.add(infoPanel, BorderLayout.CENTER);
        cardPanel.add(botoesPanel, BorderLayout.EAST);

        return cardPanel;
    }

    private JPanel criarCardConsultaAgendada(Consulta consulta) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout(10, 5));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 150, 0), 2), // Borda verde
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        cardPanel.setBackground(new Color(240, 255, 240)); // Fundo verde claro
        cardPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Informações da consulta
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(240, 255, 240));

        JLabel seniorLabel = new JLabel("Senior: " + consulta.getSenior().getNome());
        seniorLabel.setFont(new Font("Calibri", Font.BOLD, 14));
        seniorLabel.setForeground(new Color(0, 100, 0));

        JLabel dataLabel = new JLabel("Data: " + consulta.getDataHora().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")));
        dataLabel.setFont(new Font("Calibri", Font.PLAIN, 12));

        JLabel statusLabel = new JLabel("Status: AGENDADA");
        statusLabel.setFont(new Font("Calibri", Font.BOLD, 12));
        statusLabel.setForeground(new Color(0, 150, 0));

        infoPanel.add(seniorLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(dataLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(statusLabel);

        // Botões de ação
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        botoesPanel.setBackground(new Color(240, 255, 240));

        JButton chatButton = new JButton("Abrir Chat");
        chatButton.setFont(new Font("Calibri", Font.BOLD, 11));
        chatButton.setBackground(new Color(0, 102, 204));
        chatButton.setForeground(Color.WHITE);
        chatButton.setFocusPainted(false);
        chatButton.setPreferredSize(new Dimension(90, 25));

        JButton detalhesButton = new JButton("Detalhes");
        detalhesButton.setFont(new Font("Calibri", Font.PLAIN, 11));
        detalhesButton.setBackground(new Color(240, 240, 240));
        detalhesButton.setFocusPainted(false);
        detalhesButton.setPreferredSize(new Dimension(80, 25));

        chatButton.addActionListener(e -> abrirChat(consulta));
        detalhesButton.addActionListener(e -> verDetalhesConsulta(consulta));

        botoesPanel.add(detalhesButton);
        botoesPanel.add(chatButton);

        cardPanel.add(infoPanel, BorderLayout.CENTER);
        cardPanel.add(botoesPanel, BorderLayout.EAST);

        return cardPanel;
    }

    private JPanel criarCardConsulta(Consulta consulta) {
        // Card genérico para outras consultas (canceladas, concluídas)
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout(10, 5));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        cardPanel.setBackground(Color.WHITE);

        // Implementação similar às outras...
        return cardPanel;
    }

    private void verDetalhesSolicitacao(Consulta consulta) {
        new DetalhesSolicitacaoGUI(usuarioEstudante, consulta, gerenciadorUsuarios, gerenciadorConsultas).setVisible(true);
        dispose();
    }

    private void verDetalhesConsulta(Consulta consulta) {
        // Implementar tela de detalhes da consulta
        JOptionPane.showMessageDialog(this,
                "Detalhes da Consulta:\n\n" +
                        "Senior: " + consulta.getSenior().getNome() + "\n" +
                        "Data: " + consulta.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")) + "\n" +
                        "Motivo: " + consulta.getTipoConsulta() + "\n" +
                        "Status: " + consulta.getStatus(),
                "Detalhes da Consulta",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void abrirChat(Consulta consulta) {
        JOptionPane.showMessageDialog(this,
                "Funcionalidade de Chat em desenvolvimento!\n\n" +
                        "Chat com: " + consulta.getSenior().getNome(),
                "Chat",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void voltarMenu() {
        this.dispose();
        new MenuEstudanteGUI(usuarioEstudante, gerenciadorUsuarios).setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Nosso Canto - Minhas Consultas");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
    }
}