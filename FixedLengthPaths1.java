import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

// https://cses.fi/problemset/task/2080
public class FixedLengthPaths1 {
    static int n;
    static int k;
    static int ans = 0;

    static ArrayList<Integer>[] tree;
    static boolean[] removed;
    static int[] size; // or tree size

    static int countNumOfComponents(int v, int p) {
        size[v] = 1;
        for (int u : tree[v]) {
            if (u == p || removed[u])
                continue;
            size[v] += countNumOfComponents(u, v);
        }
        return size[v];
    }

    static int findCentroid(int v, int p) {
        for (int u : tree[v]) {
            if (u == p || removed[u])
                continue;
            if (size[u] > n / 2) {
                return findCentroid(u, v);
            }
        }
        return v;
    }

    static void calc(int v) {
        class DFS {
            void apply(int v, int p, int d, List<Integer> dist) {
                dist.add(d);
                for (int u : tree[v]) {
                    if (u == p || removed[u]) {
                        continue;
                    }
                    apply(u, v, d + 1, dist);
                }
            }
        }

        int[] d = new int[size[v]];
        d[0] = 1;

        for (int u : tree[v]) {
            if (removed[u]) {
                continue;
            }
            List<Integer> temp = new ArrayList<>();
            new DFS().apply(u, v, 1, temp);
            for (int x : temp) {
                if (x <= k) {
                    ans += d[k - x];
                }
            }
            for (int x : temp) {
                d[x]++;
            }
        }
    }

    static void centroidDecompose(int v) {
        int centroid = findCentroid(v, v);
        calc(centroid);
        removed[centroid] = true;

        for (int u : tree[centroid]) {
            if (!removed[u]) {
                centroidDecompose(u);
            }
        }
    }

    // in:
    // 5 2
    // 1 2
    // 2 3
    // 3 4
    // 3 5

    // ans: 4
    public static void main(String[] args) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

            String[] nk = reader.readLine().split("\\s+");

            n = Integer.parseInt(nk[0]);
            k = Integer.parseInt(nk[1]);

            tree = new ArrayList[n + 1];
            removed = new boolean[n + 1];
            size = new int[n + 1];

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

        countNumOfComponents(1, 1);
        centroidDecompose(1);
        System.out.println(ans);
    }
}