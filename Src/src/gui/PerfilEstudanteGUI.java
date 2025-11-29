package gui;

import entites.Estudante;
import entites.Senior;
import services.GerenciadorUsuarios;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PerfilEstudanteGUI extends JFrame {
    private Senior usuarioSenior;
    private Estudante estudante;
    private GerenciadorUsuarios gerenciador;

    public PerfilEstudanteGUI(Senior senior, Estudante estudante, GerenciadorUsuarios gerenciador) {
        this.usuarioSenior = senior;
        this.estudante = estudante;
        this.gerenciador = gerenciador;
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

        JLabel tituloLabel = new JLabel("Perfil do Estudante");
        tituloLabel.setFont(new Font("Calibri", Font.BOLD, 24));
        tituloLabel.setForeground(new Color(0, 102, 204));
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nomeEstudante = new JLabel(estudante.getNome());
        nomeEstudante.setFont(new Font("Calibri", Font.BOLD, 18));
        nomeEstudante.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(tituloLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(nomeEstudante);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Painel de informações com scroll
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        // Informações pessoais
        infoPanel.add(criarGrupoInfo("Informações Pessoais",
                new String[][]{
                        {"Email:", estudante.getEmail()},
                        {"Telefone:", estudante.getTelefone()},
                        {"CPF:", estudante.getCpf()},
                        {"Data Nascimento:", estudante.getDataNascimento() != null ?
                                estudante.getDataNascimento().toString() : "Não informada"},
                        {"Endereço:", estudante.getEndereco()}
                }));

        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Informações acadêmicas
        infoPanel.add(criarGrupoInfo("Informações Acadêmicas",
                new String[][]{
                        {"Instituição:", estudante.getInstuicao()},
                        {"Curso:", estudante.getCurso()},
                        {"Semestre:", estudante.getSemestre() + "º semestre"}
                }));

        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Especialidades
        JPanel especialidadesPanel = new JPanel();
        especialidadesPanel.setLayout(new BoxLayout(especialidadesPanel, BoxLayout.Y_AXIS));
        especialidadesPanel.setBackground(Color.WHITE);
        especialidadesPanel.setBorder(BorderFactory.createTitledBorder("Especialidades"));

        if (estudante.getEspecialidades().isEmpty()) {
            JLabel semEspecialidades = new JLabel("Nenhuma especialidade cadastrada");
            semEspecialidades.setFont(new Font("Calibri", Font.ITALIC, 14));
            semEspecialidades.setForeground(Color.GRAY);
            especialidadesPanel.add(semEspecialidades);
        } else {
            for (String especialidade : estudante.getEspecialidades()) {
                JLabel espLabel = new JLabel("• " + especialidade);
                espLabel.setFont(new Font("Calibri", Font.PLAIN, 14));
                especialidadesPanel.add(espLabel);
            }
        }

        infoPanel.add(especialidadesPanel);

        // Status de disponibilidade
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(Color.WHITE);

        JLabel statusLabel = new JLabel("Status: ");
        statusLabel.setFont(new Font("Calibri", Font.BOLD, 14));

        JLabel disponibilidadeLabel = new JLabel(estudante.isDisponivel() ? "DISPONÍVEL" : "INDISPONÍVEL");
        disponibilidadeLabel.setFont(new Font("Calibri", Font.BOLD, 14));
        disponibilidadeLabel.setForeground(estudante.isDisponivel() ? new Color(0, 150, 0) : Color.RED);

        statusPanel.add(statusLabel);
        statusPanel.add(disponibilidadeLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        infoPanel.add(statusPanel);

        JScrollPane scrollPane = new JScrollPane(infoPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Botões de ação
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        botoesPanel.setBackground(Color.WHITE);

        JButton agendarButton = new JButton("Agendar Consulta");
        agendarButton.setFont(new Font("Calibri", Font.BOLD, 14));
        agendarButton.setBackground(new Color(0, 102, 204));
        agendarButton.setForeground(Color.WHITE);
        agendarButton.setFocusPainted(false);
        agendarButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton voltarButton = new JButton("Voltar à Lista");
        voltarButton.setFont(new Font("Calibri", Font.PLAIN, 14));
        voltarButton.setBackground(new Color(240, 240, 240));
        voltarButton.setFocusPainted(false);
        voltarButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        botoesPanel.add(voltarButton);
        botoesPanel.add(agendarButton);

        // Adicionar componentes
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(botoesPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Configurar ações
        agendarButton.addActionListener(e -> {
            new AgendarConsultaGUI(usuarioSenior, estudante, gerenciador).setVisible(true);
            dispose();
        });
        voltarButton.addActionListener(e -> voltarBuscar());
    }

    private JPanel criarGrupoInfo(String titulo, String[][] dados) {
        JPanel grupoPanel = new JPanel();
        grupoPanel.setLayout(new BoxLayout(grupoPanel, BoxLayout.Y_AXIS));
        grupoPanel.setBackground(Color.WHITE);
        grupoPanel.setBorder(BorderFactory.createTitledBorder(titulo));

        for (String[] linha : dados) {
            if (linha[1] != null && !linha[1].trim().isEmpty()) {
                JPanel linhaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                linhaPanel.setBackground(Color.WHITE);

                JLabel label = new JLabel(linha[0]);
                label.setFont(new Font("Calibri", Font.BOLD, 14));
                label.setPreferredSize(new Dimension(150, 20));

                JLabel valor = new JLabel(linha[1]);
                valor.setFont(new Font("Calibri", Font.PLAIN, 14));

                linhaPanel.add(label);
                linhaPanel.add(valor);
                grupoPanel.add(linhaPanel);
            }
        }

        return grupoPanel;
    }

    private JPanel criarLinhaInfo(String label, String valor) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);

        JLabel labelField = new JLabel(label);
        labelField.setFont(new Font("Calibri", Font.BOLD, 14));
        labelField.setPreferredSize(new Dimension(100, 20));

        JLabel valorField = new JLabel(valor);
        valorField.setFont(new Font("Calibri", Font.PLAIN, 14));

        panel.add(labelField);
        panel.add(valorField);

        return panel;
    }

    private void agendarConsulta() {
        // Aqui vamos implementar depois a tela de agendamento
        JOptionPane.showMessageDialog(this,
                "Funcionalidade de agendamento em desenvolvimento!\n\n" +
                        "Estudante: " + estudante.getNome() + "\n" +
                        "Senior: " + usuarioSenior.getNome(),
                "Agendar Consulta",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void voltarBuscar() {
        this.dispose();
        new BuscarEstudantesGUI(usuarioSenior, gerenciador).setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Nosso Canto - Perfil Estudante");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }
}