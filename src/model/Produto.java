package model;

import enums.CategoriaProduto;

public class Produto {
    private int id_produto;
    private String nome;
    private double preco;
    private int estoque;
    private CategoriaProduto categoria;

    // criação de construtor que le do banco de dados
    public Produto(int id_produto, String nome, double preco, int estoque, CategoriaProduto categoria) {
        if (preco <= 0)
            throw new IllegalArgumentException("Preço deve ser positivo.");
        if (estoque < 0)
            throw new IllegalArgumentException("Estoque não pode ser negativo.");
        this.id_produto = id_produto;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
        this.categoria = categoria;
    }

    // construtor para criar produtos
    public Produto(String nome, double preco, int estoque, CategoriaProduto categoria) {
        this(0, nome, preco, estoque, categoria);
    }
    public int getId_produto() {
        return id_produto;
    }
    public String getNome(){
        return nome;
    }
    public double getPreco(){
        return preco;
    }
    public int getEstoque() {
        return estoque;
    }
    public CategoriaProduto getCategoria() {
        return categoria;
    }
    @Override
    public String toString() {
        return String.format("[%d] %s | R$ %.2f | Estoque: %d | %s",
                id_produto, nome, preco, estoque, categoria);
    }
    
}
