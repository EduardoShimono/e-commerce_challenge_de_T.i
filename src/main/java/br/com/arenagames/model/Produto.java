package br.com.arenagames.model;

import java.math.BigDecimal;

public class Produto {

    private int id;
    private int categoriaId;
    private String categoriaNome;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private int estoque;
    private String imagem;
    private boolean ativo;

    public boolean isDisponivel() {
        return ativo && estoque > 0;
    }

    public boolean isImagemExterna() {
        return imagem != null && (imagem.startsWith("http://") || imagem.startsWith("https://"));
    }

    public String getImagemOuPadrao() {
        return imagem == null || imagem.isBlank() ? "img/sem-imagem.svg" : imagem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getCategoriaNome() {
        return categoriaNome;
    }

    public void setCategoriaNome(String categoriaNome) {
        this.categoriaNome = categoriaNome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
