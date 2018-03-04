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
        
        
        Scanner sc = new Scanner(System.in);
        String inp;
        
        thread_main = new Thread(new bluetooth());
        thread_server = new Thread(new SampleSPPServer());
        thread_client = new Thread(new SampleSPPClient());
        
        boolean checkEnterKeyPress = true;
        thread_server.start();
        while(true)
        {
            System.out.println("Press 's' to send a message");
            
            //thread_server.start();
            
            inp = sc.next();
            
            if(inp.equals("s"))
            {
                //thread_server.join();
                //thread_server.sleep(10000);
                break;
                
            }
            else
            {
                System.out.println("Incorrect Input. Try again.");
            }
            
        }
        thread_client.start();
        thread_client.join();
        
        main();
        //}
        
    }
    
    
    public void run()
    {
        
    }
}