package net.formula97.stepscopedtasklettestex;

import net.formula97.stepscopedtasklettestex.batch.BarTasklet;
import net.formula97.stepscopedtasklettestex.batch.FooTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Batchの挙動を定義する {@link Configuration } 。
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    public BatchConfig(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
    }

    /**
     * このプロジェクトの {@link Job } 。
     *
     * @param fooStep 先に実行される {@link Step }
     * @param barStep あとで実行される {@link Step }
     * @return {@link Job } のインスタンス
     */
    @Bean
    public Job job1(Step fooStep, Step barStep) {
        return new JobBuilder("job1", jobRepository)
                .start(fooStep)
                .next(barStep)
                .build();
    }

    /**
     * 先に実行される {@link Step } 。
     *
     * @param fooTasklet 先に実行されるタスクレット
     * @return {@link Step } のインスタンス
     */
    @Bean
    public Step fooStep(FooTasklet fooTasklet) {
        return new StepBuilder("foo_step", jobRepository)
                .tasklet(fooTasklet, platformTransactionManager)
                .listener(makeListener(FooTasklet.PASS_PARAM1))
                .build();
    }

    /**
     * あとで実行される {@link Step } 。
     *
     * @param barTasklet あとで実行されるタスクレット
     * @return {@link Step } のインスタンス
     */
    @Bean
    public Step barStep(BarTasklet barTasklet) {
        return new StepBuilder("bar_step", jobRepository)
                .tasklet(barTasklet, platformTransactionManager)
                .build();
    }

    /**
     * JobのExecutionContextへプッシュするリスナを作る。
     *
     * @param keys プッシュ対象を監視するキー
     * @return {@link ExecutionContextPromotionListener } のインスタンス
     */
    private ExecutionContextPromotionListener makeListener(String... keys) {
        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
        listener.setKeys(keys);

        return listener;
    }
}
