package cfitmo_academy.segtree.part3;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.InputMismatchException;

// https://codeforces.com/edu/course/2/lesson/4/3/practice/contest/274545/problem/A
public class Task1 {
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

        void set(int v) {
            set(0, 0, size, v);
        }

        void set(int x, int xl, int xr, int v) {
            if (xr - xl == 1) {
                tree[x] = 1;
                return;
            }

            int xm = (xl + xr) / 2;
            if (v < xm) {
                set(x * 2 + 1, xl, xm, v);
            } else {
                set(x * 2 + 2, xm, xr, v);
            }
            tree[x] = tree[x * 2 + 1] + tree[x * 2 + 2];
        }

        int sum(int l) {
            return sum(0, 0, size, l, size);
        }

        int sum(int x, int xl, int xr, int l, int r) {
            if (xr <= l || xl >= r) {
                return 0;
            }
            if (l <= xl && xr <= r) {
                return tree[x];
            }

            int xm = (xl + xr) / 2;
            return sum(x * 2 + 1, xl, xm, l, r) + sum(x * 2 + 2, xm, xr, l, r);
        }
    }

    public static void main(String[] args) throws Exception {
        try (FastIO fio = new FastIO()) {
            int n = fio.nextInt();
            SegTree tree = new SegTree(n);
            for (int i = 0; i < n; i++) {
                int num = fio.nextInt() - 1;
                System.out.printf("%s ", tree.sum(num));
                tree.set(num);
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
