package linda.shm;

import linda.Callback;
import linda.Linda;
import linda.Tuple;

import java.util.*;
import java.util.concurrent.Semaphore;

/** Shared memory implementation of Linda. */
public class CentralizedLinda implements Linda {

    private  List<Tuple> tuplespace;
    private Semaphore mutex;
    //private Semaphore semTake, semRead;

	
    public CentralizedLinda() {
        tuplespace = new ArrayList<Tuple>();
        mutex = new Semaphore(1);
        //semTake = new Semaphore(0);
        //semRead = new Semaphore(1);
    }

    /** Adds a tuple t to the tuplespace. */
    @Override
    public void write (Tuple t){
        this.tuplespace.add(t);
        for (int i = 0; i < this.tuplespace.size(); i++){
            System.out.println(tuplespace.get(i));
        }
    }

    /** Returns a tuple matching the template and removes it from the tuplespace.
     * Blocks if no corresponding tuple is found. */

     /**block implémentation des sémaphote */
    @Override
    public Tuple take(Tuple template){
     
        Tuple tupleout = null;

               
        {try {
            
            
            while(tupleout == null){
                //System.out.println("je suis dans semaphore de take");
                this.mutex.acquire();
                tupleout = tryTake(template);
                this.mutex.release();
            }
            
        } catch (InterruptedException e){
            System.out.println("Interrupted exception dans le take");
        }

        }
        return tupleout;
    }

    /** Returns a tuple matching the template and leaves it in the tuplespace.
     * Blocks if no corresponding tuple is found. */
    @Override
    public Tuple read(Tuple template){
        Tuple tupleout = null;
        
        try {
            
            while (tupleout == null)
            {
                //System.out.println("je suis dans semaphore de read");
                this.mutex.acquire();
                tupleout = tryRead(template);
                this.mutex.release();
            }
            
        } catch (InterruptedException e){
            System.out.println("Interrupted exception dans le read");
        }
        return tupleout;
    }


     /** Returns a tuple matching the template and removes it from the tuplespace.
     * Returns null if none found. */
    @Override
    public Tuple tryTake(Tuple template){
        Tuple tupleout = null;
        for ( int i = 0; i < this.tuplespace.size(); i++){
            if (this.tuplespace.get(i).matches(template)) {
                tupleout = this.tuplespace.get(i);
                this.tuplespace.remove(this.tuplespace.get(i));
            }
        }
        return tupleout;
    }

    /** Returns a tuple matching the template and leaves it in the tuplespace.
     * Returns null if none found. */
    @Override
    public Tuple tryRead(Tuple template){
        Tuple tupleout = null;
        for ( int i = 0; i < this.tuplespace.size(); i++){
            if (this.tuplespace.get(i).matches(template)) {
                tupleout = this.tuplespace.get(i);
            }
        }
        return tupleout;
    }

    /** Returns all the tuples matching the template and removes them from the tuplespace.
     * Returns an empty collection if none found (never blocks).
     * Note: there is no atomicity or consistency constraints between takeAll and other methods;
     * for instance two concurrent takeAll with similar templates may split the tuples between the two results.
     */
    @Override
    public Collection<Tuple> takeAll(Tuple template){
        Collection<Tuple> liste = new ArrayList<Tuple>();
        for ( int i = 0; i < this.tuplespace.size(); i++){
            if (this.tuplespace.get(i).matches(template)) {
                liste.add(this.tuplespace.get(i).deepclone());
                this.tuplespace.remove(this.tuplespace.get(i));
            }
        }
        return liste;
    }


    /** Returns all the tuples matching the template and leaves them in the tuplespace.
     * Returns an empty collection if none found (never blocks).
     * Note: there is no atomicity or consistency constraints between readAll and other methods;
     * for instance (write([1]);write([2])) || readAll([?Integer]) may return only [2].
     */
    @Override
    public Collection<Tuple> readAll(Tuple template){
        Collection<Tuple> liste = new ArrayList<Tuple>();
        for ( int i = 0; i < this.tuplespace.size(); i++){
            if (this.tuplespace.get(i).matches(template)) {
                liste.add(this.tuplespace.get(i));
            }
        }
        return liste;
    }


  /*   public enum eventMode { READ, TAKE };
    public enum eventTiming { IMMEDIATE, FUTURE }; */

    /** Registers a callback which will be called when a tuple matching the template appears.
     * If the mode is Take, the found tuple is removed from the tuplespace.
     * The callback is fired once. It may re-register itself if necessary.
     * If timing is immediate, the callback may immediately fire if a matching tuple is already present; if timing is future, current tuples are ignored.
     * Beware: a callback should never block as the calling context may be the one of the writer (see also {@link AsynchronousCallback} class).
     * Callbacks are not ordered: if more than one may be fired, the chosen one is arbitrary.
     * Beware of loop with a READ/IMMEDIATE re-registering callback !
     *
     * @param mode read or take mode.
     * @param timing (potentially) immediate or only future firing.
     * @param template the filtering template.
     * @param callback the callback to call if a matching tuple appears.
     */
    @Override
    public void eventRegister(eventMode mode, eventTiming timing, Tuple template, Callback callback){

    }

    /** To debug, prints any information it wants (e.g. the tuples in tuplespace or the registered callbacks), prefixed by <code>prefix</code. */
    @Override
    public void debug(String prefix){
    }





}
