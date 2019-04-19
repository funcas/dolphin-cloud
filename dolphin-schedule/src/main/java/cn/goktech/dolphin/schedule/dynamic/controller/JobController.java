package cn.goktech.dolphin.schedule.dynamic.controller;

import cn.goktech.dolphin.schedule.dynamic.bean.Job;
import cn.goktech.dolphin.schedule.dynamic.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月19日
 */
@RestController
@RequestMapping("/elastic-job")
public class JobController {

    @Autowired
    private JobService jobService;

    /**
     * 添加动态任务（适用于脚本逻辑已存在的情况，只是动态添加了触发的时间）
     * @param job	任务信息
     * @return
     */
    @PostMapping("/job")
    public Object addJob(@RequestBody Job job) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", true);

        if (!StringUtils.hasText(job.getJobName())) {
            result.put("status", false);
            result.put("message", "name not null");
            return result;
        }

        if (!StringUtils.hasText(job.getCron())) {
            result.put("status", false);
            result.put("message", "cron not null");
            return result;
        }

        if (!StringUtils.hasText(job.getJobType())) {
            result.put("status", false);
            result.put("message", "getJobType not null");
            return result;
        }

        if ("ScriptJob".equals(job.getJobType())) {
            if (!StringUtils.hasText(job.getScriptCommandLine())) {
                result.put("status", false);
                result.put("message", "scriptCommandLine not null");
                return result;
            }
        } else {
            if (!StringUtils.hasText(job.getJobClass())) {
                result.put("status", false);
                result.put("message", "jobClass not null");
                return result;
            }
        }

        try {
            jobService.addJob(job);
        } catch (Exception e) {
            result.put("status", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 删除动态注册的任务（只删除注册中心中的任务信息）
     * @param jobName	任务名称
     * @throws Exception
     */
    @GetMapping("/job/remove")
    public Object removeJob(String jobName) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", true);
        try {
            jobService.removeJob(jobName);
        } catch (Exception e) {
            result.put("status", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
}

