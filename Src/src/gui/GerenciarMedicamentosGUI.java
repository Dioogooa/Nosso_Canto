package gui;

import entites.Senior;
import services.GerenciadorUsuarios;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class GerenciarMedicamentosGUI extends JFrame {
    private Senior usuarioSenior;
    private GerenciadorUsuarios gerenciador;
    private DefaultListModel<String> listModel;
    private JList<String> medicamentosList;

    public GerenciarMedicamentosGUI(Senior senior, GerenciadorUsuarios gerenciador) {
        this.usuarioSenior = senior;
        this.gerenciador = gerenciador;
        inicializarComponentes();
        configurarJanela();
        carregarMedicamentos();

        setVisible(true);
    }

    private void inicializarComponentes() {
        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);

        //CABEÇALHO
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("GERENCIAR MEDICAMENTOS");
        titulo.setFont(new Font("Calibri", Font.BOLD, 20));
        titulo.setForeground(new Color(0, 102, 204));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Adicione, edite ou remova seus medicamentos");
        subtitulo.setFont(new Font("Calibri", Font.PLAIN, 12));
        subtitulo.setForeground(Color.GRAY);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titulo);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(subtitulo);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // LISTA
        listModel = new DefaultListModel<>();
        medicamentosList = new JList<>(listModel);
        medicamentosList.setFont(new Font("Calibri", Font.PLAIN, 14));
        medicamentosList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        medicamentosList.setFixedCellHeight(25);

        JScrollPane scrollPane = new JScrollPane(medicamentosList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Medicamentos"));
        scrollPane.setPreferredSize(new Dimension(400, 200)); // TAMANHO FIXO

        //BOTÕES DE AÇÃO
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        botoesPanel.setBackground(Color.WHITE);
        botoesPanel.setBorder(BorderFactory.createTitledBorder("Ações"));

        // BOTÕES COM TAMANHO FIXO
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

        // Painel para os botões de ação + voltar
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(botoesPanel, BorderLayout.NORTH);
        southPanel.add(footerPanel, BorderLayout.SOUTH);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // AÇÕES DOS BOTÕES
        btnAdicionar.addActionListener(e -> adicionarMedicamento());
        btnEditar.addActionListener(e -> editarMedicamento());
        btnRemover.addActionListener(e -> removerMedicamento());

        // DEBUG
        System.out.println("DEBUG: Botões criados - devem estar visíveis!");
    }

    private JButton criarBotaoVisivel(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Calibri", Font.BOLD, 14));
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Borda preta para VER
        botao.setPreferredSize(new Dimension(120, 40)); // TAMANHO FIXO E GRANDE
        return botao;
    }

    private void carregarMedicamentos() {
        listModel.clear();
        List<String> medicamentos = usuarioSenior.getMedicamentos();

        if (medicamentos.isEmpty()) {
            listModel.addElement("Nenhum medicamento cadastrado");
            medicamentosList.setEnabled(false);
        } else {
            for (String medicamento : medicamentos) {
                listModel.addElement(medicamento);
            }
            medicamentosList.setEnabled(true);
        }
    }

    private void adicionarMedicamento() {
        String novoMedicamento = JOptionPane.showInputDialog(
                this,
                "Digite o nome do novo medicamento:",
                "Adicionar Medicamento",
                JOptionPane.PLAIN_MESSAGE
        );

        if (novoMedicamento != null && !novoMedicamento.trim().isEmpty()) {
            usuarioSenior.addMedicamento(novoMedicamento.trim());

            try {
                dao.usuarioDAO usuarioDAO = new dao.usuarioDAO();
                usuarioDAO.atualizarMedicamentos(usuarioSenior);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao salvar no banco de dados: " + e.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            carregarMedicamentos();
            JOptionPane.showMessageDialog(this,
                    "Medicamento adicionado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void editarMedicamento() {
        int selectedIndex = medicamentosList.getSelectedIndex();

        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um medicamento para editar!",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String medicamentoAtual = listModel.getElementAt(selectedIndex);

        String novoMedicamento = JOptionPane.showInputDialog(
                this,
                "Edite o medicamento:",
                "Editar Medicamento",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                medicamentoAtual
        ).toString();

        if (novoMedicamento != null && !novoMedicamento.trim().isEmpty()) {
            usuarioSenior.getMedicamentos().set(selectedIndex, novoMedicamento.trim());

            try {
                dao.usuarioDAO usuarioDAO = new dao.usuarioDAO();
                usuarioDAO.atualizarMedicamentos(usuarioSenior);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao salvar no banco de dados: " + e.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            carregarMedicamentos();
            JOptionPane.showMessageDialog(this,
                    "Medicamento editado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void removerMedicamento() {
        int selectedIndex = medicamentosList.getSelectedIndex();

        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um medicamento para remover!",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String medicamento = listModel.getElementAt(selectedIndex);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja remover o medicamento:\n\"" + medicamento + "\"?",
                "Confirmar Remoção",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            usuarioSenior.getMedicamentos().remove(selectedIndex);

            try {
                dao.usuarioDAO usuarioDAO = new dao.usuarioDAO();
                usuarioDAO.atualizarMedicamentos(usuarioSenior);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao salvar no banco de dados: " + e.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            carregarMedicamentos();
            JOptionPane.showMessageDialog(this,
                    "Medicamento removido com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void voltarMenu() {
        this.dispose();
        new menuSeniorGUI(usuarioSenior, gerenciador).setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Nosso Canto - Gerenciar Medicamentos");
        setSize(500, 450); // UM POUCO MAIOR
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }
}