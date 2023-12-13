package cfitmo_academy.segtree_1.part1;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.InputMismatchException;

public class Task1 {
    static class SegmentTree {
        long[] tree;
        int size;

        SegmentTree(long[] arr) {
            int n = arr.length;
            size = 1;
            while (size < n) {
                size *= 2;
            }
            tree = new long[2 * size];
            build(arr, 0, 0, size);
        }

        void build(long[] arr, int x, int lx, int rx) {
            if (rx - lx == 1) {
                if (lx < arr.length) {
                    tree[x] = arr[lx];
                }
                return;
            }
            int m = (lx + rx) / 2;
            build(arr, x * 2 + 1, lx, m);
            build(arr, x * 2 + 2, m, rx);
            tree[x] = tree[x * 2 + 1] + tree[x * 2 + 2];
        }

        void add(int i, int v) {
            add(0, 0, size, i, v);
        }

        void add(int x, int lx, int rx, int i, int v) {
            if (rx - lx == 1) {
                tree[x] = v;
                return;
            }
            int m = (lx + rx) / 2;
            if (i < m) {
                add(x * 2 + 1, lx, m, i, v);
            } else {
                add(x * 2 + 2, m, rx, i, v);
            }
            tree[x] = tree[x * 2 + 1] + tree[x * 2 + 2];
        }

        long min(int l, int r) {
            return sum(0, 0, size, l, r);
        }

        long sum(int x, int lx, int rx, int l, int r) {
            if (l >= rx || lx >= r) {
                return 0;
            }

            if (lx >= l && rx <= r) {
                return tree[x];
            }

            int m = (lx + rx) / 2;
            return sum(x * 2 + 1, lx, m, l, r) + sum(x * 2 + 2, m, rx, l, r);
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

    public static void main(String[] args) {
        try (FastIO io = new FastIO()) {
            int n = io.nextInt();
            int m = io.nextInt();

            long[] arr = new long[n];
            for (int i = 0; i < n; i++) {
                arr[i] = (long) io.nextInt();
            }

            SegmentTree tree = new SegmentTree(arr);

            while (m-- > 0) {
                int op = io.nextInt();
                if (op == 1) {
                    int i = io.nextInt();
                    int v = io.nextInt();
                    tree.add(i, v);
                } else {
                    int l = io.nextInt();
                    int r = io.nextInt();
                    long sum = tree.min(l, r);
                    System.out.println(sum);
                }
            }
        }
    }
}
