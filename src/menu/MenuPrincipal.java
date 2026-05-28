package menu;

import java.util.Scanner;

public class MenuPrincipal {
    
    private Scanner scanner;

    public MenuPrincipal(Scanner scanner) {
		this.scanner = scanner;
	}

    public void iniciar() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("");
            System.out.println("   SISTEMA DE GESTÃO PEDIDOS");
            System.out.println("-------------------------------");
            System.out.println("  1. Clientes                 ");
            System.out.println("  2. Produtos                 ");
            System.out.println("  3. Pedidos                  ");
            System.out.println("  0. Sair                     ");
            System.out.print("Opção: ");

            opcao = lerInt();

            switch (opcao) {
                case 1 -> new MenuCliente(scanner).exibir();
                case 2 -> new MenuProduto(scanner).exibir();
                case 3 -> new MenuPedido(scanner).exibir();
                case 0 -> System.out.println("Encerrando sistema. Até logo!");
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
