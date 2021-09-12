import kotlin.reflect.KAnnotatedElement;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;

public class FSServer {

    private String dir;
    private ArrayList<FSMonitor> clients;
    private volatile boolean canWork;

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

    public void start(){
        canWork = true;
        run();//!!! thread
    }



    public void stop(){
        canWork = false;
    }

    public void run()  {
      try {
          WatchService watch = FileSystems.getDefault().newWatchService();
          Paths.get(dir).register(watch,
                  StandardWatchEventKinds.ENTRY_CREATE,
                  StandardWatchEventKinds.ENTRY_DELETE);
          while (canWork){

                WatchKey key = watch.take();
                for(WatchEvent event : key.pollEvents()){
                    String fName =  event.context().toString();
                    int kind = 0;
                    if(event.kind() == StandardWatchEventKinds.ENTRY_CREATE)
                        kind = FSMonitor.CREATE;
                    else kind = FSMonitor.REMOVE;
                    for (FSMonitor client: clients){
                        client.event(fName, kind);
                    }
                }
                key.reset();
          }

          watch.close();

      }catch (IOException ex){

      }catch (InterruptedException ex){

      }


    }

}
