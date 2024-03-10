package com.guptaji.springBatchPartitionerDemo;

import static com.guptaji.springBatchPartitionerDemo.constants.Constant.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.slf4j.MDCContextMap;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBatchPartitionerDemoApplication implements CommandLineRunner {

  Logger LOG = LogManager.getLogger(SpringBatchPartitionerDemoApplication.class);

  @Autowired private JobLauncher jobLauncher;

  @Autowired
  @Qualifier("studentJob")
  private Job job;

  public static void main(String[] args) {
    SpringApplication.run(SpringBatchPartitionerDemoApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    String jobId = UUID.randomUUID().toString();
    JobParameters parameters =
        new JobParametersBuilder()
            .addString("jobId", jobId)
            .addLocalDateTime("startAt", LocalDateTime.now())
            .toJobParameters();

    LOG.info("Starting the student job with params {}", parameters);

    try {
      JobExecution execution = jobLauncher.run(job, parameters);

      if (COMPLETED_STATUS.equalsIgnoreCase(String.valueOf(execution.getStatus()))) {
        LOG.info("Job completed successfully");
        System.exit(0);
      } else {
        LOG.error("Exception encountered during student job execution");
      }
    } catch (Exception e) {
      LOG.error(
          "Exception encountered during student job execution and the exception message is {} and the exception object is {}",
          e.getMessage(),
          e);
      System.exit(1);
    }
  }
}
