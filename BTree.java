package persistence;

/**
 * @author dima
 * @date 11/4/14
 */
public class BTree {

    private int T;
    private int MAX_NODE_SIZE;
    private Node root;
    private Pair pair = new Pair();

    public BTree(int t) {
        T = t;
        MAX_NODE_SIZE = 2 * T + 1;
        root = new Node();
        root.leaf = true;
    }

    private static class Pair {
        int key;
        int val;

        Pair result(int key, int val) {
            this.key = key;
            this.val = val;
            return this;
        }
    }

    private class Node {
        boolean leaf;
        int size;
        int[] keys = new int[MAX_NODE_SIZE];
        int[] vals = new int[MAX_NODE_SIZE];
        Node[] c = new Node[MAX_NODE_SIZE + 1];

        int index(int key) {
            int i = 0;
            while(i < size && key > keys[i]) i++;
            return i;
        }

        void remove(int key) {
            //removes if leaf
            int i = 0;
            while(i < size && key > keys[i]) i++;
            for(int j = i + 1; j < size; j++) {
                keys[j - 1] = keys[j];
                vals[j - 1] = vals[j];
            }
            size--;
        }
    }

    public Integer _get(Node node, int key) {
        int j = 0, n = node.size;
        while (j < node.size && key > node.keys[j]) j++;
        if(j < n && key == node.keys[j]) return node.vals[j];
        if(node.leaf) return null;
        return _get(node.c[j], key);
    }

    public Integer get(int key) {
        Integer result =_get(root, key);
        if(result == null) return null;
        return result;
    }

    public Integer getOrDefault(int key, Integer defaultValue) {
        Integer result = get(key);
        if(result == null) return defaultValue;
        return result;
    }

    public void put(int key, int val) {
        if(root.size == MAX_NODE_SIZE) {
            Node newOne = new Node();
            newOne.c[0] = root;
            root = newOne;
            split(root, 0, newOne.c[0]);
            _put(root, key, val);
        } else {
            _put(root, key, val);
        }
    }

    private void split(Node x, int i, Node y) {
        Node z = new Node();
        z.leaf = y.leaf;
        int mid = MAX_NODE_SIZE / 2;
        for(int j = mid + 1; j < MAX_NODE_SIZE; j++) {
            z.keys[j - mid - 1] = y.keys[j];
            z.vals[j - mid - 1] = y.vals[j];
        }
        z.size = mid;
        y.size = mid;
        if(!y.leaf) {
            for(int j = mid + 1; j <= MAX_NODE_SIZE; j++) {
                z.c[j - mid - 1] = y.c[j];
            }
        }
        for(int j = x.size - 1; j >= i ; j--) {
            x.keys[j + 1] = x.keys[j];
            x.vals[j + 1] = x.vals[j];
        }
        x.keys[i] = y.keys[mid];
        x.vals[i] = y.vals[mid];
        for(int j = x.size; j >= i + 1; j--) {
            x.c[j + 1] = x.c[j];
        }
        x.c[i + 1] = z;
        x.size++;
    }

    private void _put(Node node, int key, int val) {
        int i = node.size - 1;
        if(node.leaf) {
            while (i >= 0 && key < node.keys[i]) {
                node.vals[i + 1] = node.vals[i];
                node.keys[i + 1] = node.keys[i];
                i--;
            }
            node.keys[i + 1] = key;
            node.vals[i + 1] = val;
            node.size++;
        } else {
            while (i >= 0 && key < node.keys[i]) i--;
            i++;
            Node child = node.c[i];
            if(child.size == MAX_NODE_SIZE) {
                split(node, i, child);
                if(key > node.keys[i]) i++;
            }
            _put(node.c[i], key, val);
        }
    }

    private Pair getPrev(Node root) {
        Node cur = root;
        while (!cur.leaf) {
            cur = cur.c[cur.size];
        }
        int n = cur.size - 1;
        return pair.result(cur.keys[n], cur.vals[n]);
    }

    private Pair getNext(Node root) {
        Node cur = root;
        while (!cur.leaf) {
            cur = cur.c[0];
        }
        int key = cur.keys[0];
        int val = cur.vals[0];
        return pair.result(key, val);
    }

    public boolean remove(int key) {
        return _remove(root, key);
    }

    private boolean _remove(Node root, int key) {
        int idx = root.index(key);
        if(idx < root.size && root.keys[idx] == key) {
            if(root.leaf) {
                root.remove(key);
                return true;
            } else {
                Node lc = root.c[idx];
                Node rc = root.c[idx + 1];
                if(lc.size >= T + 1) {
                    Pair p = getPrev(lc);
                    root.keys[idx] = p.key;
                    root.vals[idx] = p.val;
                    return _remove(lc, p.key);
                } else if(rc.size >= T + 1) {
                    Pair p = getNext(rc);
                    root.keys[idx] = p.key;
                    root.vals[idx] = p.val;
                    return _remove(rc, p.key);
                } else {
                    Node merged = merge(root, idx, lc, rc);
                    return _remove(merged, key);
                }
            }
        } else {
            if(root.leaf) return false;
            Node child = root.c[idx];
            if(child.size == T) {
                int ls = idx - 1;
                int rs = idx + 1;
                if (ls >= 0 && root.c[ls].size >= T + 1) {
                    borrowLeft(child, root, root.c[ls], ls);
                } else if (rs < root.size + 1 && root.c[rs].size >= T + 1) {
                    borrowRight(child, root, root.c[rs], idx);
                } else {
                    if(ls >= 0) {
                        child = merge(root, ls, root.c[ls], child);
                    } else{
                        child = merge(root, idx, child, root.c[rs]);
                    }
                }
            }
            return _remove(child, key);
        }
    }

    private Node merge(Node root, int idx, Node lc, Node rc) {
        lc.keys[lc.size] = root.keys[idx];
        lc.vals[lc.size] = root.vals[idx];
        int newSize = lc.size + 1 + rc.size;
        for(int j = lc.size + 1; j < newSize; j++) {
            lc.keys[j] = rc.keys[j - lc.size - 1];
            lc.vals[j] = rc.vals[j - lc.size - 1];
        }
        if(!lc.leaf) {
            for(int j = lc.size + 1; j < newSize + 1; j++) {
                lc.c[j] = rc.c[j - lc.size - 1];
            }
        }
        lc.size = newSize;
        for(int j = idx; j < root.size - 1; j++) {
            root.keys[j] = root.keys[j + 1];
            root.vals[j] = root.vals[j + 1];
        }
        for (int j = idx + 1; j < root.size; j++) {
            root.c[j] = root.c[j + 1];
        }
        root.size--;
        return root;
    }

    private void borrowLeft(Node node, Node root, Node sibling, int sepId) {
        int sKey;
        int sVal;
        Node sNode;
        sKey = sibling.keys[sibling.size - 1];
        sVal = sibling.vals[sibling.size - 1];
        sNode = sibling.c[sibling.size];
        sibling.size--;

        int rKey = root.keys[sepId];
        int rVal = root.vals[sepId];
        root.keys[sepId] = sKey;
        root.vals[sepId] = sVal;

        for(int i = node.size; i >= 1; i--) {
            node.keys[i] = node.keys[i - 1];
            node.vals[i] = node.vals[i - 1];
        }
        for(int i = node.size + 1; i >= 1; i--) {
            node.c[i] = node.c[i - 1];
        }
        node.keys[0] = rKey;
        node.vals[0] = rVal;
        node.c[0] = sNode;
        node.size++;
    }

    private void borrowRight(Node node, Node root, Node sibling, int sepId) {
        int sKey;
        int sVal;
        Node sNode;
        sKey = sibling.keys[0];
        sVal = sibling.vals[0];
        sNode = sibling.c[0];
        for(int i = 1; i < sibling.size; i++) {
            sibling.keys[i - 1] = sibling.keys[i];
            sibling.vals[i - 1] = sibling.vals[i];
        }
        for(int i = 1; i < sibling.size + 1; i++) {
            sibling.c[i - 1] = sibling.c[i];
        }
        sibling.size--;
        int rKey = root.keys[sepId];
        int rVal = root.vals[sepId];
        root.keys[sepId] = sKey;
        root.vals[sepId] = sVal;

        node.keys[node.size] = rKey;
        node.vals[node.size] = rVal;
        node.c[node.size + 1] = sNode;
        node.size++;
    }

}
