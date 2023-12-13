package cfitmo_academy.segtree_1.part3;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.InputMismatchException;

// https://codeforces.com/edu/course/2/lesson/4/3/practice/contest/274545/problem/A
public class Task2 {
    static class SegTree {
        int[] tree;
        int size;
        int n;

        SegTree(int n) {
            size = 1;
            while (size < n) {
                size *= 2;
            }
            this.n = n;
            tree = new int[2 * size];
            build(0, 0, size, n);
        }

        void build(int x, int lx, int rx, int n) {
            if (rx - lx == 1) {
                if (lx < n) {
                    tree[x] = 1;
                }
                return;
            }
            int mx = (lx + rx) / 2;
            build(x * 2 + 1, lx, mx, n);
            build(x * 2 + 2, mx, rx, n);
            tree[x] = tree[x * 2 + 1] + tree[x * 2 + 2];
        }

        void set(int i) {
            set(0, 0, size, i);
        }

        void set(int x, int lx, int rx, int i) {
            if (rx - lx == 1) {
                tree[x] = 0;
                return;
            }
            int mx = (lx + rx) / 2;
            if (i < mx) {
                set(x * 2 + 1, lx, mx, i);
            } else {
                set(x * 2 + 2, mx, rx, i);
            }
            tree[x] = tree[x * 2 + 1] + tree[x * 2 + 2];
        }

        int find(int k) {
            return find(0, 0, size, n - k - 1);
        }

        int find(int x, int lx, int rx, int k) {
            if (lx == rx - 1) {
                return lx;
            }

            int mx = (lx + rx) / 2;
            if (k < tree[x * 2 + 1]) {
                return find(x * 2 + 1, lx, mx, k);
            } else {
                return find(x * 2 + 2, mx, rx, k - tree[x * 2 + 1]);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        try (FastIO fio = new FastIO()) {
            int n = fio.nextInt();
            SegTree tree = new SegTree(n);
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = fio.nextInt();
            }

            int r = n - 1;
            int[] res = new int[n];
            for (int i = n - 1; i >= 0; i--) {
                int num = arr[i];
                int found = tree.find(num);
                res[r--] = found + 1;
                tree.n--;
                tree.set(found);
            }

            for (int i = 0; i < res.length; i++) {
                int num = res[i];
                if (i < res.length - 1) {
                    System.out.printf("%s ", num);
                } else {
                    System.out.println(num);
                }
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
