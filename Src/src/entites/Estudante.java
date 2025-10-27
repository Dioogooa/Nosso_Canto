package entites;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Estudante extends Usuario {
    private String instuicao;
    private String curso;
    private int semestre;
    private List<String> especialidades;
    private boolean disponivel;

    public Estudante(String id, String nome, String email, String senha,
                     String telefone, LocalDate dataNascimento,String cpf, String endereco,
                     String instuicao, String curso, int semestre, boolean disponivel) {
        super(id, nome, email, senha, telefone, dataNascimento, cpf, endereco);
        this.instuicao = instuicao;
        this.curso = curso;
        this.semestre = semestre;
        this.especialidades = new ArrayList<>();
        this.disponivel = disponivel;
    }

    @Override
    public void exibirPerfil() {
        System.out.println("=== Perfil do Estudante ===");
        System.out.println("Nome: " + nome);
        System.out.println("Curso: " + curso);
        System.out.println("Instituição: " + instuicao);
        System.out.println("Semestre: " + semestre);
        System.out.println("Especialidades: " + especialidades);
        System.out.println("Disponivel: " + (disponivel ? "Sim" : "Nao"));
    }

    @Override
    public String getTipoUsuario() {
        return "Estudante";
    }

    public void adcEspecialidade(String especialidade) {
        especialidades.add(especialidade); //adiciona a list Especialidades a especilidade colocada no parametro
    }

    //GETTERS AND SETTERS
    public String getInstuicao() { return instuicao; }
    public void setInstuicao(String instuicao) { this.instuicao = instuicao; }
    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }
    public int getSemestre() { return semestre; }
    public void setSemestre(int semestre) { this.semestre = semestre; }
    public List<String> getEspecialidades() { return especialidades; }
    public void setEspecialidades(List<String> especialidades) { this.especialidades = especialidades; }
    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }

}
