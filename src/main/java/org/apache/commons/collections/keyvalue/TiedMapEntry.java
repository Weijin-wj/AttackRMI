package org.apache.commons.collections.keyvalue;

import org.apache.commons.collections.KeyValue;

import java.io.Serializable;
import java.util.Map;

public class TiedMapEntry implements Map.Entry, KeyValue, Serializable {
    private static final long serialVersionUID = -8453869361373831205L;
    private final Map map;
    private final Object key;

    public TiedMapEntry(Map map, Object key) {
        this.map = map;
        this.key = key;
    }

    @Override
    public Object getKey() {
        return null;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public Object setValue(Object value) {
        return null;
    }
}
