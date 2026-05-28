package menu;

import java.util.Scanner;

public class MenuPrincipal {
    
    private Scanner scanner;

    public MenuPrincipal(Scanner scanner) {
		this.scanner = scanner;
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
