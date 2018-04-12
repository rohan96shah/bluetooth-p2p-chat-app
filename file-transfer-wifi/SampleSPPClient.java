
package bluetooth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Vector;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;


/**
 * A simple SPP client that connects with an SPP server
 */
public class SampleSPPClient implements DiscoveryListener, Runnable
{
 private static String My_IP = "10.4.7.125";
    //hardcode the address we are looking for...
    private String address;
	
    //object used for waiting
    private static Object lock = new Object();

    //vector containing the devices discovered
    private static Vector vecDevices = new Vector();

    private static String connectionURL = null;

    public static void main(String[] args) throws IOException
    {
        SampleSPPClient client = new SampleSPPClient();
        LocalDevice localDevice = LocalDevice.getLocalDevice();
        System.out.println("Address: " + localDevice.getBluetoothAddress());
        System.out.println("Name: " + localDevice.getFriendlyName());
        DiscoveryAgent agent = localDevice.getDiscoveryAgent();
        System.out.println("Starting device inquiry");
        agent.startInquiry(DiscoveryAgent.GIAC, client);
        
        try
        {
            synchronized (lock)
            {
                lock.wait();
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        System.out.println("Device Inquiry Completed. ");

        // this device object to use...
        boolean found = false;
        RemoteDevice remoteDevice = null;        
        
        //print all devices in vecDevices
        int deviceCount = vecDevices.size();

        if (deviceCount <= 0)
        {
            System.out.println("No Devices Found .");
            System.exit(0);
        }
        else
        {
            //print bluetooth device addresses and names in the format [ No. address (name) ]
            System.out.println("Bluetooth Devices: ");
            for (int i = 0; i < deviceCount; i++)
            {
                remoteDevice = (RemoteDevice) vecDevices.elementAt(i);
                if(remoteDevice.getBluetoothAddress().equals("8086F22C24A9")) {
                	found = true;
                	break;
                }
            }
        }
        
        if(found) {
            System.out.println("We found your peer!");
            //check for spp service
            
            UUID[] uuidSet = new UUID[1];
            uuidSet[0] = new UUID(0x1101);

            System.out.println("\nSearching for service...");
            agent.searchServices(null, uuidSet, remoteDevice, client);

            try
            {
                synchronized (lock)
                {
                    lock.wait();
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            if (connectionURL == null)
            {
                System.out.println("Device does not support Simple SPP Service.");
                System.exit(0);
            }

            //connect to the server and send a line of text
            StreamConnection streamConnection = (StreamConnection) Connector.open(connectionURL);

            //send string
            OutputStream outStream = streamConnection.openOutputStream();
            PrintWriter pWriter = new PrintWriter(new OutputStreamWriter(outStream));

            //pWriter.write("rsponse string from new laptop!\r\n");
            pWriter.println(My_IP);
            pWriter.flush();
            
            //sendFile()

            //read response
            InputStream inStream = streamConnection.openInputStream();
            BufferedReader bReader2 = new BufferedReader(new InputStreamReader(inStream));
            String lineRead = bReader2.readLine();
            System.out.println(lineRead);    
            String otherIP = lineRead;
            getFile(otherIP);
            //System.exit(0);
        }
        else {
        	agent.cancelInquiry(client);
        	//Thread.currentThread().interrupt();
        	System.out.println("We couldn't find the device.");
        }

    }//main

    //methods of DiscoveryListener
    
    public static void getFile(String OtherIP) throws IOException
    {
        FileTransferClient fc = new FileTransferClient(OtherIP); 
    }
    
    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod)
    {
        //add the device to the vector
        if (!vecDevices.contains(btDevice))
        {
            vecDevices.addElement(btDevice);
        }
    }

    //implement this method since services are not being discovered
    public void servicesDiscovered(int transID, ServiceRecord[] servRecord)
    {
        if (servRecord != null && servRecord.length > 0)
        {
            connectionURL = servRecord[0].getConnectionURL(0, false);
        }
        synchronized (lock)
        {
            lock.notify();
        }
    }

    //implement this method since services are not being discovered
    public void serviceSearchCompleted(int transID, int respCode)
    {
        synchronized (lock)
        {
            lock.notify();
        }
    }

    public void inquiryCompleted(int discType)
    {
        synchronized (lock)
        {
            lock.notify();
        }

    }//end method
    
    
    @Override
    public void run()
    {
    	try {
			main(null);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }//run


}