package com.example.demo.dao;

import java.io.Serializable;
import java.util.List;

import com.example.demo.entity.MyData;

public interface MyDataDao <T> extends Serializable {
	public List<T> getAll();
	
	public T findById(long id);
	
	public List<T> findByName(String name);
	
	public List<T> find(String fstr);
	
	public List<MyData> findByAge(int min, int max);
}