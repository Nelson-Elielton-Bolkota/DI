package infra;

import java.sql.Connection;
import java.sql.SQLException;

public class ProcessadorThreads implements Runnable{

    public volatile boolean rodando = true;

    @Override
    public void run() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }

    public void processarCiclo(){
        try(Connection conn = Conexao.conectar()){

        }catch(SQLException e){
            System.out.println("[THREAD] erro no banco de dados durando o processo "+ e.getMessage());
        }
    }

    public void parar(){
        this.rodando = false;
    }

}