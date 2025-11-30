package gui;

import entites.Estudante;
import entites.Usuario;
import services.GerenciadorUsuarios;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuEstudanteGUI extends JFrame {
    private Estudante usuario;
    private GerenciadorUsuarios gerenciador;

    public MenuEstudanteGUI(Usuario usuario, GerenciadorUsuarios gerenciador) {
        this.usuario = (Estudante) usuario;
        this.gerenciador = gerenciador;
        inicializarComponentes();
        configurarJanela();
    }

    private void inicializarComponentes() { //testando com emojis esse haha (N FUNCIONA)
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        mainPanel.setBackground(Color.WHITE);

        // Título
        JLabel tituloLabel = new JLabel("Nosso Canto");
        tituloLabel.setFont(new Font("Calibri", Font.BOLD, 28));
        tituloLabel.setForeground(new Color(0, 102, 204));
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tituloLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Salve
        JLabel saudacaoLabel = new JLabel("Olá, " + usuario.getNome() + "!");
        saudacaoLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        saudacaoLabel.setForeground(Color.DARK_GRAY);
        saudacaoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        saudacaoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // MenuPannel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Botao
        JButton buscarSeniorsBtn = criarBotaoMenu(" Buscar Seniors");
        JButton minhasConsultasBtn = criarBotaoMenu(" Minhas Consultas");
        JButton chatBtn = criarBotaoMenu(" Chat com Seniors");
        JButton especialidadesBtn = criarBotaoMenu(" Gerenciar Especialidades");
        JButton disponibilidadeBtn = criarBotaoMenu(" Atualizar Disponibilidade");
        JButton perfilBtn = criarBotaoMenu(" Ver Meu Perfil");
        JButton sairBtn = criarBotaoSair(" Sair da Conta");

        // Adicionar botao + espaçamento
        menuPanel.add(buscarSeniorsBtn);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        menuPanel.add(minhasConsultasBtn);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        menuPanel.add(chatBtn);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        menuPanel.add(especialidadesBtn);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        menuPanel.add(disponibilidadeBtn);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        menuPanel.add(perfilBtn);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(sairBtn);

        // Adicionar componentes ao main panel
        mainPanel.add(tituloLabel);
        mainPanel.add(saudacaoLabel);
        mainPanel.add(menuPanel);

        getContentPane().setBackground(Color.WHITE);
        add(mainPanel);

        configurarAcoes(buscarSeniorsBtn, minhasConsultasBtn, chatBtn, especialidadesBtn,
                disponibilidadeBtn, perfilBtn, sairBtn);
    }

    private JButton criarBotaoMenu(String texto) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Calibri", Font.BOLD, 16));
        botao.setBackground(new Color(240, 240, 240));
        botao.setForeground(Color.DARK_GRAY);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(12, 25, 12, 25)
        ));
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setMaximumSize(new Dimension(280, 50));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return botao;
    }

    private JButton criarBotaoSair(String texto) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Calibri", Font.BOLD, 16));
        botao.setBackground(new Color(220, 53, 69));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setMaximumSize(new Dimension(220, 45));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return botao;
    }

    private void configurarAcoes(JButton buscarSeniorsBtn, JButton minhasConsultasBtn,
                                 JButton chatBtn, JButton especialidadesBtn,
                                 JButton disponibilidadeBtn, JButton perfilBtn,
                                 JButton sairBtn) {

        buscarSeniorsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BuscarSeniorsGUI(usuario, gerenciador).setVisible(true);
                dispose();
            }
        });

        minhasConsultasBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MinhasConsultasEstudanteGUI(usuario, gerenciador).setVisible(true);
                dispose();
            }
        });

        chatBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SelecionarConsultaChatGUI(usuario, gerenciador, "ESTUDANTE").setVisible(true);
                dispose();
            }
        });

        especialidadesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MenuEstudanteGUI.this,
                        "Gerenciar Especialidades - Em desenvolvimento");
            }
        });

        disponibilidadeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Toggle disponibilidade
                boolean novaDisponibilidade = !usuario.isDisponivel();
                usuario.setDisponivel(novaDisponibilidade);

                String status = novaDisponibilidade ? "DISPONÍVEL" : "INDISPONÍVEL";
                JOptionPane.showMessageDialog(MenuEstudanteGUI.this,
                        "Status atualizado: " + status + "\n\n" +
                                "Agora você " + (novaDisponibilidade ? "aparecerá" : "NÃO aparecerá") +
                                " nas buscas dos seniors.",
                        "Disponibilidade Atualizada",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        perfilBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder perfil = new StringBuilder();
                perfil.append("=== MEU PERFIL ===\n\n");
                perfil.append(" Estudante: ").append(usuario.getNome()).append("\n");
                perfil.append(" Email: ").append(usuario.getEmail()).append("\n");
                perfil.append(" Telefone: ").append(usuario.getTelefone()).append("\n");
                perfil.append(" Instituição: ").append(usuario.getInstuicao()).append("\n");
                perfil.append(" Curso: ").append(usuario.getCurso()).append("\n");
                perfil.append(" Semestre: ").append(usuario.getSemestre()).append("\n");
                perfil.append(" Especialidades: ").append(usuario.getEspecialidades()).append("\n");
                perfil.append(" Disponibilidade: ").append(usuario.isDisponivel() ? "SIM" : "NÃO");

                JOptionPane.showMessageDialog(MenuEstudanteGUI.this,
                        perfil.toString(), "Meu Perfil", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        sairBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(MenuEstudanteGUI.this,
                        "Deseja realmente sair da conta?",
                        "Confirmar Saída",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    dispose();
                    new loginGUI().setVisible(true);
                }
            }
        });
    }

    private void configurarJanela() {
        setTitle("Nosso Canto - Menu Estudante");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }
}