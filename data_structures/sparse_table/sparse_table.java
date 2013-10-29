//neerc.ifmo.ru/wiki/index.php?title=Решение_RMQ_с_помощью_разреженной_таблицы
public class SparseTable {

    private final int N;
    public  int[][] dp;
    private int[] logTable;
    private int[] a;

    public SparseTable(int[] a) {
        this.a = a;
        N = a.length;
        logTable = new int[N + 1];
        for (int i = 2; i <= N; i++)
            logTable[i] = logTable[i >> 1] + 1;
        dp = new int[N][logTable[N] + 1];
        //init dp
        for (int i = 0; i < N; i++) {
            dp[i][0] = i;
        }
        //calculate dynamic
        for (int log = 1; (1 << log) < N; log++) {
            for (int i = 0; i + (1 << log) <= N; i++) {
                int x = dp[i][log - 1];
                int y = dp[i + (1 << log - 1)][log - 1];
                dp[i][log] = (a[x] >= a[y]) ? x : y;
            }
        }
    }

    public int query(int i, int j) {
        int log = logTable[j - i];
        int x = dp[i][log];
        int y = dp[j - (1 << log) + 1][log];
        return a[x] >= a[y] ? x : y;
    }
}
