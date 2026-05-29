package menu;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import DAO.ProdutoDAO;
import enums.CategoriaProduto;
import model.Produto;

public class MenuProduto {
    private Scanner scanner;
    private ProdutoDAO produtoDAO = new ProdutoDAO();

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
            System.out.println("3. Listar por nome");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");

            opcao = lerInt();

            switch (opcao) {
                case 1 -> salvar();
                case 2 -> buscarTodos();
                case 3 -> buscarPorNome();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private void salvar() {
        System.out.println("Nome:");
        // String nome = MenuPrincipal.removerAcentos(scanner.nextLine().trim());
        String nome = scanner.nextLine().trim();

        Double preco = lerPreco();
        if (preco < 0) {
            System.out.println("Erro: O preço não pode ser negativo");
            return;
        }

        System.out.println("Estoque:");
        int estoque = lerInt();
        if (estoque < 0) {
            System.out.println("Erro: O estoque não pode ser negativo");
            return;
        }

        CategoriaProduto categoria = lerCategoria();
        if (categoria == null)
            return;

        try {
            Produto produto = new Produto(nome, preco, estoque, categoria);
            produtoDAO.salvar(produto);
            System.out.println("Produto cadastrado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao salvar produto.");
        }
    }

    private void buscarTodos() {
        try {
            List<Produto> produtos = produtoDAO.buscarTodos();
            if (produtos.isEmpty()) {
                System.out.println("nenhum produto foi encontrado");
                return;
            }
            System.out.println("PRODUTOS");
            produtos.forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println("ERRO: falha ao listar produtos " + e.getMessage());
        }
    }

    private void buscarPorNome() {
        System.out.println("Digite o nome do produto:");
        String nome = MenuPrincipal.removerAcentos(scanner.nextLine().trim());
        try{
            List<Produto>produtos =  produtoDAO.buscarPorNome(nome);
        if (produtos.isEmpty()) {
            System.out.println("nenhum produto foi encontrado");
            return;
        }
        System.out.println("PRODUTOS");
        produtos.forEach(System.out::println);
        } catch(SQLException e){
            System.out.println("ERRO: falha ao listar produtos " + e.getMessage());
        }
    }

    // metodos q eu fiz pra ajudar aqui
    private CategoriaProduto lerCategoria() {
        System.out.println("Categoria:");
        CategoriaProduto[] categorias = CategoriaProduto.values();

        for (int i = 0; i < categorias.length; i++) {
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
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
