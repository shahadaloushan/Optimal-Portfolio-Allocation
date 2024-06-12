Optimal Portfolio Allocation

This project determines the optimal allocation of assets in an investment portfolio using brute force and dynamic programming algorithms. 
It compares their efficiency and runtime



to make it easy to understand Imagine you work at a prestigious investment firm, tasked with the challenging job of maximizing your clients' returns while carefully managing their risk levels.
Your goal is to determine the optimal way to allocate investments across various assets, each with its own expected return and risk.
To achieve this, you decide to employ two different strategies: brute force and dynamic programming. Both approaches aim to find the best mix of assets, but they do so in very different ways.


1) The Brute Force Method: Exhaustive Search
First, you dive into the brute force method. This approach is like exploring every possible path in a vast forest to find the shortest route. You meticulously evaluate all possible combinations of asset allocations, calculating the expected return and risk for each one. This means considering every conceivable way to distribute the investment amount across the assets, even if it takes a significant amount of time. Despite the effort, brute force ensures you don’t miss any potential solutions.


2) The Dynamic Programming Approach: Smart Efficiency
Next, you turn to dynamic programming, a more sophisticated strategy. Instead of checking every possible combination, you break down the problem into smaller, manageable sub-problems. By solving these sub-problems and storing the results, you avoid redundant calculations. This method is like having a map that helps you find the shortest path through the forest by remembering which trails you’ve already explored. It’s faster and more efficient, especially as the number of assets grows.





The Comparison: Efficiency and Runtime
With both methods implemented, you set out to compare their performance. For different portfolio sizes and investment amounts, you measure how quickly each algorithm finds the optimal solution and how efficiently they use computational resources. The brute force method, while thorough, often takes much longer, especially as the number of assets increases. Dynamic programming, on the other hand, quickly proves its worth by solving complex allocation problems in a fraction of the time.
