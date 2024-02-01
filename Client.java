
import java.net.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.time.format.DateTimeFormatter;

public class Client{
    Socket socket;
    static String userName;
    BufferedReader br;
    PrintWriter out;
    File f;
    FileWriter fw;
    BufferedWriter bw;
    static BackOfChat chat;
    static LocalDate date = LocalDate.now();
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");
    static String myDate = date.format(formatter);

    static LocalTime time = LocalTime.now();
    static DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("hh:mm");
    static String myTime = time.format(formatter1);




    static String green="\u001b[32m";
    static String underLine = "\u001b[4m";
    static String italicize = "\u001b[3m";
    static String rest="\u001b[0m";
    static String red="\u001b[31m";
    static String cyan="\u001b[36m";
    static String yellow ="\u001B[33m";
    static String blue = "\u001B[34m";
    static String bold = "\u001B[1m";
    static String magenta ="\u001B[35m";
    static Date timeStamp;

    /**
     * This constructor initializes a client for a chat application,
     * establishing a socket connection to a server,
     * setting up communication streams,
     * and creating a log file for chat messages with timestamps.
     */
    public Client(){
        timeStamp = new Date();
        try {
            System.out.println("Sending request to server.....");
            try {
                socket = new Socket("192.168.90.133", 7778);
            }catch (ConnectException exc){
                System.out.println(red+bold+"Client is not connected to Server !"+rest);
            }
            System.out.println("Connection successful.");
            chat = new BackOfChat();
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            f = new File("D:/chatApp testing file/["+myDate+"].txt");
            f.createNewFile();
            fw = new FileWriter(f,true);
            bw = new BufferedWriter(fw);
            bw.write("\n["+myDate+"] ["+myTime+"]"+"\n");
            //createGUI();
            //handleEvents();
            startReading();
            startWriting();
        }catch(NullPointerException e){
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * The startReading() method sets up a reader thread to continuously receive and process messages from a server in a chat client.
     * It removes ANSI escape codes from received messages, logs them to a file, and displays them in the console.
     * The method also handles special "exit" messages
     * to terminate the chat session gracefully while providing error handling for exceptions.
     * @throws IOException
     */
    public void startReading()throws IOException{
        //reader Thread
        System.out.println(bold+cyan+italicize+"Reader Started......."+"\n\n\n"+rest);
        System.out.println("\t\t\t\t+--------------------+");
        System.out.printf("\t\t\t\t|  %-38s |%n",bold+italicize+underLine+yellow+"CLIENT MASSAGER"+rest);
        System.out.println("\t\t\t\t+--------------------+");

        Runnable reader = ()->{
            System.out.println("\n\t\t\t\t"+bold+underLine+myDate+rest);
            System.out.println(yellow+bold+italicize+"_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ | "+rest);
            try {
                try {
                    while (true) {
                        String msg = br.readLine();
                        String plainText = msg.replaceAll("\u001b\\[[;\\d]*m", "");
                        bw.write("[" + myTime + "]" +" Server : "+ plainText+"\n");
                        try {
                            if (msg.equals(bold + "exit" + rest)) {
                                System.out.println(bold + red + "Server terminated the chat." + rest);
                                socket.close();
                                break;
                            }
                            System.out.println("[" + myTime + "]"+"|" + bold + italicize + blue + underLine + " Server " + rest + bold + italicize + blue + ":- " + rest + yellow + italicize + bold + msg);
                        } catch (NullPointerException exception) {
                        }
                    }
                }catch (SocketException exc){
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
     * The startWriting() method initiates a writer thread in a chat client
     * to handle user input and send messages to the server.
     * It continuously reads user input from the console, sends messages with formatting,
     * and provides a graceful exit mechanism when the user types "exit."
     * Exception handling is included for error management.
     * @throws Exception
     */

    public void startWriting()throws Exception{
       //System.out.println(blue+bold+"Writer Started........."+rest);
        Runnable writer = () -> {
            boolean b = true;
            try{
                while (true){
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(bold+content+rest);
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
     * The main method serves as the entry point for the Java program, representing a chat client.
     * It starts by printing the message "This is client..." to the console, indicating that it's a client application.
     * Then, it creates an instance of the Client class, initiating the client's setup and execution,
     * which includes connecting to a server and setting up communication channels.
     * @param args
     * @throws Exception
     */

    public static void main(String[] args) throws Exception{
        Login login = new Login();
        login.LoginOrCreateAccount();
        System.out.println("This is client...");
        new Client();
    }

    /**
     * The `Login` class provides a simple command-line-based user authentication
     * and account management system for a chatting application.
     * Users can create new accounts or log in as existing users.
     * After successful authentication, users can access the chat functionality.
     * The class also includes methods to initialize the user and master databases,
     * perform user login, and start the chat interface.
     */
    public static class Login extends Thread{
        Thread t = new Thread(this);
        private static final Map<String, String> userDatabase = new HashMap<>();
        private static final Map<String, String> masterDataBase = new HashMap<>();
        private static int loginAttempts = 0;
        private static int masterLoginAttempts = 0;
        public static boolean checkClient = true;

        private static boolean locked = false;

        /**
         *  Performs user login or account creation based on user input.
         *  If the user chooses to create a new account, they provide an email and password,
         *  which are used to generate a username and store their credentials in the user database.
         *  If the user chooses to log in, they must provide a username and password. After three
         *  failed login attempts, the system prompts the user to enter a master password to unlock
         *  the account.
         */
        public void LoginOrCreateAccount(){
            initializeDatabase();
            Scanner scanner = new Scanner(System.in);
            boolean b = true;
            while(b) {
                if(!t.isAlive()) {
                    System.out.println(bold + yellow + "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _" + rest);
                    System.out.printf(bold + yellow + "|" + rest + " %-40s " + bold + yellow + "|" + rest + "%n", bold + blue + "1 : create New Account " + rest);
                    System.out.println(bold + yellow + "|_ _ _ _ _ _ _ _ _ _ _ _ _ _ _|" + rest);
                    System.out.printf(bold + yellow + "|" + rest + " %-40s " + bold + yellow + "|" + rest + "%n", bold + blue + "2 : Login As Old User" + rest);
                    System.out.println(bold + yellow + "|_ _ _ _ _ _ _ _ _ _ _ _ _ _ _|" + rest);
                    System.out.printf(bold + yellow + "|" + rest + " %-40s " + bold + yellow + "|" + rest + "%n", bold + blue + "3 : Exit from Chatting" + rest);
                    System.out.println(bold + yellow + "|_ _ _ _ _ _ _ _ _ _ _ _ _ _ _|" + rest);
                    String choice = scanner.nextLine();
                    switch (choice) {
                        case "1":
                            System.out.print("Enter your Email : ");
                            String Email = scanner.nextLine();
                            userName = "";
                            for (int i = 0; i < 8; i++) {
                                userName = userName + Email.charAt(i);
                            }
                            System.out.println("Your userName : " + userName);
                            System.out.print("Enter your password: ");
                            String pass = scanner.nextLine();

                            userDatabase.put(userName, pass);
                            masterDataBase.put(userName, "1234@#$ChatApp");
                            break;
                        case "2":
                            while (!locked) {
                                if (loginAttempts >= 3) {
                                    System.out.println("Too many login attempts. You have last chance !\n\n");
                                    System.out.print("Enter your username: ");
                                    String username = scanner.nextLine();
                                    System.out.println("Enter last master password for login : ");
                                    String masterPass = scanner.nextLine();
                                    if (loginForMasterPassWord(username, masterPass)) {
                                        System.out.println("Login successful!");
                                        userName = username;
                                        startChat(userName);
                                        checkClient = true;
                                        b = false;
                                        break;
                                    } else {
                                        masterLoginAttempts++;
                                        if (masterLoginAttempts == 2) {
                                            System.out.println(bold + red + "\nThe ChatApp is lock for this account enter another account for chatting." + rest);
                                            System.exit(0);
                                        }
                                        System.out.println(bold + red + "You master password is wrong the chatting application and the system is locked for 5 minutes" + rest);
                                        checkClient = false;
                                        timer();
                                        break;
                                    }
                                }

                                System.out.print("Enter your username: ");
                                String username = scanner.nextLine();
                                System.out.print("Enter your password: ");
                                String password = scanner.nextLine();

                                if (login(username, password)) {
                                    System.out.println("Login successful!");
                                    startChat(username);
                                    b = false;
                                    break;
                                } else {
                                    System.out.println("\n\nLogin failed. Please try again.\n\n");
                                    loginAttempts++;
                                }
                            }
                            break;
                        case "3":
                            b = false;
                            System.exit(0);
                            break;
                        default:
                            System.out.println(red + bold + "Enter valid Input !" + rest);
                    }
                }
            }

        }

        /**
         *  Initializes the user and master databases with sample username-password pairs and master passwords.
         */

        private static void initializeDatabase() {
            // Populate the user database with sample username-password pairs.
            userDatabase.put("Malvin", "malvin1234");
            userDatabase.put("Vansh", "vansh1234");
            userDatabase.put("Harsh", "harsh1234");

            // Populate the recovery database with usernames and their associated master passwords.
            masterDataBase.put("Malvin", "1234@#$ChatApp");
            masterDataBase.put("Vansh", "1234@#$ChatApp");
            masterDataBase.put("Harsh", "1234@#$ChatApp");
        }

        /**
         * Validates the user's login credentials (username and password).
         * @param username
         * @param password
         * @return
         */

        private static boolean login(String username, String password) {
            String storedPassword = userDatabase.get(username);
            return storedPassword != null && storedPassword.equals(password);
        }

        /**
         * Validates the user's master password for unlocking the account after multiple failed login attempts.
         * @param username
         * @param password
         * @return
         */
        private static boolean loginForMasterPassWord(String username, String password) {
            String storedPassword = masterDataBase.get(username);

            return storedPassword != null && storedPassword.equals(password);
        }
        private void timer(){
            t.start();
        }
        public void run(){
            System.out.print(bold+blue+bold+"\nTimer : "+rest);
            for(int i = 5 ;i>=1;i--){
                System.out.print(i+"...");
                try{
                    sleep(1000);
                }
                catch (InterruptedException exc){
                    exc.printStackTrace();
                }
            }
            System.out.println("\n");
        }

        /**
         * Starts the chat interface for the authenticated user.
         * @param username The username of the authenticated user.
         * @param username
         */
        private static void startChat(String username) {
            System.out.println("\n\n\t\t\t\t+---------------------------+");
            System.out.printf("\t\t\t\t|  %-45s |%n",bold+italicize+underLine+yellow+"Chatting as "+username+rest);
            System.out.println("\t\t\t\t+---------------------------+\n\n");
            // Implement your chat functionality here.
        }

    }
}
