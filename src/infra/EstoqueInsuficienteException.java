package infra;

public class EstoqueInsuficienteException extends Exception {
    public EstoqueInsuficienteException(String nomeProduto, int solicitado, int disponivel){
        super(String.format(
            "Estoque insuficiente para '%s'. Solicitado: %d| Disponivel: %d",
            nomeProduto, solicitado, disponivel
        ));
    }
}
