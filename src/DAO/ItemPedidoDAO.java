package DAO;

import infra.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.ItemPedido;
import model.Pedido;
import model.Produto;

public class ItemPedidoDAO {
    public void salvar(ItemPedido item_pedido) throws SQLException{
        String sql = "insert into itempedido(quantidade, preco_Unitario) values(?,?)";

        try(Connection conn = Conexao.conectar();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, item_pedido.getQuantidade());
            ps.setDouble(2, item_pedido.getPrecoUnitario());
            ps.executeUpdate();
        }
    }

    public List<ItemPedido> buscarTodos() throws SQLException {
    String sql = "select id, id_pedido, id_produto, quantidade, preco_Unitario from itempedido order by id";
    List<ItemPedido> lista = new ArrayList<>();

    ProdutoDAO produtoDAO = new ProdutoDAO();

    try (Connection conn = Conexao.conectar();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Produto produto = produtoDAO.buscarPorId(rs.getInt("id_produto")); // Faz a busca pelo id do produto na Classe ProdutoDAO

            lista.add(new ItemPedido(
                rs.getInt("id"),
                rs.getInt("id_pedido"),
                produto,
                rs.getInt("quantidade"),
                rs.getDouble("preco_Unitario")
            ));
        }
    }

    return lista;
}
}