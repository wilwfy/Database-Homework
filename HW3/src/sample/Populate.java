package sample;

import oracle.jdbc.proxy.annotation.Pre;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Populate {

    private String moviesFilePath = ".\\src\\sample\\hetrec2011-movielens-2k-v2\\movies.dat";
    private String countryFilePath = ".\\src\\sample\\hetrec2011-movielens-2k-v2\\movie_countries.dat";
    private String genresFilePath = ".\\src\\sample\\hetrec2011-movielens-2k-v2\\movie_genres.dat";
    private String locationFilePath = ".\\src\\sample\\hetrec2011-movielens-2k-v2\\movie_locations.dat";
    private String tagFilePath = ".\\src\\sample\\hetrec2011-movielens-2k-v2\\tags.dat";
    private String movietagFilePath = ".\\src\\sample\\hetrec2011-movielens-2k-v2\\movie_tags.dat";

    Connection connection = null;

    public Populate(Connection cnct) {
        connection = cnct;
    }


    public void populateMovies() {
        int mid;
        String title;
        //int imdbID;
        //String spanishTitle;
        //String imdbPictureURL;
        int year;
        String rtID;
        float rtAllCriticsRating;
        int rtAllCriticsNumReviews;
        int rtAllCriticsNumFresh;
        int rtAllCriticsNumRotten;
        int rtAllCriticsScore;
        float rtTopCriticsRating;
        int rtTopCriticsNumReviews;
        int rtTopCriticsNumFresh;
        int rtTopCriticsNumRotten;
        int rtTopCriticsScore;
        float rtAudienceRating;
        int rtAudienceNumRatings;
        int rtAudienceScore;
        //String rtPictureURL;

        BufferedReader reader;
        String insertInfo = "INSERT INTO Movies(mid, title, year, rtID, " +
                            "rtAllCriticsRating, rtAllCriticsNumReviews, rtAllCriticsNumFresh, rtAllCriticsNumRotten, rtAllCriticsScore, " +
                            "rtTopCriticsRating, rtTopCriticsNumReviews, rtTopCriticsNumFresh, rtTopCriticsNumRotten, rtTopCriticsScore, " +
                            "rtAudienceRating, rtAudienceNumRatings, rtAudienceScore) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement updateTags = connection.prepareStatement(insertInfo);
            reader = new BufferedReader(new FileReader(moviesFilePath));
            String line = reader.readLine(); // This is the first row of column titles
            int line_cnt = 1;
            while ((line = reader.readLine()) != null) {
                //System.out.println("line_cnt = " + line_cnt);
                String[] info = line.trim().split("\t");
                mid = Integer.parseInt(info[0]);
                title = info[1];
                //int imdbID;
                //String spanishTitle;
                //String imdbPictureURL;
                year = Integer.parseInt(info[5]);
                rtID = info[6];
                rtAllCriticsRating = Float.parseFloat(info[7]);
                rtAllCriticsNumReviews = Integer.parseInt(info[8]);
                rtAllCriticsNumFresh = Integer.parseInt(info[9]);
                rtAllCriticsNumRotten = Integer.parseInt(info[10]);
                rtAllCriticsScore = Integer.parseInt(info[11]);
                rtTopCriticsRating = Float.parseFloat(info[12]);
                rtTopCriticsNumReviews = Integer.parseInt(info[13]);
                rtTopCriticsNumFresh = Integer.parseInt(info[14]);
                rtTopCriticsNumRotten = Integer.parseInt(info[15]);
                rtTopCriticsScore = Integer.parseInt(info[16]);
                rtAudienceRating = Float.parseFloat(info[17]);
                rtAudienceNumRatings = Integer.parseInt(info[18]);
                rtAudienceScore = Integer.parseInt(info[19]);
                //String rtPictureURL;

                //System.out.println("mid = " + mid);
                //System.out.println("country = " + country);

                updateTags.setInt(1, mid);
                updateTags.setString(2, title);
		        //int imdbID;
                //String spanishTitle;
                //String imdbPictureURL;
                updateTags.setInt(3, year);
                updateTags.setString(4, rtID);
                updateTags.setFloat(5, rtAllCriticsRating);
                updateTags.setInt(6, rtAllCriticsNumReviews);
                updateTags.setInt(7, rtAllCriticsNumFresh);
                updateTags.setInt(8, rtAllCriticsNumRotten);
                updateTags.setInt(9, rtAllCriticsScore);
                updateTags.setFloat(10, rtTopCriticsRating);
                updateTags.setInt(11, rtTopCriticsNumReviews);
                updateTags.setInt(12, rtTopCriticsNumFresh);
                updateTags.setInt(13, rtTopCriticsNumRotten);
                updateTags.setInt(14, rtTopCriticsScore);
                updateTags.setFloat(15, rtAudienceRating);
                updateTags.setInt(16, rtAudienceNumRatings);
                updateTags.setInt(17, rtAudienceScore);
                //String rtPictureURL;

                updateTags.executeUpdate();
                line_cnt++;
            }
            reader.close();
        } catch (IOException io_e) {
            io_e.printStackTrace();
        } catch (SQLException sql_e) {
            System.out.println("SQLException in populateMovies() ----");
            System.out.println("SQLException: " + sql_e.getMessage());
            System.out.println("SQLState: " + sql_e.getSQLState());
            System.out.println("VendorError: " + sql_e.getErrorCode());
        }
    }

    public void populateMovieCountry() {
        int mid;
        String country;
        BufferedReader reader;
        String insertInfo = "INSERT INTO Mcountry(mid, country) VALUES (?, ?)";

        try {
            PreparedStatement updateTags = connection.prepareStatement(insertInfo);
            reader = new BufferedReader(new FileReader(countryFilePath));
            String line = reader.readLine(); // This is the first row of column titles
            int line_cnt = 1;
            while ((line = reader.readLine()) != null) {
                //System.out.println("line_cnt = " + line_cnt);
                String[] info = line.trim().split("\t");
                if (info.length < 2) continue;
                mid = Integer.parseInt(info[0]);
                country = info[1];

                //System.out.println("mid = " + mid);
                //System.out.println("country = " + country);

                updateTags.setInt(1, mid);
                updateTags.setString(2, country);
                updateTags.executeUpdate();
                line_cnt++;
            }
            reader.close();
        } catch (IOException io_e) {
            io_e.printStackTrace();
        } catch (SQLException sql_e) {
            System.out.println("SQLException in populateMovieCountry() ----");
            System.out.println("SQLException: " + sql_e.getMessage());
            System.out.println("SQLState: " + sql_e.getSQLState());
            System.out.println("VendorError: " + sql_e.getErrorCode());
        }
    }

    public void populateGenres() {
        int mid;
        String genre;
        BufferedReader reader;
        String insertInfo = "INSERT INTO Genres(mid, genre) VALUES (?, ?)";

        try {
            PreparedStatement updateTags = connection.prepareStatement(insertInfo);
            reader = new BufferedReader(new FileReader(genresFilePath));
            String line = reader.readLine(); // This is the first row of column titles
            int line_cnt = 1;
            while ((line = reader.readLine()) != null) {
                String[] info = line.trim().split("\t");
                mid = Integer.parseInt(info[0]);
                genre = info[1];
                //System.out.println("line_cnt = " + line_cnt);
                //System.out.println("mid = " + mid);
                //System.out.println("genre = " + genre);

                updateTags.setInt(1, mid);
                updateTags.setString(2, genre);
                updateTags.executeUpdate();
                line_cnt++;
            }
            reader.close();
        } catch (IOException io_e) {
            io_e.printStackTrace();
        } catch (SQLException sql_e) {
            System.out.println("SQLException in populateGenres() ----");
            System.out.println("SQLException: " + sql_e.getMessage());
            System.out.println("SQLState: " + sql_e.getSQLState());
            System.out.println("VendorError: " + sql_e.getErrorCode());
        }
    }

    public void populateLocations() {
        int lid = 0;
        int mid = 0;
        String[] locations = {"", "", "", ""};
        BufferedReader reader;
        String insertInfo = "INSERT INTO Locations(lid, mid, location1, location2, location3, location4) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement updateLocations = connection.prepareStatement(insertInfo);
            reader = new BufferedReader(new FileReader(locationFilePath));
            String line = reader.readLine(); // This is the first row of column titles
            int line_cnt = 1;
            while ((line = reader.readLine()) != null) {
                String[] info = line.trim().split("\t");
                if (info.length < 2) continue;

                lid = line_cnt;
                mid = Integer.parseInt(info[0]);
                locations[0] = "";
                locations[1] = "";
                locations[2] = "";
                locations[3] = "";
                for (int i = 1; i < info.length; i++) {
                    locations[i-1] = info[i];
                }
                updateLocations.setInt(1, lid);
                updateLocations.setInt(2, mid);
                updateLocations.setString(3, locations[0]);
                updateLocations.setString(4, locations[1]);
                updateLocations.setString(5, locations[2]);
                updateLocations.setString(6, locations[3]);
                updateLocations.executeUpdate();

                line_cnt++;
            }
            reader.close();
        } catch (IOException io_e) {
            io_e.printStackTrace();
        } catch (SQLException sql_e) {
            System.out.println("SQLException in populateLocations() ----");
            System.out.println("SQLException: " + sql_e.getMessage());
            System.out.println("SQLState: " + sql_e.getSQLState());
            System.out.println("VendorError: " + sql_e.getErrorCode());
        }
    }

    public void populateTags () {
        int tid;
        String value;
        BufferedReader reader;
        String insertInfo = "INSERT INTO Tags(tid, value) VALUES (?, ?)";

        try {
            PreparedStatement updateTags = connection.prepareStatement(insertInfo);
            reader = new BufferedReader(new FileReader(tagFilePath));
            String line = reader.readLine(); // This is the first row of column titles
            int line_cnt = 1;
            while ((line = reader.readLine()) != null) {
                String[] info = line.trim().split("\t");
                tid = Integer.parseInt(info[0]);
                value = info[1];
                //System.out.println("line_cnt = " + line_cnt);
                //System.out.println("tid = " + tid);
                //System.out.println("value = " + value);

                updateTags.setInt(1, tid);
                updateTags.setString(2, value);
                updateTags.executeUpdate();
                line_cnt++;
            }
            reader.close();
        } catch (IOException io_e) {
            io_e.printStackTrace();
        } catch (SQLException sql_e) {
            System.out.println("SQLException in populateTags() ----");
            System.out.println("SQLException: " + sql_e.getMessage());
            System.out.println("SQLState: " + sql_e.getSQLState());
            System.out.println("VendorError: " + sql_e.getErrorCode());
        }
    }

    public void populateMovietag () {
        int mid = 0;
        int tid = 0;
        int tweight = 0;
        BufferedReader reader;
        String insertInfo = "INSERT INTO Movietag(mid, tid, tweight) VALUES (?, ?, ?)";
        //String insertInfo = "INSERT INTO Movietag() VALUES (?);";

        try {
            PreparedStatement updateMovies = connection.prepareStatement(insertInfo);
            reader = new BufferedReader(new FileReader(movietagFilePath));
            String line = reader.readLine(); // This is the first row of column titles
            int line_cnt = 1;
            while((line = reader.readLine()) != null) {
                String[] info = line.trim().split("\t");
                mid = Integer.parseInt(info[0]);
                tid = Integer.parseInt(info[1]);
                tweight = Integer.parseInt(info[2]);
                //System.out.println("line_cnt = " + line_cnt);
                //System.out.println("mid = " + mid);
                //System.out.println("tid = " + tid);
                //System.out.println("tweight = " + tweight);

                updateMovies.setInt(1, mid);
                updateMovies.setInt(2, tid);
                updateMovies.setInt(3, tweight);
                updateMovies.executeUpdate();
                line_cnt++;
            }
            reader.close();
        } catch (IOException io_e) {
            io_e.printStackTrace();
        } catch (SQLException sql_e) {
            System.out.println("SQLException in populateMovietag() ----");
            System.out.println("SQLException: " + sql_e.getMessage());
            System.out.println("SQLState: " + sql_e.getSQLState());
            System.out.println("VendorError: " + sql_e.getErrorCode());
        }
    }
}
