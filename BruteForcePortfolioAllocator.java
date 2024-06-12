
//package csc311p1;

import java.io.File;
import java.io.FileNotFoundException; 
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
// Class to represent an asset
class Asset { 
    String assetID;   // Name of the asset
double expectedReturn; // Expected return rate of the asset 
double riskLevel;// Risk level of the asset
int quantityAvailable;// Quantity available for the asset

// Constructor to initialize the asset
public Asset(String assetID, double expectedReturn, double riskLevel, int quantityAvailable) {
this.assetID = assetID;
this.expectedReturn = expectedReturn; 
this.riskLevel = riskLevel; 
this.quantityAvailable = quantityAvailable;
} 
}

public class BruteForcePortfolioAllocator {
public static void main(String[] args) {
// Read input data from the file
List<Asset> assets = new ArrayList<>(); 
double totalInvestment = 0;
double riskTolerance = 0;

// Try to read the input file and handle exceptions

try {
File file = new File("Example1.txt"); // read from file input 
Scanner scanner = new Scanner(file);
// Iterate over each line in the file
while (scanner.hasNextLine()){ 
    String line = scanner.nextLine();

// Skip empty lines
if (line.trim().isEmpty()) { continue;
}
// Parse total investment and risk tolerance from specific lines
if (line.startsWith("Total Investment is")) {
totalInvestment = Double.parseDouble(line.split(" ")[3]);
} else if (line.startsWith("Risk Tolerance Level is")) 
{ riskTolerance = Double.parseDouble(line.split(" ")[4]);
} else {
// Parse asset information from each line
String[] parts = line.split(" : "); 
if (parts.length != 4) {
System.out.println("Invalid line format: " + line);
return; 
}
String assetID = parts[0].trim();
double expectedReturn = Double.parseDouble(parts[1].trim());


double riskLevel = Double.parseDouble(parts[2].trim());
int quantityAvailable = Integer.parseInt(parts[3].trim());
assets.add(new Asset(assetID, expectedReturn, riskLevel,quantityAvailable));}
}
scanner.close();
} catch (FileNotFoundException e) {
System.out.println("File not found.");
e.printStackTrace();
} catch (NumberFormatException e) {
System.out.println("Invalid number format in file.");
e.printStackTrace(); }

// Find optimal allocation using brute force
OptimalAllocationResult result = findOptimalAllocation(assets, totalInvestment, riskTolerance);
// Output optimal allocation
System.out.println("Optimal Allocation:");
for (Asset asset : result.optimalAllocation) {
System.out.println(asset.assetID + ": " + asset.quantityAvailable + " units"); }
// Output Expected Portfolio Return and Portfolio Risk Level

System.out.println("Expected Portfolio Return: " + String.format("%.3f", result.expectedPortfolioReturn));

System.out.println("Portfolio Risk Level: " + String.format("%.3f", result.portfolioRiskLevel));
}

// Class to hold the result of optimal
static class OptimalAllocationResult { 
    List<Asset> optimalAllocation;//List of assets in optimal allocation
    double expectedPortfolioReturn;//Expected return of the optimal allocation
    double portfolioRiskLevel;//Risk level of the optimal allocation
    
// Constructor to initialize the result
public OptimalAllocationResult(List<Asset> optimalAllocation, double expectedPortfolioReturn, double portfolioRiskLevel) {
this.optimalAllocation = optimalAllocation;
this.expectedPortfolioReturn = expectedPortfolioReturn; 
this.portfolioRiskLevel = portfolioRiskLevel;
} }


// Find optimal allocation using brute force
private static OptimalAllocationResult findOptimalAllocation(List<Asset> assets, double totalInvestment, double riskTolerance) {
List<Asset> optimalAllocation = new ArrayList<>(); 
double maxExpectedReturn = 0;
double portfolioRiskLevel = Double.MAX_VALUE;

// Iterate over all possible asset allocations
for (int i = 0; i <= assets.get(0).quantityAvailable; i++) {
for (int j = 0; j <= Math.min(assets.get(1).quantityAvailable, totalInvestment -i); j++) {
int k = (int) (totalInvestment - i - j);

// Check if the allocation exceeds the available quantity for any asset
if (k > assets.get(2).quantityAvailable) {
continue; // Skip this allocation if the quantity exceeds the available quantity
}
// Calculate expected return and risk level for the current allocation
double expectedReturn = assets.get(0).expectedReturn * (i / totalInvestment) + assets.get(1).expectedReturn * (j / totalInvestment)
+ assets.get(2).expectedReturn * (k / totalInvestment);
double riskLevel = assets.get(0).riskLevel * (i / totalInvestment) + assets.get(1).riskLevel * (j / totalInvestment)
+ assets.get(2).riskLevel * (k / totalInvestment);
// Check if the allocation satisfies the risk tolerance and has higher expected return
if (riskLevel <= riskTolerance && expectedReturn > maxExpectedReturn) { maxExpectedReturn = expectedReturn;
portfolioRiskLevel = riskLevel;
optimalAllocation.clear();
optimalAllocation.add(new Asset(assets.get(0).assetID, assets.get(0).expectedReturn, assets.get(0).riskLevel, i)); 
optimalAllocation.add(new Asset(assets.get(1).assetID, assets.get(1).expectedReturn, assets.get(1).riskLevel, j)); 
optimalAllocation.add(new Asset(assets.get(2).assetID, assets.get(2).expectedReturn, assets.get(2).riskLevel, k));} }}
return new OptimalAllocationResult(optimalAllocation, maxExpectedReturn,
portfolioRiskLevel); }}
