package infra;

public class ProcessadorThreads implements Runnable{

    public volatile boolean rodando = true;

    @Override
    public void run() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }
    public void parar(){
        this.rodando = false;
    }

}