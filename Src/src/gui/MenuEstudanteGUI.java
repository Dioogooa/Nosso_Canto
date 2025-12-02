package gui;

import entites.Estudante;
import entites.Usuario;
import dao.usuarioDAO;
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

    private void inicializarComponentes() {
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

        // Saudação
        JLabel saudacaoLabel = new JLabel("Olá, " + usuario.getNome() + "!");
        saudacaoLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        saudacaoLabel.setForeground(Color.DARK_GRAY);
        saudacaoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        saudacaoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // STATUS DE DISPONIBILIDADE (NOVO!)
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        statusPanel.setBackground(Color.WHITE);

        JLabel statusLabel = new JLabel("Status Atual:");
        statusLabel.setFont(new Font("Calibri", Font.BOLD, 14));

        JLabel disponibilidadeLabel = criarLabelDisponibilidade();

        statusPanel.add(statusLabel);
        statusPanel.add(disponibilidadeLabel);
        statusPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // MenuPanel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Botões
        JButton buscarSeniorsBtn = criarBotaoMenu(" Buscar Seniors");
        JButton minhasConsultasBtn = criarBotaoMenu(" Minhas Consultas");
        JButton chatBtn = criarBotaoMenu(" Chat com Seniors");
        JButton especialidadesBtn = criarBotaoMenu(" Gerenciar Especialidades");
        JButton disponibilidadeBtn = criarBotaoMenu(" Atualizar Disponibilidade");
        JButton perfilBtn = criarBotaoMenu(" Ver Meu Perfil");
        JButton sairBtn = criarBotaoSair(" Sair da Conta");

        // Adicionar botão + espaçamento
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
        mainPanel.add(statusPanel); // ADICIONADO AQUI!
        mainPanel.add(menuPanel);

        getContentPane().setBackground(Color.WHITE);
        add(mainPanel);

        configurarAcoes(buscarSeniorsBtn, minhasConsultasBtn, chatBtn, especialidadesBtn,
                disponibilidadeBtn, perfilBtn, sairBtn);
    }

    private JLabel criarLabelDisponibilidade() {
        JLabel label = new JLabel();
        label.setFont(new Font("Calibri", Font.BOLD, 14));
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));

        atualizarLabelDisponibilidade(label);

        return label;
    }

    private void atualizarLabelDisponibilidade(JLabel label) {
        if (usuario.isDisponivel()) {
            label.setText("DISPONÍVEL");
            label.setForeground(Color.WHITE);
            label.setBackground(new Color(0, 150, 0)); // VERDE
            label.setOpaque(true);
        } else {
            label.setText("INDISPONÍVEL");
            label.setForeground(Color.WHITE);
            label.setBackground(new Color(220, 53, 69)); // VERMELHO
            label.setOpaque(true);
        }
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
                new GerenciarEspecialidadesGUI(usuario, gerenciador).setVisible(true);
                dispose();
            }
        });

        disponibilidadeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String statusAtual = usuario.isDisponivel() ? "DISPONÍVEL" : "INDISPONÍVEL";
                String novoStatus = usuario.isDisponivel() ? "INDISPONÍVEL" : "DISPONÍVEL";

                int confirm = JOptionPane.showConfirmDialog(MenuEstudanteGUI.this,
                        "Status atual: " + statusAtual + "\n\n" +
                                "Deseja alterar para: " + novoStatus + "?\n\n" +
                                "Isso afetará como os seniors te encontram nas buscas.",
                        "Confirmar Alteração",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean novaDisponibilidade = !usuario.isDisponivel();
                    usuario.setDisponivel(novaDisponibilidade);

                    try {
                        usuarioDAO dao = new usuarioDAO();
                        atualizarDisponibilidadeNoBD(usuario.getId(), novaDisponibilidade);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(MenuEstudanteGUI.this,
                                "Erro ao salvar no banco de dados: " + ex.getMessage(),
                                "Erro",
                                JOptionPane.ERROR_MESSAGE);
                    }

                    atualizarDisplayDisponibilidade();

                    JOptionPane.showMessageDialog(MenuEstudanteGUI.this,
                            "Status atualizado para: " + (novaDisponibilidade ? "DISPONÍVEL" : "INDISPONÍVEL"),
                            "Disponibilidade Atualizada",
                            JOptionPane.INFORMATION_MESSAGE);
                }
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

    private void atualizarDisplayDisponibilidade() { //esse ficou braboooo
        Component[] components = getContentPane().getComponents();
        if (components.length > 0 && components[0] instanceof JPanel) {
            JPanel mainPanel = (JPanel) components[0];
            Component[] mainComponents = mainPanel.getComponents();

            if (mainComponents.length > 2 && mainComponents[2] instanceof JPanel) {
                JPanel statusPanel = (JPanel) mainComponents[2];

                if (statusPanel.getComponentCount() > 1 && statusPanel.getComponent(1) instanceof JLabel) {
                    JLabel disponibilidadeLabel = (JLabel) statusPanel.getComponent(1);
                    atualizarLabelDisponibilidade(disponibilidadeLabel);
                }
            }
        }
    }

    private void atualizarDisponibilidadeNoBD(String estudanteId, boolean disponivel) {
        try {
            usuarioDAO dao = new usuarioDAO();
            dao.atualizarDisponibilidadeEstudante(estudanteId, disponivel);
        } catch (Exception e) {
            System.out.println("Erro ao atualizar disponibilidade no BD: " + e.getMessage());
            throw e;
        }
    }
}