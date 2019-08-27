package com.aaronhowser1.documentmod.json.factory;

// Marker interface so that the map in DocumentationLoader is a little little bit more robust
public interface Factory<T> {
    @SuppressWarnings("unchecked")
    default T asT() {
        return (T) this;
    }
}
