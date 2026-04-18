package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private MyNode[] table = new MyNode[DEFAULT_INITIAL_CAPACITY];
    private int size;
    private int capacity = DEFAULT_INITIAL_CAPACITY;
    private int index;
    private int hash;

    @Override
    public void put(K key, V value) {
        hash = hashCheck(key);
        index = getIndex(hash);
        if (table[index] == null) {
            MyNode newNode = new MyNode<>(key, value, hash);
            table[index] = newNode;
            size++;
            if (size > capacity * DEFAULT_LOAD_FACTOR) {
                resize();
            }
        } else {
            MyNode current = table[index];
            while (current != null) {
                if (Objects.equals(current.key, key) && current.hash == hash) {
                    current.value = value;
                    break;
                } else {
                    if (current.next == null) {
                        current.next = new MyNode<>(key, value, hash);
                        size++;
                        if (size > capacity * DEFAULT_LOAD_FACTOR) {
                            resize();
                        }
                        break;
                    }
                    current = current.next;
                }
            }
        }
    }

    public void resize() {
        MyNode[] newTable = new MyNode[capacity * 2];
        capacity = capacity * 2;
        for (int i = 0; i < table.length; i++) {
            MyNode current = table[i];
            while (current != null) {
                int index = current.hash & (capacity - 1);
                MyNode newCurrent = current.next;
                current.next = null;
                if (newTable[index] == null) {
                    newTable[index] = current;
                } else {
                    MyNode old = newTable[index];
                    while (old.next != null) {
                        old = old.next;
                    }
                    old.next = current;
                }
                current = newCurrent;
            }
        }
        table = newTable;
    }

    @Override
    public V getValue(K key) {
        hash = hashCheck(key);
        index = getIndex(hash);
        MyNode current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key) && current.hash == hash) {
                return (V) current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(int hash) {
        int index = hash & (capacity - 1);
        return index;
    }

    private int hashCheck(K key) {
        int hash;
        if (key == null) {
            hash = 0;
        } else {
            hash = key.hashCode();
        }
        return hash;
    }

    static class MyNode<K, V> {
        private K key;
        private V value;
        private MyNode next;
        private int hash;

        public MyNode(K key, V value, int hash) {
            this.key = key;
            this.value = value;
            this.hash = hash;
        }
    }
}
