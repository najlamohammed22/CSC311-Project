import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AssetAllocation {

    static class Asset {
        String id;
        double expectedReturn;
        double risk;
        int quantity;

        public Asset(String id, double expectedReturn, double risk, int quantity) {
            this.id = id;
            this.expectedReturn = expectedReturn;
            this.risk = risk;
            this.quantity = quantity;
        }
    }

    // Method to read assets from the file and return a list of Asset objects
    private static List<Asset> readAssetsFromFile(String fileName) {
        List<Asset> assets = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" : ");

                if (parts.length == 4) {
                    String id = parts[0];
                    double expectedReturn = Double.parseDouble(parts[1]);
                    double risk = Double.parseDouble(parts[2]);
                    int quantity = Integer.parseInt(parts[3]);
                    assets.add(new Asset(id, expectedReturn, risk, quantity));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return assets;
    }

    // Method to read total investment from the file and return the value as a double
    private static double readTotalInvestment(String fileName) {
        double totalInvestment = 0.0;

        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" : ");

                if (parts.length == 13 && parts[13].equalsIgnoreCase("Total Investment")) {
                    totalInvestment = Double.parseDouble(parts[3]);
                    break; // No need to continue reading once found
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return totalInvestment;
    }

    // Method to read risk tolerance level from the file and return the value as a double
    private static double readRiskToleranceLevel(String fileName) {
        double riskToleranceLevel = 0.0;

        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" \n ");

                if (parts.length == 4 ) {
                    String[] space = line.split(" ");

                    for (int i=0 ; i<space.length;i++){

                        //if (Character.isDigit(space[i]))
                    }
                    riskToleranceLevel = Double.parseDouble(parts[4]);
                    break; // No need to continue reading once found
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return riskToleranceLevel;
    }

    public static void main(String[] args) {
        // Read assets from the file
        List<Asset> assets = readAssetsFromFile("Example.txt");

        // Print assets
        if (assets.isEmpty()) {
            System.err.println("No assets found in the file.");
            return;
        }
        for (Asset asset : assets) {
            System.out.println("ID: " + asset.id + ", Expected Return: " + asset.expectedReturn + ", Risk: " + asset.risk + ", Quantity: " + asset.quantity);
        }

        // Read total investment and risk tolerance level from the file
        double totalInvestment = readTotalInvestment("Example.txt");
        double riskToleranceLevel = readRiskToleranceLevel("Example.txt");

        // Print total investment and risk tolerance level
        System.out.println("Total Investment: " + totalInvestment);
        System.out.println("Risk Tolerance Level: " + riskToleranceLevel);
    }
}





    
