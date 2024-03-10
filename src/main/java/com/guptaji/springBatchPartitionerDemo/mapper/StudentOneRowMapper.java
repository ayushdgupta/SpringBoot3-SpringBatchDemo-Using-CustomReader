package com.guptaji.springBatchPartitionerDemo.mapper;

import com.guptaji.springBatchPartitionerDemo.entity.StudentOne;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

public class StudentOneRowMapper implements RowMapper<StudentOne> {

  Logger LOG = LogManager.getLogger(StudentOneRowMapper.class);

  @Override
  public StudentOne mapRow(ResultSet rs, int rowNum) throws SQLException {
    StudentOne studentOne = new StudentOne();
    studentOne.setId(rs.getInt("id"));
    studentOne.setName(rs.getString("name"));
    studentOne.setCollegeName(rs.getString("college_name"));
    return studentOne;
  }
}
