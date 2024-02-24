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

    // Method to read assets from a file and organize them into sets
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
                    // Split the line to extract asset details
                    String[] parts = line.split(" : ");
                    if (parts.length == 4) {
                        String id = parts[0];
                        double expectedReturn = Double.parseDouble(parts[1].trim());
                        double risk = Double.parseDouble(parts[2].trim());
                        int quantity = Integer.parseInt(parts[3].trim());
                        // Create Asset object and add to current set
                        currentSet.add(new Asset(id, expectedReturn, risk, quantity));
                    }
                }
            }
            // Add the last set of assets if not empty
            if (!currentSet.isEmpty()) {
                allAssets.add(new ArrayList<>(currentSet));
            }
        } catch (FileNotFoundException e) {
            // Print error message if file not found
            e.printStackTrace();
        }

        return allAssets;
    }

    // Method to read the total investment from a file
    private static double readTotalInvestment(String fileName) {
        double totalInvestment = 0.0;

        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("Total investment")) {
                    // Extract the total investment value from the line
                    totalInvestment += Double.parseDouble(line.split(" ")[3]);
                }
            }
        } catch (FileNotFoundException e) {
            // Print error message if file not found
            e.printStackTrace();
        }

        return totalInvestment;
    }

    // Method to read the risk tolerance level from a file
    private static double readRiskToleranceLevel(String fileName) {
        double riskToleranceLevel = 0.0;

        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("Risk tolerance level")) {
                    // Extract the risk tolerance level value from the line
                    riskToleranceLevel += Double.parseDouble(line.split(" ")[4]);
                }
            }
        } catch (FileNotFoundException e) {
            // Print error message if file not found
            e.printStackTrace();
        }

        return riskToleranceLevel;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the filename: ");
        String fileName = scanner.nextLine();

        scanner.close();

        // Read assets from the file
        List<List<Asset>> allAssets = readAssetsFromFile(fileName);

        if (allAssets.isEmpty() || allAssets.size() < 1) {
            System.err.println("Insufficient assets found in the file.");
            return;
        }

        List<Asset> assets = allAssets.get(0);

        // Read total investment and risk tolerance level from the file
        double totalInvestment = readTotalInvestment(fileName);
        double riskToleranceLevel = readRiskToleranceLevel(fileName);

        double bestReturn = 0.0;
        double bestRisk = Double.MAX_VALUE;
        int bestQuantity1 = 0;
        int bestQuantity2 = 0;
        int bestQuantity3 = 0;

        // Loop through different combinations of asset quantities to find the optimal allocation
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

        // Print out the optimal allocation and portfolio details
        System.out.println("Optimal Allocation:");
        System.out.println(allAssets.get(0).get(0).id + ": " + bestQuantity1 + " units");
        System.out.println(allAssets.get(0).get(1).id + ": " + bestQuantity2 + " units");
        System.out.println(allAssets.get(0).get(2).id + ": " + bestQuantity3 + " units");
        System.out.println("Expected Portfolio Return: " + bestReturn);
        System.out.println("Portfolio Risk Level: " + bestRisk);
    }

       // Method to calculate the expected return of a portfolio
    private static double calculateExpectedReturn(List<Asset> assets, int quantity1, int quantity2, int quantity3) {
        double expectedReturn = 0;
        // Calculate the expected return for each asset based on its quantity in the portfolio
        for (int i = 0; i < assets.size(); i++) {
            expectedReturn += assets.get(i).expectedReturn * calculateAllocatedQuantity(i % assets.size(), quantity1, quantity2, quantity3);
        }
        return expectedReturn;
    }

    // Method to calculate the total risk of a portfolio
    private static double calculatePortfolioRisk(List<Asset> assets, int quantity1, int quantity2, int quantity3, double totalInvestment) {
        double totalRisk = 0;
        // Calculate the total risk for each asset based on its quantity in the portfolio
        for (int i = 0; i < assets.size(); i++) {
            totalRisk += assets.get(i).risk * calculateAllocatedQuantity(i % assets.size(), quantity1, quantity2, quantity3);
        }
        // Normalize the total risk by the total investment
        return totalRisk / totalInvestment;
    }

    // Method to calculate the quantity of a specific asset allocated in the portfolio
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
}
