package DAO;
import infra.*;
import model.Pedido;
import enums.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class PedidoDAO {
    public void salvar(Pedido pedido) throws SQLException{
        String sql = "insert into pedidos(dataCriacao, statusPedido) values (?,?)";

        try(Connection conn = Conexao.conectar();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setLocalDateTime(1, getDataCriacao());
            ps.setStatusPedido(2, getStatus().name());
            ps.executeUpdate();
        }
    }

    public List<Pedido> buscarTodos() throws SQLException{
        String sql = "select id_pedido, id_cliente, dataCriacao, statusPedido from pedidos order by id_cliente";
        List<Pedido> lista = new ArrayList<>();

        try(Connection conn = Conexao.conectar();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()){
            
            while(rs.next()){
                lista.add(new Pedido(
                    rs.getInt("id"),
                    rs.getString("id_cliente"),
                    rs.getString("dataCriacao"),
                    StatusPedido.valueOf(rs.getString("statusPedido"))));
            }

        }

        return lista;
    }
}
