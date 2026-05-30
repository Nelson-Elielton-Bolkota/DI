package menu;

import DAO.PedidoDAO;
import DAO.ProdutoDAO;
import DAO.ClienteDAO;
import infra.EstoqueInsuficienteException;

import model.Cliente;
import model.Produto;
import model.ItemPedido;
import model.Pedido;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MenuPedido {
    private Scanner scanner;
    private ProdutoDAO produtoDAO = new ProdutoDAO();
    private ClienteDAO clienteDAO = new ClienteDAO();
    private PedidoDAO pedidoDAO = new PedidoDAO();

    public MenuPedido(Scanner scanner) {
        this.scanner = scanner;
    }
    
    public void exibir(){
        
    }
}
