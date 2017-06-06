package com.ifitmix.sports.scheduler;

import com.ifitmix.common.scheduler.ZkSchedulerCoordinator;
import com.ifitmix.common.spring.ApplicationConstant;
import com.ifitmix.common.spring.ApplicationContextHolder;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by zhangtao on 2017/4/18.
 */
public class TestScheduler {

    private ZkSchedulerCoordinator zkSchedulerCoordinator;

    public TestScheduler(ZkSchedulerCoordinator zkSchedulerCoordinator) {
        this.zkSchedulerCoordinator = zkSchedulerCoordinator;
    }

    public Integer t = 1;

    @Scheduled(cron = "*/5 * * * * ?")
    public void test() {
        // 不是当前 leader 不执行任务
        if(zkSchedulerCoordinator.isLeader()) {
            System.out.println("A 执行 任务 : " + t++);
        }
    }

}
