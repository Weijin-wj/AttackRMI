package org.apache.commons.collections.map;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public abstract class AbstractMapDecorator implements Map {
    protected transient Map map;

    protected AbstractMapDecorator() {
    }

    public AbstractMapDecorator(Map map) {
        if (map == null) {
            throw new IllegalArgumentException("Map must not be null");
        } else {
            this.map = map;
        }
    }

    protected Map getMap() {
        return this.map;
    }

    public void clear() {
        this.map.clear();
    }

    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }

    public Set entrySet() {
        return this.map.entrySet();
    }

    public Object get(Object key) {
        return this.map.get(key);
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public Set keySet() {
        return this.map.keySet();
    }

    public Object put(Object key, Object value) {
        return this.map.put(key, value);
    }

    public void putAll(Map mapToCopy) {
        this.map.putAll(mapToCopy);
    }

    public Object remove(Object key) {
        return this.map.remove(key);
    }

    public int size() {
        return this.map.size();
    }

    public Collection values() {
        return this.map.values();
    }

    public boolean equals(Object object) {
        return object == this ? true : this.map.equals(object);
    }

    public int hashCode() {
        return this.map.hashCode();
    }

    public String toString() {
        return this.map.toString();
    }
}

