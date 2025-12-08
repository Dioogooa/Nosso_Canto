package gui;

import entites.Consulta;
import entites.Estudante;
import entites.Senior;
import services.GerenciadorCunsultas;
import services.GerenciadorUsuarios;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AgendarConsultaGUI extends JFrame {
    private Senior usuarioSenior;
    private Estudante estudante;
    private GerenciadorUsuarios gerenciadorUsuarios;
    private GerenciadorCunsultas gerenciadorConsultas;

    // componentes p seleção de data/hora
    private JComboBox<Integer> diaCombo, mesCombo, anoCombo;
    private JComboBox<String> horaCombo, minutoCombo;
    private JTextArea descricaoArea;
    private JButton agendarButton, voltarButton, buscarEstudanteButton;

    public AgendarConsultaGUI(Senior senior, Estudante estudante, GerenciadorUsuarios gerenciador) {
        this.usuarioSenior = senior;
        this.estudante = estudante;
        this.gerenciadorUsuarios = gerenciador;
        this.gerenciadorConsultas = new GerenciadorCunsultas();
        inicializarComponentes();
        configurarJanela();
    }

    // Construtor alternativo - quando vier direto do menu Agendar Consulta
    public AgendarConsultaGUI(Senior senior, GerenciadorUsuarios gerenciador) {
        this.usuarioSenior = senior;
        this.gerenciadorUsuarios = gerenciador;
        this.gerenciadorConsultas = new GerenciadorCunsultas();
        this.estudante = null;
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

        JLabel tituloLabel = new JLabel("Agendar Consulta");
        tituloLabel.setFont(new Font("Calibri", Font.BOLD, 24));
        tituloLabel.setForeground(new Color(0, 102, 204));
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(tituloLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Painel de formulário
        JPanel formularioPanel = new JPanel();
        formularioPanel.setLayout(new BoxLayout(formularioPanel, BoxLayout.Y_AXIS));
        formularioPanel.setBackground(Color.WHITE);
        formularioPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Seleção de Estudante (se não veio do perfil)
        if (estudante == null) {
            formularioPanel.add(criarSelecaoEstudante());
            formularioPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        } else {
            // Mostrar estudante selecionado!!
            JPanel estudantePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            estudantePanel.setBackground(Color.WHITE);

            JLabel estudanteLabel = new JLabel("Estudante selecionado: " + estudante.getNome() +
                    " - " + estudante.getCurso());
            estudanteLabel.setFont(new Font("Calibri", Font.BOLD, 14));
            estudantePanel.add(estudanteLabel);

            formularioPanel.add(estudantePanel);
            formularioPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        // Data da consulta
        formularioPanel.add(criarGrupoData());
        formularioPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Horário da consulta
        formularioPanel.add(criarGrupoHorario());
        formularioPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Descrição do problema
        formularioPanel.add(criarGrupoDescricao());
        formularioPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        botoesPanel.setBackground(Color.WHITE);

        voltarButton = new JButton("Voltar");
        voltarButton.setFont(new Font("Calibri", Font.PLAIN, 14));
        voltarButton.setBackground(new Color(240, 240, 240));
        voltarButton.setFocusPainted(false);
        voltarButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        agendarButton = new JButton("Agendar Consulta");
        agendarButton.setFont(new Font("Calibri", Font.BOLD, 14));
        agendarButton.setBackground(new Color(0, 102, 204));
        agendarButton.setForeground(Color.WHITE);
        agendarButton.setFocusPainted(false);
        agendarButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        botoesPanel.add(voltarButton);
        botoesPanel.add(agendarButton);

        // Adicionar componentes
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(formularioPanel), BorderLayout.CENTER);
        mainPanel.add(botoesPanel, BorderLayout.SOUTH);

        add(mainPanel);
        configurarAcoes();
    }

    private JPanel criarSelecaoEstudante() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Selecionar Estudante"));

        JLabel label = new JLabel("Primeiro selecione um estudante:");
        label.setFont(new Font("Calibri", Font.BOLD, 14));

        buscarEstudanteButton = new JButton("Buscar Estudantes");
        buscarEstudanteButton.setFont(new Font("Calibri", Font.PLAIN, 12));
        buscarEstudanteButton.setBackground(new Color(0, 102, 204));
        buscarEstudanteButton.setForeground(Color.WHITE);
        buscarEstudanteButton.setFocusPainted(false);

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(buscarEstudanteButton);

        return panel;
    }

    private JPanel criarGrupoData() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Data da Consulta"));

        // Dias (1-31)
        diaCombo = new JComboBox<>();
        for (int i = 1; i <= 31; i++) {
            diaCombo.addItem(i);
        }

        // Meses (1-12)
        mesCombo = new JComboBox<>();
        for (int i = 1; i <= 12; i++) {
            mesCombo.addItem(i);
        }

        // Anos (ano atual + 1 ano)
        anoCombo = new JComboBox<>();
        int anoAtual = LocalDate.now().getYear();
        for (int i = anoAtual; i <= anoAtual + 1; i++) {
            anoCombo.addItem(i);
        }

        LocalDate hoje = LocalDate.now();
        diaCombo.setSelectedItem(hoje.getDayOfMonth());
        mesCombo.setSelectedItem(hoje.getMonthValue());
        anoCombo.setSelectedItem(hoje.getYear());

        panel.add(new JLabel("Dia:"));
        panel.add(diaCombo);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(new JLabel("Mês:"));
        panel.add(mesCombo);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(new JLabel("Ano:"));
        panel.add(anoCombo);

        return panel;
    }

    private JPanel criarGrupoHorario() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Horário da Consulta"));

        // Horários das 7:00 às 18:30, opções de 30 em 30 minutos
        horaCombo = new JComboBox<>();
        minutoCombo = new JComboBox<>();

        // Horas (7-18)
        for (int i = 7; i <= 18; i++) {
            horaCombo.addItem(String.format("%02d", i));
        }

        // Minutos (00 e 30)
        minutoCombo.addItem("00");
        minutoCombo.addItem("30");

        // Configurar horário padrão (próximo horário disponível)
        LocalTime agora = LocalTime.now();
        horaCombo.setSelectedItem(String.format("%02d", agora.getHour()));
        minutoCombo.setSelectedItem(agora.getMinute() < 30 ? "00" : "30");

        panel.add(new JLabel("Hora:"));
        panel.add(horaCombo);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(new JLabel("Minuto:"));
        panel.add(minutoCombo);

        return panel;
    }

    private JPanel criarGrupoDescricao() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Descrição do Problema"));

        JLabel instrucaoLabel = new JLabel("Descreva brevemente o motivo da consulta:");
        instrucaoLabel.setFont(new Font("Calibri", Font.PLAIN, 12));
        instrucaoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        descricaoArea = new JTextArea(4, 30);
        descricaoArea.setLineWrap(true);
        descricaoArea.setWrapStyleWord(true);
        descricaoArea.setFont(new Font("Calibri", Font.PLAIN, 14));

        JScrollPane scrollDescricao = new JScrollPane(descricaoArea);
        scrollDescricao.setPreferredSize(new Dimension(400, 80));

        panel.add(instrucaoLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(scrollDescricao);

        return panel;
    }

    private void configurarAcoes() {
        agendarButton.addActionListener(e -> agendarConsulta());
        voltarButton.addActionListener(e -> voltar());

        if (buscarEstudanteButton != null) {
            buscarEstudanteButton.addActionListener(e -> buscarEstudantes());
        }
    }

    private void agendarConsulta() {
        try {
            // Validar se tem estudante selecionado
            if (estudante == null) {
                JOptionPane.showMessageDialog(this,
                        "Por favor, selecione um estudante primeiro!",
                        "Estudante Não Selecionado",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validar data
            LocalDate data = LocalDate.of(
                    (Integer) anoCombo.getSelectedItem(),
                    (Integer) mesCombo.getSelectedItem(),
                    (Integer) diaCombo.getSelectedItem()
            );

            // Validar se data não é no passado
            if (data.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this,
                        "Não é possível agendar consultas para datas passadas!",
                        "Data Inválida",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validar horário
            LocalTime horario = LocalTime.of(
                    Integer.parseInt((String) horaCombo.getSelectedItem()),
                    Integer.parseInt((String) minutoCombo.getSelectedItem())
            );

            // Validar se horário não é no passado (para o dia atual)
            if (data.equals(LocalDate.now()) && horario.isBefore(LocalTime.now())) {
                JOptionPane.showMessageDialog(this,
                        "Não é possível agendar consultas para horários passados!",
                        "Horário Inválido",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            LocalDateTime dataHora = LocalDateTime.of(data, horario);

            String descricao = descricaoArea.getText().trim();
            if (descricao.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Por favor, descreva o motivo da consulta!",
                        "Descrição Obrigatória",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String consultaId = "C" + System.currentTimeMillis();

            Consulta consulta = gerenciadorConsultas.agendarConsulta(
                    consultaId, usuarioSenior, estudante, dataHora, descricao
            );

            JOptionPane.showMessageDialog(this,
                    "Consulta agendada com sucesso!\n\n" +
                            "Estudante: " + estudante.getNome() + "\n" +
                            "Data: " + dataHora.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n" +
                            "Descrição: " + descricao,
                    "Agendamento Concluído",
                    JOptionPane.INFORMATION_MESSAGE);

            voltarMenu();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao agendar consulta: " + e.getMessage(),
                    "Erro no Agendamento",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarEstudantes() {
        this.dispose();
        new BuscarEstudantesGUI(usuarioSenior, gerenciadorUsuarios).setVisible(true);
    }

    private void voltar() {
        if (estudante != null) {
            // Veio do perfil do estudante - voltar para lá
            this.dispose();
            new PerfilEstudanteGUI(usuarioSenior, estudante, gerenciadorUsuarios).setVisible(true);
        } else {
            // Veio do menu - voltar para menu
            voltarMenu();
        }
    }

    private void voltarMenu() {
        this.dispose();
        new menuSeniorGUI(usuarioSenior, gerenciadorUsuarios).setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Nosso Canto - Agendar Consulta");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
    }
}