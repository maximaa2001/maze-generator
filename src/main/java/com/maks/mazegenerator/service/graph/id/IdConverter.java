package com.maks.mazegenerator.service.graph.id;

public interface IdConverter<T> {
    int convert(T oldId);
    T retrieve(int newId);
}
