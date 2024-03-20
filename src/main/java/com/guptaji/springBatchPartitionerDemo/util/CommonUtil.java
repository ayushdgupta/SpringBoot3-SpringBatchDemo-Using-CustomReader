package com.guptaji.springBatchPartitionerDemo.util;

import static com.guptaji.springBatchPartitionerDemo.constants.Constant.*;

import com.guptaji.springBatchPartitionerDemo.entity.SpringBatchLogEntry;
import com.guptaji.springBatchPartitionerDemo.entity.StudentOne;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommonUtil {

  public static List<StudentOne> createRandomData(String name, String clgName, long count) {
    List<StudentOne> randomStudentList = new ArrayList<>();
    for (long i = 0; i < count; i++) {
      StudentOne studentOne = new StudentOne();
      studentOne.setName(name + i);
      studentOne.setCollegeName(clgName + i);
      randomStudentList.add(studentOne);
    }
    return randomStudentList;
  }

  public static LocalDateTime createCurrentTimestamp() {
    return LocalDateTime.now();
  }

  public static SpringBatchLogEntry constructSpringBatchLogEntity(
      String jobId, Timestamp timestamp, String status) {
    SpringBatchLogEntry logEntry = new SpringBatchLogEntry();
    logEntry.setCreatedTimestamp(timestamp);
    logEntry.setUpdatedTimestamp(timestamp);
    logEntry.setStatus(status);
    logEntry.setJobId(jobId);
    return logEntry;
  }
}
