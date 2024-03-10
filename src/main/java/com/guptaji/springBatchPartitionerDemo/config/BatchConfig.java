package com.guptaji.springBatchPartitionerDemo.config;

import com.guptaji.springBatchPartitionerDemo.entity.StudentOne;
import com.guptaji.springBatchPartitionerDemo.entity.StudentTwo;
import com.guptaji.springBatchPartitionerDemo.partitioner.StudentPartitioner;
import com.guptaji.springBatchPartitionerDemo.processor.StudentOneDataProcessor;
import com.guptaji.springBatchPartitionerDemo.reader.StudentOneDataReader;
import com.guptaji.springBatchPartitionerDemo.tasklet.DataCountTasklet;
import com.guptaji.springBatchPartitionerDemo.writer.StudentTwoDataWriter;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

  Logger LOG = LogManager.getLogger(BatchConfig.class);

  @Value("${GRID_SIZE}")
  public int gridSize;

  @Value("${CHUNK_SIZE}")
  public int chunkSize;

  /**
   * @param jobRepository
   * @param platformTransactionManager
   * @param dataCountTasklet Here first we will read the count of data using this tasklet.
   */
  @Bean
  public Step dataCountStep(
      JobRepository jobRepository,
      PlatformTransactionManager platformTransactionManager,
      @Qualifier("dataCountTasklet") DataCountTasklet dataCountTasklet) {
    return new StepBuilder("DATA_COUNT_STEP", jobRepository)
        .tasklet(dataCountTasklet, platformTransactionManager)
        .build();
  }

  /**
   * Here we will create Partition Handler where we will provide the info like which task executor
   * it needs to use and which step it needs to execute under partition.
   */
  @Bean
  public PartitionHandler partitionHandler(
      @Qualifier("studentSlaveStep") Step studentSlaveStep,
      @Qualifier("taskExecutor") TaskExecutor taskExecutor) {
    TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
    handler.setGridSize(gridSize);
    handler.setTaskExecutor(taskExecutor);
    handler.setStep(studentSlaveStep);
    return handler;
  }

  /**
   * Task executor that will define the no. of cores available for processing, and set the thread
   * name
   *
   * @return
   */
  @Bean
  public TaskExecutor taskExecutor() {
    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    // calculating the no. of cores available to perform processing in the system in which
    // code is executing
    int coreSize = Runtime.getRuntime().availableProcessors();
    LOG.info("No. of processors available for processing {}", coreSize);
    taskExecutor.setCorePoolSize(coreSize);
    taskExecutor.setMaxPoolSize(coreSize * 2);
    taskExecutor.setThreadNamePrefix("GuptajiThread-");
    return taskExecutor;
  }

  /**
   * Let's create a slave step for partition handler which will read the data from studentOne table,
   * convert that data into studentTwo Object and then write that data into student two table in db.
   */
  @Bean
  public Step studentSlaveStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      @Qualifier("studentOneDataReader") StudentOneDataReader studentOneDataReader,
      @Qualifier("studentOneDataProcessor") StudentOneDataProcessor studentOneDataProcessor,
      @Qualifier("studentTwoDataWriter") StudentTwoDataWriter studentTwoDataWriter) {
    return new StepBuilder("STUDENT_SLAVE_STEP", jobRepository)
        .<List<StudentOne>, List<StudentTwo>>chunk(
            chunkSize, transactionManager) // just write whatever you have in itemProcessor
        .reader(studentOneDataReader)
        .processor(studentOneDataProcessor)
        .writer(studentTwoDataWriter)
        .build();
  }

  /**
   * Let's write the master step now, which need a partitioner and a partition handler that we have
   * already written
   */
  @Bean
  public Step studentMasterStep(
      JobRepository jobRepository,
      @Qualifier("partitionHandler") PartitionHandler partitionHandler,
      @Qualifier("studentPartitioner") StudentPartitioner studentPartitioner,
      @Qualifier("studentSlaveStep") Step studentSlaveStep) {
    return new StepBuilder("STUDENT_MASTER_STEP", jobRepository)
        .partitioner(studentSlaveStep.getName(), studentPartitioner)
        .partitionHandler(partitionHandler)
        .step(studentSlaveStep)
        .build();
  }

  /** Let's create a job which first count the data -> read -> process -> write */
  @Bean
  public Job studentJob(
      JobRepository jobRepository,
      @Qualifier("dataCountStep") Step dataCountStep,
      @Qualifier("studentMasterStep") Step studentMasterStep) {
    return new JobBuilder("STUDENT_JOB", jobRepository)
        .start(dataCountStep)
        .next(studentMasterStep)
        .build();
  }
}
