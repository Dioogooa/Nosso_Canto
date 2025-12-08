package gui;

import entites.Senior;
import services.GerenciadorUsuarios;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class GerenciarCondicoesSaudeGUI extends JFrame {
    private Senior usuarioSenior;
    private GerenciadorUsuarios gerenciador;
    private DefaultListModel<String> listModel;
    private JList<String> condicoesList;

    public GerenciarCondicoesSaudeGUI(Senior senior, GerenciadorUsuarios gerenciador) {
        this.usuarioSenior = senior;
        this.gerenciador = gerenciador;
        inicializarComponentes();
        configurarJanela();
        carregarCondicoes();

        setVisible(true);
    }

    private void inicializarComponentes() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);

        // CABEÇALHO
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("GERENCIAR CONDIÇÕES DE SAÚDE");
        titulo.setFont(new Font("Calibri", Font.BOLD, 20));
        titulo.setForeground(new Color(0, 102, 204));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Adicione, edite ou remova suas condições de saúde");
        subtitulo.setFont(new Font("Calibri", Font.PLAIN, 12));
        subtitulo.setForeground(Color.GRAY);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titulo);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(subtitulo);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // LISTA
        listModel = new DefaultListModel<>();
        condicoesList = new JList<>(listModel);
        condicoesList.setFont(new Font("Calibri", Font.PLAIN, 14));
        condicoesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        condicoesList.setFixedCellHeight(25);

        JScrollPane scrollPane = new JScrollPane(condicoesList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Condições de Saúde"));
        scrollPane.setPreferredSize(new Dimension(400, 200));

        // BOTÕES DE AÇÃO
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        botoesPanel.setBackground(Color.WHITE);
        botoesPanel.setBorder(BorderFactory.createTitledBorder("Ações"));

        JButton btnAdicionar = criarBotaoVisivel("ADICIONAR", new Color(0, 150, 0));
        JButton btnEditar = criarBotaoVisivel("EDITAR", new Color(0, 102, 204));
        JButton btnRemover = criarBotaoVisivel("REMOVER", new Color(220, 53, 69));

        botoesPanel.add(btnAdicionar);
        botoesPanel.add(btnEditar);
        botoesPanel.add(btnRemover);

        //BOTÃO VOLTAR
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        footerPanel.setBackground(Color.WHITE);

        JButton btnVoltar = new JButton("VOLTAR AO MENU");
        btnVoltar.setFont(new Font("Calibri", Font.BOLD, 14));
        btnVoltar.setBackground(new Color(100, 100, 100));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setFocusPainted(false);
        btnVoltar.setPreferredSize(new Dimension(180, 35));
        btnVoltar.addActionListener(e -> voltarMenu());

        footerPanel.add(btnVoltar);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(botoesPanel, BorderLayout.NORTH);
        southPanel.add(footerPanel, BorderLayout.SOUTH);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // AÇÕES
        btnAdicionar.addActionListener(e -> adicionarCondicao());
        btnEditar.addActionListener(e -> editarCondicao());
        btnRemover.addActionListener(e -> removerCondicao());
    }

    private JButton criarBotaoVisivel(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Calibri", Font.BOLD, 14));
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        botao.setPreferredSize(new Dimension(120, 40));
        return botao;
    }

    private void carregarCondicoes() {
        listModel.clear();
        List<String> condicoes = usuarioSenior.getCondicaoSaude();

        if (condicoes.isEmpty()) {
            listModel.addElement("Nenhuma condição de saúde cadastrada");
            condicoesList.setEnabled(false);
        } else {
            for (String condicao : condicoes) {
                listModel.addElement(condicao);
            }
            condicoesList.setEnabled(true);
        }
    }

    private void adicionarCondicao() {
        String novaCondicao = JOptionPane.showInputDialog(
                this,
                "Digite a nova condição de saúde:",
                "Adicionar Condição de Saúde",
                JOptionPane.PLAIN_MESSAGE
        );

        if (novaCondicao != null && !novaCondicao.trim().isEmpty()) {
            usuarioSenior.addCondicaoSaude(novaCondicao.trim());

            try {
                dao.usuarioDAO usuarioDAO = new dao.usuarioDAO();
                usuarioDAO.atualizarCondicoesSaude(usuarioSenior);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao salvar no banco de dados: " + e.getMessage(),
                                "Erro",
                                JOptionPane.ERROR_MESSAGE);
                return;
            }

            carregarCondicoes();
            JOptionPane.showMessageDialog(this,
                    "Condição de saúde adicionada com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void editarCondicao() {
        int selectedIndex = condicoesList.getSelectedIndex();

        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma condição para editar!",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String condicaoAtual = listModel.getElementAt(selectedIndex);

        String novaCondicao = JOptionPane.showInputDialog(
                this,
                "Edite a condição de saúde:",
                "Editar Condição de Saúde",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                condicaoAtual
        ).toString();

        if (novaCondicao != null && !novaCondicao.trim().isEmpty()) {
            usuarioSenior.getCondicaoSaude().set(selectedIndex, novaCondicao.trim());
            try {
                dao.usuarioDAO usuarioDAO = new dao.usuarioDAO();
                usuarioDAO.atualizarCondicoesSaude(usuarioSenior);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao salvar no banco de dados: " + e.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            carregarCondicoes();
            JOptionPane.showMessageDialog(this,
                    "Condição de saúde editada com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void removerCondicao() {
        int selectedIndex = condicoesList.getSelectedIndex();

        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma condição para remover!",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String condicao = listModel.getElementAt(selectedIndex);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja remover a condição de saúde:\n\"" + condicao + "\"?",
                "Confirmar Remoção",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            usuarioSenior.getCondicaoSaude().remove(selectedIndex);

            try {
                dao.usuarioDAO usuarioDAO = new dao.usuarioDAO();
                usuarioDAO.atualizarCondicoesSaude(usuarioSenior);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao salvar no banco de dados: " + e.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            carregarCondicoes();
            JOptionPane.showMessageDialog(this,
                    "Condição de saúde removida com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void voltarMenu() {
        this.dispose();
        new menuSeniorGUI(usuarioSenior, gerenciador).setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Nosso Canto - Gerenciar Condições de Saúde");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }
}