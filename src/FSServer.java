import java.util.ArrayList;

public class FSServer {

    private String dir;
    private ArrayList<FSMonitor> clients;

    public FSServer(String dir) {
        this.dir = dir;
        clients = new ArrayList<>();
    }

    public void addClient(FSMonitor client){
        clients.add(client);
    }

    public void removeClient(FSMonitor client){
        clients.remove(client);
    }

}
