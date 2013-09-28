public class Main {
	public static int[] factors(int n) {
		List<Integer> lst = new ArrayList<Integer>();
		for (int i = 1; (long) i * i <= n; i++) {
			if (n % i == 0) {
				lst.add(i);
				if (i * i != n) {
					lst.add(n / i);
				}
			}
		}
		int[] f = new int[lst.size()];
		for (int i = 0; i < ans.length; i++) {
			f[i] = lst.get(i);
		}
		return f;
	}
}