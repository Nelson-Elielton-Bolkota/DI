package infra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.spi.DirStateFactory.Result;

public class ProcessadorThreads implements Runnable{

    public volatile boolean rodando = true;

    @Override
    public void run() {
        System.out.println("[THREAD] Processador de pedidos em background iniciado.");

        while (rodando) {
            try {
                processarCiclo();
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("[THREAD] Processador interrompido.");
                Thread.currentThread().interrupt();
                rodando = false;
            }
        }
    }

    public void processarCiclo(){
        try(Connection conn = Conexao.conectar()){

            conn.setAutoCommit(false);

            String sqlBusca = "select id_pedido from pedidos where status = 'FILA' order by data_criacao asc limit 1 for update";
            int idPedido = -1;

            try(PreparedStatement ps = conn.prepareStatement(sqlBusca);
                ResultSet rs = ps.executeQuery()){
                    if (rs.next()) {
                        idPedido = rs.getInt("id_pedido");
                    }
                }
                if (idPedido == -1) {
                    conn.rollback();
                    return;
                }
                String sqlProcessamento = "update pedidos set status = 'PROCESSANDO' where id_pedido = ?";
                try(PreparedStatement psProcessamento = conn.prepareStatement(sqlProcessamento)){
                    psProcessamento.setInt(1, idPedido);
                    psProcessamento.executeUpdate();
                }

                conn.commit();
                System.out.println("[THREAD] Pedido " + idPedido + " entrou em PROCESSAMENTO");

                try {
                Thread.sleep(3000); 
                } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                }

                String sqlFinalizado = "update pedidos set status = 'FINALIZADO' where id_pedido = ?";
                try(PreparedStatement psFinalizado = conn.prepareStatement(sqlFinalizado)){
                    psFinalizado.setInt(1, idPedido);
                    psFinalizado.executeUpdate();
                }
                conn.commit(); 
                System.out.println("[THREAD] Pedido " + idPedido + " FINALIZADO com sucesso");

        }catch(SQLException e){
            System.out.println("[THREAD] erro no banco de dados durando o processo "+ e.getMessage());
        }
    }

    public void parar(){
        this.rodando = false;
    }

}