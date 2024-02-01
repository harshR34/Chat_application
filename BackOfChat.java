import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BackOfChat {
    static Connection con;
    static LocalDate date = LocalDate.now();
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");
    static String myDate = date.format(formatter);
    public static void getConnection()throws Exception{
        String url = "jdbc:mysql://localhost:3306/chatApp";
        String user = "root",pass = "";
        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
        con = DriverManager.getConnection(url,user,pass);
        FileReader fr = new FileReader("D:/chatApp testing file/["+myDate+"].txt");
        String sql = "Insert into backUpChat(file_content) values(?)";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setClob(1,fr);

        int row = pst.executeUpdate();
        con.close();
    }
}
