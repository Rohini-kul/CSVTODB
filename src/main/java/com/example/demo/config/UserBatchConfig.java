package com.example.demo.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import com.example.demo.model.User;
import com.example.demo.processor.UserItemProcessor;
@Configuration
@EnableBatchProcessing
public class UserBatchConfig {
	@Autowired
    public JobBuilderFactory jobBuilderFactory;
	
	 @Autowired
	    public StepBuilderFactory stepBuilderFactory;
	@Autowired
	DataSource dataSource;
	
	@Lazy
	@Bean
	public FlatFileItemReader<User> reader()
	{
		FlatFileItemReader<User> reader=new FlatFileItemReader<>();
		reader.setResource(new ClassPathResource("records.csv"));
		reader.setLineMapper(getLineMapper());
		reader.setLinesToSkip(1);
		return reader;
		
	}

	private LineMapper<User> getLineMapper() {
		DefaultLineMapper<User> lineMapper=new DefaultLineMapper<>();
		DelimitedLineTokenizer lineTokenizer=new DelimitedLineTokenizer();
		lineTokenizer.setNames(new String[] {"Emp ID","Name Prefix","Fisrt Name","Last Name"});
		lineTokenizer.setIncludedFields(new int[] {0,1,2,3});
		
		BeanWrapperFieldSetMapper<User> fieldSetMapper=new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(User.class);
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		return lineMapper;
	}
	
	@Bean
	public UserItemProcessor processor()
	{
		
		return new UserItemProcessor();
		
	}
	
	@Lazy
	@Bean
	public JdbcBatchItemWriter<User> writer()
	{
		JdbcBatchItemWriter<User>writer=new JdbcBatchItemWriter<>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		 writer.setSql("INSERT INTO user(empId,namePrefix,firstName,lastName)values(:empId,:namePrefix,:firstName,:lastName)");
	              
	        writer.setDataSource(this.dataSource);
	        return writer;
		
	}
	@Bean
	public Job importUserJob()
	{
		 return jobBuilderFactory.get("importUserJob")
	                .incrementer(new RunIdIncrementer())
	                .flow(step1())
	                .end()
	                .build();
	  
	}
	
	@Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<User,User> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }	
		
		
	}
	
	
	


