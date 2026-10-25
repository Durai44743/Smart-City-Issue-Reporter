package com.smartcity.dao;

import java.util.List;
import java.util.Optional;

/** Generic interface (Unit IV: generic classes and methods). */
public interface Repository<T, ID> {
    void save(T item);
    Optional<T> findById(ID id);
    List<T> findAll();
    void update(T item);
}
