package menu;

import java.util.Scanner;

import DAO.ProdutoDAO;
import enums.CategoriaProduto;


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
                case 1 -> salvar();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
        }
    }
    
    private void salvar() {
        System.out.println("Nome:");
        String nome = scanner.nextLine().trim();

        Double preco = lerPreco();

        System.out.println("Estoque:");
        int estoque = lerInt();

        CategoriaProduto categoria = lerCategoria();

        
    }

    private CategoriaProduto lerCategoria() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'lerCategoria'");
    }
    private Double lerPreco() {
        System.out.println("Preço:");
        try {
            return Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            System.out.println("Preço Inválido");
            return -1.0;
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
