import java.net.*;
import java.io.*;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Server{
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    File f;
    FileWriter fw;
    BufferedWriter bw;
    static boolean b =true;
    static String green="\u001b[32m";
    static String underLine = "\u001b[4m";
    static String italicize = "\u001b[3m";
    static String rest="\u001b[0m";
    static String red="\u001b[31m";
    static String cyan="\u001b[36m";
    static String yellow ="\u001B[33m";
    static String blue = "\u001B[34m";//unicode
    static String bold = "\u001B[1m";
    static String magenta ="\u001B[35m";
    static LocalDate date = LocalDate.now();
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");
    static String myDate = date.format(formatter);
    static LocalTime time = LocalTime.now();
    static DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("hh:mm");
    static String myTime = time.format(formatter1);

    /**
     * This constructor initializes a server and its components for a chat application.
     * It sets up a ServerSocket to accept connections and creates a log file with a timestamp to record chat messages.
     */
    public Server(){
        try {
            server = new ServerSocket(7778);
            System.out.println("Server is ready to accept Connection.");
            System.out.println("Waiting..........");
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            f = new File("D:/chatApp testing file/["+myDate+"].txt");
            f.createNewFile();
            fw = new FileWriter(f,true);
            bw = new BufferedWriter(fw);
            bw.write("\n----------------------------------------------------\n["+myDate+"] ["+myTime+"]"+"\n");

            startReading();
            startWriting();

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * This function, startReading(), sets up a reader thread to continuously receive
     *  and process messages from a connected client in a chat application.
     *  It also handles message logging to a file and provides console output for incoming messages.
     *  The function removes ANSI escape codes from received messages before logging them to maintain plain text formatting.
     *  Additionally, it includes an exit condition when the client requests to terminate the chat session.
     * @throws IOException
     */
    public void startReading()throws IOException{

        //reader Thread
        System.out.println(bold+cyan+italicize+"Reader Started......."+"\n\n\n"+rest);
        System.out.println("\t\t\t\t+--------------------+");
        System.out.printf("\t\t\t\t|  %-38s |%n",bold+italicize+underLine+yellow+"SERVER MASSAGER"+rest);
        System.out.println("\t\t\t\t+--------------------+");

        System.out.println("");
        Runnable reader = ()->{
            System.out.println("\n\t\t\t\t"+bold+underLine+myDate+rest);
            System.out.println(yellow+bold+italicize+"_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ |"+rest);
            try {
                try {
                    while (true) {
                        String msg = br.readLine();
                        String plainText = msg.replaceAll("\u001b\\[[;\\d]*m", "");
                        bw.write("[" + myTime + "]"+" "+"Client : "+plainText+"\n");
                        try {
                            if (msg.equals(bold + "exit" + rest)) {
                                System.out.println(bold + red + "Client terminated the chat." + rest);
                                socket.close();
                                b = false;
                                break;
                            }
                            System.out.println( "[" + myTime + "]"+"|" + bold + italicize + blue + underLine + " Client " + rest + bold + italicize + blue + ":- " + rest + yellow + italicize + bold + msg);
                        } catch (NullPointerException exception) {
                        }
                    }
                }
                catch (SocketException exc){

                }
                bw.close();
                fw.close();
            }catch(Exception exc){
                exc.printStackTrace();
            }
        };
        new Thread(reader).start();
    }

    /**
     * The `startWriting()` function sets up a writer thread for a chat application.
     * This thread continuously reads user input from the console and sends messages to the connected client.
     * It handles user input, sends messages to the client through the output stream, and provides an
     * exit condition when the user types "exit," which results in closing the socket and terminating the chat session.
     * @throws Exception
     */
    public void startWriting()throws Exception{
       // System.out.println(blue+bold+"Writer Started........."+rest);
        Runnable writer = () -> {
            try{
                while (true) {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(bold + content + rest);
                    out.flush();
                    if(content.equalsIgnoreCase("exit")){
                        socket.close();
                        break;
                    }
                }
            }catch (Exception exc){
                exc.printStackTrace();
            }
        };
        new Thread(writer).start();
    }

    /**
     * The main method is the entry point of the Java program.
     * In this specific program, it performs the following tasks:
     *
     * It prints a message to the console: "This is server...we are going to start a server."
     * This message serves as a notification to the user that the server application is starting.
     *
     * It creates an instance of the Server class using the new Server() statement.
     * This constructor initializes and starts the server, establishing a connection for communication.
     *
     * After starting the server, it calls the BackOfChat.getConnection() method.
     * The exact functionality of this method is not provided in the code snippet you've shown,
     * but it appears to be related to establishing or managing connections within the chat application.
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        System.out.println("This is server...we are going to start a server.");
        new Server();
        BackOfChat.getConnection();
    }
}
