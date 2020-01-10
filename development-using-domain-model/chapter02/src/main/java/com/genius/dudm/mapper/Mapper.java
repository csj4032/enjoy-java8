package com.genius.dudm.mapper;

import java.util.List;

public interface Mapper<T> {

	List<T> findAll();

	T findByKey(long id);
}