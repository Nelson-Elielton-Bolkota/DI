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

