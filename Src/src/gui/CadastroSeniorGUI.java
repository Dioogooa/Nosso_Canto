package gui;

import entites.Senior;
import services.GerenciadorUsuarios;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CadastroSeniorGUI extends JFrame {
    private GerenciadorUsuarios gerenciadorUsuarios;
    private JTextField nomeField, emailField, telefoneField, cpfField, enderecoField, contatoEmergenciaField;
    private JPasswordField senhaField, confirmarSenhaField;
    private JTextField dataNascimentoField;
    private JCheckBox temAcompanhanteCheck;
    private JButton cadastrarButton, voltarButton;

    public CadastroSeniorGUI(GerenciadorUsuarios gerenciadorUsuarios) {
        this.gerenciadorUsuarios = gerenciadorUsuarios;
        inicializarComponentes();
        configurarJanela();
    }

    private void inicializarComponentes() {
        //Painel principal com scroll
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20 ,30));
        mainPanel.setBackground(Color.WHITE);

        //logo
        ImageIcon logoIcon = carregarImagem("/resources/logo.png");
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        mainPanel.add(logoLabel);


        //TITULO grandao
        JLabel tituloLabel = new JLabel("Cadastro - Senior");
        tituloLabel.setFont(new Font("calibri", Font.BOLD, 22));
        tituloLabel.setForeground(new Color(0, 102, 204));
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tituloLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(tituloLabel);

        //Painel de formulario bebe
        JPanel formularioPanel = new JPanel();
        formularioPanel.setLayout(new BoxLayout(formularioPanel, BoxLayout.Y_AXIS));
        formularioPanel.setBackground(Color.WHITE);
        formularioPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10 ,10));

        //CAMPOS DO FORMULARIO
        formularioPanel.add(criarCampoComLabel("Nome completo:", nomeField = new JTextField(20)));
        formularioPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        formularioPanel.add(criarCampoComLabel("Email:", emailField = new JTextField(20)));
        formularioPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        formularioPanel.add(criarCampoComLabel("Senha:", senhaField = new JPasswordField(20)));
        formularioPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        formularioPanel.add(criarCampoComLabel("Confirmar Senha:", confirmarSenhaField = new JPasswordField(20)));
        formularioPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        formularioPanel.add(criarCampoComLabel("Telefone:", telefoneField = new JTextField(20)));
        formularioPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        formularioPanel.add(criarCampoComLabel("Data Nascimento:", criarCampoDataComPlaceholder()));
        formularioPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        formularioPanel.add(criarCampoComLabel("CPF:", cpfField = new JTextField(20)));
        formularioPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        formularioPanel.add(criarCampoComLabel("Endereço:", enderecoField = new JTextField(20)));
        formularioPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        formularioPanel.add(criarCampoComLabel("Contato Emergência:", contatoEmergenciaField = new JTextField(20)));
        formularioPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        //Checagemmm
        JPanel  checkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        checkPanel.setBackground(Color.WHITE);
        temAcompanhanteCheck = new JCheckBox("Precisa de acompanhante?");
        checkPanel.add(temAcompanhanteCheck);
        formularioPanel.add(checkPanel);

        mainPanel.add(formularioPanel);

        //Painel dos Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        cadastrarButton = new JButton("Cadastrar");
        cadastrarButton.setFont(new Font("calibri", Font.BOLD, 14));
        cadastrarButton.setForeground(new Color(0, 102, 204));
        cadastrarButton.setBackground(Color.WHITE);
        cadastrarButton.setFocusPainted(false);
        cadastrarButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        voltarButton = new JButton("Voltar");
        voltarButton.setFont(new Font("Calibri", Font.PLAIN, 14));
        voltarButton.setBackground(new Color(240, 240, 240));
        voltarButton.setFocusPainted(false);
        voltarButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        buttonPanel.add(voltarButton);
        buttonPanel.add(cadastrarButton);

        mainPanel.add(buttonPanel);

        //SCROLLL AHAHHA
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane);

        configurarAcoes();
    }

    //metodo auxiliar para a trabalhosa da data
    private JTextField criarCampoDataComPlaceholder() {
        dataNascimentoField = new JTextField(20);

        // Adiciona dica de como preencher
        dataNascimentoField.setToolTipText("Formato: DD/MM/AAAA (ex: 20/01/2001)");

        dataNascimentoField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (dataNascimentoField.getText().equals("DD/MM/AAAA")) {
                    dataNascimentoField.setText("");
                    dataNascimentoField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (dataNascimentoField.getText().isEmpty()) {
                    dataNascimentoField.setText("DD/MM/AAAA");
                    dataNascimentoField.setForeground(Color.GRAY);
                }
            }
        });

        return dataNascimentoField;
    }

    //metodo auxiliar para os campos
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
                cadastrarSenior();
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                voltarLogin();
            }
        });

    }

    private void cadastrarSenior() {
        try {
            if (!validarCampos()) {
                return;
            }

            String id = "S" + (gerenciadorUsuarios.listarSeniores().size() + 1);

            //Converter data !
            LocalDate dataNascimento = LocalDate.parse(dataNascimentoField.getText(),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            //Criar o senior
            Senior senior = new Senior(
                    id,
                    nomeField.getText().trim(),
                    emailField.getText().trim(),
                    new String(senhaField.getPassword()),
                    telefoneField.getText().trim(),
                    dataNascimento,
                    cpfField.getText().trim(),
                    enderecoField.getText().trim(),
                    contatoEmergenciaField.getText().trim(),
                    temAcompanhanteCheck.isSelected()
            );

            //Cadastrar dql jeito + aviso!
            gerenciadorUsuarios.cadastrarUsuario(senior);
            JOptionPane.showMessageDialog(this,
                    "Sênior cadastrado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);

            voltarLogin();

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                    "Data de nascimento invalida! Use o formato DD/MM/AAAA",
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
                contatoEmergenciaField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Preencha todos os campos!",
                        "Erro de Validação",
                        JOptionPane.WARNING_MESSAGE);
                return false;
        }

        //Senha e Confirmar senha tem q ser iGUAL
        if (!senhaField.getText().equals(confirmarSenhaField.getText())) {
            JOptionPane.showMessageDialog(this,
                    "As senhas não coincidem!",
                    "Erro na validação",
                    JOptionPane.WARNING_MESSAGE);
            senhaField.setText("");
            confirmarSenhaField.setText("");
            senhaField.requestFocus();
            return false;
        }

        //Teste de cpf
        if (cpfField.getText().trim().length() != 11) {
            JOptionPane.showMessageDialog(this,
                    "CPF deve ter 11 dígitos!",
                    "Erro",
                    JOptionPane.WARNING_MESSAGE);
            cpfField.setText("");
            cpfField.requestFocus();
            return false;
        }

        // Verificar email
        if (!emailField.getText().contains("@")) {
            JOptionPane.showMessageDialog(this,
                    "Email inválido!",
                    "Erro",
                    JOptionPane.WARNING_MESSAGE);
            emailField.requestFocus();
            return false;
        }

        return true;
    }

    private void voltarLogin() {
        this.dispose();
        new loginGUI().setVisible(true);
    }

    private void configurarJanela(){
        setTitle("Nosso Canto - Cadastro Sênior");
        setSize(500, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    private ImageIcon carregarImagem(String caminho) {
        try {
            System.out.println("Procurando imagem em: " + caminho);

            // tentando caminhos -
            java.net.URL imgURL = getClass().getResource(caminho);

            if (imgURL == null) {
                // Tenta caminho alternativo--
                imgURL = getClass().getResource(caminho.substring(1));
            }

            if (imgURL == null) {
                // Tenta como arquivo local---
                try {
                    java.io.File file = new java.io.File("src" + caminho);
                    if (file.exists()) {
                        imgURL = file.toURI().toURL();
                        System.out.println("Imagem encontrada como arquivo: " + file.getAbsolutePath());
                    }
                } catch (Exception e) {
                    System.out.println("Arquivo local não encontrado");
                }
            }

            if (imgURL != null) {
                ImageIcon iconOriginal = new ImageIcon(imgURL);

                Image imagemRedimensionada = iconOriginal.getImage()
                        .getScaledInstance(120, 120, Image.SCALE_SMOOTH);

                System.out.println("Imagem carregada: " + imgURL);
                return new ImageIcon(imagemRedimensionada);

            } else {

                System.out.println("Imagem NÃO encontrada em nenhum caminho: " + caminho);
                // Retorna um ícone vazio para evitar Erro
                return criarIconeVazio(60, 60);
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar imagem: " + e.getMessage());
            return criarIconeVazio(60,60);
        }
    }

    private ImageIcon criarIconeVazio(int largura, int altura) {
        // Cria um ícone vazio para evitar NullPointerException
        java.awt.Image imagemVazia = new java.awt.image.BufferedImage(largura, altura, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        return new ImageIcon(imagemVazia);
    }

}
