package com.pulse.config;

import com.pulse.amqp.producer.SuggestionProducer;
import com.pulse.dto.suggestion.Suggestion;
import com.pulse.job.suggestion.SuggestionsProcessor;
import com.pulse.model.Talent;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final EntityManagerFactory entityManagerFactory;

    private final SuggestionsProcessor suggestionsProcessor;

    private final SuggestionProducer suggestionProducer;

    public ItemReader<Talent> suggestionsReader(){
        JpaPagingItemReader<Talent> jpaPagingItemReader = new JpaPagingItemReader<>();
        jpaPagingItemReader.setEntityManagerFactory(entityManagerFactory);
        jpaPagingItemReader.setQueryString("SELECT talent FROM Talent talent WHERE talent.status = 'VERIFIED'");
        jpaPagingItemReader.setPageSize(10);
        return jpaPagingItemReader;
    }

    public ItemProcessor<Talent, Suggestion> SuggestionsProcessor(){
        return suggestionsProcessor;
    }

    public ItemWriter<Suggestion> suggestionsWriter(){

        return suggestions -> suggestions.forEach(suggestion -> {
            System.out.println(suggestion.getFirstName() + " " + suggestion.getLastName());
            suggestion.getSuggestedJobs().forEach(job -> System.out.println(job.getId()));
            suggestionProducer.sendSuggestion(suggestion);
        });
    }

    public Step step(){
        return new StepBuilder("suggestions-step", jobRepository)
                .<Talent, Suggestion>chunk(10, transactionManager)
                .reader(suggestionsReader())
                .processor(SuggestionsProcessor())
                .writer(suggestionsWriter())
                .build();
    }

    @Bean
    public Job job(){
        return new JobBuilder("suggestions-job", jobRepository)
                .flow(step())
                .end()
                .build();
    }
}
