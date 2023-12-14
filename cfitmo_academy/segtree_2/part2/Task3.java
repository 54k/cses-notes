package cfitmo_academy.segtree_2.part2;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.InputMismatchException;

public class Task3 {
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
                    tree.mult(l, r, v);
                } else {
                    int l = io.nextInt();
                    int r = io.nextInt();
                    long res = tree.sum(l, r);
                    System.out.println(res);
                }
            }
        }
    }

    static class SegTree {
        static final long NEUTRAL = -1;

        static class Node {
            long mult;
            long sum;

            Node(long m, long a) {
                mult = m;
                sum = a;
            }
        }

        Node[] tree;
        int size;

        SegTree(int n) {
            size = 1;
            while (size < n) {
                size *= 2;
            }
            tree = new Node[2 * size - 1];
            build(0, 0, size);
        }

        long opModify(long a, long b) {
            return a | b;
        }

        long opSum(long a, long b) {
            return a & b;
        }

        void build(int x, int lx, int rx) {
            if (rx - 1 == lx) {
                tree[x] = new Node(0, 0);
            } else {
                int m = (lx + rx) / 2;
                build(x * 2 + 1, lx, m);
                build(x * 2 + 2, m, rx);
                tree[x] = new Node(0, opSum(tree[x * 2 + 1].sum, tree[x * 2 + 2].sum));
            }
        }

        void mult(int l, int r, int v) {
            mult(0, 0, size, l, r, v);
        }

        void mult(int x, int xl, int xr, int l, int r, int v) {
            if (xl >= r || xr <= l) {
                return;
            }

            if (l <= xl && xr <= r) {
                tree[x].mult = opModify(tree[x].mult, v);
                tree[x].sum = opModify(tree[x].sum, v);
                return;
            }

            int xm = (xl + xr) / 2;
            mult(x * 2 + 1, xl, xm, l, r, v);
            mult(x * 2 + 2, xm, xr, l, r, v);

            tree[x].sum = opModify(opSum(tree[x * 2 + 1].sum, tree[x * 2 + 2].sum), tree[x].mult);
        }

        long sum(int l, int r) {
            return sum(0, 0, size, l, r);
        }

        long sum(int x, int xl, int xr, int l, int r) {
            if (xl >= r || xr <= l) {
                return NEUTRAL;
            }
            if (l <= xl && xr <= r) {
                return tree[x].sum;
            }
            int xm = (xl + xr) / 2;
            long lm = sum(x * 2 + 1, xl, xm, l, r);
            long rm = sum(x * 2 + 2, xm, xr, l, r);

            long res = opSum(lm, rm);
            return opModify(res, tree[x].mult);
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
