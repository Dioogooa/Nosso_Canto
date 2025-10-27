package entites;

import java.time.LocalDate;

public abstract class Usuario { //classe abstrata para dados gerais
    protected String id;
    protected String nome;
    protected String email;
    protected String senha;
    protected String telefone;
    protected LocalDate dataNascimento;
    protected String cpf;
    protected String endereco;

    public Usuario(String id, String nome, String email, String senha, String telefone,
                   LocalDate dataNascimento, String cpf, String endereco) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.endereco = endereco;
    }

    public abstract void exibirPerfil();
    public abstract String getTipoUsuario();

    //Getters and Setters
    public String getId() { return id;}
    public void setId(String id) {this.id = id;}
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome;}
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) {this.dataNascimento = dataNascimento;}
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf;}
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
}