	static void sort(int[] a, int k) {
		int n = a.length;
		int[] b = new int[n];
		int[] c = new int[k];
		for(int i = 0; i < n; i++) {
			c[a[i]]++;
		}
		for(int i = 1; i < k; i++) c[i] += c[i - 1];
		for(int i = n - 1; i >= 0; i--) {
			b[--c[a[i]]] = a[i];
		}
		for(int i = 0; i < n; i++) a[i] = b[i];
	}
