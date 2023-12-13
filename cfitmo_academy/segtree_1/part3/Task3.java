package cfitmo_academy.segtree_1.part3;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Objects;

public class Task3 {

    static Map<Integer, Integer> leftBound = new HashMap<>();
    static SegTree segTree;

    public static void main(String[] args) {
        try (FastIO io = new FastIO()) {
            int n = io.nextInt();

            String[] answer = new String[n];
            segTree = new SegTree(2 * n);

            for (int i = 0; i < 2 * n; i++) {
                int num = io.nextInt();

                if (leftBound.containsKey(num)) {
                    int q = segTree.query(leftBound.get(num), i + 1);
                    answer[num - 1] = Objects.toString(q);
                    segTree.update(leftBound.get(num), 1);
                } else {
                    leftBound.put(num, i);
                }
            }

            for (String num : answer) {
                System.out.printf("%s ", num);
            }
        }
    }

    static class SegTree {
        int[] tree;
        int size;

        SegTree(int n) {
            size = 1;
            while (size < n) {
                size *= 2;
            }
            tree = new int[2 * size];
        }

        void update(int i, int v) {
            update(0, 0, size, i, v);
        }

        void update(int x, int xl, int xr, int i, int v) {
            if (xl + 1 == xr) {
                tree[x] = v;
                return;
            }

            int xm = (xl + xr) / 2;
            if (i < xm) {
                update(x * 2 + 1, xl, xm, i, v);
            } else {
                update(x * 2 + 2, xm, xr, i, v);
            }

            tree[x] = tree[x * 2 + 1] + tree[x * 2 + 2];
        }

        int query(int l, int r) {
            return query(0, 0, size, l, r);
        }

        int query(int x, int xl, int xr, int l, int r) {
            if (l >= xr || xl >= r) {
                return 0;
            }

            if (xl >= l && xr <= r) {
                return tree[x];
            }

            int xm = (xl + xr) / 2;
            int left = query(x * 2 + 1, xl, xm, l, r);
            int right = query(x * 2 + 2, xm, xr, l, r);
            return left + right;
        }
    }

    static class FastIO extends PrintWriter {
        private InputStream stream;
        private byte[] buf = new byte[1 << 16];
        private int curChar;
        private int numChars;

        // standard input
        public FastIO() {
            this(System.in, System.out);
        }

        public FastIO(InputStream i, OutputStream o) {
            super(o);
            stream = i;
        }

        // file input
        public FastIO(String i, String o) throws IOException {
            super(new FileWriter(o));
            stream = new FileInputStream(i);
        }

        // throws InputMismatchException() if previously detected end of file
        private int nextByte() {
            if (numChars == -1) {
                throw new InputMismatchException();
            }
            if (curChar >= numChars) {
                curChar = 0;
                try {
                    numChars = stream.read(buf);
                } catch (IOException e) {
                    throw new InputMismatchException();
                }
                if (numChars == -1) {
                    return -1; // end of file
                }
            }
            return buf[curChar++];
        }

        // to read in entire lines, replace c <= ' '
        // with a function that checks whether c is a line break
        public String next() {
            int c;
            do {
                c = nextByte();
            } while (c <= ' ');

            StringBuilder res = new StringBuilder();
            do {
                res.appendCodePoint(c);
                c = nextByte();
            } while (c > ' ');
            return res.toString();
        }

        public int nextInt() { // nextLong() would be implemented similarly
            int c;
            do {
                c = nextByte();
            } while (c <= ' ');

            int sgn = 1;
            if (c == '-') {
                sgn = -1;
                c = nextByte();
            }

            int res = 0;
            do {
                if (c < '0' || c > '9') {
                    throw new InputMismatchException();
                }
                res = 10 * res + c - '0';
                c = nextByte();
            } while (c > ' ');
            return res * sgn;
        }

        public double nextDouble() {
            return Double.parseDouble(next());
        }
    }
}
