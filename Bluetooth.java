package bluetooth;

/**
 *
 * @author Rohan Shah
 */
public class bluetooth implements Runnable
{
    static Thread thread_server;
    static Thread thread_client;
    
    
    public static void main(String... args)
    {
        thread_server = new Thread(new SampleSPPServer());
        thread_client = new Thread(new SampleSPPClient());
        
        thread_client.start();
        
    }
    
    
    public void run()
    {
        
    }
}