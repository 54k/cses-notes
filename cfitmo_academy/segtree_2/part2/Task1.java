package cfitmo_academy.segtree_2.part2;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.InputMismatchException;

public class Task1 {
    public static void main(String[] args) {
        try (FastIO io = new FastIO()) {
            int n = io.nextInt();
            int m = io.nextInt();

            SegTree tree = new SegTree(n);
            for (int i = 0; i < m; i++) {
                int op = io.nextInt();
                if (op == 1) {
                    int l = io.nextInt();
                    int r = io.nextInt();
                    int v = io.nextInt();
                    tree.add(l, r, v);
                } else {
                    int l = io.nextInt();
                    int r = io.nextInt();
                    long res = tree.min(l, r);
                    System.out.println(res);
                }
            }
        }
    }

    static class SegTree {
        static class Node {
            long add;
            long min;
        }

        Node[] tree;
        int size;

        SegTree(int n) {
            size = 1;
            while (size < n) {
                size *= 2;
            }

            tree = new Node[2 * size];
            for (int i = 0; i < tree.length; i++) {
                tree[i] = new Node();
            }
        }

        void add(int l, int r, int v) {
            add(0, 0, size, l, r, v);
        }

        void add(int x, int xl, int xr, int l, int r, int v) {
            if (xl >= r || xr <= l) {
                return;
            }

            if (l <= xl && xr <= r) {
                tree[x].add += v;
                tree[x].min += v;
                return;
            }

            int xm = (xl + xr) / 2;
            add(x * 2 + 1, xl, xm, l, r, v);
            add(x * 2 + 2, xm, xr, l, r, v);

            tree[x].min = Math.min(tree[x * 2 + 1].min, tree[x * 2 + 2].min) + tree[x].add;
        }

        long min(int l, int r) {
            return min(0, 0, size, l, r);
        }

        long min(int x, int xl, int xr, int l, int r) {
            if (xl >= r || xr <= l) {
                return Long.MAX_VALUE;
            }
            if (l <= xl && xr <= r) {
                return tree[x].min;
            }
            int xm = (xl + xr) / 2;
            long lm = min(x * 2 + 1, xl, xm, l, r);
            long rm = min(x * 2 + 2, xm, xr, l, r);
            return Math.min(lm, rm) + tree[x].add;
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
