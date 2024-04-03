package edu.sakovich.servlet.repository;

import java.util.Optional;
import java.util.Set;

public interface Repository<T, K> {
    T save(T t);

    Set<T> findAll();

    Optional<T> findById(K id);

    boolean update(T t);

    boolean deleteById(K id);
}
