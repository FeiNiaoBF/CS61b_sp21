package bstmap;

import java.lang.Comparable;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private BSTNode root;
    private int size;

    /**
     * BSTNode class
     */
        private class BSTNode {
            private K key;
            private V val;
            private BSTNode left, right;

            private BSTNode(K k, V v) {
                this.key = k;
                this.val = v;
            }
    }

    public BSTMap() {
            root = null;
            size = 0;
    }
    @Override
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null) throw new IllegalArgumentException("argument to containsKey() is null");
        return getNode(root, key) != null;
    }

    @Override
    public V get(K key) {
            BSTNode tmp = getNode(root, key);
            return tmp == null ? null : tmp.val;
    }

    private BSTNode getNode(BSTNode node, K key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp == 0) {
            return node;
        }else if (cmp < 0) {
            return getNode(node.left, key);
        }else {
            return getNode(node.right, key);
        }
    }

    @Override
    public int size() {
            return root == null ? 0 : this.size;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) throw new IllegalArgumentException("calls put() with a null key");
        root = put(root, key, value);
        size += 1;
    }
    private BSTNode put(BSTNode node, K key, V val) {
        if (node == null) {
            return new BSTNode(key, val);
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = put(node.left, key, val);
        else if (cmp > 0) node.right = put(node.right, key, val);
        else node.val = val;
        return node;
    }

    public void printInOrder() {
            printInOrderHelper(root);
    }

    private void printInOrderHelper(BSTNode node) {
            if (node == null) {
                return;
            }
            printInOrderHelper(node.left);
            System.out.println(node.key.toString() + "->" + node.val);
            printInOrderHelper(node.right);
    }

    @Override
    public Set<K> keySet() {
            throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    public Iterator<K> iterator() {
            return keySet().iterator();
    }
}
