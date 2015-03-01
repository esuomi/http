package com.stackoverflow.guava;

import com.google.common.collect.ForwardingMap;

import java.io.Serializable;
import java.util.Map;

/**
 * Slightly modified version of publicly shared case insensitive multimap. The most Major change is uppercasing instead
 * of lowercasing keys for better unicode compatibility.
 *
 * @since 1.3.2015
 * @see <a href="http://stackoverflow.com/a/4592778/44523">is there a case insensitive multimap in google collections</a>
 */
public final class CaseInsensitiveForwardingMap<V> extends ForwardingMap<String, V> implements Serializable {

    private static final long serialVersionUID = -7741335486707072323L;

    private final Map<String, V> delegate;

    public CaseInsensitiveForwardingMap(final Map<String, V> delegate) {
        this.delegate = delegate;
    }

    @Override
    protected Map<String, V> delegate() {
        return delegate;
    }

    private static String upper(final Object key) {
        return key == null ? null : key.toString().toUpperCase();
    }

    @Override
    public V get(final Object key) {
        return delegate.get(upper(key));
    }

    @Override
    public void putAll(final Map<? extends String, ? extends V> map) {
        if(map == null || map.isEmpty()) {
            delegate.putAll(map);
        } else {
            for (Entry<? extends String, ? extends V> entry : map.entrySet()) {
                delegate.put(upper(entry.getKey()), entry.getValue());
            }
        }
    }

    @Override
    public V remove(final Object object) {
        return delegate.remove(upper(object));
    }

    @Override
    public boolean containsKey(final Object key) {
        return delegate.containsKey(upper(key));
    }

    @Override
    public V put(final String key, final V value) {
        return delegate.put(upper(key), value);
    }


}
