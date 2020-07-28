package com.example.demo.dao;

import java.util.List;

import com.example.demo.entity.MsgData;

public interface MsgDataDao<T> {
	public List<MsgData> getAll();
	
	public MsgData findById(long id);
}