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
        int opcao = -1;
            while(opcao != 0){
                System.out.println("=== MENU PEDIDOS ===");
                System.out.println("1. Criar Pedido");
                System.out.println("2. Listar Pedidos");
                System.out.println("3. Relatório: total por cliente");
                System.out.println("4. Relatório: produtos mais vendidos");
                System.out.println("0. Voltar");
                System.out.print("Opção: ");

                opcao = lerInt();

                switch(opcao){
                    case 1 -> criarPedido();
                    case 2 -> listarPedidos();
                    case 3 -> relatorioClientes();
                    case 4 -> relatorioProdutos();
                    case 0 -> System.out.println("Voltando...");
                    default -> System.out.println("Opção Invalida");
                }
            }
    }

            private void criarPedido(){
                // Seleciona o Cliente

                List<Cliente> clientes;
                try{
                    clientes = clienteDAO.buscarTodos();
                } catch(SQLException e){
                    System.out.println("Erro ao carregar os clientes!!" + e.getMessage());
                    return;
                }

                if(clientes.isEmpty()){
                    System.out.println("Nenhum cliente cadastrado. Cadastre um cliente primeiro!!");
                    return;
                }

                System.out.println("Clientes Disponiveis: ");
                clientes.forEach(System.out::println);
                System.out.print("ID do cliente: ");
                int idCliente = lerInt();

                Cliente clienteSelecionado = clientes.stream()
                        .filter(c -> getId() == idCliente)
                        .findFirst()
                        .orElse(null);

                
                if(clienteSelecionado == null){
                    System.out.println("Cliente não encontrado!! ");
                    return;
                }
            }
}
