package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.MsgData;

public interface MsgDataRepository extends JpaRepository<MsgData, Long> {

}