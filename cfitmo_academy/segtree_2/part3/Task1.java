package cfitmo_academy.segtree_2.part3;

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

            SegTree segTree = new SegTree(n);

            for (int i = 0; i < m; i++) {
                int l = io.nextInt();
                int r = io.nextInt();
                int v = io.nextInt();

                segTree.set(l, r, v);
                System.out.println(segTree.tree[0].seg);
            }
        }
    }

    static class SegTree {
        static class Node {
            long seg;
            long pref;
            long suf;
            long sum;

            Node(long v) {
                sum = v;
                if (sum > 0) {
                    seg = pref = suf = sum;
                } else {
                    seg = pref = suf = 0;
                }
            }

            Node(long s, long p, long su, long sum) {
                seg = s;
                pref = p;
                suf = su;
                this.sum = sum;
            }
        }

        static final long INF = Long.MAX_VALUE;
        Node[] tree;
        long[] lazy;
        int size;

        SegTree(int n) {
            size = 1;
            while (size < n) {
                size *= 2;
            }

            tree = new Node[2 * size - 1];
            lazy = new long[2 * size - 1];
            for (int i = 0; i < tree.length; i++) {
                tree[i] = new Node(0);
                lazy[i] = INF;
            }
        }

        void propagate(int x, int lx, int rx) {
            if (lx + 1 != rx && lazy[x] != INF) {
                int mid = (lx + rx) / 2;
                tree[x * 2 + 1] = new Node(lazy[x] * (mid - lx));
                tree[x * 2 + 2] = new Node(lazy[x] * (rx - mid));
                lazy[x * 2 + 1] = lazy[x * 2 + 2] = lazy[x];
                lazy[x] = INF;
            }
        }

        Node combine(Node a, Node b) {
            return new Node(
                    Math.max(a.seg, Math.max(b.seg, a.suf + b.pref)),
                    Math.max(a.pref, a.sum + b.pref),
                    Math.max(b.suf, b.sum + a.suf),
                    a.sum + b.sum);
        }

        void set(int l, int r, long v) {
            set(0, 0, size, l, r, v);
        }

        void set(int x, int lx, int rx, int l, int r, long v) {
            propagate(x, lx, rx);

            if (r <= lx || l >= rx) {
                return;
            }

            if (l <= lx && rx <= r) {
                long sum = v * (rx - lx);
                tree[x] = new Node(sum);
                lazy[x] = v;
                return;
            }

            int mid = (lx + rx) / 2;
            set(x * 2 + 1, lx, mid, l, r, v);
            set(x * 2 + 2, mid, rx, l, r, v);

            tree[x] = combine(tree[x * 2 + 1], tree[x * 2 + 2]);
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
