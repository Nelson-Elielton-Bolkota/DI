import menu.MenuPrincipal;
import infra.ProcessadorThreads;
import java.nio.charset.StandardCharsets;
import java.util.Scanner; 

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, "CP850");

        ProcessadorThreads processador = new ProcessadorThreads();

        Thread threadProcessador = new Thread(processador);
        threadProcessador.start();

        MenuPrincipal menu = new MenuPrincipal(scanner);
        menu.iniciar();

        System.out.println("Encerrando serviços em segundo plano...");
        processador.parar();

        scanner.close();
        System.out.println("Sistema encerrado com sucesso.");
    }
}
