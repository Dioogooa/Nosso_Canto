package gui;

import entites.Estudante;
import entites.Senior;
import entites.Usuario;
import services.GerenciadorUsuarios;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BuscarEstudantesGUI extends JFrame {
    private Senior usuarioSenior;
    private GerenciadorUsuarios gerenciador;
    private JPanel estudantesPanel;

    public BuscarEstudantesGUI(Usuario usuario, GerenciadorUsuarios gerenciador) {
        this.usuarioSenior = (Senior) usuario;
        this.gerenciador = gerenciador;
        inicializarComponentes();
        configurarJanela();
        carregarEstudantes();
    }

    private void inicializarComponentes() {
        // Painel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Cabeçalho
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);

        JLabel tituloLabel = new JLabel("Buscar Estudantes");
        tituloLabel.setFont(new Font("Calibri", Font.BOLD, 24));
        tituloLabel.setForeground(new Color(0, 102, 204));
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtituloLabel = new JLabel("Clique em um estudante para ver o perfil completo e agendar consulta");
        subtituloLabel.setFont(new Font("Calibri", Font.PLAIN, 14));
        subtituloLabel.setForeground(Color.GRAY);
        subtituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(tituloLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(subtituloLabel);

        // Painel de estudantes (com scroll)
        estudantesPanel = new JPanel();
        estudantesPanel.setLayout(new BoxLayout(estudantesPanel, BoxLayout.Y_AXIS));
        estudantesPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(estudantesPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Estudantes Disponíveis"));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Botão voltar
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(Color.WHITE);

        JButton voltarButton = new JButton("Voltar ao Menu");
        voltarButton.setFont(new Font("Calibri", Font.BOLD, 14));
        voltarButton.setBackground(new Color(240, 240, 240));
        voltarButton.setFocusPainted(false);
        voltarButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                voltarMenu();
            }
        });

        footerPanel.add(voltarButton);

        // Adicionar componentes
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void carregarEstudantes() {
        List<Estudante> todosEstudantes = gerenciador.listarEstudantes();
        System.out.println("Debug - Total de estudantes encotrados: " + todosEstudantes.size());

        List<Estudante> estudantes = todosEstudantes.stream()
                .filter(Estudante :: isDisponivel)
                .toList();

        System.out.println("Debug - Estudantes disponiveis: " + estudantes.size());

        for (Estudante e : estudantes) {
            System.out.println("Disponivel: " + e.getNome() + " - Curso: "+ e.getCurso());
        }

        if (estudantes.isEmpty()) {
            JLabel semEstudantesLabel = new JLabel("Nenhum estudante disponível no momento");
            semEstudantesLabel.setFont(new Font("Calibri", Font.ITALIC, 14));
            semEstudantesLabel.setForeground(Color.GRAY);
            semEstudantesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            estudantesPanel.add(semEstudantesLabel);
        } else {
            for (Estudante estudante : estudantes) {
                estudantesPanel.add(criarCardEstudante(estudante));
                estudantesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        estudantesPanel.revalidate();
        estudantesPanel.repaint();
    }

    private JPanel criarCardEstudante(Estudante estudante) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout(10, 5));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Informações do estudante
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel nomeLabel = new JLabel(estudante.getNome());
        nomeLabel.setFont(new Font("Calibri", Font.BOLD, 16));
        nomeLabel.setForeground(new Color(0, 102, 204));

        JLabel cursoLabel = new JLabel(estudante.getCurso() + " - " + estudante.getInstuicao());
        cursoLabel.setFont(new Font("Calibri", Font.PLAIN, 14));

        JLabel semestreLabel = new JLabel(estudante.getSemestre() + "º semestre");
        semestreLabel.setFont(new Font("Calibri", Font.PLAIN, 14));

        // Especialidades (máximo 2 para não ficar muito longo)
        String especialidades = String.join(", ", estudante.getEspecialidades());
        if (especialidades.length() > 50) {
            especialidades = especialidades.substring(0, 47) + "...";
        }

        JLabel especialidadesLabel = new JLabel("Especialidades: " + especialidades);
        especialidadesLabel.setFont(new Font("Calibri", Font.PLAIN, 12));
        especialidadesLabel.setForeground(Color.GRAY);

        infoPanel.add(nomeLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(cursoLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        infoPanel.add(semestreLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(especialidadesLabel);

        // Botão de ação - COM TAMANHO FIXO
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton verPerfilButton = new JButton("Ver Perfil");
        verPerfilButton.setFont(new Font("Calibri", Font.BOLD, 12));
        verPerfilButton.setBackground(new Color(0, 102, 204));
        verPerfilButton.setForeground(Color.WHITE);
        verPerfilButton.setFocusPainted(false);
        verPerfilButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        verPerfilButton.setPreferredSize(new Dimension(100, 35)); // TAMANHO FIXO

        verPerfilButton.addActionListener(e -> abrirPerfilEstudante(estudante));

        buttonPanel.add(verPerfilButton);

        // Também permite clicar no card inteiro
        cardPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                abrirPerfilEstudante(estudante);
            }
        });

        cardPanel.add(infoPanel, BorderLayout.CENTER);
        cardPanel.add(buttonPanel, BorderLayout.EAST);

        return cardPanel;
    }

    private void abrirPerfilEstudante(Estudante estudante) {
        this.dispose();
        new PerfilEstudanteGUI(usuarioSenior, estudante, gerenciador).setVisible(true);
    }

    private void voltarMenu() {
        this.dispose();
        new menuSeniorGUI(usuarioSenior, gerenciador).setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Nosso Canto - Buscar Estudantes");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
    }
}