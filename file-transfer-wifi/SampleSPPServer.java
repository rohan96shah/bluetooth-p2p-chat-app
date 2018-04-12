
package bluetooth;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import static java.sql.DriverManager.println;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.bluetooth.*;
import javax.microedition.io.*;

/**
 * Class that implements an SPP Server which accepts single line of
 * message from an SPP client and sends a single line of response to the client.
 */

public class SampleSPPServer implements Runnable
{
   
    Thread fileServerThread;
    public boolean madeConnection = false;
   
   
    private static String MY_IP = "10.4.7.125";
 
    //start server
    private void startServer() throws IOException
    {
 
        //Create a UUID for SPP
        UUID uuid = new UUID("1105", true);
        //Create the servicve url
        String connectionString = "btspp://localhost:" + uuid + ";name=Sample SPP Server";
 
        //open server url
        StreamConnectionNotifier streamConnNotifier = (StreamConnectionNotifier) Connector.open(connectionString);
 
        //Wait for client connection
        System.out.println("\nServer Started. Waiting for clients to connectâ€¦");
        StreamConnection connection = streamConnNotifier.acceptAndOpen();
        RemoteDevice dev = RemoteDevice.getRemoteDevice(connection);
        System.out.println("Remote device address: " + dev.getBluetoothAddress());
        System.out.println("Remote device name: " + dev.getFriendlyName(true));
        madeConnection = true;
       
 
        //read string from spp client
        InputStream inStream = connection.openInputStream();
        BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
        String lineRead = bReader.readLine();
        System.out.println(lineRead);
 
        //send response to spp client
        OutputStream outStream = connection.openOutputStream();
        PrintWriter pWriter = new PrintWriter(new OutputStreamWriter(outStream));
        pWriter.println(MY_IP);
        pWriter.flush();
              
        sendFile();
       
        //System.exit(0);
    }
   
    public void sendFile() throws IOException {
        FileTransferServer fs = new FileTransferServer();
    }
 
    public static void main(String[] args) throws IOException
    {
        //display local device address and name
        LocalDevice localDevice = LocalDevice.getLocalDevice();
        System.out.println("Address: " + localDevice.getBluetoothAddress());
        System.out.println("Name: " + localDevice.getFriendlyName());
 
        SampleSPPServer sampleSPPServer = new SampleSPPServer();
        sampleSPPServer.startServer();
 
    }
   
    public void run()
    {
        //display local device address and name
        LocalDevice localDevice = null;
        try {
            localDevice = LocalDevice.getLocalDevice();
        } catch (BluetoothStateException ex) {
            Logger.getLogger(SampleSPPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Address: " + localDevice.getBluetoothAddress());
        System.out.println("Name: " + localDevice.getFriendlyName());
 
        SampleSPPServer sampleSPPServer = new SampleSPPServer();
        try {
            sampleSPPServer.startServer();
        } catch (IOException ex) {
            Logger.getLogger(SampleSPPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }
}