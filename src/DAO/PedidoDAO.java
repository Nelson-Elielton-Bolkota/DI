package DAO;
import enums.*;
import infra.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.Cliente;
import model.Pedido;


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
}