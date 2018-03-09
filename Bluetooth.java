package bluetooth;

import java.util.Scanner;

/**
 *
 * @author Rohan Shah
 */
public class bluetooth implements Runnable
{
    static Thread thread_server;
    static Thread thread_client;
    static Thread thread_main;
    
    
    
    public static void main(String... args) throws InterruptedException
    {
        //if(!(thread_server.isAlive())) {
            thread_server = new Thread(new SampleSPPServer());
        //}
        //if(!(thread_client.isAlive())) {
            thread_client = new Thread(new SampleSPPClient());
        //}
        
        Scanner sc = new Scanner(System.in);
        String inp;
        
        thread_server.start();
        while(true)
        {
            System.out.println("Press 's' to send a message");
            
            //thread_server.start();
            
            inp = sc.next();
            
            if(inp.equals("s"))
            {
                break;
            }
            else
            {
                System.out.println("Incorrect Input. Try again.");
            }
            
        }
        thread_client.start();
        thread_client.join();
        
        thread_server = null;
        thread_client = null;
        
        main();
        //}
        
    }
    
    
    public void run()
    {
        
    }
}
