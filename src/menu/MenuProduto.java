package menu;

import java.sql.SQLException;
import java.util.Scanner;

import DAO.ProdutoDAO;
import enums.CategoriaProduto;
import model.Produto;


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
                case 2 -> buscarTodos();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
        }
    }
    
    
    private void salvar() {
        System.out.println("Nome:");
        String nome = scanner.nextLine().trim();

        Double preco = lerPreco();
        if (preco<0) return;

        System.out.println("Estoque:");
        int estoque = lerInt();

        CategoriaProduto categoria = lerCategoria();
        if (categoria == null) return;

        try {
            Produto produto = new Produto(nome, estoque, estoque, categoria);
            produtoDAO.salvar(produto);
            System.out.println("Produto cadastrado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao salvar produto.");
        }
    }

    private Object buscarTodos() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarTodos'");
    }


    //metodos q eu fiz pra ajudar aqui
    private CategoriaProduto lerCategoria() {
        System.out.println("Categoria:");
        CategoriaProduto[] categorias = CategoriaProduto.values();
        
        for(int i = 0; i< categorias.length; i++){
            System.out.println((i + 1) + ". " + categorias[i]);
        }
        System.out.println("Opção:");
        int opcao = lerInt();

        if (opcao < 1 || opcao > categorias.length) {
            System.out.println("Categoria inválida");
            return null;
        }
        return categorias[opcao - 1];
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
