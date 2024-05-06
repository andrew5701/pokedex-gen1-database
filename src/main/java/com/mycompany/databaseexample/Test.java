package com.mycompany.databaseexample;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Test {

    public static void main(String[] args) {
        String directoryPath = "C:\\Users\\ak7272\\Desktop\\Leaders";
        File directory = new File(directoryPath);

        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png"));
        if (files != null) {
            for (File file : files) {
                try {
                    String leaderName = file.getName().substring(0, file.getName().lastIndexOf('.'));
                    byte[] imageData = readFileToByteArray(file);
                    insertLeaderIntoDatabase(leaderName, imageData);
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static byte[] readFileToByteArray(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] byteArray = new byte[(int) file.length()];
            fis.read(byteArray);
            return byteArray;
        }
    }

    private static void insertLeaderIntoDatabase(String leaderName, byte[] imageData) throws SQLException {
        

        try (Connection conn = ConnectionHelp.getConn();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO GymLeaders (LeaderName, LeaderImage) VALUES (?, ?)")) {

            pstmt.setString(1, leaderName);
            pstmt.setBytes(2, imageData);
            pstmt.executeUpdate();
        }
    }
}
