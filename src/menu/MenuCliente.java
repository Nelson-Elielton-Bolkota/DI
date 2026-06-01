package menu;

import DAO.ClienteDAO;
import model.Cliente;
import java.util.Scanner;

public class MenuCliente {
    private Scanner scanner;
    private ClienteDAO clienteDAO;

    public MenuCliente(Scanner scanner) {
        this.scanner = scanner;
        this.clienteDAO = new ClienteDAO();
    }
    
    public void exibir(){
        int opcao;
    }
}
