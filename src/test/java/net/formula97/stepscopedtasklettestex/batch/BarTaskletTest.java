package net.formula97.stepscopedtasklettestex.batch;

import net.formula97.stepscopedtasklettestex.service.Some1Service;
import net.formula97.stepscopedtasklettestex.service.Some2Service;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BarTaskletTest {

    @Autowired
    private Some1Service some1Service;
    @Autowired
    private Some2Service some2Service;

//    @Autowired
//    private JobRepository jobRepository;
//    @Autowired
//    private JobLauncher jobLauncher;
//    @Autowired
//    private Job job1;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("BarTaskletを実行できる")
    void runsTasklet() throws Exception {
        // ChunkContextをモック化
        StepExecution se = Mockito.mock(StepExecution.class);
        StepContext sc = Mockito.mock(StepContext.class);
        ChunkContext cc = Mockito.mock(ChunkContext.class);

        JobExecution je = MetaDataInstanceFactory.createJobExecution();
        je.getExecutionContext().put(FooTasklet.PASS_PARAM1, "次に引き継ぐ値");

        Mockito.when(cc.getStepContext()).thenReturn(sc);
        Mockito.when(sc.getStepExecution()).thenReturn(se);
        Mockito.when(se.getJobExecution()).thenReturn(je);

        // StepContributionをモック化
        StepContribution contrib = Mockito.mock(StepContribution.class, Mockito.CALLS_REAL_METHODS);

        // StepScopeはAutowiredできないので自力でnewする
        BarTasklet barTasklet = new BarTasklet(some1Service, some2Service);

        RepeatStatus rs = StepScopeTestUtils.doInStepScope(se, () -> barTasklet.execute(contrib, cc));
        assertEquals(RepeatStatus.FINISHED, rs);
        assertEquals(ExitStatus.COMPLETED, contrib.getExitStatus());
    }
}