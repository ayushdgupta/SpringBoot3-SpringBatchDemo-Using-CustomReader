package com.guptaji.springBatchPartitionerDemo.util;

import com.guptaji.springBatchPartitionerDemo.entity.StudentOne;

import java.util.ArrayList;
import java.util.List;

public class CommonUtil {

  public static List<StudentOne> createRandomData(String name, String clgName, int count) {
    List<StudentOne> randomStudentList = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      StudentOne studentOne = new StudentOne();
      studentOne.setName(name + i);
      studentOne.setCollegeName(clgName + i);
      randomStudentList.add(studentOne);
    }
    return randomStudentList;
  }
}
