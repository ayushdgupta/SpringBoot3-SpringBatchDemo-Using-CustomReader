package com.guptaji.springBatchPartitionerDemo.service;

import com.guptaji.springBatchPartitionerDemo.entity.StudentOne;
import com.guptaji.springBatchPartitionerDemo.repositories.StudentOneRepo;
import com.guptaji.springBatchPartitionerDemo.util.CommonUtil;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentOneService {

  Logger LOG = LogManager.getLogger(StudentOneService.class);

  @Autowired private StudentOneRepo studentOneRepo;

  public void createData(String name, String clgName, int count) {
    List<StudentOne> randomData = CommonUtil.createRandomData(name, clgName, count);
    studentOneRepo.saveAll(randomData);
    LOG.info("Created the {} rows in db with name {} and clg name {}.", count, name, clgName);
  }
}
