package cfitmo_academy.segtree_2.part3;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.InputMismatchException;

public class Task2 {
    public static void main(String[] args) {
        try (FastIO io = new FastIO()) {
            int n = io.nextInt();
            int m = io.nextInt();

            SegTree segTree = new SegTree(n);

            while (m-- > 0) {
                int op = io.nextInt();
                if (op == 1) {
                    int l = io.nextInt();
                    int r = io.nextInt();
                    segTree.set(l, r);
                } else {
                    int k = io.nextInt();
                    int pos = segTree.find(k);
                    System.out.println(pos);
                }
            }
        }
    }

    static class SegTree {
        int[] tree;
        int[] lazy;
        int size;

        SegTree(int n) {
            size = 1;
            while (size < n) {
                size *= 2;
            }

            tree = new int[2 * size - 1];
            lazy = new int[2 * size - 1];
        }

        void propagate(int x, int xl, int xr) {
            if (xl + 1 == xr || lazy[x] == 0) {
                return;
            }
            int m = (xl + xr) / 2;
            lazy[x * 2 + 1] ^= 1;
            tree[x * 2 + 1] = (m - xl) - tree[x * 2 + 1];

            lazy[x * 2 + 2] ^= 1;
            tree[x * 2 + 2] = (xr - m) - tree[x * 2 + 2];
            lazy[x] = 0;
        }

        void set(int l, int r) {
            set(0, 0, size, l, r);
        }

        void set(int x, int xl, int xr, int l, int r) {
            propagate(x, xl, xr);

            if (xr <= l || xl >= r) {
                return;
            }

            if (l <= xl && xr <= r) {
                lazy[x] ^= 1;
                tree[x] = (xr - xl) - tree[x];
                return;
            }

            int mid = (xl + xr) / 2;
            set(x * 2 + 1, xl, mid, l, r);
            set(x * 2 + 2, mid, xr, l, r);

            tree[x] = tree[x * 2 + 1] + tree[x * 2 + 2];
        }

        int find(int k) {
            return find(0, 0, size, k);
        }

        int find(int x, int xl, int xr, int k) {
            propagate(x, xl, xr);

            if (xl + 1 == xr) {
                return xl;
            }
            int mid = (xl + xr) / 2;
            if (k < tree[x * 2 + 1]) {
                return find(x * 2 + 1, xl, mid, k);
            } else {
                return find(x * 2 + 2, mid, xr, k - tree[x * 2 + 1]);
            }
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
