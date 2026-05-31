package DAO;

import enums.*;
import infra.*;
import model.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    public void salvar(Produto produto) throws SQLException {
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

    public List<Produto> buscarTodos() throws SQLException {
        String sql = "select id_produto, nome, preco, estoque, categoria from produtos order by nome";
        List<Produto> lista = new ArrayList<>();

        try (Connection conn = Conexao.conectar();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Produto(
                        rs.getInt("id_produto"),
                        rs.getString("nome"),
                        rs.getDouble("preco"),
                        rs.getInt("estoque"),
                        CategoriaProduto.valueOf(rs.getString("categoria"))));
            }

        }

        return lista;
    }

    public Produto buscarPorId(int id_produto) throws SQLException {

        String sql = "select id_produto, nome, preco, estoque, categoria from produtos where id_produto = ?";

        try (Connection conn = Conexao.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id_produto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Produto(
                            rs.getInt("id_produto"),
                            rs.getString("nome"),
                            rs.getDouble("preco"),
                            rs.getInt("estoque"),
                            CategoriaProduto.valueOf(rs.getString("categoria")));
                }
            }

        }
        return null;
    }

    public List<Produto> buscarPorNome(String nome) throws SQLException {
        String sql = "select id_produto, nome, preco, estoque, categoria from produtos where nome like  ?";
        List<Produto> lista = new ArrayList<>();

        try (Connection conn = Conexao.conectar();
                PreparedStatement ps = conn.prepareStatement(sql);
                ) {

            ps.setString(1, "%" + nome + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new Produto(
                        rs.getInt("id_produto"),
                        rs.getString("nome"),
                        rs.getDouble("preco"),
                        rs.getInt("estoque"),
                        CategoriaProduto.valueOf(rs.getString("categoria"))));
            }

        }
        return lista;
    }
    public List<Produto> buscarPorCategoria(String nome) throws SQLException{
        
    }
}
