package bluetooth;

import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Rohan Shah
 */
public class Bluetooth implements Runnable
{
    static Thread thread_server;
    static Thread thread_client;
    static Thread thread_main;
    static Random generator;
    
    
    public static void main(String... args) throws InterruptedException
    {
        
        generator = new Random();
        thread_server = new Thread(new SampleSPPServer());
        thread_client = new Thread(new SampleSPPClient());        
        int random = generator.nextInt(2);
       // int random = 1;
        
        
        System.out.println("Starting main...");
        System.out.println("Our number is: " + random + ".");
        
        if(random == 0) { // start client if the boolean value is 0
        	Thread.sleep(10000);
        	thread_client.start();
        	Thread.sleep(10000);
        	if(!thread_client.isInterrupted()) {
        		main();
        	}
        }
        else { // start server if the vollean value is 1
        	thread_server.run();  
        	Thread.sleep(40000);
        	main();
        }
        
    }
    
    
    public void run()
    {
        
    }
}