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
            ps.setInt(1, ItemPedido.getQuantidade());
            ps.setDouble(2, ItemPedido.getPrecoUnitario());
            ps.executeUpdate();
        }

    }
}
