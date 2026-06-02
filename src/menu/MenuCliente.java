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
        do {
            System.out.println("\n=== MENU CLIENTE ===");
            System.out.println("1 - Cadastrar cliente");
            System.out.println("2 - Listar todos");
            System.out.println("3 - Buscar por ID");
            System.out.println("4 - Atualizar cliente");
            System.out.println("5 - Deletar cliente");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");

            opcao = lerInteiro();

            switch (opcao) {
                case 1 -> cadastrar();
                case 2 -> listar();
                case 3 -> buscar();
                case 4 -> atualizar();
                case 5 -> deletar();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção Invalida");
            }
        } while (opcao != 0);
    }

    private void cadastrar() {
        System.out.println("Nome: ");
        String nome = 
    }
}
