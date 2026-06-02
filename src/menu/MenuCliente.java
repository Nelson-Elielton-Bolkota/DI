    package menu;

    import DAO.ClienteDAO;
    import model.Cliente;

    import java.sql.SQLException;
    import java.util.List;
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
                System.out.println("4 - Deletar cliente");
                System.out.println("0 - Voltar");
                System.out.print("Escolha uma opção: ");

                opcao = lerInteiro();

                switch (opcao) {
                    case 1 -> cadastrar();
                    case 2 -> listar();
                    case 3 -> buscar();
                    case 4 -> deletar();
                    case 0 -> System.out.println("Voltando...");
                    default -> System.out.println("Opção Invalida");
                }
            } while (opcao != 0);
        }

        private void cadastrar() {
            System.out.println("Nome: ");
            String nome = scanner.nextLine();
            System.out.println("Email= ");
            String email = scanner.nextLine();
        
        try {
                clienteDAO.salvar(new Cliente(nome, email));
                System.out.println("Cliente cadastrado com sucesso!");
            } catch (SQLException e) {
                System.out.println("Erro ao cadastrar: " + e.getMessage());
            }
        }

        private void listar() {
            try {
                List<Cliente> clientes = clienteDAO.buscarTodos();
                if (clientes.isEmpty()) {
                    System.out.println("Nenhum cliente cadastrado.");
                } else {
                    clientes.forEach(System.out::println);
                }
            } catch (SQLException e) {
                System.out.println("Erro ao listar: " + e.getMessage());
            }
        }


        private void buscar() {
            System.out.print("ID do cliente: ");
            int id = lerInteiro();
        
            try {
                Cliente cliente = clienteDAO.buscarPorId(id);
                if (cliente != null) {
                    System.out.println(cliente);
                } else {
                    System.out.println("Cliente não encontrado.");
                }
            } catch (SQLException e) {
                System.out.println("Erro ao buscar: " + e.getMessage());
            }
        }


        private void deletar() {
            System.out.print("ID do cliente a deletar: ");
            int id = lerInteiro();
    
            try {
                boolean removido = clienteDAO.deletar(id);
                if (removido) {
                    System.out.println("Cliente deletado com sucesso!");
                } else {
                    System.out.println("Cliente não encontrado.");
                }
            } catch (SQLException e) {
                System.out.println("Erro ao deletar: " + e.getMessage());
            }
        }
        
        private int lerInteiro() {
            while (true) {
                try {
                    return Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.print("Digite um número válido: ");
                }
            }
        }
    }
