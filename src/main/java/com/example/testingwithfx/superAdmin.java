package com.example.testingwithfx;

import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class superAdmin extends User {



    public superAdmin(String name, String email, String userID, String userPassword, Address address) {
        super(name, email, userID, userPassword, address);

    }

    public superAdmin() {
    }

    @Override
    User signUp(String name, String email, String userID, String userPassword, String address, String town, String city) {
        try {
            Address address1 = new Address(address, town, city);
            superAdmin superAdmin = new superAdmin(name, email, userID, userPassword, address1);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("admins/" + userID + ".txt"));
            bufferedWriter.write(superAdmin.getName() + "\n");
            bufferedWriter.write(superAdmin.getEmail() + "\n");
            bufferedWriter.write(superAdmin.getUserID() + "\n");
            bufferedWriter.write(superAdmin.getUserPassword() + "\n");
            bufferedWriter.write(address1.getAddress() + "\n");
            bufferedWriter.write(address1.getTown() + "\n");
            bufferedWriter.write(address1.getCity() + "\n");
            String adminRank = AdminRank.RANK_1.toString();
            bufferedWriter.write(adminRank + "\n");
            bufferedWriter.write(String.valueOf(AdminRank.RANK_1.getAdminSalary()) + "\n");
            bufferedWriter.close();
            return superAdmin;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    boolean signIn(String userID, String userPassword, String useless1, String useless2) {

        try (BufferedReader reader = new BufferedReader(new FileReader("superadmin/" + userID + ".txt"))) {
            String storedUserID = reader.readLine();
            String storedPassword = reader.readLine();
            // Compare password and rank
            if (storedUserID.equals(userID) && storedPassword.equals(userPassword) ) {
                System.out.println("Sign-in successful!");
                return true;
            } else {

                return false;
            }
        } catch (IOException e) {
            System.out.println("Error reading user data: " + e.getMessage());
            return false;
        }
    }

    @Override
    boolean updatePassword(String username,String privateAnswer, String Password) {
        File file = new File("superadmin/" + username + ".txt");
        String line;
        ArrayList<String> userData=new ArrayList<String>();
        if(file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                while((line=reader.readLine())!=null){
                    userData.add(line);
                }

                reader.close();

                if(! userData.get(1).equals(Password)&&privateAnswer.equalsIgnoreCase(userData.get(3))){
                    userData.set(1,Password);
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                   for(String data:userData){
                       writer.write(data);
                       writer.newLine();
                   }
                   writer.close();
                    return true;
                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("It is Existing Password or Wrong Answer to Question");
                    alert.showAndWait();
                    return false;
                }
            } catch (IOException e) {
                System.out.println("Error reading user data: " + e.getMessage());
                return false;
            }

        }else{
            return false;
        }
    }
    @Override
    boolean savePrivateQusetion(String username,String question, String answer) {
        File file=new File("superadmin/"+username+".txt");
        String line;
        ArrayList<String> userData=new ArrayList<>();
        if(file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                while ((line=reader.readLine())!=null){
                    userData.add(line);
                }
                reader.close();
                userData.add(question);
                userData.add(answer);

                BufferedWriter writer=new BufferedWriter(new FileWriter(file));
                for(String data:userData){
                    writer.write(data);
                    writer.newLine();
                }
                writer.close();
                return true;
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("User Does not exist or File has been moved");
                alert.showAndWait();
                return false;
            }
        }
        return false;
    }
    @Override
    boolean checkIfPrivateQuestionExists(String username) {
        File file=new File("superadmin/"+username+".txt");
        if(file.exists()){
            try (BufferedReader reader=new BufferedReader(new FileReader(file))){
                String line;
                ArrayList <String> userData = new ArrayList<>();
                while((line=reader.readLine())!=null){
                    userData.add(line);
                }
                if(userData.size()==4){
                    return true;
                }

            }catch (IOException e){
                return false;
            }
        }
        return false;
    }

    public boolean fireAdmin(String username) {
        String filePath = "admins/"+ username + ".txt";

        if (Files.exists(Path.of(filePath))) {
            System.out.println("File exists");
            try {
                // Try deleting the file
                Files.delete(Path.of(filePath));
                System.out.println("File deleted successfully: " + filePath);
                return true;
            } catch (IOException e) {
                // Handle the exception (log it, rethrow, etc.)
                System.out.println("Error deleting file: " + e.getMessage());
                return false;
            }
        } else {
            System.out.println("File does not exist: " + filePath);
            return false;
        }
    }



    public void updateAdminRank(String username, AdminRank rank1) {
        String filePath = "admins\\" + username + ".txt";

        ArrayList<String> userData = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                userData.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading user data: " + e.getMessage());
            return;
        }

        if (userData.size() < 8) {
            System.err.println("File format is invalid for user: " + username);
            return;
        }

        String currentRank = userData.get(7);
        if (currentRank.equals(rank1.name())) {
            System.out.println("Rank is already set to the specified value. No update needed.");
            return;
        }
        userData.set(7, rank1.name());
        userData.set(8, Double.toString(rank1.getAdminSalary()));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String data : userData) {
                writer.write(data);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing updated user data: " + e.getMessage());
        }
    }
}
