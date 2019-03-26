package com.example.demo;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

import java.util.ArrayList;
import java.util.List;


public class Scp {
    /**
     * 定义限流的规则
     */
    public static void initFlowStat(){
        List<FlowRule> flowRules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        //定义规则名称
        rule.setRefResource("helloworld");
        //流量控制方式 0 是线程数 1是请求数
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setCount(20);
        flowRules.add(rule);
        //加载到规则管理器
        FlowRuleManager.loadRules(flowRules);
    }
    public static void main(String [] args){
        //定义限流的规则
        initFlowStat();
        while (true){
            Entry entry = null;
            try {
                //当前线程的调用规则
                entry = SphU.entry("helloworld");
                System.out.println("hello world");
            } catch (BlockException e) {
                /*流控逻辑处理 - 开始*/
                System.out.println("block!");
                /*流控逻辑处理 - 结束*/
                System.out.println(e.getMessage());
            } finally {
                if(entry!=null){
                    entry.exit();
                }
            }
        }
    }
}
