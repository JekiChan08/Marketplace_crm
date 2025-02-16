package com.example.marketplace_crm.Service;

import java.util.List;
import java.util.Optional;

public interface BaseService<T, ID> {
    List<T> getAll();
    T getById(ID id);
    T save(T entity);
    void delete(ID id);
}

