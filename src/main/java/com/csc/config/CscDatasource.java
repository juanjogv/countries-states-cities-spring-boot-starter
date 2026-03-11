package com.csc.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Qualifier for the CSC-managed SQLite {@link javax.sql.DataSource} and
 * {@link org.springframework.jdbc.core.JdbcTemplate}. Use this annotation to
 * inject the starter's own data source without conflicting with the application's
 * primary data source.
 *
 * <pre>{@code
 * @Autowired @CscDatasource
 * private JdbcTemplate cscJdbcTemplate;
 * }</pre>
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier
public @interface CscDatasource {}
