import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Login {
    private static Map<String, String> userDatabase = new HashMap<>();
    private static Map<String, String> recoveryDatabase = new HashMap<>();
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

    private static int loginAttempts = 0;
    private static boolean locked = false;

    public static void LoginOrCreateAccount(){
        initializeDatabase();
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println(bold + yellow + "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _" + rest);
            System.out.printf(bold + yellow + "|" + rest + " %-40s " + bold + yellow + "|" + rest + "%n", bold + blue + "1 : create New Account " + rest);
            System.out.println(bold + yellow + "|_ _ _ _ _ _ _ _ _ _ _ _ _ _ _|" + rest);
            System.out.printf(bold + yellow + "|" + rest + " %-40s " + bold + yellow + "|" + rest + "%n", bold + blue + "2 : Login As Old User" + rest);
            System.out.println(bold + yellow + "|_ _ _ _ _ _ _ _ _ _ _ _ _ _ _|" + rest);
            System.out.printf(bold + yellow + "|" + rest + " %-40s " + bold + yellow + "|" + rest + "%n", bold + blue + "3 : Exit from Chatting" + rest);
            System.out.println(bold + yellow + "|_ _ _ _ _ _ _ _ _ _ _ _ _ _ _|" + rest);
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 :
                    System.out.print("Enter your Email : ");
                    String Email = scanner.nextLine();
                    String userName = "";
                    for(int i = 0;i<8;i++){
                        userName = userName + Email.charAt(i);
                    }
                    System.out.println("Your userName : "+userName);
                    System.out.print("Enter your password: ");
                    String pass = scanner.nextLine();

                    userDatabase.put(userName,pass);
                    break;
                case 2 :
                    while (!locked) {
                        if (loginAttempts >= 3) {
                            System.out.println("Too many login attempts. The application is locked.");
                            break;
                        }

                        System.out.print("Enter your username: ");
                        String username = scanner.nextLine();
                        System.out.print("Enter your password: ");
                        String password = scanner.nextLine();

                        if (login(username, password)) {
                            System.out.println("Login successful!");
                            startChat(username);
                            break;
                        } else {
                            System.out.println("Login failed. Please try again.");
                            loginAttempts++;
                        }
                    }
                    break;
                case 3 :
                    System.exit(0);
                    break;
            }
        }
    }

    private static void initializeDatabase() {
        // Populate the user database with sample username-password pairs.
        userDatabase.put("Malvin", "malvin1234");
        userDatabase.put("Vansh", "vansh1234");
        userDatabase.put("Harsh", "harsh1234");

        // Populate the recovery database with usernames and their associated master passwords.
        recoveryDatabase.put("Malvin", "1234@#$ChatApp");
        recoveryDatabase.put("Vansh", "1234@#$ChatApp");
        recoveryDatabase.put("Harsh", "1234@#$ChatApp");
    }

    private static boolean login(String username, String password) {
        String storedPassword = userDatabase.get(username);
        return storedPassword != null && storedPassword.equals(password);
    }

    private static void startChat(String username) {
        System.out.println("Chatting as " + username);
        // Implement your chat functionality here.
    }

    private static boolean recoverAccount(String username, String masterPassword) {
        String storedMasterPassword = recoveryDatabase.get(username);
        return storedMasterPassword != null && storedMasterPassword.equals(masterPassword);
    }

//    public static void main(String[] args) {
//        LoginOrCreateAccount();
//    }
}
