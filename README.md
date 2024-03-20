### Spring batch using custom reader
1. To enable spring batch in our code we need to add below dependency in our code -
```groovy
implementation 'org.springframework.boot:spring-boot-starter-batch'
```
2. Here I implemented a custom item reader to read the data from db because all existing item readers like **JDBCPagingItemReader & RepositoryItemReader etc.** was reading the same record in multiple threads i.e. there is some bug in there implementation.
3. So we implemented our own custom reader and used partitioner strategy for multithreading.

### Partitioner Strategy
1. In partitioner, we follow the master-slave architecture. where the same step (multithreaded one) will break into two parts -
   1. Master (who will supply configuration).
   2. Slave (who will actually perform the task).
2. in master step we will tell that what will be the grid size, how to segregate the data between the grids (partitions), on which step we need to apply partition and which task executor we will use.
3. For partitioner, we need the count of data that we gonna process.
4. for detail check spring boot official documentation -- https://docs.spring.io/spring-batch/reference/scalability.html
5. Grid size decides the total no. of slave steps to be created (But not the no. of threads will execute in parallel that depends on the what configuration we have provided in task executor and system resources).
6. Depends on provided configuration and system resources, no. of grids will execute in parallel at a time not the partitions (or records present in the grid) will execute in parallel i.e. multithreading will occur between the grids not inside the grid. If a grid contain 10 records then all 10 records will execute in series.

### Our Process
1. First we created on API and inserted around 400000 records in 'student_one' table now we are reading this table and inserting the data into another table 'student_two'.
2. First we are creating an entry in a table **spring_batch_log_entry** with 'INPROGRESS' status to track our batch run, although spring internally creates the entry in it's internal tables but we don't want to look into that.
3. Now we need to get the count so we created another tasklet to extract the count from DB.
4. Now we need to create the **PartitionHandler** to provide grid size, slave step (where the partitioner work) and task executor.
5. Now we need to create the taks executor to provide it's reference in the partition handler.
6. Now we need to create the slave step, where we will create our custom reader, writer and processor.
7. Now we need to create the master step where we will provide two information one is the 'partition hadler' and another one is 'partitioner' where we will define the segregation logic (partitioner logic) in the data and store it in the **StepExecutionContext**.
8. Now we need to create the partitioner.
9. Now we need to create the job listener which will work according to the job status i.e. if the job status is success then it'll update the status success in the table where we created the entry for the current run or failue in case of failure.
10. Now we need to create the job where the first step will be 'logEntryStep' second 'dataCountStep' third 'master step' fourth will be 'job listener'.
11. Now we need to trigger our job that can be done either by hitting an API but sometimes there is a possibility spring batch might also take 1 hr or more than that because of large size of data but an API has a time limit, within that it need to respond to the client so aprt from API triggering we are using **commandLineRunner** where we will override the run method and trigger our job using **JobLauncher**.

### Custom reader and Partitioner logic
1. Query
