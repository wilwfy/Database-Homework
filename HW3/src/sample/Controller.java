package sample;

//import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

//import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.FlowPane;

public class Controller implements Initializable {

    ConnectDriver connectDrv;
    Connection connection;

    //Set<String> genreSet = new HashSet<String>();
    List<CheckBox> genreSelectedBox = new ArrayList<CheckBox>();
    Set<CheckBox> genreBoxSet = new HashSet<CheckBox>();

    List<CheckBox> countrySelectedBox = new ArrayList<CheckBox>();
    List<CheckBox> locationSelectedBox = new ArrayList<CheckBox>();

    ObservableList<String> ratingList = FXCollections.observableArrayList(">", "<", "=", ">=", "<=");
    ObservableList<String> reviewNumberList = FXCollections.observableArrayList(">", "<", "=", ">=", "<=");
    ObservableList<String> tagWeightList = FXCollections.observableArrayList(">", "<", "=", ">=", "<=");
    ObservableList<String> andOrList = FXCollections.observableArrayList("AND", "OR");

    String andOrOperation = "AND";
    String ratingOperation = ">";
    String reviewNumberOperation = ">";
    String tagWeightOperation = ">";

    String rating = "";
    String reviewNumber = "";

    String yearFrom = "";
    String yearTo = "";

    String tagWeight = "";

    String genreQuery = "";
    String countryQuery = "";
    String countryQueryMid = "";
    String locationQuery = "";
    String locationQueryMid = "";
    String ratingQuery = "";
    String reviewNumberQuery = "";
    String tagWeightQuery = "";
    String otherQueryMid = "";
    String finalQuery = "";

    @FXML
    private ScrollPane genresPane;

    @FXML
    private FlowPane genresContainer;

    @FXML
    private ScrollPane countryPane;

    @FXML
    private FlowPane countryContainer;

    @FXML
    private ScrollPane locationCountryPane;

    @FXML
    private FlowPane locationCountryContainer;

    @FXML
    private ChoiceBox<String> ratingBox;

    @FXML
    private TextField ratingValue;

    @FXML
    private ChoiceBox<String> reviewNumberBox;

    @FXML
    private TextField reviewValue;

    @FXML
    private ChoiceBox<String> tagWeightBox;

    @FXML
    private TextField weightValue;

    @FXML
    private ScrollPane tagValuePane;

    @FXML
    private TextFlow tagValueContainer;

    @FXML
    private DatePicker dateFrom;

    @FXML
    private DatePicker dateTo;

    @FXML
    private ChoiceBox<String> andOrBox;

    @FXML
    private TextArea queryText;

    @FXML
    private ScrollPane resultTextPane;

    @FXML
    private TextFlow resultTextFlow;

    @FXML
    private Button executeButton;


    @FXML
    void executeQuery(ActionEvent event){
        System.out.println("clicked");
        System.out.println(andOrBox.getSelectionModel().getSelectedItem());
        andOrBox.getSelectionModel().getSelectedItem();

        resultTextFlow.getChildren().clear();

        loadTag();

        finalQuery = "SELECT M.title, G.genre, M.year, MC.country, L.location1, L.location2, L.location3, L.location4, \n" +
                "(M.rtAllCriticsRating + M.rtTopCriticsRating + M.rtAudienceRating)/3 AS AverageRating, \n" +
                "(M.rtAllCriticsNumReviews + M.rtTopCriticsNumReviews + M.rtAudienceNumRatings)/3 AS AverageNumReviewsRatings \n" +
                "FROM Movies M, Genres G, Mcountry MC, Locations L, Movietag MT \n" +
                "WHERE M.mid = G.mid AND M.mid = MC.mid AND M.mid = L.mid AND M.mid = MT.mid \n" +
                (tagWeight.length() == 0 ? "" : "AND MT.tweight " + tagWeightOperation + tagWeight) + " " +
                "AND M.mid IN (" +
                locationQueryMid +
                otherQueryMid +
                ")";
        //((otherQueryMid.length() == 0) ? "" : "\nINTERSECT\n" + otherQueryMid) +

        //(rating == "" ? "" : "AND M.rtAllCriticsRating "+ ratingOperation + rating) +
        //        (reviewNumber == "" ? "" : "AND M.rtAllCriticsNumReviews " + reviewNumberOperation + reviewNumber) +
        //        (yearFrom == "" ? "" : "AND M.year >= " + yearFrom) +
        //        (yearTo == "" ? "" : "AND M.year <= " + yearTo) +
        //        (tagWeight == "" ? "" : "AND MT.tweight " + tagWeightOperation + tagWeight) +

        queryText.setText(finalQuery);

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(finalQuery);

            String title;
            String genre;
            String country;
            String location1;
            String location2;
            String location3;
            String location4;
            String averageRating;
            String averageNumReviewsRatings;
            String movieInfo;
            int result_cnt = 0;
            while(resultSet.next()) {
                title = resultSet.getString(1);
                genre = resultSet.getString(2);
                country = resultSet.getString(3);
                location1 = resultSet.getString(4);
                location2 = resultSet.getString(5);
                location3 = resultSet.getString(6);
                location4 = resultSet.getString(7);
                averageRating = resultSet.getString(8);
                averageNumReviewsRatings = resultSet.getString(9);
                movieInfo = title + " ---- " + genre + ", " + country + ", " + location1 + ", " + location2 + ", " + location3 + ", " + location4 + ", " +
                        averageRating + ", " + averageNumReviewsRatings;
                System.out.println(movieInfo);

                Text movieText = new Text(movieInfo);
                resultTextFlow = new TextFlow();
                resultTextFlow.getChildren().add(movieText);
                result_cnt++;
            }
            System.out.println("result count = " + result_cnt);
            if (result_cnt != 0)
                resultTextPane.setContent(resultTextFlow);

        } catch (SQLException sql_e) {
            System.out.println("SQLException: " + sql_e.getMessage());
            System.out.println("SQLState: " + sql_e.getSQLState());
            System.out.println("VendorError: " + sql_e.getErrorCode());
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        queryText.setText("Hello World!");
    }

    @FXML
    private void loadGenres() {
        genreQuery= "SELECT DISTINCT G.genre " +
                "FROM Genres G ";
        String genre = "";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(genreQuery);

            while(resultSet.next()) {
                genre = resultSet.getString(1);
                //System.out.println(genre);

                //genreSet.add(genre);
                CheckBox genreBox = new CheckBox(genre);
                genreBox.setStyle("-fx-font-size:20px");
                genreBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                    if (isNowSelected) {
                        genreSelectedBox.add(genreBox);
                        loadCountries();
                        loadCountryLocations();
                    } else {
                        genreSelectedBox.remove(genreBox);
                        loadCountries();
                        loadCountryLocations();
                    }
                });
                genresContainer.getChildren().add(genreBox);
            }

            genresPane.setContent(genresContainer);

        } catch (SQLException sql_e) {
            System.out.println("SQLException: " + sql_e.getMessage());
            System.out.println("SQLState: " + sql_e.getSQLState());
            System.out.println("VendorError: " + sql_e.getErrorCode());
        }
    }

    @FXML
    //private void loadCountries(MouseEvent event) {
    private void loadCountries() {

        //System.out.println("DEBUG -- yearFrom = " + yearFrom);
        //System.out.println("DEBUG -- yearTo = " + yearTo);

        countryContainer.getChildren().clear();
        countrySelectedBox.clear();
        andOrOperation = andOrBox.getValue();
        System.out.println("andOrOperation = " + andOrOperation);
        if (genreSelectedBox.size() == 0)
            System.out.println("WARNING -- At least one Genre must be selected!");

        String country = "";

        String genre = "";

        countryQuery = "SELECT DISTINCT MC.country " +
                "FROM Mcountry MC " +
                "WHERE MC.mid IN (";
        countryQueryMid = " ";

        for (int i = 0; i < genreSelectedBox.size(); i++) {
            genre = genreSelectedBox.get(i).getText();
            //System.out.println(genre);
            if (i == 0) countryQueryMid += "(";
            if (i > 0) {
                countryQueryMid += (andOrOperation.equals("AND") ? "\nINTERSECT\n" : "\nUNION\n");
            }
            countryQueryMid += "SELECT G.mid " +
                    "FROM Genres G " +
                    "WHERE G.genre = '" + genre + "'";
            if (i == genreSelectedBox.size()-1) countryQueryMid += ")";
        }
        countryQuery += countryQueryMid + ")";
        System.out.println("Mid for Country Query: " + countryQueryMid);
        System.out.println("Country Query: " + countryQuery);

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(countryQuery);

            while(resultSet.next()) {
                country = resultSet.getString(1);
                //System.out.println(genre);

                //genreSet.add(genre);
                CheckBox countryBox = new CheckBox(country);
                countryBox.setStyle("-fx-font-size:20px");
                countryBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                    if (isNowSelected) {
                        countrySelectedBox.add(countryBox);
                        loadCountryLocations();
                    } else {
                        countrySelectedBox.remove(countryBox);
                        loadCountryLocations();
                    }
                });
                countryContainer.getChildren().add(countryBox);
            }

            countryPane.setContent(countryContainer);

        } catch (SQLException sql_e) {
            System.out.println("SQLException: " + sql_e.getMessage());
            System.out.println("SQLState: " + sql_e.getSQLState());
            System.out.println("VendorError: " + sql_e.getErrorCode());
        }
    }

    @FXML
    //private void loadCountryLocations(MouseEvent event) {
    private void loadCountryLocations() {
        locationCountryContainer.getChildren().clear();
        locationSelectedBox.clear();
        andOrOperation = andOrBox.getValue();

        System.out.println("############### in loadCountryLocations() ##############");
        String genre = "";
        String country = "";
        String location = "";
        locationQuery = "SELECT DISTINCT L.location1 " +
                "FROM Locations L " +
                "WHERE L.mid IN (";

        locationQueryMid = " ";
        for (int i = 0; i < genreSelectedBox.size(); i++) {
            genre = genreSelectedBox.get(i).getText();
            //System.out.println(genre);
            if (i == 0) locationQueryMid += "(";
            if (i > 0) {
                locationQueryMid += (andOrOperation.equals("AND") ? "\nINTERSECT\n" : "\nUNION\n");
            }
            locationQueryMid += "SELECT G.mid " +
                    "FROM Genres G " +
                    "WHERE G.genre = '" + genre + "'";
            if (i == genreSelectedBox.size()-1) locationQueryMid += ")";
        }

        if (countrySelectedBox.size() > 0) {
            // If countrySelectedBox.size() is bigger than zero it means that there is countryBox selected,
            // then the selected countries should be 'AND' with previous selected genres.
            locationQueryMid += "\nINTERSECT\n";

            for (int i = 0; i < countrySelectedBox.size(); i++) {
                country = countrySelectedBox.get(i).getText();
                //System.out.println(genre);
                if (i == 0) locationQueryMid += "(";
                if (i > 0) {
                    locationQueryMid += (andOrOperation.equals("AND") ? "\nINTERSECT\n" : "\nUNION\n");
                }
                locationQueryMid += "SELECT MC.mid " +
                        "FROM Mcountry MC " +
                        "WHERE MC.country = '" + country + "'";
                if (i == countrySelectedBox.size()-1) locationQueryMid += ")";
            }
        }

        locationQuery += locationQueryMid + ")";
        System.out.println("Mid for Location Query: " + locationQueryMid);
        System.out.println("Location Query: " + locationQuery);

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(locationQuery);

            while(resultSet.next()) {
                location = resultSet.getString(1);
                //System.out.println(location);

                CheckBox locationBox = new CheckBox(location);
                locationBox.setStyle("-fx-font-size:20px");
                locationBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                    if (isNowSelected) {
                        locationSelectedBox.add(locationBox);
                    } else {
                        locationSelectedBox.remove(locationBox);
                    }
                });
                locationCountryContainer.getChildren().add(locationBox);
            }

            locationCountryPane.setContent(locationCountryContainer);

        } catch (SQLException sql_e) {
            System.out.println("SQLException: " + sql_e.getMessage());
            System.out.println("SQLState: " + sql_e.getSQLState());
            System.out.println("VendorError: " + sql_e.getErrorCode());
        }
    }

    @FXML
    private void setYearFrom(ActionEvent event) {
        LocalDate ldFrom = dateFrom.getValue();
        if (ldFrom == null)
            yearFrom = "";
        else
            yearFrom = Integer.toString(ldFrom.getYear());
        System.out.println("yearFrom = " + yearFrom);
    }

    @FXML
    private void setYearTo(ActionEvent event) {
        LocalDate ldTo = dateTo.getValue();
        if (ldTo == null)
            yearTo = "";
        else
            yearTo = Integer.toString(ldTo.getYear());
        System.out.println("yearTo = " + yearTo);
    }

    @FXML void updateOtherQueryMid() {

        otherQueryMid = "";
        String locationCountry = "";
        if (locationSelectedBox.size() > 0) {
            otherQueryMid += "\nINTERSECT\n";
            for (int i = 0; i < locationSelectedBox.size(); i++) {
                locationCountry = locationSelectedBox.get(i).getText();
                //System.out.println(locationCountry);
                if (i == 0) otherQueryMid += "(";
                if (i > 0) {
                    otherQueryMid += (andOrOperation.equals("AND") ? "\nINTERSECT\n" : "\nUNION\n");
                }
                otherQueryMid += "SELECT DISTINCT L.mid " +
                        "FROM Locations L " +
                        "WHERE L.location1 = '" + locationCountry + "'";
                if (i == locationSelectedBox.size()-1) otherQueryMid += ")";
            }
        }

        if ((rating.length() != 0) || (reviewNumber.length() != 0) || (yearFrom.length() != 0) || (yearTo.length() != 0)) {
            System.out.println("rating.length = " + rating.length() + ", rating = " + rating);
            System.out.println("reviewNumber.length = " + reviewNumber.length() + ", reviewNumber = " + reviewNumber);
            System.out.println("yearFrom.length = " + yearFrom.length() + ", yearFrom = " + yearFrom);
            System.out.println("yearTo.length = " + yearTo.length() + ", yearTo = " + yearTo);

            otherQueryMid += "\nINTERSECT\n";
            otherQueryMid += "(SELECT M.mid " +
                    "FROM Movies M " +
                    "WHERE ";

            String[] tmp = new String[4];
            tmp[0] = (rating.length() == 0 ? "" : "M.rtAllCriticsRating " + ratingOperation + rating + " ");
            tmp[1] = (reviewNumber.length() == 0 ? "" : "M.rtAllCriticsNumReviews " + reviewNumberOperation + reviewNumber + " ");
            tmp[2] = (yearFrom.length() == 0 ? "" : "M.year >= " + yearFrom + " ");
            tmp[3] = (yearTo.length() == 0 ? "" : "M.year <= " + yearTo + " ");

            List<String> constraintList = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                System.out.println("tmp -- " + tmp[i]);
                if (tmp[i] != "") constraintList.add(tmp[i]);
            }

            for (int i = 0; i < constraintList.size(); i++) {
                if (i > 0)
                    otherQueryMid += " AND ";
                otherQueryMid += constraintList.get(i);
            }

            otherQueryMid += ")";
        }

        System.out.println("-- otherQueryMid in updateotherQueryMid() --: " + otherQueryMid);
    }

    @FXML
    private void loadTag() {

        System.out.println(" ######### locationQueryMid #####  1  ######### = " + locationQueryMid);

        updateOtherQueryMid();

        System.out.println(" ######### locationQueryMid #####  2  ######### = " + locationQueryMid);

        tagValueContainer.getChildren().clear();
        //locationSelectedBox.clear();
        tagWeightOperation = tagWeightBox.getValue();

        String tag = "";
        tagWeightQuery = "SELECT DISTINCT T.value \n" +
                "FROM Tags T, Movietag MT \n" +
                "WHERE T.tid = MT.tid " +
                (tagWeight.length() == 0 ? "" : "AND MT.tweight " + tagWeightOperation + tagWeight + " ") +
                " AND MT.mid IN (" +
                locationQueryMid +
                otherQueryMid +
                ")";
        //((otherQueryMid.length() == 0) ? "" : "\nINTERSECT\n" + otherQueryMid) +

        System.out.println("Tag Weight Query: " + tagWeightQuery);

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(tagWeightQuery);

            while(resultSet.next()) {
                tag = resultSet.getString(1);
                System.out.println("tag -- " + tag);

                TextField tagValue = new TextField(tag);
                tagValueContainer.getChildren().add(tagValue);
            }

            tagValuePane.setContent(tagValueContainer);

        } catch (SQLException sql_e) {
            System.out.println("SQLException: " + sql_e.getMessage());
            System.out.println("SQLState: " + sql_e.getSQLState());
            System.out.println("VendorError: " + sql_e.getErrorCode());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        andOrBox.setValue("AND");
        andOrBox.setItems(andOrList);
        // add a listener
        andOrBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue observable, String oldValue, String newValue) {
                andOrOperation = newValue;
                System.out.println("andOrOperation changed to be: " + andOrOperation);
                loadCountries();
                loadCountryLocations();
                loadTag();
            }
        });

        ratingBox.setValue(">");
        ratingBox.setItems(ratingList);
        ratingBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue observable, String oldValue, String newValue) {
                ratingOperation = newValue;
                System.out.println("ratingOperation changed to be: " + ratingOperation);
                loadTag();
            }
        });

        reviewNumberBox.setValue(">");
        reviewNumberBox.setItems(reviewNumberList);
        reviewNumberBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue observable, String oldValue, String newValue) {
                reviewNumberOperation = newValue;
                System.out.println("ratingOperation changed to be: " + reviewNumberOperation);
                loadTag();
            }
        });

        tagWeightBox.setValue(">");
        tagWeightBox.setItems(tagWeightList);
        tagWeightBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue observable, String oldValue, String newValue) {
                tagWeightOperation = newValue;
                System.out.println("tagWeightOperation changed to be: " + tagWeightOperation);
                loadTag();
            }
        });


        ratingValue.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue observable,
                                String oldValue, String newValue) {

                System.out.println(" Text Changed to  " + newValue);
                rating = newValue; //Float.parseFloat(newValue);
                System.out.println("rating = " + rating);
                loadTag();
            }
        });

        reviewValue.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue observable,
                                String oldValue, String newValue) {

                System.out.println(" Text Changed to  " + newValue);
                reviewNumber = newValue; //Integer.parseInt(newValue);
                System.out.println("reviewNumber = " + reviewNumber);
                loadTag();
            }
        });

        weightValue.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue observable,
                                String oldValue, String newValue) {

                System.out.println(" Text Changed to  " + newValue);
                tagWeight = newValue; //Integer.parseInt(newValue);
                System.out.println("tagWeight = " + tagWeight);
                loadTag();
            }
        });

        /*
        dateFrom = new DatePicker();
        // Print the new date in the TextArea
        dateFrom.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                LocalDate ldFrom = dateFrom.getValue();
                yearFrom = ldFrom.getYear();
                System.out.println("yearFrom = " + yearFrom);
            }
        });

        dateTo = new DatePicker();
        dateTo.setOnAction(event -> {
                LocalDate ldTo = dateTo.getValue();
                yearTo = ldTo.getYear();
                System.out.println("yearTo = " + yearTo);
        });
        */

        // Connect to database
        connectDrv = new ConnectDriver();
        connection = connectDrv.loadDriver();
        loadGenres();


    }
}
