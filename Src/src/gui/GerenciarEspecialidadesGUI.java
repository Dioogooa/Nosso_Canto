package gui;

import entites.Estudante;
import dao.usuarioDAO;
import services.GerenciadorUsuarios;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class GerenciarEspecialidadesGUI extends JFrame {
    private Estudante usuarioEstudante;
    private GerenciadorUsuarios gerenciador;
    private DefaultListModel<String> listModel;
    private JList<String> especialidadesList;

    public GerenciarEspecialidadesGUI(Estudante estudante, GerenciadorUsuarios gerenciador) {
        this.usuarioEstudante = estudante;
        this.gerenciador = gerenciador;
        inicializarComponentes();
        configurarJanela();
        carregarEspecialidades();

        setVisible(true);
    }

    private void inicializarComponentes() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);

        //CABEÇALHO
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("GERENCIAR ESPECIALIDADES");
        titulo.setFont(new Font("Calibri", Font.BOLD, 20));
        titulo.setForeground(new Color(0, 102, 204));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Adicione, edite ou remova suas especialidades");
        subtitulo.setFont(new Font("Calibri", Font.PLAIN, 12));
        subtitulo.setForeground(Color.GRAY);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titulo);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(subtitulo);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // LISTA
        listModel = new DefaultListModel<>();
        especialidadesList = new JList<>(listModel);
        especialidadesList.setFont(new Font("Calibri", Font.PLAIN, 14));
        especialidadesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        especialidadesList.setFixedCellHeight(25);

        JScrollPane scrollPane = new JScrollPane(especialidadesList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Minhas Especialidades"));
        scrollPane.setPreferredSize(new Dimension(400, 200));

        //BOTÕES DE AÇÃO
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        botoesPanel.setBackground(Color.WHITE);
        botoesPanel.setBorder(BorderFactory.createTitledBorder("Ações"));

        // BOTÕES COM TAMANHO FIXO !! -- !!
        JButton btnAdicionar = criarBotaoVisivel("ADICIONAR", new Color(0, 150, 0));
        JButton btnEditar = criarBotaoVisivel("EDITAR", new Color(0, 102, 204));
        JButton btnRemover = criarBotaoVisivel("REMOVER", new Color(220, 53, 69));

        botoesPanel.add(btnAdicionar);
        botoesPanel.add(btnEditar);
        botoesPanel.add(btnRemover);

        // BOTÃO VOLTAR
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

        // AÇÕES DOS BOTÕES
        btnAdicionar.addActionListener(e -> adicionarEspecialidade());
        btnEditar.addActionListener(e -> editarEspecialidade());
        btnRemover.addActionListener(e -> removerEspecialidade());
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

    private void carregarEspecialidades() {
        listModel.clear();
        List<String> especialidades = usuarioEstudante.getEspecialidades();

        if (especialidades.isEmpty()) {
            listModel.addElement("Nenhuma especialidade cadastrada");
            especialidadesList.setEnabled(false);
        } else {
            for (String especialidade : especialidades) {
                listModel.addElement(especialidade);
            }
            especialidadesList.setEnabled(true);
        }
    }

    private void adicionarEspecialidade() {
        String novaEspecialidade = JOptionPane.showInputDialog(
                this,
                "Digite a nova especialidade:",
                "Adicionar Especialidade",
                JOptionPane.PLAIN_MESSAGE
        );

        if (novaEspecialidade != null && !novaEspecialidade.trim().isEmpty()) {
            usuarioEstudante.adcEspecialidade(novaEspecialidade.trim());

            try {
                usuarioDAO dao = new usuarioDAO();
                dao.atualizarEspecialidades(usuarioEstudante);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao salvar no banco: " + e.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            carregarEspecialidades();
            JOptionPane.showMessageDialog(this,
                    "Especialidade adicionada com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void editarEspecialidade() {
        int selectedIndex = especialidadesList.getSelectedIndex();

        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma especialidade para editar!",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String especialidadeAtual = listModel.getElementAt(selectedIndex);

        String novaEspecialidade = JOptionPane.showInputDialog(
                this,
                "Edite a especialidade:",
                "Editar Especialidade",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                especialidadeAtual
        ).toString();

        if (novaEspecialidade != null && !novaEspecialidade.trim().isEmpty()) {
            usuarioEstudante.getEspecialidades().set(selectedIndex, novaEspecialidade.trim());

            try {
                usuarioDAO dao = new usuarioDAO();
                dao.atualizarEspecialidades(usuarioEstudante);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao salvar no banco: " + e.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            carregarEspecialidades();
            JOptionPane.showMessageDialog(this,
                    "Especialidade editada com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void removerEspecialidade() {
        int selectedIndex = especialidadesList.getSelectedIndex();

        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma especialidade para remover!",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String especialidade = listModel.getElementAt(selectedIndex);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja remover a especialidade:\n\"" + especialidade + "\"?",
                "Confirmar Remoção",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            usuarioEstudante.getEspecialidades().remove(selectedIndex);

            try {
                usuarioDAO dao = new usuarioDAO();
                dao.atualizarEspecialidades(usuarioEstudante);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao salvar no banco: " + e.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            carregarEspecialidades();
            JOptionPane.showMessageDialog(this,
                    "Especialidade removida com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void voltarMenu() {
        this.dispose();
        new MenuEstudanteGUI(usuarioEstudante, gerenciador).setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Nosso Canto - Gerenciar Especialidades");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }
}