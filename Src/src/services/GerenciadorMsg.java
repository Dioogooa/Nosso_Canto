package services;

import entites.Mensagem;
import entites.Usuario;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GerenciadorMsg {
    private List<Mensagem> mensagens;

    public GerenciadorMsg() {
        this.mensagens = new ArrayList<>();
    }

    public void enviarMensagem(String id, Usuario remetente, Usuario destinatario, String texto) {
        Mensagem mensagem = new Mensagem(id, remetente, destinatario, texto);
        this.mensagens.add(mensagem);
    }

    public List<Mensagem> getChat(String Usario1Id, String Usario2Id) {
        return mensagens.stream()
                .filter(m -> (m.getRemetente().getId().equals(Usario1Id) &&
                        m.getDestinatario().getId().equals(Usario2Id)) ||
                        (m.getRemetente().getId().equals(Usario2Id) &&
                        m.getDestinatario().getId().equals(Usario1Id)))
                .sorted((m1, m2) -> m1.getDataHora().compareTo(m2.getDataHora()))
                .collect(Collectors.toList());
    }

    public List<Mensagem> getMensagensNLidas(String usarioId) {
        return mensagens.stream()
                .filter(m -> m.getDestinatario().getId().equals(usarioId))
                .collect(Collectors.toList());
    }
}
