package com.example.testingwithfx;

import javafx.scene.control.Alert;

import java.io.*;
import java.util.ArrayList;

class Admin extends User {

    private AdminRank adminRank;

    public AdminRank getAdminRank() {
        return adminRank;
    }


    Admin() {
    }


    public Admin(String name, String email, String userID, String userPassword, Address address, AdminRank adminRank) {
        super(name, email, userID, userPassword, address);
        this.adminRank = adminRank;
    }

    @Override
    User signUp(String name, String email, String userID, String userPassword, String address, String town, String city) {
        Admin admin = new Admin();
        return admin;
    }

    @Override
    boolean signIn(String userID, String userPassword, String email, String adminRank) {

        try (BufferedReader reader = new BufferedReader(new FileReader("admins//" + userID + ".txt"))) {
            String storedName = reader.readLine();
            String storedEmail = reader.readLine();
            String storedUserID = reader.readLine();
            String storedPassword = reader.readLine();
            String storedAddress = reader.readLine();
            String storedTown = reader.readLine();
            String storedCity = reader.readLine();
            String storedRank = reader.readLine();

            // Compare password and rank
            if (storedUserID.equals(userID) && storedPassword.equals(userPassword) && storedEmail.equals(email) && storedRank.equals(adminRank)) {
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
        File file = new File("admins/" + username + ".txt");
        String line;
        ArrayList<String> userData=new ArrayList<>();
        if(file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                while ((line=reader.readLine())!=null){
                    userData.add(line);
                }
                reader.close();
                String storedPassword=userData.get(3);
                if(! storedPassword.equals(Password)&&privateAnswer.equalsIgnoreCase(userData.get(10))){
                    userData.set(3,Password);
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
                    alert.setContentText("It is the existing Password or Wrong Answer of Private Question");
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
        File file=new File("admins/"+username+".txt");
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

            }catch (IOException e) {
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
        File file=new File("admins/"+username+".txt");
        if(file.exists()){
            try (BufferedReader reader=new BufferedReader(new FileReader(file))){
                    String line;
                    ArrayList <String> userData = new ArrayList<>();
                    while((line=reader.readLine())!=null){
                        userData.add(line);
                }
                    if(userData.size()==11){
                        return true;
                    }

            }catch (IOException e){
                return false;
            }
        }
        return false;
    }

    public void loadInventoryFromFiles() {
        File directory = new File("inventory");
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".txt")) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        ArrayList<String> fileContent = new ArrayList<>();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            fileContent.add(line);
                        }
                        String productName = fileContent.get(0);
                        double price = Double.parseDouble(fileContent.get(1));
                        int stock = Integer.parseInt(fileContent.get(2));
                        String category = fileContent.get(3);
                        String productDescription = fileContent.get(4);
                        String imageFilepath = fileContent.get(5);
                        Product product = new Product(productName, price, stock, Category.valueOf(category), productDescription, imageFilepath);
                        getInventory().add(product);
                    } catch (IOException | NumberFormatException e) {
                        throw new RuntimeException();
                    }
                }
            }
        } else {
            System.out.println("The inventory directory is empty or doesn't exist.");
        }
    }

    public void addProductToInventory(String productName, double price, int stock, Category category, String productDescription, String imageFilepath) {
        File productFile = new File("inventory/" + productName + ".txt");
        if (productFile.exists()) {
            try {
                throw new IOException("com.example.testingwithfx.Product file already exists. Cannot add the product.");
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return;
            }
        }

        Product product = new Product(productName, price, stock, category, productDescription, imageFilepath);
        getInventory().add(product);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("inventory/" + productName + ".txt"))) {
            writer.write(productName + "\n");
            writer.write(price + "\n");
            writer.write(stock + "\n");
            writer.write(category + "\n");
            writer.write(productDescription + "\n");
            writer.write(imageFilepath + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving the product: " + e.getMessage());
        }
    }

    public void removeProductFromInventory(String productName) throws FileNotFoundException {
        boolean found = searchProductInInventory(productName);

        if (found) {
            File productFile = new File("inventory/" + productName + ".txt");
            if (productFile.exists()) {
                boolean fileDeleted = productFile.delete();
                if (fileDeleted) {
                    System.out.println("Product file deleted successfully.");
                    Product productToRemove = null;
                    for (Product product : User.getInventory()) {
                        if (product.getProductName().equalsIgnoreCase(productName)) {
                            productToRemove = product;
                            break;
                        }
                    }
                    if (productToRemove != null) {
                        User.getInventory().remove(productToRemove);
                        System.out.println("Product removed from inventory.");
                    } else {
                        System.out.println("Product not found in inventory list.");
                    }
                } else {
                    System.out.println("Failed to delete the product file.");
                }
            } else {
                throw new FileNotFoundException("Product file " + productName + ".txt not found. Unable to delete.");
            }
        } else {
            throw new FileNotFoundException("Product " + productName + " not found in inventory. Unable to delete.");
        }
    }


    public boolean searchProductInInventory(String productName) {
        for (Product product : getInventory()) {
            if (productName.equalsIgnoreCase(product.getProductName())) {
                return true;
            }
        }
        return false;
    }


    public void updateProductInInventory(String productName, int field, String newValue) {
        String filePath = "inventory//" + productName + ".txt";
        ArrayList<String> fileContent = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }

            if (!fileContent.isEmpty() && fileContent.get(0).equalsIgnoreCase(productName)) {
                // Update file content based on the field
                switch (field) {
                    case 1: // Update Price
                        fileContent.set(1, newValue);
                        break;
                    case 2: // Update Category
                        fileContent.set(3, newValue);
                        break;
                    case 3: // Update Description
                        fileContent.set(4, newValue);
                        break;
                    case 4: // Update Image File Path
                        fileContent.set(5, newValue);
                        break;
                    default:
                        System.out.println("Invalid field selection.");
                        return;
                }

                // Write updated content back to file
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                    for (String updatedLine : fileContent) {
                        writer.write(updatedLine);
                        writer.newLine();
                    }
                    System.out.println("Product updated successfully.");
                }

                // Update in-memory representation of the product
                for (Product product : User.getInventory()) {
                    if (product.getProductName().equalsIgnoreCase(productName)) {
                        switch (field) {
                            case 1: // Update Price
                                product.setProductPrice(Double.parseDouble(newValue));
                                break;
                            case 2: // Update Category
                                product.setCategory(Category.valueOf(newValue));
                                break;
                            case 3: // Update Description
                                product.setProductDescription(newValue);
                                break;
                            case 4: // Update Image File Path
                                product.setImageFilepath(newValue);
                                break;
                        }
                        break;
                    }
                }
            } else {
                System.out.println("Product not found in file.");
            }

        } catch (IOException e) {
            System.out.println("Error updating the product: " + e.getMessage());
        }
    }



    public void restock(String productName, int newStock) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("inventory/" + productName + ".txt"))) {
            String name = bufferedReader.readLine();
            double price = Double.parseDouble(bufferedReader.readLine());
            int stock = Integer.parseInt(bufferedReader.readLine());
            String category = bufferedReader.readLine();
            String productDescription = bufferedReader.readLine();
            String imageFilepath = bufferedReader.readLine();

            stock = stock + newStock; // Update stock

            for (Product product : getInventory()) {
                if (productName.equalsIgnoreCase(product.getProductName())) {
                    product.setProductStock(stock);
                }
            }

            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("inventory//" + productName + ".txt"))) {
                bufferedWriter.write(name + "\n");
                bufferedWriter.write(price + "\n");
                bufferedWriter.write(stock + "\n");
                bufferedWriter.write(category + "\n");
                bufferedWriter.write(productDescription + "\n");
                bufferedWriter.write(imageFilepath + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error updating stock.");
        }
    }


}
