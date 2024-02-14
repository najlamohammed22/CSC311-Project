import java.util.ArrayList;
import java.util.List;
public class DynamicProgramming {
	//useing of Markowitz's Mean-Variance Model. algorithm
	// we need to  modify the input values according to our pro
	    public static List<Integer> portfolioOptimization(double[] expectedReturns, double[] risks, double totalInvestment, double riskTolerance) {
	        int n = expectedReturns.length;

	        // Initialize a table to store optimal allocations
	        double[][] optimalAllocations = new double[n + 1][(int) totalInvestment + 1];

	        for (int i = 1; i <= n; i++) {
	            for (int j = 1; j <= totalInvestment; j++) {
	                // Check if including asset i in the portfolio is beneficial
	                if (risks[i - 1] <= riskTolerance) {
	                    // Calculate the optimal allocation
	                    optimalAllocations[i][j] = Math.max(
	                            optimalAllocations[i - 1][j],
	                            optimalAllocations[i - 1][(int) (j - risks[i - 1])] + expectedReturns[i - 1]
	                    );
	                } else {
	                    // If adding asset i exceeds the risk tolerance, skip it
	                    optimalAllocations[i][j] = optimalAllocations[i - 1][j];
	                }
	            }
	        }

	        // Backtrack to find the optimal allocation for each asset
	        List<Integer> allocations = new ArrayList<>();
	        int i = n, j = (int) totalInvestment;
	        while (i > 0 && j > 0) {
	            if (optimalAllocations[i][j] != optimalAllocations[i - 1][j]) {
	                allocations.add(i - 1);
	                j -= risks[i - 1];
	            }
	            i--;
	        }

	        return allocations;
	    }

	    public static void main(String[] args) {
	        double[] expectedReturns = {0.08, 0.12, 0.1, 0.15};
	        double[] risks = {0.1, 0.2, 0.15, 0.25};
	        double totalInvestment = 1000000;
	        double riskTolerance = 0.18;

	        List<Integer> result = portfolioOptimization(expectedReturns, risks, totalInvestment, riskTolerance);

	        System.out.println("Optimal Asset Allocations: " + result);
	    }
	


}
