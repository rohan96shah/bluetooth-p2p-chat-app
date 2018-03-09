/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bluetooth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.BluetoothStateException;

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
                RemoteDevice remoteDevice = (RemoteDevice) vecDevices.elementAt(i);
                System.out.println((i + 1) + ". " + remoteDevice.getBluetoothAddress() + " (" + remoteDevice.getFriendlyName(false) + ")");
            }
        }

        System.out.print("Choose Device index: ");
        BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));

        String chosenIndex = bReader.readLine();
        int index = Integer.parseInt(chosenIndex.trim());

        //check for spp service
        RemoteDevice remoteDevice = (RemoteDevice) vecDevices.elementAt(index - 1);
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
        pWriter.write("Test String from SPP Client\r\n");
        pWriter.flush();

        //read response
        InputStream inStream = streamConnection.openInputStream();
        BufferedReader bReader2 = new BufferedReader(new InputStreamReader(inStream));
        String lineRead = bReader2.readLine();
        System.out.println(lineRead);

    }//main

    //methods of DiscoveryListener
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
        SampleSPPClient client = new SampleSPPClient();
        LocalDevice localDevice = null;
        try {
            localDevice = LocalDevice.getLocalDevice();
        } catch (BluetoothStateException ex) {
            Logger.getLogger(SampleSPPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Address: " + localDevice.getBluetoothAddress());
        System.out.println("Name: " + localDevice.getFriendlyName());
        DiscoveryAgent agent = localDevice.getDiscoveryAgent();
        System.out.println("Starting device inquiry");
        try {
            agent.startInquiry(DiscoveryAgent.GIAC, client);
        } catch (BluetoothStateException ex) {
            Logger.getLogger(SampleSPPClient.class.getName()).log(Level.SEVERE, null, ex);
        }

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
                RemoteDevice remoteDevice = (RemoteDevice) vecDevices.elementAt(i);
                try {
                    System.out.println((i + 1) + ". " + remoteDevice.getBluetoothAddress() + " (" + remoteDevice.getFriendlyName(false) + ")");
                } catch (IOException ex) {
                    Logger.getLogger(SampleSPPClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        System.out.print("Choose Device index: ");
        BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));

        String chosenIndex = null;
        try {
            chosenIndex = bReader.readLine();
        } catch (IOException ex) {
            Logger.getLogger(SampleSPPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        int index = Integer.parseInt(chosenIndex.trim());

        //check for spp service
        RemoteDevice remoteDevice = (RemoteDevice) vecDevices.elementAt(index - 1);
        UUID[] uuidSet = new UUID[1];
        uuidSet[0] = new UUID(0x1101);

        System.out.println("\nSearching for service...");
        try {
            agent.searchServices(null, uuidSet, remoteDevice, client);
        } catch (BluetoothStateException ex) {
            Logger.getLogger(SampleSPPClient.class.getName()).log(Level.SEVERE, null, ex);
        }

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
        StreamConnection streamConnection = null;
        try {
            streamConnection = (StreamConnection) Connector.open(connectionURL);
        } catch (IOException ex) {
            Logger.getLogger(SampleSPPClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        //send string
        OutputStream outStream = null;
        try {
            outStream = streamConnection.openOutputStream();
        } catch (IOException ex) {
            Logger.getLogger(SampleSPPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        PrintWriter pWriter = new PrintWriter(new OutputStreamWriter(outStream));
        pWriter.write("Test String from SPP Client\r\n");
        pWriter.flush();

        //read response
        InputStream inStream = null;
        try {
            inStream = streamConnection.openInputStream();
        } catch (IOException ex) {
            Logger.getLogger(SampleSPPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedReader bReader2 = new BufferedReader(new InputStreamReader(inStream));
        String lineRead = null;
        try {
            lineRead = bReader2.readLine();
        } catch (IOException ex) {
            Logger.getLogger(SampleSPPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(lineRead);
    }

}
