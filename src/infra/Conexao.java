package infra;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

 public class Conexao {
private static final String URL =
"jdbc:mysql://localhost:3306/desafio_integrador";
 private static  String USER = "root";
 private static String PASS = " ";

public static Connection conectar() throws SQLException {
 return DriverManager.getConnection(URL, USER, PASS);
}
}
