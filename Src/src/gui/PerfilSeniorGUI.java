package gui;

import entites.Estudante;
import entites.Senior;
import services.GerenciadorUsuarios;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PerfilSeniorGUI extends JFrame {
    private Estudante usuarioEstudante;
    private Senior senior;
    private GerenciadorUsuarios gerenciador;

    public PerfilSeniorGUI(Estudante estudante, Senior senior, GerenciadorUsuarios gerenciador) {
        this.usuarioEstudante = estudante;
        this.senior = senior;
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

        JLabel tituloLabel = new JLabel("Perfil do Senior");
        tituloLabel.setFont(new Font("Calibri", Font.BOLD, 24));
        tituloLabel.setForeground(new Color(0, 102, 204));
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nomeSenior = new JLabel(senior.getNome());
        nomeSenior.setFont(new Font("Calibri", Font.BOLD, 18));
        nomeSenior.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(tituloLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(nomeSenior);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Painel de informações com scroll
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        // Informações pessoais
        infoPanel.add(criarGrupoInfo("Informações Pessoais",
                new String[][]{
                        {"Email:", senior.getEmail()},
                        {"Telefone:", senior.getTelefone()},
                        {"Data Nascimento:", senior.getDataNascimento() != null ?
                                senior.getDataNascimento().toString() : "Não informada"},
                        {"Endereço:", senior.getEndereco()},
                        {"Contato Emergência:", senior.getContatoEmergencia()},
                        {"Precisa de Acompanhante:", senior.isTemAcompanhante() ? "Sim" : "Não"}
                }));

        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Condições de saúde
        JPanel condicoesPanel = new JPanel();
        condicoesPanel.setLayout(new BoxLayout(condicoesPanel, BoxLayout.Y_AXIS));
        condicoesPanel.setBackground(Color.WHITE);
        condicoesPanel.setBorder(BorderFactory.createTitledBorder("Condições de Saúde"));

        if (senior.getCondicaoSaude().isEmpty()) {
            JLabel semCondicoes = new JLabel("Nenhuma condição de saúde cadastrada");
            semCondicoes.setFont(new Font("Calibri", Font.ITALIC, 14));
            semCondicoes.setForeground(Color.GRAY);
            condicoesPanel.add(semCondicoes);
        } else {
            for (String condicao : senior.getCondicaoSaude()) {
                JLabel condLabel = new JLabel("• " + condicao);
                condLabel.setFont(new Font("Calibri", Font.PLAIN, 14));
                condicoesPanel.add(condLabel);
            }
        }

        infoPanel.add(condicoesPanel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Medicamentos
        JPanel medicamentosPanel = new JPanel();
        medicamentosPanel.setLayout(new BoxLayout(medicamentosPanel, BoxLayout.Y_AXIS));
        medicamentosPanel.setBackground(Color.WHITE);
        medicamentosPanel.setBorder(BorderFactory.createTitledBorder("Medicamentos"));

        if (senior.getMedicamentos().isEmpty()) {
            JLabel semMedicamentos = new JLabel("Nenhum medicamento cadastrado");
            semMedicamentos.setFont(new Font("Calibri", Font.ITALIC, 14));
            semMedicamentos.setForeground(Color.GRAY);
            medicamentosPanel.add(semMedicamentos);
        } else {
            for (String medicamento : senior.getMedicamentos()) {
                JLabel medLabel = new JLabel("• " + medicamento);
                medLabel.setFont(new Font("Calibri", Font.PLAIN, 14));
                medicamentosPanel.add(medLabel);
            }
        }

        infoPanel.add(medicamentosPanel);

        JScrollPane scrollPane = new JScrollPane(infoPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Botões de ação
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        botoesPanel.setBackground(Color.WHITE);

        JButton voltarButton = new JButton("Voltar à Lista");
        voltarButton.setFont(new Font("Calibri", Font.PLAIN, 14));
        voltarButton.setBackground(new Color(240, 240, 240));
        voltarButton.setFocusPainted(false);
        voltarButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        botoesPanel.add(voltarButton);

        // Adicionar componentes
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(botoesPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Configurar ações
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
                label.setPreferredSize(new Dimension(180, 20));

                JLabel valor = new JLabel(linha[1]);
                valor.setFont(new Font("Calibri", Font.PLAIN, 14));

                linhaPanel.add(label);
                linhaPanel.add(valor);
                grupoPanel.add(linhaPanel);
            }
        }

        return grupoPanel;
    }

    private void voltarBuscar() {
        this.dispose();
        new BuscarSeniorsGUI(usuarioEstudante, gerenciador).setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Nosso Canto - Perfil Senior");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }
}