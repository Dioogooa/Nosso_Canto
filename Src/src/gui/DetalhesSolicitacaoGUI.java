package gui;

import entites.Consulta;
import entites.Estudante;
import services.GerenciadorCunsultas;
import services.GerenciadorUsuarios;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;

public class DetalhesSolicitacaoGUI extends JFrame {
    private Estudante usuarioEstudante;
    private Consulta consulta;
    private GerenciadorUsuarios gerenciadorUsuarios;
    private GerenciadorCunsultas gerenciadorConsultas;

    public DetalhesSolicitacaoGUI(Estudante estudante, Consulta consulta,
                                  GerenciadorUsuarios gerenciadorUsuarios,
                                  GerenciadorCunsultas gerenciadorConsultas) {
        this.usuarioEstudante = estudante;
        this.consulta = consulta;
        this.gerenciadorUsuarios = gerenciadorUsuarios;
        this.gerenciadorConsultas = gerenciadorConsultas;
        inicializarComponentes();
        configurarJanela();
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

        JLabel tituloLabel = new JLabel("Detalhes da Solicitação");
        tituloLabel.setFont(new Font("Calibri", Font.BOLD, 24));
        tituloLabel.setForeground(new Color(255, 140, 0)); // Laranja
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel statusLabel = new JLabel("SOLICITAÇÃO PENDENTE");
        statusLabel.setFont(new Font("Calibri", Font.BOLD, 16));
        statusLabel.setForeground(new Color(255, 140, 0));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(tituloLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(statusLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Informações detalhadas
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informações da Solicitação"));

        infoPanel.add(criarLinhaInfo("Senior:", consulta.getSenior().getNome()));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(criarLinhaInfo("Data:", consulta.getDataHora().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(criarLinhaInfo("Horário:", consulta.getDataHora().format(
                DateTimeFormatter.ofPattern("HH:mm"))));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Descrição completa
        JPanel descricaoPanel = new JPanel();
        descricaoPanel.setLayout(new BoxLayout(descricaoPanel, BoxLayout.Y_AXIS));
        descricaoPanel.setBackground(Color.WHITE);

        JLabel descricaoLabel = new JLabel("Descrição do Problema:");
        descricaoLabel.setFont(new Font("Calibri", Font.BOLD, 14));
        descricaoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea descricaoArea = new JTextArea(consulta.getTipoConsulta());
        descricaoArea.setLineWrap(true);
        descricaoArea.setWrapStyleWord(true);
        descricaoArea.setEditable(false);
        descricaoArea.setFont(new Font("Calibri", Font.PLAIN, 14));
        descricaoArea.setBackground(new Color(248, 248, 248));
        descricaoArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollDescricao = new JScrollPane(descricaoArea);
        scrollDescricao.setPreferredSize(new Dimension(400, 100));

        descricaoPanel.add(descricaoLabel);
        descricaoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        descricaoPanel.add(scrollDescricao);

        infoPanel.add(descricaoPanel);

        // Botões de ação
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        botoesPanel.setBackground(Color.WHITE);

        JButton recusarButton = new JButton("Recusar Solicitação");
        recusarButton.setFont(new Font("Calibri", Font.BOLD, 14));
        recusarButton.setBackground(new Color(220, 53, 69));
        recusarButton.setForeground(Color.WHITE);
        recusarButton.setFocusPainted(false);
        recusarButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton aceitarButton = new JButton("Aceitar Solicitação");
        aceitarButton.setFont(new Font("Calibri", Font.BOLD, 14));
        aceitarButton.setBackground(new Color(0, 150, 0));
        aceitarButton.setForeground(Color.WHITE);
        aceitarButton.setFocusPainted(false);
        aceitarButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton voltarButton = new JButton("Voltar");
        voltarButton.setFont(new Font("Calibri", Font.PLAIN, 14));
        voltarButton.setBackground(new Color(240, 240, 240));
        voltarButton.setFocusPainted(false);
        voltarButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        botoesPanel.add(voltarButton);
        botoesPanel.add(recusarButton);
        botoesPanel.add(aceitarButton);

        // Adicionar componentes
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(botoesPanel, BorderLayout.SOUTH);

        add(mainPanel);
        configurarAcoes(aceitarButton, recusarButton, voltarButton);
    }

    private JPanel criarLinhaInfo(String label, String valor) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);

        JLabel labelField = new JLabel(label);
        labelField.setFont(new Font("Calibri", Font.BOLD, 14));
        labelField.setPreferredSize(new Dimension(120, 20));

        JLabel valorField = new JLabel(valor);
        valorField.setFont(new Font("Calibri", Font.PLAIN, 14));

        panel.add(labelField);
        panel.add(valorField);

        return panel;
    }

    private void configurarAcoes(JButton aceitarButton, JButton recusarButton, JButton voltarButton) {
        aceitarButton.addActionListener(e -> aceitarSolicitacao());
        recusarButton.addActionListener(e -> recusarSolicitacao());
        voltarButton.addActionListener(e -> voltar());
    }

    private void aceitarSolicitacao() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja aceitar esta solicitação de consulta?\n\n" +
                        "Senior: " + consulta.getSenior().getNome() + "\n" +
                        "Data: " + consulta.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                "Confirmar Aceitação",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Atualizar status para AGENDADA
            consulta.setStatus("AGENDADA");

            // Aqui você pode atualizar no banco também
            // new ConsultaDAO().atualizarStatus(consulta.getId(), "AGENDADA");

            JOptionPane.showMessageDialog(this,
                    "Solicitação aceita com sucesso!\n\n" +
                            "A consulta foi agendada e já aparece na sua lista de consultas.",
                    "Solicitação Aceita",
                    JOptionPane.INFORMATION_MESSAGE);

            voltarParaConsultas();
        }
    }

    private void recusarSolicitacao() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja recusar esta solicitação de consulta?\n\n" +
                        "Senior: " + consulta.getSenior().getNome(),
                "Confirmar Recusa",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Atualizar status para RECUSADA
            consulta.setStatus("RECUSADA");

            // Aqui você pode atualizar no banco também
            // new ConsultaDAO().atualizarStatus(consulta.getId(), "RECUSADA");

            JOptionPane.showMessageDialog(this,
                    "Solicitação recusada.",
                    "Solicitação Recusada",
                    JOptionPane.INFORMATION_MESSAGE);

            voltarParaConsultas();
        }
    }

    private void voltar() {
        this.dispose();
        new MinhasConsultasEstudanteGUI(usuarioEstudante, gerenciadorUsuarios).setVisible(true);
    }

    private void voltarParaConsultas() {
        this.dispose();
        new MinhasConsultasEstudanteGUI(usuarioEstudante, gerenciadorUsuarios).setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Nosso Canto - Detalhes da Solicitação");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }
}