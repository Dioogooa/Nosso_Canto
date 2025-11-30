package gui;

import entites.Consulta;
import entites.Mensagem;
import entites.Usuario;
import dao.mensagemDAO;
import services.GerenciadorMsg;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ChatGUI extends JFrame {
    private Usuario usuarioLogado;
    private Usuario outroUsuario;
    private Consulta consulta;
    private GerenciadorMsg gerenciadorMsg;
    private mensagemDAO mensagemDAO;

    private JTextArea chatArea;
    private JTextField mensagemField;
    private JButton enviarButton;
    private Timer timerAtualizacao;

    public ChatGUI(Usuario usuarioLogado, Usuario outroUsuario, Consulta consulta) {
        this.usuarioLogado = usuarioLogado;
        this.outroUsuario = outroUsuario;
        this.consulta = consulta;
        this.gerenciadorMsg = new GerenciadorMsg();
        this.mensagemDAO = new mensagemDAO();

        inicializarComponentes();
        configurarJanela();
        carregarMensagens();
        iniciarAtualizacaoAutomatica();
    }

    private void inicializarComponentes() {
        // Painel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);

        // Cabeçalho do chat
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 102, 204));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel tituloLabel = new JLabel("Chat - " + outroUsuario.getNome());
        tituloLabel.setFont(new Font("Calibri", Font.BOLD, 16));
        tituloLabel.setForeground(Color.WHITE);

        JLabel consultaLabel = new JLabel("Consulta: " +
                consulta.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        consultaLabel.setFont(new Font("Calibri", Font.PLAIN, 12));
        consultaLabel.setForeground(new Color(200, 200, 255));

        headerPanel.add(tituloLabel, BorderLayout.WEST);
        headerPanel.add(consultaLabel, BorderLayout.EAST);

        // Área do chat
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chatArea.setBackground(new Color(248, 248, 248));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);

        JScrollPane chatScroll = new JScrollPane(chatArea);
        chatScroll.setBorder(BorderFactory.createTitledBorder("Mensagens"));
        chatScroll.getVerticalScrollBar().setUnitIncrement(16);

        // Painel de entrada de mensagem
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        mensagemField = new JTextField();
        mensagemField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mensagemField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        enviarButton = new JButton("Enviar");
        enviarButton.setFont(new Font("Calibri", Font.BOLD, 14));
        enviarButton.setBackground(new Color(0, 102, 204));
        enviarButton.setForeground(Color.WHITE);
        enviarButton.setFocusPainted(false);
        enviarButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        enviarButton.setPreferredSize(new Dimension(80, 40));

        inputPanel.add(mensagemField, BorderLayout.CENTER);
        inputPanel.add(enviarButton, BorderLayout.EAST);

        // Botão voltar
        JButton voltarButton = new JButton("Voltar");
        voltarButton.setFont(new Font("Calibri", Font.PLAIN, 12));
        voltarButton.setBackground(new Color(240, 240, 240));
        voltarButton.setFocusPainted(false);
        voltarButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footerPanel.setBackground(Color.WHITE);
        footerPanel.add(voltarButton);

        // Adicionar componentes
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(chatScroll, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Configurar ações
        configurarAcoes(voltarButton);
    }

    private void configurarAcoes(JButton voltarButton) {
        // Enviar mensagem com botão
        enviarButton.addActionListener(e -> enviarMensagem());

        // Enviar mensagem com Enter
        mensagemField.addActionListener(e -> enviarMensagem());

        // Voltar
        voltarButton.addActionListener(e -> voltar());
    }

    private void carregarMensagens() {
        try {
            List<Mensagem> mensagens = mensagemDAO.buscarConversa(
                    usuarioLogado.getId(), outroUsuario.getId());

            chatArea.setText("");

            for (Mensagem mensagem : mensagens) {
                adicionarMensagemNaTela(mensagem);
            }

            chatArea.setCaretPosition(chatArea.getDocument().getLength());

        } catch (Exception e) {
            System.out.println("Erro ao carregar mensagens: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar mensagens: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarMensagemNaTela(Mensagem mensagem) {
        boolean isMinhaMensagem = mensagem.getRemetente().getId().equals(usuarioLogado.getId());

        String formatoMensagem = isMinhaMensagem ?
                "[%s] Eu: %s\n" :
                "[%s] %s: %s\n";

        String horario = mensagem.getDataHora().format(DateTimeFormatter.ofPattern("HH:mm"));
        String texto = String.format(formatoMensagem, horario,
                isMinhaMensagem ? "" : mensagem.getRemetente().getNome(),
                mensagem.getTexto());

        chatArea.append(texto);
    }

    private void enviarMensagem() {
        String texto = mensagemField.getText().trim();

        if (texto.isEmpty()) {
            return;
        }

        try {
            // Gerar ID único para a mensagem
            String mensagemId = "M" + System.currentTimeMillis();

            // Criar e salvar mensagem
            Mensagem mensagem = new Mensagem(mensagemId, usuarioLogado, outroUsuario, texto);
            mensagemDAO.salvar(mensagem);

            // Adicionar na tela
            adicionarMensagemNaTela(mensagem);

            // Limpar campo de texto
            mensagemField.setText("");

            // Rolagem automática
            chatArea.setCaretPosition(chatArea.getDocument().getLength());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao enviar mensagem: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void iniciarAtualizacaoAutomatica() {
        // Atualizar mensagens a cada 3 segundos
        timerAtualizacao = new Timer(3000, e -> {
            atualizarMensagens();
        });
        timerAtualizacao.start();
    }

    private void atualizarMensagens() {
        try {
            int posicaoAtual = chatArea.getCaretPosition();
            int linhaAtual = chatArea.getLineOfOffset(posicaoAtual);

            List<Mensagem> mensagens = mensagemDAO.buscarConversa(
                    usuarioLogado.getId(), outroUsuario.getId());

            if (mensagens.size() > chatArea.getLineCount()) {
                carregarMensagens();
                try {
                    int novaPosicao = chatArea.getLineStartOffset(Math.min(linhaAtual, chatArea.getLineCount() - 1));
                    chatArea.setCaretPosition(novaPosicao);
                } catch (Exception ex) {
                    chatArea.setCaretPosition(chatArea.getDocument().getLength());
                }
            }

        } catch (Exception e) {
            System.out.println("Erro na atualização automática: " + e.getMessage());
        }
    }

    private void voltar() {
        if (timerAtualizacao != null) {
            timerAtualizacao.stop();
        }

        this.dispose();

        // Volta para tela de usuario certo --
        if (usuarioLogado.getTipoUsuario().equals("Senior")) {
            new MinhasConsultasSeniorGUI(usuarioLogado, new services.GerenciadorUsuarios()).setVisible(true);
        } else {
            new MinhasConsultasEstudanteGUI(usuarioLogado, new services.GerenciadorUsuarios()).setVisible(true);
        }
    }

    private void configurarJanela() {
        setTitle("Nosso Canto - Chat");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    @Override
    public void dispose() { //Garantir que vai fechar a outra tela
        if (timerAtualizacao != null) {
            timerAtualizacao.stop();
        }
        super.dispose();
    }
}