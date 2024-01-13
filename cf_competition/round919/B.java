package cf_competition.round919;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.InputMismatchException;

public class B {
    static long solve(Integer[] arr, int k, int x) {
        Arrays.sort(arr, (a, b) -> b - a);
        var sum = 0;
        for (int i = 0; i < arr.length; i++) {
            if (i < x) {
                sum -= arr[i];
            } else {
                sum += arr[i];
            }
        }
        var best = sum;
        for (int i = 0; i < k; i++) {
            sum += arr[i];
            if (i + x < arr.length) {
                sum -= 2 * arr[i + x];
            }
            best = Math.max(best, sum);
        }
        return best;
    }

    public static void main(String[] args) {
        try (FastIO io = new FastIO()) {
            int t = io.nextInt();
            while (t-- > 0) {
                var n = io.nextInt();
                var k = io.nextInt();
                var x = io.nextInt();
                Integer[] arr = new Integer[n];
                for (int i = 0; i < n; i++) {
                    arr[i] = io.nextInt();
                }
                var res = solve(arr, k, x);
                System.out.println(res);
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
