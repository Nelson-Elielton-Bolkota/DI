package DAO;

import infra.*;
import model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    public void salvar(Cliente cliente) throws SQLException {
        String sql = "insert into cliente(nome, email) values (?, ?) ";
        
        try (Connection conn = Conexao.conectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setString(1, cliente.getNome());
        ps.setString(2, cliente.getEmail());
        ps.executeUpdate();
        }
    }

public List<Cliente> buscarTodos() throws SQLException {
    String sql = "select id, nome, email from cliente order by nome";
    List<Cliente> lista = new ArrayList<>();

    try(Connection conn = Conexao.conectar();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Cliente(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("Email")
                ));
            }
         }
         return lista;
}
}
