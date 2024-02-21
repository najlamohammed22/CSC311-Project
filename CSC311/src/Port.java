
public class Port {
	private static int calculateTotalInvestment(List<Asset> assets) { // assets list take it from Aso code 
	    int totalInvestment = 0;
	    for (Asset asset : assets) {
	        totalInvestment += asset.quantity;
	    }
	    return totalInvestment;
	}
	private static double calculateExpectedReturn(List<Asset> assets, int quantity1, int quantity2, int quantity3) {
	    double expectedReturn = 0;
	    for (int i = 0; i < assets.size(); i++) {
	        expectedReturn += assets.get(i).expectedReturn * calculateAllocatedQuantity(assets.get(i), quantity1, quantity2, quantity3);
	    }
	    return expectedReturn;
	}
	private static double calculatePortfolioRisk(List<Asset> assets, int quantity1, int quantity2, int quantity3) {
	    double totalRisk = 0;
	    for (int i = 0; i < assets.size(); i++) {
	        totalRisk += assets.get(i).riskLevel * calculateAllocatedQuantity(assets.get(i), quantity1, quantity2, quantity3);
	    }
	    return totalRisk / (quantity1 + quantity2 + quantity3);
	}
	private static int calculateAllocatedQuantity(Asset asset, int quantity1, int quantity2, int quantity3) {
	    switch (asset.id) {
	        case "AAPL":
	            return quantity1;
	        case "GOOGL":
	            return quantity2;
	        case "MSFT":
	            return quantity3;
	        // Add more cases if there are additional assets
	        default:
	            throw new IllegalArgumentException("Invalid asset ID");
	    }
	}
	
	

}
