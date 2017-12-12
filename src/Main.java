

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        boolean test = true;
        Scanner sc = new Scanner(System.in);
        createTable();
        while (test){
            System.out.println("Choose option: ");
            System.out.println("add remove edit showAll exit");
           switch (sc.nextLine()){
               case "add":post();
               break;
               case "remove":remove();
               break;
               case "edit":edit();
               break;
               case "showAll":get();
               break;
               case "exit":test = false;
               break;
               default:
                   System.out.println("You have entered a non-existent option");

           }
        }

    }
    private static void edit(){
        Scanner scan = new Scanner(System.in);
        System.out.print("Bookname:");
        String booknameToUpdate = scan.nextLine();
        String newBookname;
        int idToUpdate;
        try {

                Connection con = getConnection();
                PreparedStatement chekstatement = con.prepareStatement("SELECT * FROM books WHERE bookname ='"+booknameToUpdate+"' ");
                ResultSet result = chekstatement.executeQuery();
                ArrayList<String> array = new ArrayList<>();
                while (result.next()){
                    array.add(result.getString("id")+" "+result.getString("author")+" \""+result.getString("bookname")+"\"");
                }
            if(array.size()>1){
                Iterator<String> iter = array.iterator();
                while (iter.hasNext()){
                    System.out.println(iter.next());
                }
                System.out.println("We have few books with such name please choose one by typing a number of book:");
                idToUpdate = scan.nextInt();
                System.out.print("New bookname:");
                Scanner sc = new Scanner(System.in);
                newBookname = sc.nextLine();
                PreparedStatement statement = con.prepareStatement("UPDATE books SET bookname ='"+newBookname+"' WHERE id = '"+idToUpdate+"'");
                statement.executeUpdate();
            }else{
                System.out.print("New bookname:");
                newBookname = scan.nextLine();
                PreparedStatement statement = con.prepareStatement("UPDATE books SET bookname ='"+newBookname+"' WHERE bookname ='"+booknameToUpdate+"'");
                statement.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void remove() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Bookname:");
        String booknameToRemove = scan.nextLine();
        int idToRemove;
        try {

            Connection con = getConnection();
            PreparedStatement chekstatement = con.prepareStatement("SELECT * FROM books WHERE bookname ='"+booknameToRemove+"' ");
            ResultSet result = chekstatement.executeQuery();
            ArrayList<String> array = new ArrayList<>();
            while (result.next()){
                array.add(result.getString("id")+" "+result.getString("author")+" \""+result.getString("bookname")+"\"");
            }
            if(array.size()>1){
                Iterator<String> iter = array.iterator();
                while (iter.hasNext()){
                    System.out.println(iter.next());
                }
                System.out.println("We have few books with such name please choose one by typing a number of book:");
                idToRemove = scan.nextInt();
                PreparedStatement statement = con.prepareStatement("DELETE FROM books WHERE id = '"+idToRemove+"' ");
                statement.executeUpdate();
            }
            else {
                PreparedStatement statement = con.prepareStatement("DELETE FROM books WHERE bookname ='"+booknameToRemove+"' ");
                statement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private static ArrayList<String> get() {
        try {
            Connection con = getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT * FROM books ORDER  BY bookname");
            ResultSet result  = statement.executeQuery();
            ArrayList<String> array = new ArrayList<>();
            System.out.println("Our books:");
            while(result.next()){
                System.out.println(result.getString("author")+"\""+result.getString("bookname")+"\"");
                array.add(result.getString("bookname"));
            }
            return array;

        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }
    private static void post() {
       Scanner scan = new Scanner(System.in);
       System.out.print("Author name:");
       String author = scan.nextLine();
       System.out.print("Bookname:");
        String bookname = scan.nextLine();

        try {
            Connection con = getConnection();
            PreparedStatement posted = con.prepareStatement("INSERT INTO books(author,bookname) VALUES ('"+author+"','"+bookname+"')");
            posted.executeUpdate();
            System.out.println("Book "+author+" \""+bookname+"\""+ " was add");
        }
        catch (Exception e){
            System.out.println(e);
        }
   }
    private static void createTable() {
        try {
            Connection con  = getConnection();
            PreparedStatement create = con.prepareStatement("CREATE TABLE IF NOT EXISTS books(id int NOT NULL AUTO_INCREMENT,author VARCHAR(255) ,bookname VARCHAR(255),PRIMARY KEY(id))");
            create.executeUpdate();
        }catch (Exception e){
            System.out.println(e);
        }
    }
    private static Connection getConnection() {
        try {
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost/books";
            String username = "root";
            String password = "root";
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url,username,password);
//            System.out.println("Connected successfully");
            return conn;
        }catch (Exception e){
            System.out.println(e);
        }

        return null;
    }

}
