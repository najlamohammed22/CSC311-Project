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
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the filename: ");
        String fileName = scanner.nextLine();

        scanner.close();

        List<List<Asset>> allAssets = readAssetsFromFile(fileName);

        if (allAssets.isEmpty() || allAssets.size() < 1) {
            System.err.println("Insufficient assets found in the file.");
            return;
        }

        List<Asset> assets = allAssets.get(0);

        double totalInvestment = readTotalInvestment(fileName);
        double riskToleranceLevel = readRiskToleranceLevel(fileName);

        double bestReturn = 0.0;
        double bestRisk = Double.MAX_VALUE;
        int bestQuantity1 = 0;
        int bestQuantity2 = 0;
        int bestQuantity3 = 0;

        for (int i = 0; i <= allAssets.get(0).get(0).quantity; i++) {
            for (int j = 0; j <= allAssets.get(0).get(1).quantity; j++) {
                for (int k = 0; k <= allAssets.get(0).get(2).quantity; k++) {
                    int totalUnits = i + j + k;
                    if (totalUnits <= totalInvestment) {
                        double totalReturn = calculateExpectedReturn(allAssets.get(0), i, j, k);
                        double totalRisk = calculatePortfolioRisk(allAssets.get(0), i, j, k, totalInvestment);
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
        System.out.println(allAssets.get(0).get(0).id + ": " + bestQuantity1 + " units");
        System.out.println(allAssets.get(0).get(1).id + ": " + bestQuantity2 + " units");
        System.out.println(allAssets.get(0).get(2).id + ": " + bestQuantity3 + " units");
        System.out.println("Expected Portfolio Return: " + bestReturn);
        System.out.println("Portfolio Risk Level: " + bestRisk);
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
