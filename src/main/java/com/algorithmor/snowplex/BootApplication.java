/*Copyright 2019 algorithmor labs
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.algorithmor.snowplex;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.algorithmor.snowplex.util.AnyObjectMapper;

import net.snowflake.client.jdbc.SnowflakeBasicDataSource;

@SpringBootApplication
@PropertySources({@PropertySource("classpath:snowflake.properties")})
@ComponentScan("com.algorithmor")
public class BootApplication {
	
	@Value("${snowflake.user}")
	private String user;
	@Value("${snowflake.password}")
	private String password;
	@Value("${snowflake.account}")
	private String account;
	@Value("${snowflake.db}")
	private String db;
	@Value("${snowflake.schema}")
	private String schema;
	@Value("${snowflake.warehouse}")
	private String warehouse;
	@Value("${snowflake.url}")
	private String url;
	
	@Value("${snowflake.warehouse.xs}")
	private String XS_WH;
	@Value("${snowflake.warehouse.s}")
	private String S_WH;
	@Value("${snowflake.warehouse.m}")
	private String M_WH;
	@Value("${snowflake.warehouse.l}")
	private String L_WH;
	@Value("${snowflake.warehouse.xl}")
	private String XL_WH;
	@Value("${snowflake.warehouse.2xl}")
	private String XXL_WH;
	@Value("${snowflake.warehouse.3xl}")
	private String XXXL_WH;
	@Value("${snowflake.warehouse.4xl}")
	private String XXXXL_WH;
	
	public static void main(String[] args) {
		SpringApplication.run(BootApplication.class, args);
	}
	 
	/*
	 * Helper method to configure and create 
	 * SnowflakeBasicDataSource instance
	 */
	private SnowflakeBasicDataSource warehouseSnowflakeDataSource(String warehouse) {
       SnowflakeBasicDataSource dataSource = new SnowflakeBasicDataSource();
       dataSource.setAccount(account);
       dataSource.setDatabaseName(db);
       dataSource.setUrl(url);
	   dataSource.setUser(user);
	   dataSource.setPassword(password);
	   //dataSource.setSchema(schema);
	   dataSource.setWarehouse(warehouse);
	   return dataSource;
	}
	
    /*
     * JdbcTemplate using RoutingDataSource
     */
    @Bean(name="routingJdbcTemplate")
    @Autowired
    @Qualifier("routingDatasource")
    public JdbcTemplate routingJdbcTemplate(AbstractRoutingDataSource routingDataSource) {
    	return new JdbcTemplate(routingDataSource, true);
    }
    
    /*
     * NamedParameterJdbcTemplate using RoutingDataSource
     */
    @Bean(name="routingNamedParameterJdbcTemplate")
    @Autowired
    @Qualifier("routingDatasource")
    public NamedParameterJdbcTemplate routingNamedParameterJdbcTemplate(AbstractRoutingDataSource routingDataSource) {
    	return new NamedParameterJdbcTemplate(routingDataSource);
    }
  
    /*
     * RoutingDataSource to dynamically use different size warehouses
     * for executing queries
     */
    @Bean(name="routingDatasource")
    public AbstractRoutingDataSource routingDatasource() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("XS_WH",warehouseSnowflakeDataSource(XS_WH));
        targetDataSources.put("S_WH",warehouseSnowflakeDataSource(S_WH));
        targetDataSources.put("M_WH",warehouseSnowflakeDataSource(M_WH));
        targetDataSources.put("L_WH",warehouseSnowflakeDataSource(L_WH));
        targetDataSources.put("XL_WH",warehouseSnowflakeDataSource(XL_WH));
        targetDataSources.put("XXL_WH",warehouseSnowflakeDataSource(XXL_WH));
        targetDataSources.put("XXXL_WH",warehouseSnowflakeDataSource(XXXL_WH));
        targetDataSources.put("XXXXL_WH",warehouseSnowflakeDataSource(XXXXL_WH));
 
        WarehouseRoutingDataSource warehouseRoutingDataSource = new WarehouseRoutingDataSource();
        warehouseRoutingDataSource.setTargetDataSources(targetDataSources);
        warehouseRoutingDataSource.setDefaultTargetDataSource(targetDataSources.get("XS_WH"));
        return warehouseRoutingDataSource;
    }
    
    /*
     * Generic RowMapper bean to map query results to JSON
     */
    @Bean(name="anyObjectMapper")
    public RowMapper<Map<String,Object>> rowMapper(){
    	return new AnyObjectMapper();
    }
}