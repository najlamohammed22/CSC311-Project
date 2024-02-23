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

    private static List<List<Asset>> readAssetsFromFile(String fileName) {
        List<List<Asset>> allAssets = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(fileName))) {
            List<Asset> currentSet = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    if (!currentSet.isEmpty()) {
                        allAssets.add(new ArrayList<>(currentSet));
                        currentSet.clear();
                    }
                } else if (!line.startsWith("Total investment") && !line.startsWith("Risk tolerance level")) {
                    String[] parts = line.split(" : ");
                    if (parts.length == 4) {
                        String id = parts[0];
                        double expectedReturn = Double.parseDouble(parts[1].trim());
                        double risk = Double.parseDouble(parts[2].trim());
                        int quantity = Integer.parseInt(parts[3].trim());
                        currentSet.add(new Asset(id, expectedReturn, risk, quantity));
                    }
                }
            }
            if (!currentSet.isEmpty()) {
                allAssets.add(new ArrayList<>(currentSet));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return allAssets;
    }

    private static double readTotalInvestment(String fileName) {
        double totalInvestment = 0.0;

        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("Total investment")) {
                    totalInvestment += Double.parseDouble(line.split(" ")[3]);
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
                    riskToleranceLevel += Double.parseDouble(line.split(" ")[4]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return riskToleranceLevel;
    }

    public static void main(String[] args) {
        List<List<Asset>> allAssets = readAssetsFromFile("Example.txt");

        if (allAssets.isEmpty() || allAssets.size() < 2) {
            System.err.println("Insufficient assets found in the file.");
            return;
        }

        List<Asset> firstAssets = allAssets.get(0);
        List<Asset> secondAssets = allAssets.get(1);

        double totalInvestmentFirst = readTotalInvestment("Example.txt");
        double riskToleranceLevelFirst = readRiskToleranceLevel("Example.txt");
        double totalInvestmentSecond = readTotalInvestment("Example.txt");
        double riskToleranceLevelSecond = readRiskToleranceLevel("Example.txt");

        double bestReturn1 = 0.0;
        double bestRisk1 = Double.MAX_VALUE;
        int bestQuantity1_1 = 0;
        int bestQuantity2_1 = 0;
        int bestQuantity3_1 = 0;

        double bestReturn2 = 0.0;
        double bestRisk2 = Double.MAX_VALUE;
        int bestQuantity1_2 = 0;
        int bestQuantity2_2 = 0;
        int bestQuantity3_2 = 0;

        for (int i = 0; i <= firstAssets.get(0).quantity; i++) {
            for (int j = 0; j <= firstAssets.get(1).quantity; j++) {
                for (int k = 0; k <= firstAssets.get(2).quantity; k++) {
                    int totalUnits = i + j + k;
                    if (totalUnits <= totalInvestmentFirst) {
                        double totalReturn = calculateExpectedReturn(firstAssets, i, j, k);
                        double totalRisk = calculatePortfolioRisk(firstAssets, i, j, k, totalInvestmentFirst);
                        if (totalRisk <= riskToleranceLevelFirst && totalReturn > bestReturn1) {
                            bestReturn1 = totalReturn;
                            bestRisk1 = totalRisk;
                            bestQuantity1_1 = i;
                            bestQuantity2_1 = j;
                            bestQuantity3_1 = k;
                        }
                    }
                }
            }
        }

      for (int i = 0; i <= secondAssets.get(0).quantity; i++) {
    for (int j = 0; j <= secondAssets.get(1).quantity; j++) {
        for (int k = 0; k <= secondAssets.get(2).quantity; k++) {
            int totalUnits = i + j + k;
            if (totalUnits <= totalInvestmentSecond) {
                double totalReturn = calculateExpectedReturn(secondAssets, i, j, k);
                double totalRisk = calculatePortfolioRisk(secondAssets, i, j, k, totalInvestmentSecond);
                if (totalRisk <= riskToleranceLevelSecond && totalReturn > bestReturn2) {
                    bestReturn2 = totalReturn;
                    bestRisk2 = totalRisk;
                    bestQuantity1_2 = i;
                    bestQuantity2_2 = j;
                    bestQuantity3_2 = k;
                }
            }
        }
    }
}



        System.out.println("Optimal Allocation for First Assets:");
        System.out.println(firstAssets.get(0).id + ": " + bestQuantity1_1 + " units");
        System.out.println(firstAssets.get(1).id + ": " + bestQuantity2_1 + " units");
        System.out.println(firstAssets.get(2).id + ": " + bestQuantity3_1 + " units");
        System.out.println("Expected Portfolio Return: " + bestReturn1);
        System.out.println("Portfolio Risk Level: " + bestRisk1);

        System.out.println("\nOptimal Allocation for Second Assets:");
        System.out.println(secondAssets.get(0).id + ": " + bestQuantity1_2 + " units");
        System.out.println(secondAssets.get(1).id + ": " + bestQuantity2_2 + " units");
        System.out.println(secondAssets.get(2).id + ": " + bestQuantity3_2 + " units");
        System.out.println("Expected Portfolio Return: " + bestReturn2);
        System.out.println("Portfolio Risk Level: " + bestRisk2);
    }

    private static double calculateExpectedReturn(List<Asset> assets, int quantity1, int quantity2, int quantity3) {
        double expectedReturn = 0;
        for (int i = 0; i < assets.size(); i++) {
            expectedReturn += assets.get(i).expectedReturn * calculateAllocatedQuantity(i % assets.size(), quantity1, quantity2, quantity3);
        }
            return expectedReturn;
    }

    private static double calculatePortfolioRisk(List<Asset> assets, int quantity1, int quantity2, int quantity3, double totalInvestment) {
        double totalRisk = 0;
        for (int i = 0; i < assets.size(); i++) {
            totalRisk += assets.get(i).risk * calculateAllocatedQuantity(i % assets.size(), quantity1, quantity2, quantity3);
        }
        return totalRisk / totalInvestment;
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


