package linda.server;

import java.util.concurrent.Flow.Subscriber;
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.rmi.registry.*;
import linda.Callback;
import linda.Linda;
import linda.Tuple;
import linda.shm.CentralizedLinda;

/** Client part of a client/server implementation of Linda.
 * It implements the Linda interface and propagates everything to the server it is connected to.
 * */
public class LindaClient implements Linda {
	
    private HashMap<String, Vector<Callback>> subscribers;


    /** Initializes the Linda implementation.
     *  @param serverURI the URI of the server, e.g. "rmi://localhost:4000/LindaServer" or "//localhost:4000/LindaServer".
     */
    CentralizedLinda linda ;
    public LindaClient(String serverURI) {
        linda = new CentralizedLinda();
        subscribers = new HashMap<String, Vector<Callback>>();
    }

     


    public static void main(String[] args) {
        try {
            LindaClient server = new LindaClient();
            for (int i = 0; i < args.length; i++) {
                server.subscribers.put(args[i], new Vector<Callback>());
            }
            Registry registry = LocateRegistry.createRegistry(4000);
            Naming.rebind("//localhost:4000/linda", server);
            System.out.println("L'objet linda a été publié dans le registre.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    

    



    public void write(Tuple t){
        linda.write(t);
    }

    public Tuple take(Tuple template){
        linda.take(template);
    }

    public Tuple read(Tuple template){
        linda.read(template);
    }
    public Tuple tryTake(Tuple template){
        linda.tryTake(template);
    }

    public Tuple tryRead(Tuple template){
        linda.tryRead(template);
    }

    public Collection<Tuple> takeAll(Tuple template){
        linda.takeAll(template);
    }
 
    public Collection<Tuple> readAll(Tuple template){
         linda.readAll(template);
    }

    public void eventRegister(eventMode mode, eventTiming timing, Tuple template, Callback callback){
        linda.eventRegister(mode, timing, template, callback);
    }

    public void debug(String prefix){
        linda.debug(prefix);
    }
}
