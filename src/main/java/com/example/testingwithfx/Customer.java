
package com.example.testingwithfx;

import javafx.scene.control.Alert;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;

class Customer extends User {
    private int productQuantity;
    private int shoppingPoints;

    public Customer(String name, String email, String customerID, String userPassword, Address address, int shoppingPoints, int productQuantity) {
        super(name, email, customerID, userPassword, address);
        this.shoppingPoints = shoppingPoints;
        this.productQuantity = productQuantity;
    }

    public Customer() {

    }


    public int getShoppingPoints() {
        return shoppingPoints;
    }

    public void setShoppingPoints(int shoppingPoints) {
        this.shoppingPoints = shoppingPoints;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    User signUp(String name, String email, String userID, String userPassword, String address, String town, String city) {
        try {
            Customer customer = new Customer(name, email, userID, userPassword, new Address(address, town, city), 0, 0);
            Address address1 = new Address(address, town, city);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("customers/" + userID + ".txt"));
            bufferedWriter.write(name + "\n");
            bufferedWriter.write(email + "\n");
            bufferedWriter.write(userID + "\n");
            bufferedWriter.write(userPassword + "\n");
            bufferedWriter.write(address1.getAddress() + "\n");
            bufferedWriter.write(address1.getTown() + "\n");
            bufferedWriter.write(address1.getCity() + "\n");
            bufferedWriter.write(shoppingPoints + "\n");
            bufferedWriter.close();
            return customer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    boolean signIn(String userID, String userPassword, String generatedCaptcha, String enteredCaptcha) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("customers/" + userID + ".txt"));
            String name = bufferedReader.readLine();
            String email = bufferedReader.readLine();
            String storedID = bufferedReader.readLine();
            String storedUserPassword = bufferedReader.readLine();
            String address = bufferedReader.readLine();
            String town = bufferedReader.readLine();
            String city = bufferedReader.readLine();
            int shoppingPoints = Integer.parseInt(bufferedReader.readLine());
            if (storedID.equals(userID) && storedUserPassword.equals(userPassword) && enteredCaptcha.equals(generatedCaptcha)) {
                System.out.println("Sign-in successful!");
                return true;
            } else {
                System.out.println("Sign-in failed!");
                return false;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    boolean updatePassword(String username,String privateAnswer, String Password) {
        File file = new File("customers/" + username + ".txt");
        String line;
        ArrayList<String> userData=new ArrayList<>();
        if(file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                while ((line=reader.readLine())!=null){
                    userData.add(line);
                }
                reader.close();
                String storedPassword=userData.get(3);
                if(! storedPassword.equals(Password)&&privateAnswer.equalsIgnoreCase(userData.get(9))){
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
                    alert.setContentText("It is the existing Password or Wrong Answer to private Question");
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
        File file=new File("customers/"+username+".txt");
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
        File file=new File("customers/"+username+".txt");
        if(file.exists()){
            try (BufferedReader reader=new BufferedReader(new FileReader(file))){
                String line;
                ArrayList <String> userData = new ArrayList<>();
                while((line=reader.readLine())!=null){
                    userData.add(line);
                }
                if(userData.size()==10){
                    return true;
                }

            }catch (IOException e){
                return false;
            }
        }
        return false;
    }


    public void addProductToCart(String productName, int productQuantity) {
        for (int i = 0; i < getInventory().size(); i++) {
            if (productName.equalsIgnoreCase(getInventory().get(i).getProductName())) {
                if (productQuantity > 0) {
                    boolean productExistsInCart = false;
                    for (Product cartProduct : getShoppingCart()) {
                        if (productName.equalsIgnoreCase(cartProduct.getProductName())) {
                            int newQuantity = cartProduct.getProductQuantity() + productQuantity;
                            cartProduct.setProductQuantity(newQuantity);
                            productExistsInCart = true;
                            break;
                        }
                    }

                    if (!productExistsInCart) {
                        Product inventoryProduct = getInventory().get(i);
                        ArrayList<Review> reviews = new ArrayList<>(inventoryProduct.getReview());

                        Product cartProduct = new Product(
                                inventoryProduct.getProductName(),
                                inventoryProduct.getProductPrice(),
                                productQuantity,
                                inventoryProduct.getCategory(),
                                inventoryProduct.getProductDescription(),
                                reviews,
                                inventoryProduct.getImageFilepath()
                        );
                        getShoppingCart().add(cartProduct);
                    }
                }
                break; // Break out of the inventory loop once the product is handled
            }
        }
    }


    public boolean removeProductFromCart(String productname) {
        for (int i = 0; i < getShoppingCart().size(); i++) {
            if (productname.equalsIgnoreCase(getShoppingCart().get(i).getProductName())) {
                getShoppingCart().remove(i);
                System.out.println("This product has been removed!");
                return true;
            }
        }
        System.out.println("Product not found in the cart.");
        return false;
    }


    public double calculateBill(ArrayList<Product> shoppingCart) {
        double sum = 0;
        for (Product product : getShoppingCart()) {
            double productPrice = product.getProductPrice();
            int productQuantity = product.getProductQuantity();
            double totalPriceForProduct = productPrice * productQuantity;
            sum = sum + totalPriceForProduct;

            System.out.println(product.getProductName() + " cost: Rs. "
                    + totalPriceForProduct + " /- (Quantity: " + productQuantity + ")");
        }
        System.out.println("Total Bill: Rs. " + sum + " /-");
        return sum;
    }


    public double discountPrice(String username, double totalBill) {
        String filePath = "customers/" + username + ".txt";
        ArrayList<String> userData = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                userData.add(line);
            }

            if (userData.size() < 8) {
                throw new RuntimeException("Invalid file format for user data.");
            }

            int shoppingPoints = Integer.parseInt(userData.get(7));
            setShoppingPoints(shoppingPoints);
            double discountValue = shoppingPoints * 10;

            double discountedPrice = totalBill - discountValue;

            if (discountedPrice < 0) {
                int leftoverPoints = (int) Math.abs(discountedPrice) / 10;
                userData.set(7, String.valueOf(leftoverPoints));
                discountedPrice = 0;
            } else {
                userData.set(7, "0");
            }

            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath))) {
                for (String data : userData) {
                    bufferedWriter.write(data);
                    bufferedWriter.newLine();
                }
            }

            return discountedPrice;

        } catch (FileNotFoundException e) {
            throw new RuntimeException("User file not found: " + username, e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading or writing user file.", e);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid shopping points format in file.", e);
        }
    }


    public void addReview(String username, String productName, double reviewRating, String reviewText) {
        try {

            Review review = new Review(username, reviewRating, reviewText);
            Product product = new Product();
            product.getReview().add(review);

            // Write review details to the file
            BufferedWriter reviewWriter = new BufferedWriter(new FileWriter("reviews//" + productName + "Review.txt", true));
            reviewWriter.write(username + "\n");
            reviewWriter.write(reviewRating + "\n");
            reviewWriter.write(reviewText + "\n");
            reviewWriter.close();

            System.out.println("Review saved to file and list.");
        } catch (IOException e) {
            System.out.println("Error saving the review: " + e.getMessage());
        }
    }


    public ArrayList<Review> loadReviewsFromFile(String productName) {
        ArrayList<Review> reviews = new ArrayList<>();

        try {
            BufferedReader reviewReader = new BufferedReader(new FileReader("reviews//" + productName + "Review.txt"));
            String line;

            while (true) {
                String username = reviewReader.readLine();  // Line 1: username
                if (username == null || username.isBlank()) break;

                String ratingLine = reviewReader.readLine();
                if (ratingLine == null || ratingLine.isBlank()) break;

                double reviewRating;
                try {
                    reviewRating = Double.parseDouble(ratingLine);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid review rating format: " + ratingLine);
                    continue;
                }

                String reviewText = reviewReader.readLine();
                if (reviewText == null || reviewText.isBlank()) break;

                Review review = new Review(username, reviewRating, reviewText);

                reviews.add(review);
            }

            reviewReader.close();
            System.out.println("Reviews loaded from file.");
        } catch (FileNotFoundException e) {
            System.out.println("Review file not found for product: " + productName);
        } catch (IOException e) {
            System.out.println("Error reading the review file for product: " + productName);
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }

        return reviews;
    }

    public void deleteReview(String productName, String username) {
        ArrayList<Review> reviews = loadReviewsFromFile(productName);
        int indexToRemove = -1;

        for (int i = 0; i < reviews.size(); i++) {
            if (reviews.get(i).getReviewerName().equals(username)) {
                indexToRemove = i;
                break;
            }
        }

        if (indexToRemove == -1) {
            System.out.println("No review found for user: " + username);
            return;
        }
        reviews.remove(indexToRemove);

        try {
            BufferedWriter reviewWriter = new BufferedWriter(new FileWriter("reviews//" + productName + "Review.txt"));
            for (Review review : reviews) {
                reviewWriter.write(review.getReviewerName() + "\n");
                reviewWriter.write(review.getReviewRating() + "\n");
                reviewWriter.write(review.getReviewText() + "\n");
            }
            reviewWriter.close();

            System.out.println("Review for user " + username + " deleted successfully.");
        } catch (IOException e) {
            System.out.println("Error saving the updated review file: " + e.getMessage());
        }
    }


    public boolean stockSubtraction() {
        boolean success = true;
        for (int i = 0; i < getShoppingCart().size(); i++) {
            String productName = getShoppingCart().get(i).getProductName();
            int quantityPurchased = getShoppingCart().get(i).getProductQuantity();

            try (BufferedReader bufferedReader = new BufferedReader(new FileReader("inventory/" + productName + ".txt"))) {

                String name = bufferedReader.readLine();
                String price = bufferedReader.readLine();
                String stockLine = bufferedReader.readLine();
                String category = bufferedReader.readLine();
                String description = bufferedReader.readLine();
                String imageFilepath = bufferedReader.readLine();

                int availableStock = Integer.parseInt(stockLine.trim());

                if (quantityPurchased <= availableStock) {
                    // Subtract stock
                    int remainingStock = availableStock - quantityPurchased;

                    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("inventory/" + productName + ".txt"))) {
                        bufferedWriter.write(name + "\n");
                        bufferedWriter.write(price + "\n");
                        bufferedWriter.write(Integer.toString(remainingStock) + "\n");
                        bufferedWriter.write(category + "\n");
                        bufferedWriter.write(description + "\n");
                        bufferedWriter.write(imageFilepath + "\n");
                    }

                    System.out.println("Purchase successful for " + productName + ". Remaining stock: " + remainingStock);
                } else {
                    System.out.println("Insufficient stock for " + productName + ". Available: " + availableStock);
                    success = false;
                }
            } catch (IOException | NumberFormatException e) {
                System.out.println("An error occurred during processing of " + productName + ": " + e.getMessage());
                e.printStackTrace();
                success = false;
            }
        }

        if (success) {
            System.out.println("Payment processed successfully.");
        }

        return success;
    }


    public void convertBilltoPoints(String username) {
        Customer customer = new Customer();
        double bill = calculateBill(getShoppingCart());
        int newShoppingPoints = (int) (bill / 100);
        ArrayList<String> userData = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("customers/" + username + ".txt"))) {
            for (int i = 0; i < 7; i++) {
                userData.add(bufferedReader.readLine());
            }
            int currentPoints = Integer.parseInt(bufferedReader.readLine());
            int updatedPoints = currentPoints + newShoppingPoints;
            userData.add(String.valueOf(updatedPoints));

            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("customers/" + username + ".txt"))) {
                for (String data : userData) {
                    bufferedWriter.write(data);
                    bufferedWriter.newLine();
                }
            }
            System.out.println("Shopping points updated successfully.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error updating shopping points: " + e.getMessage());
        }
    }

    private int orderNo = 1;

    public ArrayList<Orders> createOrder(String username) {
        ArrayList<Orders> order = new ArrayList<>();
        try {
            File orderFile = new File("orders/" + username + ".txt");
            if (orderFile.exists()) {
                int lastOrderNo = getLastOrderNumberFromFile(orderFile);
                orderNo = lastOrderNo + 1;
            } else {
                orderNo = 1;
            }

            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(orderFile, true))) {
                double totalBeforeDiscount = 0;

                bufferedWriter.write("Order No: " + orderNo + "\n");

                // Process each product in the shopping cart
                for (Product product : getShoppingCart()) {
                    String name = product.getProductName();
                    int quantity = product.getProductQuantity();
                    double price = product.getProductPrice();
                    Orders orderedProduct = new Orders(name, quantity, price * quantity, 0);  // Discount will be applied later
                    order.add(orderedProduct);
                    bufferedWriter.write(String.format("Product: %s, Quantity: %d, Subtotal before discount: %.2f%n",
                            name, quantity, price * quantity));
                    totalBeforeDiscount = totalBeforeDiscount + price * quantity;
                }
                CustomerMainPage customerMainPage = new CustomerMainPage();
                double discountApplied = customerMainPage.ReturnDiscountedPrice(username, totalBeforeDiscount);
                bufferedWriter.write(String.format("Total before discount: %.2f%n", totalBeforeDiscount));
                bufferedWriter.write(String.format("Total after discount: %.2f%n%n", discountApplied));


            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return order;
    }

    private int getLastOrderNumberFromFile(File orderFile) throws IOException {
        int lastOrderNo = 0;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(orderFile))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("Order No: ")) {
                    lastOrderNo = Integer.parseInt(line.substring(10).trim());
                }
            }
        }
        return lastOrderNo;
    }

    public String viewOrderHistory(String username) {
        File orderFile = new File("orders/" + username + ".txt");
        StringBuilder orderHistory = new StringBuilder();
        if (orderFile.exists()) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(orderFile))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    orderHistory.append(line).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error reading order history.";
            }
        } else {
            return "No order history available for this user.";
        }
        return orderHistory.toString();
    }

    public void deleteCustomerProfile(String username) {
        String filePath = "customers/" + username + ".txt";
        File file = new File(filePath);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Profile for " + username + " deleted successfully.");
            } else {
                System.out.println("Failed to delete the profile for " + username + ".");
            }
        } else {
            System.out.println("Profile for " + username + " does not exist.");
        }
    }


    public void changeAddress(String username, String addressNo, String town, String city) {
        // Define the file path
        String filePath = "customers/" + username + ".txt";
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("Profile for " + username + " does not exist.");
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String name = reader.readLine();
            String email = reader.readLine();
            String username1 = reader.readLine();
            String password = reader.readLine();
            String currentAddressNo = reader.readLine();
            String currentTown = reader.readLine();
            String currentCity = reader.readLine();
            String otherData = reader.readLine();
            reader.close();
            currentAddressNo = addressNo;
            currentTown = town;
            currentCity = city;
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(name + "\n");
            writer.write(email + "\n");
            writer.write(username1 + "\n");
            writer.write(password + "\n");
            writer.write(currentAddressNo + "\n");
            writer.write(currentTown + "\n");
            writer.write(currentCity + "\n");
            writer.write(otherData + "\n");
            writer.close();

            System.out.println("Address updated successfully for user: " + username);
        } catch (IOException e) {
            System.out.println("An error occurred while updating the address: " + e.getMessage());
        }
    }



}





