package entites;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class Senior extends Usuario {
    private List<String> condicaoSaude;
    private List<String> medicamentos;
    private String contatoEmergencia;
    private boolean temAcompanhante;

    public Senior(String id, String nome, String email, String senha,
                  String telefone, LocalDate dataNascimento,String cpf, String endereco,
                  String contatoEmergencia, boolean temAcompanhante) {
        super(id, nome, email, senha, telefone, dataNascimento, cpf, endereco);
        this.condicaoSaude = new ArrayList<>();
        this.medicamentos = new ArrayList<>();
        this.contatoEmergencia = contatoEmergencia;
        this.temAcompanhante = temAcompanhante;
    }

    @Override //Override (sobscrição) - Pega um metodo da superclasse e modifica ele.
    public void exibirPerfil() {
        System.out.println("\n=== Perfil do Sênior ===");
        System.out.println("Nome: " + nome + " id:" +id);
        System.out.println("Idade "+ calcularIdade() + " anos");
        System.out.println("Condições de Saúde: " + condicaoSaude);
        System.out.println("Precisa de companhia: "+ (temAcompanhante ? "Sim" : "Não"));

    }

    @Override
    public void exibirPerfilReduzido() {
        System.out.println("\n=== Perfil do Sênior ===");
        System.out.println("Nome: " + nome + " id:" +id);
        System.out.println("Idade "+ calcularIdade() + " anos");
    }

    @Override
    public String getTipoUsuario() {
        return "Senior";
    }

    private int calcularIdade() {
        return LocalDate.now().getYear() - dataNascimento.getYear();
    }

    public void addCondicaoSaude(String condicao) {
        condicaoSaude.add(condicao);
    }

    public void addMedicamento(String medicamento) {
        medicamentos.add(medicamento);
    }

    //GETTERS AND SETTERS
    public List<String> getCondicaoSaude() { return condicaoSaude; }
    public List<String> getMedicamentos() { return medicamentos; }
    public String getContatoEmergencia() { return contatoEmergencia; }
    public boolean isTemAcompanhante() { return temAcompanhante; }

}
