package net.formula97.stepscopedtasklettestex.batch;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class FooTasklet implements Tasklet {

    /**
     * 後続タスクへ引き渡す値のキー
     */
    public static final String PASS_PARAM1 = "PassParam1FromFooTasklet";

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        contribution.setExitStatus(ExitStatus.COMPLETED);
        ExecutionContext ec = chunkContext.getStepContext().getStepExecution().getExecutionContext();

        ec.put(PASS_PARAM1, "次に引き継ぐ値");

        Thread.sleep(15000L);

        return RepeatStatus.FINISHED;
    }
}
