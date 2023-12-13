package cfitmo_academy.segtree_1.part3;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.InputMismatchException;

public class Task5 {
    public static void main(String[] args) {
        try (FastIO io = new FastIO()) {
            int n = io.nextInt();
            int m = io.nextInt();
            SegTree tree = new SegTree(n);

            while (m-- > 0) {
                int op = io.nextInt();
                if (op == 1) {
                    int l = io.nextInt();
                    int r = io.nextInt();
                    int v = io.nextInt();
                    tree.update(l, r, v);
                } else if (op == 2) {
                    int i = io.nextInt();
                    long q = tree.query(i);
                    System.out.println(q);
                }
            }
        }
    }

    static class SegTree {
        long[] tree;
        int sz;

        SegTree(int n) {
            sz = 1;
            while (sz < n) {
                sz *= 2;
            }
            tree = new long[2 * sz];
        }

        void update(int l, int r, int v) {
            update(0, 0, sz, l, r, v);
        }

        void update(int x, int xl, int xr, int l, int r, int v) {
            if (xl >= r || xr <= l) {
                return;
            }
            if (l <= xl && xr <= r) {
                tree[x] += v;
                return;
            }
            int xm = (xl + xr) / 2;

            update(x * 2 + 1, xl, xm, l, r, v);
            update(x * 2 + 2, xm, xr, l, r, v);
        }

        long query(int i) {
            return query(0, 0, sz, i);
        }

        long query(int x, int xl, int xr, int i) {
            if (xl + 1 == xr) {
                return tree[x];
            }
            int xm = (xl + xr) / 2;
            if (i < xm) {
                return tree[x] + query(x * 2 + 1, xl, xm, i);
            } else {
                return tree[x] + query(x * 2 + 2, xm, xr, i);
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
