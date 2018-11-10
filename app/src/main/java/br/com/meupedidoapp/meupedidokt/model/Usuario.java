package br.com.meupedidoapp.meupedidokt.model;

import com.google.firebase.Timestamp;

public abstract class Usuario {
    private String uid;
    private String nome;
    private String email;
    private boolean usuarioAtivo;
    private long dataHoraCadastro;
    private String telefone1;
    private String telefone2;

    public Usuario() {}

    public Usuario(String uid, String nome, String email, String telefone1, String telefone2) {
        this.uid = uid;
        this.nome = nome;
        this.email = email;
        this.telefone1 = telefone1;
        this.telefone2 = telefone2;

        this.usuarioAtivo = true;
        this.dataHoraCadastro = Timestamp.now().getSeconds(); }

    public String getUid() {
        return uid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getDataHoraCadastro() {
        return dataHoraCadastro;
    }

    public String getTelefone1() {
        return telefone1;
    }

    public void setTelefone1(String telefone1) {
        this.telefone1 = telefone1;
    }

    public String getTelefone2() {
        return telefone2;
    }

    public void setTelefone2(String telefone2) {
        this.telefone2 = telefone2;
    }
}
