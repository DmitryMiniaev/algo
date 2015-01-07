import static java.lang.Math.max;
import static java.lang.Math.min;
 
//segment tree for sum function
class SegmentTree {
    	int n;
    	long[] a;
    	long[] t;
    	int INDENTITY = 0;
    	
    	SegmentTree(long[] a) {
    		n = a.length;
    		this.a = a;
    		t = new long[4 * n];
    		build(0, 0, n - 1);
    	}
		
    	long f(long a, long b){
			return a + b;
		}
		
    	void build(int v, int tl, int tr) {
    		if(tl == tr) {
    			t[v] = a[tl];
    		} else {
    			int tm = (tl + tr) / 2;
        		build(v * 2 + 1, tl, tm);
        		build(v * 2 + 2, tm + 1, tr);
        		t[v] = f(t[v * 2 + 1],t[v * 2 + 2]);
    		}
    	}
    	
    	long query(int l, int r) {
    		return query(0, 0, n - 1, l, r);
    	}
    	
    	void push(int v) {
    		if(t[v] != -1) {
    			t[v * 2 + 1] = t[v * 2 + 2] = t[v];
    			t[v] = -1;
    		}
    	}
    	
    	void update(int l, int r, long x) { 
    		update(0, 0, n - 1, l, r, x);
    	}
    	
    	void update(int v, int tl, int tr, int l, int r, long color) {
    		if(l > r) {
    			return;
    		}
    		if(l == tl && r == tr) {
    			t[v] = color;
    		} else {
    			push(v);
        		int tm = (tl + tr) / 2;
        		update(v * 2 + 1, tl, tm, l, min(r, tm), color);
        		update(v * 2 + 2, tm + 1, tr, max(l, tm + 1), r, color);
    		}
    	}
    	
    	long query(int v, int tl, int tr, int l, int r) {
    		if(l > r) {
    			return INDENTITY;
    		}
    		if(l == tl && r == tr && t[v] != -1) {
    			return t[v] * (r - l + 1);
    		} else {
    			push(v);
        		int tm = (tl + tr) / 2;
        		return f(query(v * 2 + 1, tl, tm, l, min(r, tm)),
						 query(v * 2 + 2, tm + 1, tr, max(l, tm + 1), r));
    		}	
    	}
}