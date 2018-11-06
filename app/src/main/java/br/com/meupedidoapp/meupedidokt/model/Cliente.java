package br.com.meupedidoapp.meupedidokt.model;

public final class Cliente extends Usuario{
    private String sobrenome;
    private String genero;
    private long dataNascimento;
    //private Endereco endereco[];

    public Cliente(){}

    public Cliente(String uid, String nome, String email, String sobrenome, String genero, long dataNascimento, String telefone1, String telefone2) {
        super(uid, nome, email, telefone1, telefone2);
        this.sobrenome = sobrenome;
        this.genero = genero;
        this.dataNascimento = dataNascimento;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public long getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(long dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}