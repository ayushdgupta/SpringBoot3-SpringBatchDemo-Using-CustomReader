package com.guptaji.springBatchPartitionerDemo.repositories;

import com.guptaji.springBatchPartitionerDemo.entity.SpringBatchLogEntry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringBatchLogEntryRepo extends JpaRepository<SpringBatchLogEntry, Long> {}
