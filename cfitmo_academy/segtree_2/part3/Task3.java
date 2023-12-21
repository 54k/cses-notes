package cfitmo_academy.segtree_2.part3;

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
            SegTree segTree = new SegTree(n);
            while (m-- > 0) {
                int t = io.nextInt();
                if (t == 1) {
                    int l = io.nextInt();
                    int r = io.nextInt();
                    int v = io.nextInt();
                    segTree.update(l, r, v);
                } else {
                    int k = io.nextInt();
                    int l = io.nextInt();
                    int found = segTree.findAbove(k, l);
                    System.out.println(found);
                }
            }
        }
    }

    static class SegTree {
        int[] lazy;
        int[] tree;
        int size;

        SegTree(int n) {
            size = 1;
            while (size < n) {
                size *= 2;
            }
            tree = new int[size * 2 - 1];
            lazy = new int[size * 2 - 1];
        }

        void propagate(int x, int lx, int rx) {
            tree[x] += lazy[x];
            if (lx + 1 != rx) {
                lazy[x * 2 + 1] += lazy[x];
                lazy[x * 2 + 2] += lazy[x];
            }
            lazy[x] = 0;
        }

        int combine(int a, int b) {
            return Math.max(a, b);
        }

        void update(int l, int r, int v) {
            update(0, 0, size, l, r, v);
        }

        void update(int x, int lx, int rx, int l, int r, int v) {
            propagate(x, lx, rx);

            if (lx >= r || rx <= l) {
                return;
            }
            if (l <= lx && rx <= r) {
                lazy[x] += v;
                propagate(x, lx, rx);
                return;
            }

            int mx = (lx + rx) / 2;
            update(x * 2 + 1, lx, mx, l, r, v);
            update(x * 2 + 2, mx, rx, l, r, v);
            tree[x] = combine(tree[x * 2 + 1] + lazy[x * 2 + 1], tree[x * 2 + 2] + lazy[x * 2 + 2]);
        }

        int findAbove(int k, int l) {
            return findAbove(0, 0, size, k, l);
        }

        int findAbove(int x, int lx, int rx, int k, int l) {
            propagate(x, lx, rx);

            if (tree[x] < k) {
                return -1;
            }
            if (rx <= l) {
                return -1;
            }
            if (lx == rx - 1) {
                return lx;
            }

            int mx = (lx + rx) / 2;
            int ans = findAbove(x * 2 + 1, lx, mx, k, l);
            if (ans == -1) {
                ans = findAbove(x * 2 + 2, mx, rx, k, l);
            }
            return ans;
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
