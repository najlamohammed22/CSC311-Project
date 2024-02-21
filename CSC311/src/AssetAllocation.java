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


public static void main(String[] args) {
    List<Asset> Assets = readAssetsFromFile("Example.txt");
    if (Assets.isEmpty()) {
        System.err.println("No assets found in the file.");
        return;
    }
    for (Asset asset : Assets) {
        System.out.println("ID: " + asset.id + ", Expected Return: " + asset.expectedReturn + ", Risk: " + asset.risk + ", Quantity: " + asset.quantity);
    }

}}






    