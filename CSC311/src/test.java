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

    private static double readTotalInvestment(String fileName) {
        double totalInvestment = 0.0;

        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("Total investment")) {
                    totalInvestment = Double.parseDouble(line.split(" ")[3]);
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return totalInvestment;
    }

    private static double readRiskToleranceLevel(String fileName) {
        double riskToleranceLevel = 0.0;

        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("Risk tolerance level")) {
                    riskToleranceLevel = Double.parseDouble(line.split(" ")[4]);
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return riskToleranceLevel;
    }

    public static void main(String[] args) {
        List<Asset> assets = readAssetsFromFile("Example.txt");

        if (assets.isEmpty()) {
            System.err.println("No assets found in the file.");
            return;
        }

        double totalInvestment = readTotalInvestment("Example.txt");
        double riskToleranceLevel = readRiskToleranceLevel("Example.txt");

        double bestReturn = 0.0;
        double bestRisk = Double.MAX_VALUE;
        int bestQuantity1 = 0;
        int bestQuantity2 = 0;
        int bestQuantity3 = 0;

        for (int i = 0; i <= assets.get(0).quantity; i++) {
            for (int j = 0; j <= assets.get(1).quantity; j++) {
                for (int k = 0; k <= assets.get(2).quantity; k++) {
                    int totalUnits = i + j + k;
                    if (totalUnits <= totalInvestment) {
                        double totalReturn = calculateExpectedReturn(assets, i, j, k);
                        double totalRisk = calculatePortfolioRisk(assets, i, j, k);
                        if (totalRisk <= riskToleranceLevel && totalReturn > bestReturn) {
                            bestReturn = totalReturn;
                            bestRisk = totalRisk;
                            bestQuantity1 = i;
                            bestQuantity2 = j;
                            bestQuantity3 = k;
                        }
                    }
                }
            }
        }

        System.out.println("Optimal Allocation:");
        System.out.println(assets.get(0).id + ": " + bestQuantity1 + " units");
        System.out.println(assets.get(1).id + ": " + bestQuantity2 + " units");
        System.out.println(assets.get(2).id + ": " + bestQuantity3 + " units");
        System.out.println("Expected Portfolio Return: " + bestReturn);
        System.out.println("Portfolio Risk Level: " + bestRisk);
    }

    private static double calculateExpectedReturn(List<Asset> assets, int quantity1, int quantity2, int quantity3) {
        double expectedReturn = 0;
        for (int i = 0; i < assets.size(); i++) {
           expectedReturn += assets.get(i).expectedReturn * calculateAllocatedQuantity(i % assets.size(), quantity1, quantity2, quantity3);

        return expectedReturn;
    }

     private static double calculatePortfolioRisk(List<Asset> assets, int quantity1, int quantity2, int quantity3) {

        double totalRisk = 0;
        for (int i = 0; i < assets.size(); i++) {
            totalRisk += assets.get(i).risk * calculateAllocatedQuantity(i% assets.size(), quantity1, quantity2, quantity3);
        }
        return totalRisk / (quantity1 + quantity2 + quantity3);
    }

    private static int calculateAllocatedQuantity(int assetIndex, int quantity1, int quantity2, int quantity3) {
        if (assetIndex == 0) {
            return quantity1;
        } else if (assetIndex == 1) {
            return quantity2;
        } else if (assetIndex == 2) {
            return quantity3;
        } else {
            throw new IllegalArgumentException("Invalid asset index");
        }
    }
}
