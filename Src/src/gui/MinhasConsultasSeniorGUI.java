package gui;

import entites.Consulta;
import entites.Senior;
import entites.Usuario;
import dao.ConsultaDAO;
import services.GerenciadorCunsultas;
import services.GerenciadorUsuarios;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MinhasConsultasSeniorGUI extends JFrame {
    private Senior usuarioSenior;
    private GerenciadorUsuarios gerenciadorUsuarios;
    private GerenciadorCunsultas gerenciadorConsultas;
    private JPanel consultasPanel;

    public MinhasConsultasSeniorGUI(Usuario usuario, GerenciadorUsuarios gerenciador) {
        this.usuarioSenior = (Senior) usuario;
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

        JLabel subtituloLabel = new JLabel("Acompanhe suas consultas agendadas");
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
        scrollPane.setBorder(BorderFactory.createTitledBorder("Minhas Consultas"));
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
        // LIMPAR O PAINEL ANTES DE RECARREGAR
        consultasPanel.removeAll();

        // Buscar consultas do senior (do banco)
        System.out.println("DEBUG - Carregando consultas para senior: " + usuarioSenior.getId());

        List<Consulta> consultas = gerenciadorConsultas.getConsultarSenior(usuarioSenior.getId());

        System.out.println("Debug - Total de consultas encontradas: " + consultas.size());

        if (consultas.isEmpty()) {
            JLabel semConsultasLabel = new JLabel("Nenhuma consulta agendada");
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

            // Consultas agendadas (verde)
            if (!agendadas.isEmpty()) {
                JLabel agendadasLabel = new JLabel("Consultas Confirmadas:");
                agendadasLabel.setFont(new Font("Calibri", Font.BOLD, 16));
                agendadasLabel.setForeground(new Color(0, 150, 0));
                agendadasLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                consultasPanel.add(agendadasLabel);
                consultasPanel.add(Box.createRigidArea(new Dimension(0, 10)));

                for (Consulta consulta : agendadas) {
                    consultasPanel.add(criarCardConsultaAgendada(consulta));
                    consultasPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }

                consultasPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            }

            // Solicitações pendentes (laranja)
            if (!solicitadas.isEmpty()) {
                JLabel solicitacoesLabel = new JLabel("Aguardando Confirmação:");
                solicitacoesLabel.setFont(new Font("Calibri", Font.BOLD, 16));
                solicitacoesLabel.setForeground(new Color(255, 140, 0));
                solicitacoesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                consultasPanel.add(solicitacoesLabel);
                consultasPanel.add(Box.createRigidArea(new Dimension(0, 10)));

                for (Consulta consulta : solicitadas) {
                    consultasPanel.add(criarCardSolicitacao(consulta));
                    consultasPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }

                consultasPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            }

            // Outras consultas (canceladas, recusadas, concluídas)
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

    private JPanel criarCardConsultaAgendada(Consulta consulta) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout(10, 5));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 150, 0), 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        cardPanel.setBackground(new Color(240, 255, 240));
        cardPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Informações da consulta
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(240, 255, 240));

        JLabel estudanteLabel = new JLabel("Estudante: " + consulta.getEstudante().getNome());
        estudanteLabel.setFont(new Font("Calibri", Font.BOLD, 14));
        estudanteLabel.setForeground(new Color(0, 100, 0));

        JLabel cursoLabel = new JLabel("Curso: " + consulta.getEstudante().getCurso());
        cursoLabel.setFont(new Font("Calibri", Font.PLAIN, 12));

        JLabel dataLabel = new JLabel("Data: " + consulta.getDataHora().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")));
        dataLabel.setFont(new Font("Calibri", Font.PLAIN, 12));

        JLabel statusLabel = new JLabel("Status: CONFIRMADA");
        statusLabel.setFont(new Font("Calibri", Font.BOLD, 12));
        statusLabel.setForeground(new Color(0, 150, 0));

        infoPanel.add(estudanteLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(cursoLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 2)));
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

        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.setFont(new Font("Calibri", Font.PLAIN, 11));
        cancelarButton.setBackground(new Color(220, 53, 69));
        cancelarButton.setForeground(Color.WHITE);
        cancelarButton.setFocusPainted(false);
        cancelarButton.setPreferredSize(new Dimension(80, 25));

        JButton detalhesButton = new JButton("Detalhes");
        detalhesButton.setFont(new Font("Calibri", Font.PLAIN, 11));
        detalhesButton.setBackground(new Color(240, 240, 240));
        detalhesButton.setFocusPainted(false);
        detalhesButton.setPreferredSize(new Dimension(80, 25));

        chatButton.addActionListener(e -> abrirChat(consulta));
        cancelarButton.addActionListener(e -> cancelarConsulta(consulta));
        detalhesButton.addActionListener(e -> verDetalhesConsulta(consulta));

        botoesPanel.add(detalhesButton);
        botoesPanel.add(chatButton);
        botoesPanel.add(cancelarButton);

        cardPanel.add(infoPanel, BorderLayout.CENTER);
        cardPanel.add(botoesPanel, BorderLayout.EAST);

        return cardPanel;
    }

    private JPanel criarCardSolicitacao(Consulta consulta) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout(10, 5));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 165, 0), 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        cardPanel.setBackground(new Color(255, 250, 240));
        cardPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Informações da solicitação
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(255, 250, 240));

        JLabel estudanteLabel = new JLabel("Estudante: " + consulta.getEstudante().getNome());
        estudanteLabel.setFont(new Font("Calibri", Font.BOLD, 14));
        estudanteLabel.setForeground(new Color(255, 140, 0));

        JLabel cursoLabel = new JLabel("Curso: " + consulta.getEstudante().getCurso());
        cursoLabel.setFont(new Font("Calibri", Font.PLAIN, 12));

        JLabel dataLabel = new JLabel("Data: " + consulta.getDataHora().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")));
        dataLabel.setFont(new Font("Calibri", Font.PLAIN, 12));

        JLabel statusLabel = new JLabel("Status: AGUARDANDO CONFIRMAÇÃO");
        statusLabel.setFont(new Font("Calibri", Font.BOLD, 12));
        statusLabel.setForeground(new Color(255, 140, 0));

        infoPanel.add(estudanteLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(cursoLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        infoPanel.add(dataLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(statusLabel);

        // Botões de ação
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        botoesPanel.setBackground(new Color(255, 250, 240));

        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.setFont(new Font("Calibri", Font.BOLD, 11));
        cancelarButton.setBackground(new Color(220, 53, 69));
        cancelarButton.setForeground(Color.WHITE);
        cancelarButton.setFocusPainted(false);
        cancelarButton.setPreferredSize(new Dimension(80, 25));

        JButton detalhesButton = new JButton("Detalhes");
        detalhesButton.setFont(new Font("Calibri", Font.PLAIN, 11));
        detalhesButton.setBackground(new Color(240, 240, 240));
        detalhesButton.setFocusPainted(false);
        detalhesButton.setPreferredSize(new Dimension(80, 25));

        cancelarButton.addActionListener(e -> cancelarConsulta(consulta));
        detalhesButton.addActionListener(e -> verDetalhesConsulta(consulta));

        botoesPanel.add(detalhesButton);
        botoesPanel.add(cancelarButton);

        cardPanel.add(infoPanel, BorderLayout.CENTER);
        cardPanel.add(botoesPanel, BorderLayout.EAST);

        return cardPanel;
    }

    private JPanel criarCardConsulta(Consulta consulta) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout(10, 5));

        // Cor baseada no status
        Color corBorda = Color.GRAY;
        Color corFundo = new Color(248, 248, 248);

        if ("RECUSADA".equals(consulta.getStatus())) {
            corBorda = new Color(220, 53, 69);
            corFundo = new Color(255, 240, 240);
        } else if ("CANCELADA".equals(consulta.getStatus())) {
            corBorda = new Color(108, 117, 125);
            corFundo = new Color(248, 249, 250);
        } else if ("CONCLUIDA".equals(consulta.getStatus())) {
            corBorda = new Color(0, 102, 204);
            corFundo = new Color(240, 245, 255);
        }

        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(corBorda, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        cardPanel.setBackground(corFundo);

        // Informações da consulta
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(corFundo);

        JLabel estudanteLabel = new JLabel("Estudante: " + consulta.getEstudante().getNome());
        estudanteLabel.setFont(new Font("Calibri", Font.BOLD, 14));
        estudanteLabel.setForeground(Color.DARK_GRAY);

        JLabel dataLabel = new JLabel("Data: " + consulta.getDataHora().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")));
        dataLabel.setFont(new Font("Calibri", Font.PLAIN, 12));

        JLabel statusLabel = new JLabel("Status: " + consulta.getStatus());
        statusLabel.setFont(new Font("Calibri", Font.BOLD, 12));
        statusLabel.setForeground(corBorda);

        infoPanel.add(estudanteLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(dataLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(statusLabel);

        cardPanel.add(infoPanel, BorderLayout.CENTER);

        return cardPanel;
    }

    private void cancelarConsulta(Consulta consulta) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja cancelar esta consulta?\n\n" +
                        "Estudante: " + consulta.getEstudante().getNome() + "\n" +
                        "Data: " + consulta.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                "Confirmar Cancelamento",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Atualizar status para CANCELADA
                consulta.setStatus("CANCELADA");

                ConsultaDAO consultaDAO = new ConsultaDAO(); // ATUALIZAR NO BANCO DE DADOS
                consultaDAO.atualizarStatus(consulta.getId(), "CANCELADA");

                JOptionPane.showMessageDialog(this,
                        "Consulta cancelada com sucesso!",
                        "Cancelamento Concluído",
                        JOptionPane.INFORMATION_MESSAGE);

                carregarConsultas();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao cancelar consulta: " + e.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void verDetalhesConsulta(Consulta consulta) {
        StringBuilder detalhes = new StringBuilder();
        detalhes.append("=== Detalhes da Consulta ===\n\n");
        detalhes.append("Estudante: ").append(consulta.getEstudante().getNome()).append("\n");
        detalhes.append("Curso: ").append(consulta.getEstudante().getCurso()).append("\n");
        detalhes.append("Instituição: ").append(consulta.getEstudante().getInstuicao()).append("\n");
        detalhes.append("Data: ").append(consulta.getDataHora().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm"))).append("\n");
        detalhes.append("Motivo: ").append(consulta.getTipoConsulta()).append("\n");
        detalhes.append("Status: ").append(consulta.getStatus());

        JOptionPane.showMessageDialog(this,
                detalhes.toString(),
                "Detalhes da Consulta",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void abrirChat(Consulta consulta) {
        try {
            new ChatGUI(usuarioSenior, consulta.getEstudante(), consulta).setVisible(true);
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
        new menuSeniorGUI(usuarioSenior, gerenciadorUsuarios).setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Nosso Canto - Minhas Consultas");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
    }
}