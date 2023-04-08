package net.formula97.stepscopedtasklettestex.batch;

import net.formula97.stepscopedtasklettestex.service.Some1Service;
import net.formula97.stepscopedtasklettestex.service.Some2Service;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class BarTasklet implements Tasklet {

    private final Some1Service some1Service;
    private final Some2Service some2Service;

    public BarTasklet(Some1Service some1Service, Some2Service some2Service) {
        this.some1Service = some1Service;
        this.some2Service = some2Service;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        String val = (String) chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get(FooTasklet.PASS_PARAM1);

        System.out.println(val);
        some1Service.doSomething1();
        some2Service.doSomething2();

        contribution.setExitStatus(ExitStatus.COMPLETED);

        return RepeatStatus.FINISHED;
    }
}
