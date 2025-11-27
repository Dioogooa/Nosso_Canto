package entities;

import java.time.LocalDateTime;

public class Mensagem {
    private String id;
    private Usuario remetente;
    private Usuario destinatario;
    private String texto;
    private LocalDateTime dataHora;
    private boolean visto;

    public Mensagem(String id, Usuario remetente, Usuario destinatario, String texto) {
        this.id = id;
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.texto = texto;
        this.dataHora = LocalDateTime.now();
        this.visto = false;
    }

    public void marcarVisto() {
        this.visto = true;
    }

    public void exibirTexto() {
        System.out.println("["+dataHora+"]"+ remetente.getNome() + ": "+ texto);
    }

    //GETTERS
    public String getId() {return id;}
    public Usuario getRemetente() {return remetente;}
    public Usuario getDestinatario() {return destinatario;}
    public String getTexto() {return texto;}
    public LocalDateTime getDataHora() {return dataHora;}
    public boolean isVisto() {return visto;}

}

