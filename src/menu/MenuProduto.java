package menu;

import java.util.Scanner;

import DAO.ProdutoDAO;


public class MenuProduto {
    private Scanner scanner;
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    public MenuProduto(Scanner scanner) {
        this.scanner = scanner;
    }
    public void exibir() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("        MENU PRODUTOS");
            System.out.println("----------------------------");
            System.out.println("1. Cadastrar produto");
            System.out.println("2. Listar todos os produtos");
            System.out.println("3. Listar por categoria");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");

            opcao = lerInt();

            switch (opcao) {
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
        }
    }
    
    private int lerInt() {
            try{
                return Integer.parseInt(scanner.nextLine().trim());
            }
            catch(NumberFormatException e){
                return -1;
            }
    }
}
