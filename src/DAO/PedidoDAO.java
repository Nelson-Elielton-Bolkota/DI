package DAO;
import enums.*;
import infra.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.Cliente;
import model.ItemPedido;
import model.Pedido;
import model.Produto;


public class PedidoDAO {
   public void salvar(Pedido pedido) throws SQLException, EstoqueInsuficienteException {
    Connection conn = null;
    try {
        conn = Conexao.conectar();
        conn.setAutoCommit(false);

        for (ItemPedido item : pedido.getItens()) {
            String sqlEstoque = "SELECT estoque FROM produtos WHERE id_produto = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlEstoque)) {
                ps.setInt(1, item.getProduto().getId_produto());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int estoqueAtual = rs.getInt("estoque");
                    if (item.getQuantidade() > estoqueAtual) {
                        throw new EstoqueInsuficienteException(
                            item.getProduto().getNome(),
                            item.getQuantidade(),
                               estoqueAtual
                        );
                    }
                }
            }
        }

        String sqlPedido = "INSERT INTO pedidos(id_cliente, dataCriacao, statusPedido) VALUES (?, ?, ?)";
        int idPedidoGerado;
        try (PreparedStatement ps = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, pedido.getCliente().getId());
            ps.setTimestamp(2, Timestamp.valueOf(pedido.getDataCriacao()));
            ps.setString(3, pedido.getStatus().name());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (!rs.next()) throw new SQLException("Falha ao obter ID do pedido.");
            idPedidoGerado = rs.getInt(1);
        }

        String sqlItem = "INSERT INTO itens_pedido(id_pedido, id_produto, quantidade, precoUnitario) VALUES (?, ?, ?, ?)";
        String sqlDesconto = "UPDATE produtos SET estoque = estoque - ? WHERE id_produto = ?";

        for (ItemPedido item : pedido.getItens()) {
            try (PreparedStatement ps = conn.prepareStatement(sqlItem)) {
                ps.setInt(1, idPedidoGerado);
                ps.setInt(2, item.getProduto().getId_produto());
                ps.setInt(3, item.getQuantidade());
                ps.setDouble(4, item.getProduto().getPreco());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement(sqlDesconto)) {
                ps.setInt(1, item.getQuantidade());
                ps.setInt(2, item.getProduto().getId_produto());
                ps.executeUpdate();
            }
        }

        conn.commit();

    } catch (EstoqueInsuficienteException | SQLException e) {
        if (conn != null) conn.rollback();
        throw e;
    } finally {
        if (conn != null) conn.close();
    }
}

   public List<Pedido> buscarTodos() throws SQLException {
    String sql = "SELECT id_pedido, id_cliente, dataCriacao, statusPedido FROM pedidos ORDER BY id_pedido";
    List<Pedido> lista = new ArrayList<>();

    try (Connection conn = Conexao.conectar();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            int idCliente = rs.getInt("id_cliente");
            int idPedido = rs.getInt("id_pedido");

            // Busca cliente
            Cliente cliente = null;
            String sqlCliente = "SELECT id_cliente, nome, email FROM clientes WHERE id_cliente = ?";
            try (PreparedStatement psC = conn.prepareStatement(sqlCliente)) {
                psC.setInt(1, idCliente);
                ResultSet rsC = psC.executeQuery();
                if (rsC.next()) {
                    cliente = new Cliente(rsC.getInt("id_cliente"), rsC.getString("nome"), rsC.getString("email"));
                }
            }

            // Busca itens
            List<ItemPedido> itens = new ArrayList<>();
            String sqlItens = "SELECT id_item, id_produto, quantidade FROM itens_pedido WHERE id_pedido = ?";
            try (PreparedStatement psI = conn.prepareStatement(sqlItens)) {
                psI.setInt(1, idPedido);
                ResultSet rsI = psI.executeQuery();
                while (rsI.next()) {
                    int idProduto = rsI.getInt("id_produto");
                    Produto produto = null;
                    String sqlProduto = "SELECT id_produto, nome, preco, estoque FROM produtos WHERE id_produto = ?";
                    try (PreparedStatement psP = conn.prepareStatement(sqlProduto)) {
                        psP.setInt(1, idProduto);
                        ResultSet rsP = psP.executeQuery();
                        if (rsP.next()) {
                            produto = new Produto(
                                rsP.getInt("id_produto"), 
                                rsP.getString("nome"), rsP.getDouble
                                ("preco"), rsP.getInt
                                ("estoque"),
                                CategoriaProduto.valueOf(rsP.getString("categoria"))
                            );
                                
                        }
                    }
                    itens.add(new ItemPedido(rsI.getInt("id_item"), produto, rsI.getInt("quantidade")));
                }
            }

            lista.add(new Pedido(
                idPedido,
                cliente,
                StatusPedido.valueOf(rs.getString("statusPedido")),
                rs.getTimestamp("dataCriacao").toLocalDateTime(),
                itens
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