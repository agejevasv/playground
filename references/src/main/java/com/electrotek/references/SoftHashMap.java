package com.electrotek.references;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * SoftHashMap implementation. Can be used as in-memory cache.
 * This implementation is not thread safe.
 * 
 * @author Viktoras Agejevas
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public final class SoftHashMap<K, V> extends AbstractMap<K, V> {

    /** Internal HashMap */
    private Map<K, SoftValueReference<K, V>> map = new HashMap<K, SoftValueReference<K, V>>();

    /** Queue for GC'ed values */
    private final ReferenceQueue<V> queue = new ReferenceQueue<V>();

    @Override
    public V get(Object key) {
        cleanUp();
        V value = null;
        SoftReference<V> reference = map.get(key);
        
        if (reference != null) {
            value = reference.get();
        } else {
            map.remove(key);
        }
        
        return value;
    }

    @Override
    public V put(K key, V value) {
        cleanUp();
        SoftValueReference<K, V> sv = map.put(key, new SoftValueReference<K, V>(key, value, queue));
        return sv != null ? sv.get() : null;
    }

    @Override
    public V remove(Object key) {
        cleanUp();
        return map.remove(key).get();
    }

    @Override
    public void clear() {
        cleanUp();
        map.clear();
    }

    @Override
    public int size() {
        cleanUp();
        return map.size();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Iterator<Entry<K, SoftValueReference<K, V>>> i = map.entrySet().iterator();
        Set<Entry<K, V>> set = new HashSet<Entry<K, V>>();
        
        while (i.hasNext()) {
            Entry<K, SoftValueReference<K, V>> e = i.next();
            SoftValueReference<K, V> ref = e.getValue();
            if (ref == null) {
                map.remove(e.getKey());
                continue;
            }
            Entry<K, V> entry = new SimpleImmutableEntry<K, V>(ref.key, ref.get());
            set.add(entry);
        }
        return set;
    }

    @Override
    public String toString() {
        return map.toString();
    }

    public boolean containsValue(Object value) {
        Iterator<Entry<K, SoftValueReference<K, V>>> i = map.entrySet().iterator();
        if (value == null) {
            while (i.hasNext()) {
                Entry<K, SoftValueReference<K, V>> e = i.next();
                SoftValueReference<K, V> ref = e.getValue();
                if (ref != null && ref.get() == null)
                    return true;
            }
        } else {
            while (i.hasNext()) {
                Entry<K, SoftValueReference<K, V>> e = i.next();
                SoftValueReference<K, V> ref = e.getValue();

                if (ref != null) {
                    V found = ref.get();
                    if (value.equals(found))
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @SuppressWarnings("unchecked")
    private void cleanUp() {
        SoftValueReference<K, V> sv = null;
        while ((sv = (SoftValueReference<K, V>) queue.poll()) != null) {
            map.remove(sv.getKey());
        }
    }
    
    private static class SoftValueReference<K, V> extends SoftReference<V> {
        private final K key;

        private SoftValueReference(K key, V value, ReferenceQueue<V> queue) {
            super(value, queue);
            this.key = key;
        }
        
        protected K getKey() {
            return key;
        }
    }
}