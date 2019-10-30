package org.asck.uiTests;

import liquibase.integration.spring.SpringLiquibase;
import org.asck.api.repository.*;
import org.hibernate.cfg.AvailableSettings;
import org.junit.ClassRule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Properties;

import static java.lang.String.format;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = {EventRepository.class, AnswerRepository.class, QuestionOptionRepository.class, QuestionRepository.class, QuestionTypeRepository.class, UserRepository.class})
@Profile("DaoTest")
public class DbConfigPostgresContainers {

    @Value("${docker.image.name}")
    private static final String dockerImageName = "postgres:10.3";
    @Value("${database.name}")
    private static final String databaseName = "test";
    @Value("${db.user.name}")
    private static final String username = "user";
    @Value("${db.password}")
    private static final String password = "pass";
    @Value("${driver.class.name}")
    private final String driverClassName = "org.postgresql.Driver";
    @Value("${postgres.connection.string}")
    private final String postgresConnectionString = "jdbc:postgresql://%s:%s/%s";
    @Value("${packages.to.scan}")
    private final String packagesToScan = "org.asck";
    @Value("${default.schema}")
    private final String defaultSchema = "test";
    @Value("${liquibase.change.log}")
    private final String liquibaseChangeLog = "classpath:/db/changelog/db.changelog-master.xml";



    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer(
            dockerImageName)
            .withDatabaseName(databaseName)
            .withUsername(username)
            .withPassword(password).withStartupTimeout(Duration.ofSeconds(600));

    @Bean
        public DataSource dataSource() {
            postgreSQLContainer.start();
            DriverManagerDataSource ds = new DriverManagerDataSource();
            ds.setDriverClassName(driverClassName);
            ds.setUrl(format(postgresConnectionString, postgreSQLContainer.getContainerIpAddress(),
                    postgreSQLContainer.getMappedPort(
                            PostgreSQLContainer.POSTGRESQL_PORT), postgreSQLContainer.getDatabaseName()));
            ds.setUsername(postgreSQLContainer.getUsername());
            ds.setPassword(postgreSQLContainer.getPassword());
            ds.setSchema(postgreSQLContainer.getDatabaseName());
            return ds;
        }


        /**
         * @param dataSource the db data source
         * @return the local entity manager factory bean
         */
        @Bean
        public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {

            LocalContainerEntityManagerFactoryBean lcemfb
                    = new LocalContainerEntityManagerFactoryBean();
            lcemfb.setDataSource(dataSource);
            // set the packages to scan , it can be useful if you have big project and you just need to local partial entities for testing
            lcemfb.setPackagesToScan(packagesToScan);
            HibernateJpaVendorAdapter va = new HibernateJpaVendorAdapter();
            lcemfb.setJpaVendorAdapter(va);
            lcemfb.setJpaProperties(getHibernateProperties());
            lcemfb.afterPropertiesSet();
            return lcemfb;

        }

        /**
         * @param localContainerEntityManagerFactoryBean
         * @return the JPA transaction manager
         */
        @Bean
        public JpaTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
            JpaTransactionManager transactionManager = new JpaTransactionManager();

            transactionManager.setEntityManagerFactory(localContainerEntityManagerFactoryBean.getObject());

            return transactionManager;
        }

        @Bean
        public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
            return new PersistenceExceptionTranslationPostProcessor();
        }

        @Bean
        public SpringLiquibase springLiquibase(DataSource dataSource) throws SQLException {
            tryToCreateSchema(dataSource);
            SpringLiquibase liquibase = new SpringLiquibase();
            liquibase.setDropFirst(true);
            liquibase.setDataSource(dataSource);
            liquibase.setDefaultSchema(defaultSchema);
            //liquibase.setIgnoreClasspathPrefix(false);
            liquibase.setChangeLog(liquibaseChangeLog);
            return liquibase;
        }


        /**
         * @return the hibernate properties
         */
        private Properties getHibernateProperties() {
            Properties ps = new Properties();
            ps.put(AvailableSettings.FORMAT_SQL, "true");
            ps.put(AvailableSettings.SHOW_SQL, "true");
            return ps;

        }

        private void tryToCreateSchema(DataSource dataSource) throws SQLException {
            String CREATE_SCHEMA_QUERY = "CREATE SCHEMA IF NOT EXISTS test";
            dataSource.getConnection().createStatement().execute(CREATE_SCHEMA_QUERY);
        }

    }

