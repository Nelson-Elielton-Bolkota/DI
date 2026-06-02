import menu.MenuPrincipal;

import java.nio.charset.StandardCharsets;
import java.util.Scanner; 

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, "CP850");
        MenuPrincipal menu = new MenuPrincipal(scanner);
        menu.iniciar();
        scanner.close();
    }
}
