class SplayTree {
		
		Node root;
		
		static class Node {
			Node left, right, parent;
			int key;
			
			Node(int key, Node left, Node right) {
				this.key = key;
				this.left = left;
				this.right = right;
			}
			
		}
		
		static void setParent(Node child, Node parent) {
			if(child != null) child.parent = parent;
		}
		
		static void propagate(Node n) {
			setParent(n.left, n);
			setParent(n.right, n);
		}
		
		static Node rotate(Node p, Node child) {
			Node gp = p.parent;
			if(gp != null) {
				if(gp.left == p) {
					gp.left = child;
				} else {
					gp.right = child;	
				}
			}
			if(p.left == child) {
				p.left = child.right;
				child.right = p;
			} else {
				p.right = child.left;
				child.left = p;
			}
			propagate(child);
			propagate(p);
			child.parent = gp;
			return child;
		}
		
		public Node splay(Node v) {
			if(v.parent == null) return v;
			Node p = v.parent;
			Node gp = p.parent;
			return (gp == null) 
					? rotate(p, v)
					: ((gp.left == p && p.left == v) ||
					  (gp.right == p && p.right == v)
					  ? splay(rotate(rotate(gp, p), v))
				      : splay(rotate(gp, rotate(p, v))));
		}
		
		public boolean contains(int key) {
			return (root = find(root, key)).key == key;
		}
		
		public Node find(Node v, int key) {
			if(v == null) return null;
			int cmp = key - v.key;
			if(cmp == 0) return splay(v);
			if(cmp < 0 && v.left != null) return find(v.left, key);
			if(cmp > 0 && v.right != null) return find(v.right, key);
			return splay(v);
		}
		
		public Node[] split(Node root, int key) {
			if(root == null) {
				return new Node[]{null, null};
			}
			root = find(root, key);
			if(root.key == key) {
				setParent(root.left, null);
				setParent(root.right, null);
				return new Node[] {root.left, root.right};
			} else if(root.key < key) {
				Node right = root.right;
				root.right = null;
				setParent(right, null);
				return new Node[] {root, right};
			} else {
				Node left = root.left;
				root.left = null;
				setParent(left, null);
				return new Node[] {left, root};
			}
		}
		
		public void add(Node p, int key) {
			Node[] childs = split(p, key);
			propagate(root = new Node(key, childs[0], childs[1]));
		}
		
		public Node remove(int key) {
			Node item = find(root, key);
			setParent(item.left, null);
			setParent(item.right, null);
			return root = merge(item.left, item.right);
		}
		
		public Node merge(Node left, Node right) {
			if(left == null) return right;
			if(right == null) return left;
			right = find(right, left.key);
			right.left = left;
			left.parent = right;
			return right;
		}	
	}
