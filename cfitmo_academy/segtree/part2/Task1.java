package cfitmo_academy.segtree.part2;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.InputMismatchException;

// https://codeforces.com/edu/course/2/lesson/4/2/practice/contest/273278/problem/A
public class Task1 {
    public static void main(String[] args) {
        try (FastIO io = new FastIO()) {
            int n = io.nextInt();
            int m = io.nextInt();

            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = io.nextInt();
            }

            SegmentTree tree = new SegmentTree(arr);

            var res = tree.tree[0].seg;// tree.get(0, n);
            System.out.println(res);

            while (m-- > 0) {
                int i = io.nextInt();
                int v = io.nextInt();
                tree.set(i, v);
                res = tree.tree[0].seg;// tree.get(0, n);
                System.out.println(res);
            }
        }
    }

    static class SegmentTree {
        static class Node {
            long seg, pref, suf, sum;

            Node(long x) {
                this(Math.max(x, 0), Math.max(x, 0), Math.max(x, 0), x);
            }

            Node(long s, long p, long su, long sum) {
                seg = s;
                pref = p;
                suf = su;
                this.sum = sum;
            }
        }

        static final Node ZERO = new Node(0);

        Node[] tree;
        int size;

        SegmentTree(int[] arr) {
            int n = arr.length;
            size = 1;
            while (size < n) {
                size *= 2;
            }
            tree = new Node[2 * size];
            for (int i = 0; i < tree.length; i++) {
                tree[i] = ZERO;
            }
            build(arr, 0, 0, size);
        }

        Node combine(Node a, Node b) {
            return new Node(
                    Math.max(a.seg, Math.max(b.seg, a.suf + b.pref)),
                    Math.max(a.pref, a.sum + b.pref),
                    Math.max(b.suf, b.sum + a.suf),
                    a.sum + b.sum);
        }

        void build(int[] arr, int x, int lx, int rx) {
            if (rx - lx == 1) {
                if (lx < arr.length) {
                    tree[x] = new Node(arr[lx]);
                }
                return;
            }
            int mx = (lx + rx) / 2;
            build(arr, x * 2 + 1, lx, mx);
            build(arr, x * 2 + 2, mx, rx);
            tree[x] = combine(tree[x * 2 + 1], tree[x * 2 + 2]);
        }

        void set(int i, int v) {
            set(0, 0, size, i, v);
        }

        void set(int x, int lx, int rx, int i, int v) {
            if (rx - lx == 1) {
                tree[x] = new Node(v);
                return;
            }
            int mx = (lx + rx) / 2;
            if (i < mx) {
                set(x * 2 + 1, lx, mx, i, v);
            } else {
                set(x * 2 + 2, mx, rx, i, v);
            }
            tree[x] = combine(tree[x * 2 + 1], tree[x * 2 + 2]);
        }

        Node get(int l, int r) {
            return get(0, 0, size, l, r);
        }

        Node get(int x, int lx, int rx, int l, int r) {
            if (r <= lx || l >= rx) {
                return ZERO;
            }
            if (l <= lx && rx <= r) {
                return tree[x];
            }
            int mx = (lx + rx) / 2;
            return combine(get(x * 2 + 1, lx, mx, l, r), get(x * 2 + 2, mx, rx, l, r));
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
