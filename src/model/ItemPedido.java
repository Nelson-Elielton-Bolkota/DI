package model;
public class ItemPedido {
    private int id;
    private int idPedido;
    private Produto produto;
    private int quantidade;
    private double precoUnitario;

    // Construtor para leitura do Banco

    public ItemPedido(int id, int idPedido, Produto produto, int quantidade, double precoUnitario){
        this.id = id;
        this.idPedido = idPedido;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    // Ao criar um pedido(Sem o id)
    public ItemPedido(int idPedido, Produto produto, int quantidade){
        this(0, idPedido, produto, quantidade, produto.getPreco());
    }

     public int getId() {
        return id;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public double getSubtotal() {
        return precoUnitario * quantidade;
    }

    @Override

    public String toString(){
       return String.format("  → %s x%d @ R$ %.2f = R$ %.2f",
                produto.getNome(), quantidade, precoUnitario, getSubtotal());
}
}