package DAO;

import infra.*;
import model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    public void salvar(Cliente cliente) throws SQLException {
        String sql = "insert into clientes(nome, email) values (?, ?) ";
        
        try (Connection conn = Conexao.conectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setString(1, cliente.getNome());
        ps.setString(2, cliente.getEmail());
        ps.executeUpdate();
        
        }
    }
}
