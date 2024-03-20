package com.guptaji.springBatchPartitionerDemo.listener;

import static com.guptaji.springBatchPartitionerDemo.constants.Constant.*;

import com.guptaji.springBatchPartitionerDemo.entity.SpringBatchLogEntry;
import com.guptaji.springBatchPartitionerDemo.repositories.SpringBatchLogEntryRepo;
import com.guptaji.springBatchPartitionerDemo.util.CommonUtil;

import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StudentJobListener implements JobExecutionListener {

  Logger LOG = LogManager.getLogger(StudentJobListener.class);

  @Autowired private SpringBatchLogEntryRepo springBatchLogEntryRepo;

  @Override
  public void beforeJob(JobExecution jobExecution) {
    JobExecutionListener.super.beforeJob(jobExecution);
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    BatchStatus status = jobExecution.getStatus();
    String jobStatus = ERROR_STATUS;
    ExecutionContext jobExecutionContext = jobExecution.getExecutionContext();
    if (COMPLETED_STATUS.equalsIgnoreCase(String.valueOf(status))) {
      jobStatus = COMPLETED_STATUS;
    }
    LOG.info("Job status {}", jobStatus.toString());
    long seqId = jobExecutionContext.getLong(SEQ_ID);
    Timestamp createdTimestamp = (Timestamp) jobExecutionContext.get(TIMESTAMP);
    String jobId = jobExecution.getJobParameters().getString(JOB_ID);
    SpringBatchLogEntry logEntry =
        CommonUtil.constructSpringBatchLogEntity(jobId, createdTimestamp, jobStatus);
    logEntry.setSeqId(seqId);
    logEntry.setUpdatedTimestamp(Timestamp.valueOf(CommonUtil.createCurrentTimestamp()));
    springBatchLogEntryRepo.save(logEntry);
  }
}
