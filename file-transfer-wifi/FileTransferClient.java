package bluetooth;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileTransferClient{

    private BufferedReader in;
    private PrintWriter out;
    private Socket generalIOSocket;
    private Socket fileIOSocket;
    
    private String PATH;

    public FileTransferClient(String host) {
        try {
            this.PATH = "C:\\Users\\Rohan Shah\\Desktop\\Wifi\\New folder\\";
            this.generalIOSocket = new Socket(host, 9898);
            this.fileIOSocket = new Socket(host, 9899);

            in = new BufferedReader(new InputStreamReader(generalIOSocket.getInputStream()));
            out = new PrintWriter(generalIOSocket.getOutputStream(), true);

            // Consume the initial welcoming messages from the server
            System.out.println(in.readLine());
            //in.close();
            processMessages();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void processMessages() {
        Scanner userInput = new Scanner(System.in);
        String command;

        while (true) {
            System.out.printf("Enter a command: ");
            command = userInput.nextLine();

            out.println(command);
            //out.close();

            String response;

            try {
                if (command.equalsIgnoreCase("index")) {
                    //in = new BufferedReader(new InputStreamReader(generalIOSocket.getInputStream()));
                    int length = Integer.parseInt(in.readLine());
                    System.out.println("length is: " + length);
                    //in.close();
                    for (int i = 0; i < length; i++) {
                        //in = new BufferedReader(new InputStreamReader(generalIOSocket.getInputStream()));
                        System.out.printf(in.readLine() + "\n");
                        //in.close();
                    }
                } else if (command.startsWith("download")) {
                    //in = new BufferedReader(new InputStreamReader(generalIOSocket.getInputStream()));
                    response = in.readLine();
                    //in.close();
                    if (response.equalsIgnoreCase("File exists... Transferring...")) {
                        System.out.println(response);
                        //in = new BufferedReader(new InputStreamReader(generalIOSocket.getInputStream()));
                        int fileSize = Integer.parseInt(in.readLine());
                        //in.close();
                        getFile(command.split(" ")[1], fileSize);
                        System.out.println(in.readLine());
                    } else {
                        System.out.printf("else: " + response + "\n");
                    }
                } else if(command.startsWith("change")) {
                    response = in.readLine();
                    System.out.println(response);
                }
                
                else {
                    response = in.readLine();
                    if (response == null || response.equals("")) {
                        System.exit(0);
                    }
                    System.out.printf(response + "\n");
                }

            } catch (IOException ex) {
                response = "Error: " + ex;
            }
        }
    }

    public void getFile(String fileName, int fileSize) {
        byte fileContent[] = new byte[fileSize];
        InputStream is = null;
        FileOutputStream fos = null;
        System.out.println("transferring " + fileName + " with size " + fileSize + " bytes...");

        try {
            fos = new FileOutputStream(new File(PATH + fileName));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        try {
            is = fileIOSocket.getInputStream();
            is.read(fileContent, 0, fileSize);
            //is.close();
            fos.write(fileContent, 0, fileSize);
            fos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
        FileTransferClient fc = new FileTransferClient("10.162.185.31");
       // fc.processMessages();
    }
}