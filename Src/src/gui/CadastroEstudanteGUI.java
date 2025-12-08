package gui;

import entites.Estudante;
import services.GerenciadorUsuarios;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CadastroEstudanteGUI extends JFrame {
    private GerenciadorUsuarios gerenciadorUsuarios;
    private JTextField nomeField, emailField, telefoneField, cpfField, enderecoField;
    private JTextField instituicaoField, cursoField, semestreField, dataNascimentoField;
    private JPasswordField senhaField, confirmarSenhaField;
    private JCheckBox disponivelCheck;
    private JButton cadastrarButton, voltarButton;

    public CadastroEstudanteGUI(GerenciadorUsuarios gerenciadorUsuarios) {
        this.gerenciadorUsuarios = gerenciadorUsuarios;
        inicializarComponentes();
        configurarJanela();
    }

    private void inicializarComponentes() {
        // Painel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);

        // Logo
        ImageIcon logoIcon = carregarImagem("/resources/logo.png");
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        mainPanel.add(logoLabel);

        // Título
        JLabel tituloLabel = new JLabel("Cadastro - Estudante");
        tituloLabel.setFont(new Font("Calibri", Font.BOLD, 22));
        tituloLabel.setForeground(new Color(0, 102, 204));
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tituloLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(tituloLabel);

        // Painel de formulário
        JPanel formularioPanel = new JPanel();
        formularioPanel.setLayout(new BoxLayout(formularioPanel, BoxLayout.Y_AXIS));
        formularioPanel.setBackground(Color.WHITE);
        formularioPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Campos do formulário
        formularioPanel.add(criarCampoComLabel("Nome Completo:", nomeField = new JTextField(20)));
        formularioPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        formularioPanel.add(criarCampoComLabel("Email:", emailField = new JTextField(20)));
        formularioPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        formularioPanel.add(criarCampoComLabel("Senha:", senhaField = new JPasswordField(20)));
        formularioPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        formularioPanel.add(criarCampoComLabel("Confirmar Senha:", confirmarSenhaField = new JPasswordField(20)));
        formularioPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        formularioPanel.add(criarCampoComLabel("Telefone:", telefoneField = new JTextField(20)));
        formularioPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        formularioPanel.add(criarCampoComLabel("Data Nascimento:", dataNascimentoField = new JTextField(20)));
        dataNascimentoField.setToolTipText("Formato: DD/MM/AAAA (ex: 15/05/2000)");
        formularioPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        formularioPanel.add(criarCampoComLabel("CPF:", cpfField = new JTextField(20)));
        formularioPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        formularioPanel.add(criarCampoComLabel("Endereço:", enderecoField = new JTextField(20)));
        formularioPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        formularioPanel.add(criarCampoComLabel("Instituição:", instituicaoField = new JTextField(20)));
        formularioPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        formularioPanel.add(criarCampoComLabel("Curso:", cursoField = new JTextField(20)));
        formularioPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        formularioPanel.add(criarCampoComLabel("Semestre:", semestreField = new JTextField(20)));
        semestreField.setToolTipText("Número do semestre (ex: 4)");
        formularioPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Caixa de disponibilidade
        JPanel checkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        checkPanel.setBackground(Color.WHITE);
        disponivelCheck = new JCheckBox("Disponível para atendimentos");
        disponivelCheck.setSelected(false); // Inicia como não disponível
        checkPanel.add(disponivelCheck);
        formularioPanel.add(checkPanel);

        mainPanel.add(formularioPanel);

        // Painel de botões
        JPanel botoesPanel = new JPanel();
        botoesPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        botoesPanel.setBackground(Color.WHITE);
        botoesPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        cadastrarButton = new JButton("Cadastrar");
        cadastrarButton.setFont(new Font("Calibri", Font.BOLD, 14));
        cadastrarButton.setBackground(new Color(0, 102, 204));
        cadastrarButton.setForeground(Color.WHITE);
        cadastrarButton.setFocusPainted(false);
        cadastrarButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        voltarButton = new JButton("Voltar");
        voltarButton.setFont(new Font("Calibri", Font.PLAIN, 14));
        voltarButton.setBackground(new Color(240, 240, 240));
        voltarButton.setFocusPainted(false);
        voltarButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        botoesPanel.add(voltarButton);
        botoesPanel.add(cadastrarButton);

        mainPanel.add(botoesPanel);

        // Scrollzada
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane);

        configurarAcoes();
    }

    private JPanel criarCampoComLabel(String labelText, JComponent campo) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Calibri", Font.BOLD, 16));
        label.setPreferredSize(new Dimension(180, 25));

        campo.setPreferredSize(new Dimension(200, 25));

        panel.add(label);
        panel.add(campo);

        return panel;
    }

    private void configurarAcoes() {
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarEstudante();
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                voltarLogin();
            }
        });
    }

    private void cadastrarEstudante() {
        try {
            if (!validarCampos()) {
                return;
            }

            String id = "E" + (gerenciadorUsuarios.listarEstudantes().size() + 1);

            // Converter data
            LocalDate dataNascimento = LocalDate.parse(dataNascimentoField.getText(),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            // Converter semestre para int para não dar erro tmj
            int semestre = Integer.parseInt(semestreField.getText().trim());

            Estudante estudante = new Estudante(
                    id,
                    nomeField.getText().trim(),
                    emailField.getText().trim(),
                    new String(senhaField.getPassword()),
                    telefoneField.getText().trim(),
                    dataNascimento,
                    cpfField.getText().trim(),
                    enderecoField.getText().trim(),
                    instituicaoField.getText().trim(),
                    cursoField.getText().trim(),
                    semestre,
                    disponivelCheck.isSelected()
            );

            gerenciadorUsuarios.cadastrarUsuario(estudante);

            JOptionPane.showMessageDialog(this,
                    "Estudante cadastrado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);

            voltarLogin();

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                    "Data de nascimento inválida! Use o formato DD/MM/AAAA",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Semestre deve ser um número!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao cadastrar: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCampos() {
        if (nomeField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty() ||
                new String(senhaField.getPassword()).isEmpty() ||
                telefoneField.getText().trim().isEmpty() ||
                dataNascimentoField.getText().trim().isEmpty() ||
                cpfField.getText().trim().isEmpty() ||
                enderecoField.getText().trim().isEmpty() ||
                instituicaoField.getText().trim().isEmpty() ||
                cursoField.getText().trim().isEmpty() ||
                semestreField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Preencha todos os campos!",
                    "Erro de Validação",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String senha = new String(senhaField.getPassword());
        String confirmarSenha = new String(confirmarSenhaField.getPassword());

        if (!senha.equals(confirmarSenha)) {
            JOptionPane.showMessageDialog(this,
                    "As senhas não coincidem!",
                    "Erro de Validação",
                    JOptionPane.WARNING_MESSAGE);
            senhaField.setText("");
            confirmarSenhaField.setText("");
            senhaField.requestFocus();
            return false;
        }

        if (cpfField.getText().trim().length() != 11) { //Mesma validacao do senior
            JOptionPane.showMessageDialog(this,
                    "CPF deve ter 11 dígitos!",
                    "Erro de Validação",
                    JOptionPane.WARNING_MESSAGE);
            cpfField.requestFocus();
            return false;
        }

        if (!emailField.getText().contains("@")) { //Mesma validacao do senior
            JOptionPane.showMessageDialog(this,
                    "Email inválido!",
                    "Erro de Validação",
                    JOptionPane.WARNING_MESSAGE);
            emailField.requestFocus();
            return false;
        }

        // Validar semestre
        try {
            int semestre = Integer.parseInt(semestreField.getText().trim());
            if (semestre <= 0 || semestre > 20) {
                JOptionPane.showMessageDialog(this,
                        "Semestre deve ser um número entre 1 e 20!",
                        "Erro de Validação",
                        JOptionPane.WARNING_MESSAGE);
                semestreField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Semestre deve ser um número!",
                    "Erro de Validação",
                    JOptionPane.WARNING_MESSAGE);
            semestreField.requestFocus();
            return false;
        }

        return true;
    }

    private void voltarLogin() {
        this.dispose();
        new loginGUI().setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Nosso Canto - Cadastro Estudante");
        setSize(500, 700); // Um pouco mais alto por ter mais campos!!
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    private ImageIcon carregarImagem(String caminho) { //metodo para carregar a logo!
        try {
            java.net.URL imgURL = getClass().getResource(caminho);

            if (imgURL == null) {
                imgURL = getClass().getResource(caminho.substring(1));
            }

            if (imgURL == null) {
                try {
                    java.io.File file = new java.io.File("src" + caminho);
                    if (file.exists()) {
                        imgURL = file.toURI().toURL();
                    }
                } catch (Exception e) {
                    System.out.println("Arquivo local não encontrado");
                }
            }

            if (imgURL != null) {
                ImageIcon iconOriginal = new ImageIcon(imgURL);
                Image imagemRedimensionada = iconOriginal.getImage()
                        .getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                return new ImageIcon(imagemRedimensionada);
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar imagem: " + e.getMessage());
        }

        java.awt.Image imagemVazia = new java.awt.image.BufferedImage(60, 60,
                java.awt.image.BufferedImage.TYPE_INT_ARGB);
        return new ImageIcon(imagemVazia);
    }
}