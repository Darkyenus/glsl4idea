/*
 *     Copyright 2010 Jean-Paul Balabanian and Yngve Devik Hammersland
 *
 *     This file is part of glsl4idea.
 *
 *     Glsl4idea is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as
 *     published by the Free Software Foundation, either version 3 of
 *     the License, or (at your option) any later version.
 *
 *     Glsl4idea is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with glsl4idea.  If not, see <http://www.gnu.org/licenses/>.
 */

package glslplugin.lang.parser;

import java.util.*;

/**
 * ScopedDictionary is basically a dictionary stack with the ability to
 * optionally query the underlying dictionaries.
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 21, 2009
 *         Time: 12:34:18 PM
 */
public class ScopedDictionary<T> {
    /**
     * The parent dictionary, null if none.
     */
    private ScopedDictionary<T> parent = null;
    private String debugName = "(noname)";
    private Map<String, T> map = new HashMap<String, T>();

    public ScopedDictionary() {
    }

    public ScopedDictionary(String debugName) {
        this.debugName = debugName;
    }

    public ScopedDictionary(ScopedDictionary<T> parent) {
        this.parent = parent;
    }

    public ScopedDictionary(ScopedDictionary<T> parent, String debugName) {
        this.parent = parent;
        this.debugName = debugName;
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    /**
     * Searches for the key in the parent dictionaries as well as this one.
     *
     * @param key the key to find.
     * @return true if the key was found, false otherwise.
     */
    public boolean containsKeyAnyScope(String key) {
        if (containsKey(key)) return true;
        else if (parent != null) return parent.containsKeyAnyScope(key);
        else return false;
    }

    public T get(String key) {
        return map.get(key);
    }

    /**
     * Searches for the key in the parent dictionaries as well as this one.
     *
     * @param key the key to find the value of.
     * @return the value of the given key, null if it was not found.
     */
    public T getAnyScope(String key) {
        if (containsKey(key)) return get(key);
        else if (parent != null) return parent.getAnyScope(key);
        else return null;
    }

    public T put(String name, T value) {
        return map.put(name, value);
    }

    public T remove(String key) {
        return map.remove(key);
    }

    public void putAll(Map<? extends String, ? extends T> m) {
        map.putAll(m);
    }

    public void clear() {
        map.clear();
    }

    public Set<String> keySet() {
        return map.keySet();
    }

    public String toString() {
        return debugName + ":{" + map.toString() + "}, " + (parent != null ? parent.toString() : ".");
    }
}
