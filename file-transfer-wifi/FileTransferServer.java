package bluetooth;
 
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
 
/**
 * A server program which accepts requests from clients to capitalize strings.
 * When clients connect, a new thread is started to handle an interactive dialog
 * in which the client sends in a string and the server thread sends back the
 * capitalized version of the string.
 *
 * The program is runs in an infinite loop, so shutdown in platform dependent.
 * If you ran it from a console window with the "java" interpreter, Ctrl+C
 * generally will shut it down.
 */
public class FileTransferServer {
 
    private static String PATH = "C:\\Users\\Rohan Shah\\Desktop\\Wifi\\";
    private static File currentDirectory;
    private ServerSocket generalIO;
    private ServerSocket fileIO;
    private Socket generalIOSocket;
    private Socket fileIOSocket;
    private BufferedReader in;
    private PrintWriter out;
    private FileInputStream fis;
    private OutputStream os;
 
    public FileTransferServer() {
        System.out.println("The file transfer server is running.");
        try {
            this.generalIO = new ServerSocket(9898);
            this.fileIO = new ServerSocket(9899);
            this.generalIOSocket = this.generalIO.accept();
            this.fileIOSocket = this.fileIO.accept();
 
            in = new BufferedReader(new InputStreamReader(generalIOSocket.getInputStream()));
            out = new PrintWriter(generalIOSocket.getOutputStream(), true);
            // Send a welcome message to the client.
            out.println("Welcome to file transfer!");
//            out.close();
            startServer();
 
        } catch (IOException ex) {
            Logger.getLogger(FileTransferServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
    public static void main(String[] args) {
        //FileServer server = new FileServer();
        //server.startServer();
    }
 
    private boolean sendFile(File fileToSend) {
        byte fileContent[] = new byte[(int) fileToSend.length()];
       
        /*if(this.fileIOSocket.isClosed()) {
            try {
                this.fileIOSocket = this.fileIO.accept();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        } */
       
        try {
            fis = new FileInputStream(fileToSend); // create input stream from the file...
        } catch (FileNotFoundException ex) {
            System.out.println("The file was not found!");
            return false;
        }
 
        try {
            fis.read(fileContent, 0, fileContent.length); // read lenght of file into byte array
            fis.close();
            os = this.fileIOSocket.getOutputStream();
            os.write(fileContent, 0, fileContent.length);
            //os.close();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(FileTransferServer.class.getName()).log(Level.SEVERE, null, ex);
        }
 
        return false;
    }
 
    private void startServer() {
        try {
            currentDirectory = new File(PATH);
            while (true) {
                String input = in.readLine();
                System.out.println("input: " + input);
                //in.close();
                if (input == null || input.equals(".")) {
                    break;
                }
                if (input.equalsIgnoreCase("index")) {
                    File[] fileList = currentDirectory.listFiles();
                    //out = new PrintWriter(generalIOSocket.getOutputStream(), true);
                    out.print(fileList.length + "\n");
                    for (int i = 0; i < fileList.length; i++) {
                        out.println(fileList[i].getName());
                    }
                    //out.close();
                } else if (input.startsWith("download")) {
                    String[] parts = input.split(" ");
                    File file = new File(PATH + parts[1]);
                    int fileLength;
                   
                    //out = new PrintWriter(generalIOSocket.getOutputStream(), true);
                   
                    if (file.exists()) {
                        out.println("File exists... Transferring...");
                        fileLength = (int) file.length();
                        out.println(fileLength); // send length of file in bytes
                        if (sendFile(file)) {
                            System.out.println("File transfer complete.");
                        } else {
                            System.out.println("There was an error in transferring the file.");
                        }
                        out.println("File transfer complete!");
 
                    } else {
                        out.println("The file does not exist.");
                    }
                   
                    //out.close();
 
                } else if(input.startsWith("change")) {
                    String[] parts = input.split(" ");
                    PATH = PATH.concat(parts[1] + "\\");
                    currentDirectory = new File(PATH);
                    out.println("changed server directory to: " + currentDirectory.getAbsolutePath());
                }
               
                else {
                    //out = new PrintWriter(generalIOSocket.getOutputStream(), true);                    
                    out.println("Invalid Command: " + input);
                    //out.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
}