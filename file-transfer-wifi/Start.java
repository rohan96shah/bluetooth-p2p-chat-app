
package bluetooth;

import java.io.IOException;
import java.util.Random;

public class Start implements Runnable
{
    static Thread thread_server;
    static Thread thread_client;
    static Random generator;
    
    
    public static void main(String... args) throws InterruptedException, IOException
    {
        
        generator = new Random();
        thread_server = new Thread(new SampleSPPServer());
        thread_client = new Thread(new SampleSPPClient());        
        int random = generator.nextInt(2); 
        //int random = 1;
        
        System.out.println("Starting main...");
        System.out.println("Our number is: " + random + ".");
        
        if(random % 2 == 0) { // start client on even
        	//Thread.sleep(10000);
        	thread_client.start();
        	Thread.sleep(300000);
        	if(!thread_client.isInterrupted()) {
        		main();
        	}
        }
        else { // start server on odd
        	thread_server.run();  
                //testThread.run();
        	Thread.sleep(300000);
        	main();
        }
        
    }
    
    
    public void run()
    {
        
    }
}