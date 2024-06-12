import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
class Asset {
    String assetID;
    double expectedReturn;
    double riskLevel;
    int quantityAvailable;

    public Asset(String assetID, double expectedReturn, double riskLevel, int quantityAvailable) {
        this.assetID = assetID;
        this.expectedReturn = expectedReturn;
        this.riskLevel = riskLevel;
        this.quantityAvailable = quantityAvailable;
    }
}

public class DynamicPortfolioAllocator {
    public static void main(String[] args) {
        List<Asset> assets = readAssetsFromFile("Input.txt");
        double totalInvestment = 0;
        double riskTolerance = 0;

        for (Asset asset : assets) {
            totalInvestment += asset.quantityAvailable;
        }

        try {
            Scanner scanner = new Scanner(new File("Input.txt"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().toLowerCase();
                if (line.startsWith("total investment is")) {
                    totalInvestment = Double.parseDouble(line.split("is ")[1].split(" units")[0]);
                } else if (line.startsWith("risk tolerance level is")) {
                    riskTolerance = Double.parseDouble(line.split("is ")[1]);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        OptimalAllocationResult result = findOptimalAllocation(assets, totalInvestment, riskTolerance);
        System.out.println("Optimal Allocation:");
        for (Asset asset : result.optimalAllocation) {
            System.out.println(asset.assetID + ": " + asset.quantityAvailable + " units");
        }
        System.out.println("Expected Portfolio Return: " + String.format("%.3f", result.expectedPortfolioReturn/totalInvestment));
        System.out.println("Portfolio Risk Level: " + String.format("%.3f", result.portfolioRiskLevel));
    }
    static class OptimalAllocationResult {
        List<Asset> optimalAllocation;
        double expectedPortfolioReturn;
        double portfolioRiskLevel;
        public OptimalAllocationResult(List<Asset> optimalAllocation, double expectedPortfolioReturn, double portfolioRiskLevel) {
            this.optimalAllocation = optimalAllocation;
            this.expectedPortfolioReturn = expectedPortfolioReturn;
            this.portfolioRiskLevel = portfolioRiskLevel;
        }
    }
    private static OptimalAllocationResult findOptimalAllocation(List<Asset> assets, double totalInvestment, double riskTolerance) {
        int n = assets.size();
        double[][] dp = new double[n + 1][(int) totalInvestment + 1];
        int[][][] parent = new int[n + 1][(int) totalInvestment + 1][n];

        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= totalInvestment; j++) {
                for (int k = 0; k < n; k++) {
                    dp[i][j] = -1;
                }
            }
        }

        dp[0][0] = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= totalInvestment; j++) {
                for (int k = 0; k <= assets.get(i).quantityAvailable; k++) {
                    if (j + k <= totalInvestment) {
                        double value = dp[i][j] + k * assets.get(i).expectedReturn;
                        if (dp[i + 1][j + k] < value && isRiskAcceptable(i, j, k, assets, dp, riskTolerance)) {
                            dp[i + 1][j + k] = value;

                            for (int l = 0; l < i; l++) {
                                parent[i + 1][j + k][l] = parent[i][j][l];
                            }
                            parent[i + 1][j + k][i] = k;
                        }
                    }
                }
            }
        }

        List<Asset> optimalAllocation = new ArrayList<>();
        int remainingInvestment = (int) totalInvestment;

        for (int i = n - 1; i >= 0; i--) {
            optimalAllocation.add(new Asset(assets.get(i).assetID, assets.get(i).expectedReturn,
                    assets.get(i).riskLevel, parent[n][remainingInvestment][i]));
            remainingInvestment -= parent[n][remainingInvestment][i];
        }

        double expectedPortfolioReturn = dp[n][(int) totalInvestment];
        double portfolioRiskLevel = calculatePortfolioRisk(optimalAllocation, totalInvestment);

        return new OptimalAllocationResult(optimalAllocation, expectedPortfolioReturn, portfolioRiskLevel);
    }

    private static boolean isRiskAcceptable(int assetIndex, int currentInvestment, int additionalQuantity,
                                            List<Asset> assets, double[][] dp, double riskTolerance) {
        double totalRisk = 0;
        int totalInvestment = currentInvestment + additionalQuantity;

        for (int i = 0; i <= assetIndex; i++) {
            totalRisk += (double) additionalQuantity / totalInvestment * assets.get(i).riskLevel;
        }

        return totalRisk <= riskTolerance;
    }

    private static double calculatePortfolioRisk(List<Asset> assets, double totalInvestment) {
        double portfolioRisk = 0;

        for (Asset asset : assets) {
            portfolioRisk += (double) asset.quantityAvailable / totalInvestment * asset.riskLevel;
        }

        return portfolioRisk;
    }
    private static List<Asset> readAssetsFromFile(String fileName) {
        List<Asset> assets = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().toLowerCase();
                if (!line.startsWith("total investment is") && !line.startsWith("risk tolerance level is")) {
                    String[] parts = line.split(" : ");
                    String symbol = parts[0].toUpperCase();
                    double expectedReturn = Double.parseDouble(parts[1]);
                    double riskLevel = Double.parseDouble(parts[2]);
                    int quantity = Integer.parseInt(parts[3]);
                    assets.add(new Asset(symbol, expectedReturn, riskLevel, quantity));
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return assets;
    }
}
