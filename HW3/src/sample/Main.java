package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import  java.sql.*;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        ConnectDriver connectDrv = new ConnectDriver();
        Connection connection = connectDrv.loadDriver();

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);

        // Drop tables for initialization
        // Create Statement
        Statement statement = connection.createStatement();
        String dropdbFilePath = ".\\src\\sample\\sql\\dropdb.sql";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dropdbFilePath));
            String line = null;
            int line_cnt = 0;
            while((line = reader.readLine()) != null) {
                //System.out.println("line_cnt = " + line_cnt + "\nline content is:" + line);
                statement.executeUpdate(line);
                line_cnt++;
            }
            reader.close();
        } catch (IOException io_e) {
            io_e.printStackTrace();
        } catch (SQLException sql_e) {
            System.out.println("SQLException while drop db ----");
            System.out.println("SQLException: " + sql_e.getMessage());
            System.out.println("SQLState: " + sql_e.getSQLState());
            System.out.println("VendorError: " + sql_e.getErrorCode());
        }

        String createdbFilePath = ".\\src\\sample\\sql\\createdb.sql";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(createdbFilePath));
            String line;
            String sql = "";
            int line_cnt = 0;
            while((line = reader.readLine()) != null) {
                //System.out.println("line_cnt = " + line_cnt + " line.length() = " + line.length() + "\nline content is:" + line);
                if ((line.length() == 0) || (line.contains("--"))) continue;
                sql += line + "\n";
                if (line.equals(")")) {
                    System.out.println("sql is:\n" + sql);
                    statement.executeUpdate(sql);
                    sql = "";
                }
                line_cnt++;
            }
            reader.close();
        } catch (IOException io_e) {
            io_e.printStackTrace();
        } catch (SQLException sql_e) {
            System.out.println("SQLException while creating db ----");
            System.out.println("SQLException: " + sql_e.getMessage());
            System.out.println("SQLState: " + sql_e.getSQLState());
            System.out.println("VendorError: " + sql_e.getErrorCode());
        }

        Populate dbPop = new Populate(connection);
        dbPop.populateMovies();
        dbPop.populateMovieCountry();
        dbPop.populateGenres();
        dbPop.populateLocations();
        dbPop.populateTags();
        dbPop.populateMovietag();

        System.out.println("Disconnect the database");
        connection.close();

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
