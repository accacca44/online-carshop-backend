package edu.bbte.idde.keim2152.spring.repository;

import java.util.Collection;

public interface AbstractDao<T> {
    T create(T entity);

    T update(T entity);

    void delete(T entity);

    T findById(Long id);

    Collection<T> findAll();
}
