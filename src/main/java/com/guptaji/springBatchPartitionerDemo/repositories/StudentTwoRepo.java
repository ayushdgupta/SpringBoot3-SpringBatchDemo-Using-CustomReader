package com.guptaji.springBatchPartitionerDemo.repositories;

import com.guptaji.springBatchPartitionerDemo.entity.StudentTwo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentTwoRepo extends JpaRepository<StudentTwo, Integer> {}
