package dao;

import java.util.List;

public interface Dao<E, N> {
    List<E> findAll();

    E findById(N id);

    void save(E e);

    void update(E e);

    void delete(N id);
}
