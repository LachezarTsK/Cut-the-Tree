import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

public class Solution {
	private static List<Integer>[] neighboringNodes;
	private static int[] valueOfNodes;
	private static int numberOfNodes;
	private static int sum_valuesOfAllNodes;

	public static void main(String[] args) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

		numberOfNodes = Integer.parseInt(stringTokenizer.nextToken());
		valueOfNodes = new int[numberOfNodes + 1];

		stringTokenizer = new StringTokenizer(bufferedReader.readLine());
		for (int i = 1; i <= numberOfNodes; i++) {
			valueOfNodes[i] = Integer.parseInt(stringTokenizer.nextToken());
			sum_valuesOfAllNodes += valueOfNodes[i];
		}

		initialize_neighboringNodes();

		for (int i = 0; i < numberOfNodes - 1; i++) {
			stringTokenizer = new StringTokenizer(bufferedReader.readLine());
			int nodeOne = Integer.parseInt(stringTokenizer.nextToken());
			int nodeTwo = Integer.parseInt(stringTokenizer.nextToken());

			neighboringNodes[nodeOne].add(nodeTwo);
			neighboringNodes[nodeTwo].add(nodeOne);
		}
		bufferedReader.close();

		int startNode = 1;
		int result = findPairOfSubtrees_withMinimumAbsoluteDifference(startNode);
		System.out.println(result);
	}

	@SuppressWarnings("unchecked")
	public static void initialize_neighboringNodes() {
		neighboringNodes = new ArrayList[numberOfNodes + 1];
		for (int i = 1; i <= numberOfNodes; i++) {
			neighboringNodes[i] = new ArrayList<Integer>();
		}
	}

	/**
	 * A helper method for subroutine
	 * "findPairOfSubtrees_withMinimumAbsoluteDifference".
	 * 
	 * The method applies depth first search to retrieve and store the nodes,
	 * ordered from the leaves to the node chosen for root.
	 * 
	 * @return A stack with the nodes, ordered from the leaves to the root.
	 */
	private static Stack<Integer> depthFirstSearch_getNodes_orderedfromLeavesToRoot(int start) {

		boolean[] visited = new boolean[numberOfNodes + 1];
		Stack<Integer> queue = new Stack<Integer>();
		Stack<Integer> queue_fromLeavesToRoot = new Stack<Integer>();
		queue.push(start);
		queue_fromLeavesToRoot.push(start);

		while (!queue.isEmpty()) {
			int current = queue.pop();
			if (visited[current] == false) {
				visited[current] = true;

				for (int neighbour : neighboringNodes[current]) {
					if (visited[neighbour] == false) {
						queue.push(neighbour);
						queue_fromLeavesToRoot.push(neighbour);
					}
				}
			}
		}
		return queue_fromLeavesToRoot;
	}

	/**
	 * The method goes through the whole tree, starting from the leaves and
	 * finishing at the node chosen for root. At each edge, the sum of the values of
	 * all nodes on each side of the edge is calculated, and then the absolute
	 * difference between these two sums.
	 * 
	 * The thus calculated difference is compared to the previous one and those with
	 * less value is stored.
	 * 
	 * @return The minimum absolute difference between the sums of the node values
	 *         of two subtrees.
	 */
	private static int findPairOfSubtrees_withMinimumAbsoluteDifference(int start) {
		boolean[] visited = new boolean[numberOfNodes + 1];
		Stack<Integer> queue_fromLeavesToRoot = depthFirstSearch_getNodes_orderedfromLeavesToRoot(start);
		int[] valueOfSubtree = new int[numberOfNodes + 1];
		int mininumDifference = Integer.MAX_VALUE;

		while (!queue_fromLeavesToRoot.isEmpty()) {
			int current = queue_fromLeavesToRoot.pop();

			if (visited[current] == false) {
				visited[current] = true;
				valueOfSubtree[current] = valueOfNodes[current];

				for (int neighbour : neighboringNodes[current]) {

					/**
					 * In contrast to the various search methods, where a node is processed only if
					 * it is not visited, here a node is processed only if it is visited.
					 * 
					 * That is to say, in calculating the sum of the node values of a subtree, only
					 * the nodes from below the current node (thus leading to the leaves) are taken
					 * into account. Those nodes are only the visited ones.
					 */
					if (visited[neighbour] == true) {
						valueOfSubtree[current] += valueOfSubtree[neighbour];
					}
				}
			}
			mininumDifference = Math.min(mininumDifference,
					(Math.abs(valueOfSubtree[current] - (sum_valuesOfAllNodes - valueOfSubtree[current]))));
		}
		return mininumDifference;
	}
}
