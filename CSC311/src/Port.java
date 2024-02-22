
public class Port {
	

for (int i = 0; i <= assets.get(0).quantity; i++) {
    for (int j = 0; j <= assets.get(1).quantity; j++) {
        for (int k = 0; k <= assets.get(2).quantity; k++) {
            int totalUnits = i + j + k;
            if (totalUnits <= totalInvestment) {
                double totalReturn = Port.calculateExpectedReturn(assets, i, j, k);
                double totalRisk = Port.calculatePortfolioRisk(assets, i, j, k);
                if (totalRisk <= riskTolerance && totalReturn > bestReturn) {
                    // Check if the difference between this choice and the best choice so far is acceptable
                    if (Math.abs(totalReturn - bestReturn) < acceptableDifference) {
                        // If the difference is within acceptable range, stop the search
                        return;
                    }
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

	private static double calculateExpectedReturn(List<Asset> assets, int quantity1, int quantity2, int quantity3) {
	    double expectedReturn = 0;
	    for (int i = 0; i < assets.size(); i++) {
	        expectedReturn += assets.get(i).expectedReturn * calculateAllocatedQuantity(assets, i, quantity1, quantity2, quantity3);
	    }
	    return expectedReturn;
	}
	private static double calculatePortfolioRisk(List<Asset> assets, int quantity1, int quantity2, int quantity3) {
	    double totalRisk = 0;
	    for (int i = 0; i < assets.size(); i++) {
	        totalRisk += assets.get(i).riskLevel * calculateAllocatedQuantity(assets, i, quantity1, quantity2, quantity3);
	    }
	    return totalRisk / (quantity1 + quantity2 + quantity3);
	}
	private static int calculateAllocatedQuantity(List<Asset> assets, int assetIndex ,int quantity1, int quantity2, int quantity3) {
	    if (assetIndex < 0 || assetIndex >= assets.size()) {
            throw new IllegalArgumentException("Invalid asset index");
        }
        switch (assetIndex) {
            case 0:
                return quantity1;
            case 1:
                return quantity2;
            case 2:
                return quantity3;
            default:
                throw new IllegalArgumentException("Invalid asset index");
        }
	}
	
	

}
