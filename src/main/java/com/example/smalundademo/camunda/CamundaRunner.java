package com.example.smalundademo.camunda;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstanceQuery;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.runtime.Incident;
import org.camunda.bpm.engine.runtime.IncidentQuery;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

import java.util.List;
import java.util.function.Function;

public class CamundaRunner {

    public static void main(String... args) throws InterruptedException {


        BpmnModelInstance model = Bpmn.readModelFromStream(CamundaRunner.class.getResourceAsStream("/bpmn/example.bpmn"));
        System.out.println(model.getModel());


        long t = System.currentTimeMillis();
        new CamundaRunner().setup();

        long tt = System.currentTimeMillis();

        System.out.println("Time for execution: " + (tt - t) + "ms.");
    }

    private <T, R> R meter(String id, T t, Function<T, R> theFunction) {

        long t0 = System.currentTimeMillis();
        R result = theFunction.apply(t);
        long t1 = System.currentTimeMillis();
        System.out.println(id + " took " + (t1 - t0) + "ms.");
        return result;
    }

    public void setup() throws InterruptedException {
        // Configure Camunda Engine without database persistence

        ProcessEngineConfiguration processEngineConfiguration = meter("setup", null,  (foo)->{
            ProcessEngineConfiguration processEngineConfiguration2   = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration()
                    .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
                    .setAuthorizationEnabled(false)
                    .setJdbcUrl("jdbc:h2:file:.\\data\\demodb")
                    .setSkipHistoryOptimisticLockingExceptions(true)
                    .setCreateIncidentOnFailedJobEnabled(true)
                    .setJobExecutorActivate(false);

            return processEngineConfiguration2;
        });


        // Build ProcessEngine
        ProcessEngine processEngine = meter("build", processEngineConfiguration, (i)->i.buildProcessEngine());

        // Deploy BPMN process definition
        Deployment deployment = meter("deployment", processEngine, (pe)->{
            RepositoryService repositoryService = pe.getRepositoryService();
            return repositoryService.createDeployment()
                    .addClasspathResource("bpmn/example.bpmn")
                    .deploy();

        });
        final String processDefKey = "example_process";
        final String businessKey = "foo-business-key";
        // Start a process instance
        ProcessInstance processInstance = meter("run process", processEngine, (pe)->{
            RuntimeService runtimeService = pe.getRuntimeService();
            return runtimeService.startProcessInstanceByKey(processDefKey, businessKey);
        });

        List<HistoricActivityInstance> historicActivityInstances = processEngine.getHistoryService().createHistoricActivityInstanceQuery()
                .processInstanceId(processInstance.getId())
                .unlimitedList();

        for(HistoricActivityInstance hai : historicActivityInstances){
            System.out.println(hai.getActivityId());
            System.out.println(hai.getActivityName());
            System.out.println(hai.getDurationInMillis());
        }

        // Perform other workflow-related operations as needed

        Thread.sleep(1_000);
        // Shut down the process engine when done
        meter("shutdown", processEngine, (pe)->{
            pe.close();
            return null;
        });
    }
}