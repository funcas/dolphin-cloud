### elastic-job-springboot-starter

* 使用方式
```
@EnableElasticJob
public class JobApplication {
    
}

@ElasticJobConf(name = "SimpleJobDemo", 
    cron = "0/10 * * * * ?", description = "任务描述")
public class SimpleJobDemo implements SimpleJob {
...
}

```

* properties 文件配置方式
```
 @ElasticJobConf(name = "SimpleJobDemo")

 elastic.job.MySimpleJob.cron=0/10 * * * * ?
 elastic.job.MySimpleJob.overwrite=true
 elastic.job.MySimpleJob.shardingTotalCount=1
 elastic.job.MySimpleJob.shardingItemParameters=0=0,1=1
 elastic.job.MySimpleJob.jobParameter=test
 elastic.job.MySimpleJob.failover=true
 elastic.job.MySimpleJob.misfire=true
 elastic.job.MySimpleJob.description=simple job
 elastic.job.MySimpleJob.monitorExecution=false
 elastic.job.MySimpleJob.listener=com.cxytiandi.job.core.MessageElasticJobListener
 elastic.job.MySimpleJob.jobExceptionHandler=com.cxytiandi.job.core.CustomJobExceptionHandler
 elastic.job.MySimpleJob.disabled=true
 ```