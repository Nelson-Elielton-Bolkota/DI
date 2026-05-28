package DAO;

import enums.*;
import infra.*;
import model.Produto;
import java.sql.*;


public class ProdutoDAO {
    public void salvar(Produto produto) throws SQLException{
        String sql = "insert into produtos(nome, preco, estoque, categoria) values (?,?,?,?)";
    
    try (Connection conn = Conexao.conectar();
        PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, produto.getNome());
            ps.setDouble(2, produto.getPreco());
            ps.setInt(3, produto.getEstoque());
            ps.setString(4, produto.getCategoria().name());
            ps.executeUpdate();
        
    } 
    }
}
