package com.guptaji.springBatchPartitionerDemo.constants;

public class Constant {

  public static final String COUNT_QUERY = "select count(*) from public.student_one";
  public static final String DATA_EXTRACTION_QUERY = "select * from public.student_one";
  public static final String DATA_COUNT = "count";
  public static final String FINAL_QUERY = "finalQuery";
  public static final String OFFSET = "offset";
  public static final String LIMIT = "limit";
  public static final String IS_COMPLETED = "isCompleted";
  public static final String PARTITION_NO = "partitionNo";
  public static final String COMPLETED_STATUS = "COMPLETED";
}
