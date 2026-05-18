package model;

import enums.CategoriaProduto;

public class Produto {
    private int id;
    private String nome;
    private double preco;
    private int estoque;
    private CategoriaProduto categoria;

    //criação de construtor que le do banco de dados
    public Produto(int id, String nome, double preco, int estoque, CategoriaProduto categoria){
        if (preco <= 0)
            throw new IllegalArgumentException("Preço deve ser positivo.");
        if (estoque < 0)
            throw new IllegalArgumentException("Estoque não pode ser negativo.");
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
        this.categoria = categoria;
    }
}
