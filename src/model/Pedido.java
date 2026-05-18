
package model;

import enums.StatusPedido;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
     private int id;
     private Cliente cliente;
     private StatusPedido status;
     private LocalDateTime dataCriacao;
     private List<ItemPedido> itens;

     // Construtor para leitura do Banco
     public Pedido(int id, Cliente cliente, StatusPedido status,
          LocalDateTime dataCriacao, List<ItemPedido> itens
     ) {
          this.id = id;
          this.cliente = cliente;
          this.status = status;
          this.dataCriacao = dataCriacao;
          this.itens = itens != null ? itens : new ArrayList<>();
     }

     // Usado ao criar novo pedido
     public Pedido(Cliente cliente){
          this(0, cliente, StatusPedido.ABERTO, LocalDateTime.now() new ArrayList<>());
     }

     public int getId(){
          return id;
     }

     public Cliente getCliente(){
          return cliente;
     }

      public StatusPedido getStatus() {
        return status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public double getTotal() {
        return itens.stream().mapToDouble(ItemPedido::getSubtotal).sum();
    }

    @Override
    public String toString() {
        return String.format("[%d] Cliente: %s | Status: %s | Total: R$ %.2f | %s",
                id, cliente.getNome(), status, getTotal(), dataCriacao);
    }
}
