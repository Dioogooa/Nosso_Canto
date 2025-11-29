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

public class BuscarSeniorsGUI extends JFrame {
    private Estudante usuarioEstudante;
    private GerenciadorUsuarios gerenciador;
    private JPanel seniorsPanel;

    public BuscarSeniorsGUI(Usuario usuario, GerenciadorUsuarios gerenciador) {
        this.usuarioEstudante = (Estudante) usuario;
        this.gerenciador = gerenciador;
        inicializarComponentes();
        configurarJanela();
        carregarSeniors();
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

        JLabel tituloLabel = new JLabel("Buscar Seniors");
        tituloLabel.setFont(new Font("Calibri", Font.BOLD, 24));
        tituloLabel.setForeground(new Color(0, 102, 204));
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtituloLabel = new JLabel("Clique em um senior para ver o perfil completo");
        subtituloLabel.setFont(new Font("Calibri", Font.PLAIN, 14));
        subtituloLabel.setForeground(Color.GRAY);
        subtituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(tituloLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(subtituloLabel);

        // Painel de seniors (com scroll)
        seniorsPanel = new JPanel();
        seniorsPanel.setLayout(new BoxLayout(seniorsPanel, BoxLayout.Y_AXIS));
        seniorsPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(seniorsPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Seniors Cadastrados"));
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

    private void carregarSeniors() {
        List<Senior> seniors = gerenciador.listarSeniores();

        System.out.println("Debug - Total de seniors encontrados: " + seniors.size());

        if (seniors.isEmpty()) {
            JLabel semSeniorsLabel = new JLabel("Nenhum senior cadastrado no momento");
            semSeniorsLabel.setFont(new Font("Calibri", Font.ITALIC, 14));
            semSeniorsLabel.setForeground(Color.GRAY);
            semSeniorsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            seniorsPanel.add(semSeniorsLabel);
        } else {
            for (Senior senior : seniors) {
                seniorsPanel.add(criarCardSenior(senior));
                seniorsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        seniorsPanel.revalidate();
        seniorsPanel.repaint();
    }

    private JPanel criarCardSenior(Senior senior) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout(10, 5));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Informações do senior
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel nomeLabel = new JLabel(senior.getNome());
        nomeLabel.setFont(new Font("Calibri", Font.BOLD, 16));
        nomeLabel.setForeground(new Color(0, 102, 204));

        // Calcular idade
        int idade = calcularIdade(senior.getDataNascimento());
        JLabel idadeLabel = new JLabel(idade + " anos");
        idadeLabel.setFont(new Font("Calibri", Font.PLAIN, 14));

        // Condições de saúde (resumido)
        String condicoes = String.join(", ", senior.getCondicaoSaude());
        if (condicoes.length() > 50) {
            condicoes = condicoes.substring(0, 47) + "...";
        }

        JLabel condicoesLabel = new JLabel("Condições: " + condicoes);
        condicoesLabel.setFont(new Font("Calibri", Font.PLAIN, 12));
        condicoesLabel.setForeground(Color.GRAY);

        // Acompanhante
        JLabel acompanhanteLabel = new JLabel("Acompanhante: " + (senior.isTemAcompanhante() ? "Sim" : "Não"));
        acompanhanteLabel.setFont(new Font("Calibri", Font.PLAIN, 12));
        acompanhanteLabel.setForeground(Color.GRAY);

        infoPanel.add(nomeLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(idadeLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(condicoesLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        infoPanel.add(acompanhanteLabel);

        // Botão de ação
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton verPerfilButton = new JButton("Ver Perfil");
        verPerfilButton.setFont(new Font("Calibri", Font.BOLD, 12));
        verPerfilButton.setBackground(new Color(0, 102, 204));
        verPerfilButton.setForeground(Color.WHITE);
        verPerfilButton.setFocusPainted(false);
        verPerfilButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        verPerfilButton.setPreferredSize(new Dimension(100, 35));

        verPerfilButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirPerfilSenior(senior);
            }
        });

        buttonPanel.add(verPerfilButton);

        // Também permite clicar no card inteiro
        cardPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                abrirPerfilSenior(senior);
            }
        });

        cardPanel.add(infoPanel, BorderLayout.CENTER);
        cardPanel.add(buttonPanel, BorderLayout.EAST);

        return cardPanel;
    }

    private int calcularIdade(java.time.LocalDate dataNascimento) {
        if (dataNascimento == null) return 0;
        return java.time.LocalDate.now().getYear() - dataNascimento.getYear();
    }

    private void abrirPerfilSenior(Senior senior) {
        this.dispose();
        new PerfilSeniorGUI(usuarioEstudante, senior, gerenciador).setVisible(true);
    }

    private void voltarMenu() {
        this.dispose();
        new MenuEstudanteGUI(usuarioEstudante, gerenciador).setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Nosso Canto - Buscar Seniors");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
    }
}
