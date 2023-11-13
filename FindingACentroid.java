import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

// https://ru.algorithmica.org/cs/trees/centroid/
// https://www.youtube.com/watch?v=FFjHDX5D4VA&list=PLrS21S1jm43iUIpR51VCJgxY1MjwS-pAZ&index=11
public class FindingACentroid {
    static int n;
    static ArrayList<Integer>[] tree;
    static int[] numOfComponents; // or tree size

    static int countNumOfComponents(int v, int p) {
        numOfComponents[v] = 1;
        for (int u : tree[v]) {
            if (u == p)
                continue;
            numOfComponents[v] += countNumOfComponents(u, v);
        }
        return numOfComponents[v];
    }

    static int findCentroid(int v, int p) {
        for (int u : tree[v]) {
            if (u == p)
                continue;
            if (numOfComponents[u] > n / 2) {
                return findCentroid(u, v);
            }
        }
        return v;
    }

    static int solve() {
        countNumOfComponents(1, 1);
        return findCentroid(1, 1);
    }

    // in:
    // 5
    // 1 2
    // 2 3
    // 3 4
    // 3 5

    // ans: 3
    public static void main(String[] args) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            n = Integer.parseInt(reader.readLine());

            tree = new ArrayList[n + 1];
            numOfComponents = new int[n + 1];

            for (int i = 1; i <= n; i++) {
                tree[i] = new ArrayList<Integer>();
            }

            for (int i = 0; i < n - 1; i++) {
                String[] line = reader.readLine().split("\\s+");
                int a = Integer.parseInt(line[0]);
                int b = Integer.parseInt(line[1]);
                tree[a].add(b);
                tree[b].add(a);
            }
        }

        int result = solve();
        System.out.println(result);
    }
}