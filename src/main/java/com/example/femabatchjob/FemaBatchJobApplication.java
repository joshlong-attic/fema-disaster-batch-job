package com.example.femabatchjob;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;

import javax.sql.DataSource;

@EnableBatchProcessing
@SpringBootApplication
public class FemaBatchJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(FemaBatchJobApplication.class, args);
    }

}

@Configuration
class FemaBatchJobConfiguration {
    @Bean
    Job job(JobBuilderFactory jobFactory, StepOneConfiguration s1) {
        return jobFactory
                .get("fema-ingest-job")
                .start(s1.one())
                .incrementer(new RunIdIncrementer())
                .build();
    }
}

@Log4j2
@Configuration
class StepOneConfiguration {

    private final StepBuilderFactory sbf;
    private final String insertSql;
    private final Resource resource;
    private final DataSource dataSource;

    StepOneConfiguration(StepBuilderFactory sbf, DataSource ds, @Value("${fema.file:file://${HOME}/workspace/fema-disaster-batch-job/data/fema.csv}") Resource resource, @Value("${insert.sql}") String sqlToUse) {
        this.sbf = sbf;
        this.dataSource = ds;
        this.resource = resource;
        this.insertSql = sqlToUse;
    }

    FieldSetMapper<FemaDistaster> fieldSetMapper() {
        return new FieldSetMapper<FemaDistaster>() {
            @Override
            public FemaDistaster mapFieldSet(FieldSet fieldSet) throws BindException {
                String femaDeclarationString = fieldSet.readString("femaDeclarationString");

                return new FemaDistaster(femaDeclarationString, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
            }
        };
    }

    @Bean
    FieldSetMapper<FemaDistaster> beanWrapperFieldSetMapper() {
        var fsm = new BeanWrapperFieldSetMapper<FemaDistaster>();
        fsm.setTargetType(FemaDistaster.class);
        return fsm;
    }

    @Bean
    ItemReader<FemaDistaster> reader() {
        String colNames[] = "femaDeclarationString\tdisasterNumber\tstate\tdeclarationType\tdeclarationDate\tfyDeclared\tincidentType\tdeclarationTitle\tihProgramDeclared\tiaProgramDeclared\tpaProgramDeclared\thmProgramDeclared\tincidentBeginDate\tincidentEndDate\tdisasterCloseoutDate\tfipsStateCode\tfipsCountyCode\tplaceCode\tdesignatedArea\tdeclarationRequestNumber\thash\tlastRefresh\tid".split("\t");
        log.info("col name: " + StringUtils.arrayToDelimitedString(colNames, ":::"));

        return new FlatFileItemReaderBuilder<FemaDistaster>()
                .linesToSkip(1)
                .name("fema.csv")
                .resource(resource)
                .fieldSetMapper(beanWrapperFieldSetMapper())
                .delimited().delimiter(",").names(colNames)
                .build();
    }

    @Bean
    ItemProcessor<FemaDistaster, FemaDistaster> processor() {
        return new ItemProcessor<FemaDistaster, FemaDistaster>() {
            @Override
            public FemaDistaster process(FemaDistaster femaDistaster) throws Exception {
                return femaDistaster;
            }
        };
    }



    @Bean
    ItemWriter<FemaDistaster> writer() {
        /*
         insert into fema_disaster ( fema_declaration_string , hash )
        values ( ? , ? )
        on conflict  (hash)
        do update
         set
          extended = ?,
          description = ?,
          hash = ?,
          href = ?,
          meta = ?,
          time  = ?,
          tags = ?
         */
        JdbcBatchItemWriterBuilder<FemaDistaster> fds = new JdbcBatchItemWriterBuilder<FemaDistaster>();
        fds.dataSource(this.dataSource);
        fds.beanMapped();
        fds.sql(insertSql) ;
        return fds.build();
    }


    @Bean
    Step one() {
        return sbf
                .get("csv-to-db-step")
                .<FemaDistaster, FemaDistaster>chunk(1800)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
}