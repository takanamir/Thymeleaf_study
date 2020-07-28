package com.example.demo.bean;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entity.MyData;
import com.example.demo.repository.MyDataRepository;

public class MyDataBean {
	@Autowired
	MyDataRepository repository;

	public String getTableTagById(Long id) {
		Optional<MyData> opt = repository.findById(id);
		MyData data = opt.get();
		String result = "<tr><td>" + data.getName() + "</td><td>" + data.getPhoneNumber() + "</td><td>" + data.getMail() + "</td><td>" + data.getAge()
				+ "</td><td>" + data.getMemo() + "</td></tr>";
		return result;
	}
}