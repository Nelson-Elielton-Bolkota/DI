package DAO;
import enums.*;
import infra.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.Cliente;
import model.Pedido;
import model.Produto;


public class PedidoDAO {
    public void salvar(Pedido pedido) throws SQLException{
        String sql = "insert into pedidos(dataCriacao, statusPedido) values (?,?)";

        try(Connection conn = Conexao.conectar();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setTimestamp(1, Timestamp.valueOf(pedido.getDataCriacao()));
            ps.setString(2, pedido.getStatus().name());
            ps.executeUpdate();
        }
    }


    public List<Pedido> buscarTodos() throws SQLException {
        String sql = "select id_pedido, id_cliente, dataCriacao, statusPedido from pedidos order by id_cliente";
        List<Pedido> lista = new ArrayList<>();

        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                    rs.getInt("id_cliente"),
                    "",
                    ""
                );

                LocalDateTime dataCriacao = rs.getTimestamp("dataCriacao").toLocalDateTime();

                lista.add(new Pedido(
                    rs.getInt("id_pedido"),
                    cliente,
                    StatusPedido.valueOf(rs.getString("statusPedido")),
                    dataCriacao,
                    null
                ));
            }
        }

        return lista;
    }

    public void relatorioTotalPorCliente() throws SQLException {
        String sqlPedidos = "SELECT id_pedido, id_cliente FROM pedidos";
        String sqlItens = "SELECT quantidade, precoUnitario FROM itens_pedido WHERE id_pedido = ?";

        System.out.println("=== RELATÓRIO: TOTAL POR CLIENTE ===");
        System.out.printf("%-25s %-15s %-15s%n", "Cliente", "Pedidos", "Valor Total");
        System.out.println("-".repeat(55));

        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sqlPedidos);
             ResultSet rs = ps.executeQuery()) {

            java.util.Map<Integer, double[]> totaisPorCliente = new java.util.LinkedHashMap<>();

            while (rs.next()) {
                int idPedido = rs.getInt("id_pedido");
                int idCliente = rs.getInt("id_cliente");

                double valorPedido = 0;
                try (PreparedStatement psItens = conn.prepareStatement(sqlItens)) {
                    psItens.setInt(1, idPedido);
                    ResultSet rsItens = psItens.executeQuery();
                    while (rsItens.next()) {
                        valorPedido += rsItens.getInt("quantidade") * rsItens.getDouble("precoUnitario");
                    }
                }

                totaisPorCliente.putIfAbsent(idCliente, new double[]{0, 0});
                totaisPorCliente.get(idCliente)[0]++;
                totaisPorCliente.get(idCliente)[1] += valorPedido;
            }

            for (java.util.Map.Entry<Integer, double[]> entry : totaisPorCliente.entrySet()) {
                String sqlCliente = "SELECT nome FROM clientes WHERE id_cliente = ?";
                try (PreparedStatement psCliente = conn.prepareStatement(sqlCliente)) {
                    psCliente.setInt(1, entry.getKey());
                    ResultSet rsCliente = psCliente.executeQuery();
                    String nome = rsCliente.next() ? rsCliente.getString("nome") : "Desconhecido";
                    System.out.printf("%-25s %-15.0f R$ %,.2f%n",
                        nome,
                        entry.getValue()[0],
                        entry.getValue()[1]
                    );
                }
            }
        }
    }

    public void relatorioProdutosMaisVendidos() throws SQLException {
        String sqlItens = "SELECT id_produto, quantidade FROM itens_pedido";

        System.out.println("=== RELATÓRIO: PRODUTOS MAIS VENDIDOS ===");
        System.out.printf("%-30s %-15s%n", "Produto", "Qtd Vendida");
        System.out.println("-".repeat(45));

        java.util.Map<Integer, Integer> vendasPorProduto = new java.util.HashMap<>();

        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sqlItens);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idProduto = rs.getInt("id_produto");
                int quantidade = rs.getInt("quantidade");
                vendasPorProduto.merge(idProduto, quantidade, Integer::sum);
            }

            for (java.util.Map.Entry<Integer, Integer> entry : vendasPorProduto.entrySet()) {
                String sqlProduto = "SELECT nome FROM produtos WHERE id_produto = ?";
                try (PreparedStatement psProduto = conn.prepareStatement(sqlProduto)) {
                    psProduto.setInt(1, entry.getKey());
                    ResultSet rsProduto = psProduto.executeQuery();
                    String nome = rsProduto.next() ? rsProduto.getString("nome") : "Desconhecido";
                    System.out.printf("%-30s %-15d%n", nome, entry.getValue());
                }
            }
        }
    }
}