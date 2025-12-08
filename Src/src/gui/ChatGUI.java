package gui;

import entites.Consulta;
import entites.Mensagem;
import entites.Usuario;
import dao.mensagemDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ChatGUI extends JFrame {
    private Usuario usuarioLogado;
    private Usuario outroUsuario;
    private Consulta consulta;
    private mensagemDAO mensagemDAO;

    private JTextArea chatArea;
    private JTextField mensagemField;
    private JButton enviarButton;

    public ChatGUI(Usuario usuarioLogado, Usuario outroUsuario, Consulta consulta) {
        this.usuarioLogado = usuarioLogado;
        this.outroUsuario = outroUsuario;
        this.consulta = consulta;
        this.mensagemDAO = new mensagemDAO();

        // CONFIGURAÇÃO DA JANELA (igual ao que funciona)
        setTitle("Chat - " + outroUsuario.getNome());
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        criarInterfaceQueFunciona();
        carregarMensagens();

        setVisible(true);
    }

    private void criarInterfaceQueFunciona() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);

        // CABEÇALHO (Cima)
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titulo = new JLabel("Chat com " + outroUsuario.getNome());
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Calibri", Font.BOLD, 16));
        headerPanel.add(titulo);

        // ÁREA DO CHAT (Centro)
        chatArea = new JTextArea(15, 40);
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chatArea.setBackground(new Color(248, 248, 248));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);

        JScrollPane chatScroll = new JScrollPane(chatArea);
        chatScroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        //ÁREA DE ENVIO (parte de baixo)
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        inputPanel.setBackground(Color.WHITE);

        // CAMPO DE TEXTO (simples, mas bonito)
        mensagemField = new JTextField();
        mensagemField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mensagemField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        mensagemField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    enviarMensagem();
                }
            }
        });

        // BOTÃO ENVIAR (visível e bonito)
        enviarButton = new JButton("Enviar");
        enviarButton.setFont(new Font("Calibri", Font.BOLD, 14));
        enviarButton.setBackground(new Color(0, 102, 204));
        enviarButton.setForeground(Color.WHITE);
        enviarButton.setFocusPainted(false);
        enviarButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        enviarButton.setPreferredSize(new Dimension(100, 40)); // TAMANHO FIXO

        // GARANTIR que o botão está visível (estava sumindo)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(enviarButton);

        inputPanel.add(mensagemField, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.EAST);

        // BOTÃO VOLTAR (Parte de  baixo)
        JButton voltarButton = new JButton("Voltar");
        voltarButton.setFont(new Font("Calibri", Font.PLAIN, 12));
        voltarButton.setBackground(new Color(240, 240, 240));
        voltarButton.addActionListener(e -> voltar());

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footerPanel.setBackground(Color.WHITE);
        footerPanel.add(voltarButton);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(chatScroll, BorderLayout.CENTER);

        // Painel combinado para input + footer (famoso rodapé)
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(inputPanel, BorderLayout.CENTER);
        southPanel.add(footerPanel, BorderLayout.SOUTH);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        add(mainPanel);

        enviarButton.addActionListener(e -> enviarMensagem()); //--> jeito de envio que funcionou antes
        //request focus
        SwingUtilities.invokeLater(() -> {
            mensagemField.requestFocusInWindow();
            System.out.println("DEBUG: Interface carregada - botão visível? " + enviarButton.isVisible());
            System.out.println("DEBUG: Tamanho do botão: " + enviarButton.getSize());
        });
    }

    private void carregarMensagens() {
        try {
            List<Mensagem> mensagens = mensagemDAO.buscarConversa(
                    usuarioLogado.getId(), outroUsuario.getId());

            chatArea.setText(""); // Limpar

            for (Mensagem msg : mensagens) {
                boolean minha = msg.getRemetente().getId().equals(usuarioLogado.getId());
                String nome = minha ? "Você" : msg.getRemetente().getNome();
                String hora = msg.getDataHora().format(DateTimeFormatter.ofPattern("HH:mm"));

                chatArea.append("[" + hora + "] " + nome + ": " + msg.getTexto() + "\n");
            }

            chatArea.setCaretPosition(chatArea.getDocument().getLength());

        } catch (Exception e) {
            chatArea.setText("Erro ao carregar mensagens: " + e.getMessage());
        }
    }

    private void enviarMensagem() {
        String texto = mensagemField.getText().trim();

        if (texto.isEmpty()) {
            return;
        }

        try {
            // Salvar no banco
            String msgId = "M" + System.currentTimeMillis();
            Mensagem mensagem = new Mensagem(msgId, usuarioLogado, outroUsuario, texto);
            mensagemDAO.salvar(mensagem);

            // Atualizar tela
            carregarMensagens();

            // Limpar campo
            mensagemField.setText("");
            mensagemField.requestFocusInWindow();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao enviar mensagem: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void voltar() {
        dispose();
        if (usuarioLogado.getTipoUsuario().equals("Senior")) {
            new MinhasConsultasSeniorGUI(usuarioLogado, new services.GerenciadorUsuarios()).setVisible(true);
        } else {
            new MinhasConsultasEstudanteGUI(usuarioLogado, new services.GerenciadorUsuarios()).setVisible(true);
        }
    }
}