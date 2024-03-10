package com.guptaji.springBatchPartitionerDemo.repositories;

import com.guptaji.springBatchPartitionerDemo.entity.StudentOne;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentOneRepo extends JpaRepository<StudentOne, Integer> {}
