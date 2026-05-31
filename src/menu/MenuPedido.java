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
                        .filter(c -> c.getId() == idCliente)
                        .findFirst()
                        .orElse(null);

                
                if(clienteSelecionado == null){
                    System.out.println("Cliente não encontrado!! ");
                    return;
                }
    

            // Montagem de Pedidos

            Pedido pedido = new Pedido(clienteSelecionado);

            List<Produto> produtos;
            try {
                produtos = produtoDAO.buscarTodos();
            } catch(SQLException e){
                System.out.println("Erro ao carregar os produtos!! " + e.getMessage());
                return;
            }

            if(produtos.isEmpty()){
                System.out.println("Nenhum produto cadastrado!!");
                return;
            }

            boolean adicionado = true;

            while(adicionado){
                System.out.println("Produtos Disponíveis: ");
                produtos.forEach(System.out::println);
                System.out.print("ID do produto(0 para finalizar): ");
                int idProduto = lerInt();

                
            if(idProduto == 0){
                adicionado = false;
                continue;
            }

                Produto produtoSelecionado = produtos.stream()
                    .filter(p -> p.getId_produto() == idProduto)
                    .findFirst()
                    .orElse(null);

            
                    if(produtoSelecionado == null){
                        System.out.println("Produto não encontrado!!");
                        continue;
                    }

                System.out.println("Quantidade: ");
                int quantidade = lerInt();

                if(quantidade <= 0){
                    System.out.println("Quantidade inválida!!");
                    continue;
                }

                pedido.getItens().add(new ItemPedido(0, produtoSelecionado, quantidade));
                System.out.println("Item adicionado: " + produtoSelecionado.getNome() + " x" + quantidade);
            }

            if(pedido.getItens().isEmpty()) {
                System.out.println("Pedido cancelado --- nenhum item adicionado!!!");
                return;
            }

            // Salva o pedido com a transação

            try{
                pedidoDAO.salvar(pedido);
                System.out.println("Pedido criado com sucesso! Status: FILA(Será processado em breve)");
            } catch(EstoqueInsuficienteException e) {
                System.out.println("Pedido não criado!! " + e.getMessage());
            } catch(SQLException e){
                System.out.println("Erro ao salvar o pedido!!" + e.getMessage());
            }
    }

        private void listarPedidos(){
            try{
                List<Pedido> pedidos = pedidoDAO.buscarTodos();
                if(pedidos.isEmpty()){
                    System.out.println("Nenhum pedido encontrado!!");
                    return;
                }
                System.out.println(" === PEDIDOS === ");
                pedidos.forEach(System.out::println);
            } catch(SQLException e){
                System.out.println("Erro ao listar pedidos!! " + e.getMessage());
            }
        }
        
    
        private void relatorioClientes(){
            try{
                pedidoDAO.relatorioTotalPorCliente();
            } catch(SQLException e){
                System.out.println("Erro ao gerar relatório:" + e.getMessage());
            }
        }

        private void relatorioProdutos() {
        try {
            pedidoDAO.relatorioProdutosMaisVendidos();
        } catch (SQLException e) {
            System.out.println("Erro ao gerar relatório: " + e.getMessage());
        }
    }

    private int lerInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}