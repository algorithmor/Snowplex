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

package com.algorithmor.snowplex.repositories.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.algorithmor.snowplex.WarehouseContextHolder;
import com.algorithmor.snowplex.exceptions.ConfigParsingException;
import com.algorithmor.snowplex.repositories.SnowflakeRepository;
import com.algorithmor.snowplex.util.AnyObjectMapper;

@Repository(value="SnowflakeRepositoryJDBC")
@PropertySources({@PropertySource("classpath:snowflake.properties")})
public class SnowflakeRepositoryJDBC implements SnowflakeRepository {
	
	@Autowired
	@Qualifier("routingJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	@Qualifier("routingNamedParameterJdbcTemplate")
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	RowMapper<Map<String, Object>> rowMapper;
	
	// Report configuration properties
	@Value("${snowflake.warehouse.reportConfigDatabase}")
	private String reportConfigDatabase;
	@Value("${snowflake.warehouse.reportConfigSchema}")
	private String reportConfigSchema;
	@Value("${snowflake.warehouse.reportConfigTable}")
	private String reportConfigTable;
	
	/*
	 * Execute any static query with a certain warehouse size
	 */
	public List<Map<String, Object>> query(String sql,String warehouse){
		WarehouseContextHolder.set(warehouse);
		return jdbcTemplate.query(sql, new AnyObjectMapper());
	}
	
	/*
	 * Get Report configuration based on report name from the database 
	 */
	@Override
	public List<Map<String, Object>> getReportConfigByName(String reportName) {
		MapSqlParameterSource paramSource = new MapSqlParameterSource("reportName", reportName);
		return namedParameterJdbcTemplate.query("SELECT * FROM \""+reportConfigDatabase+"\".\""+reportConfigSchema+"\".\""+reportConfigTable+"\" WHERE report_name = :reportName",paramSource,rowMapper);
	}
	
	/*
	 * Execute Report with the configuration defined in DB
	 */
	@Override
	public List<Map<String, Object>> executeReport(String reportName) throws ConfigParsingException{
		List<Map<String, Object>> configs = getReportConfigByName(reportName);
		List<Map<String, Object>> reportData = null;

		for(Map<String, Object> config : configs) {
			Map<String, Object> paramMap = null;
			try {
				paramMap = getParamMapFromJsonConfig((String) config.get("REPORT_PARAMS"));
			} catch (ConfigParsingException e) {
				throw new ConfigParsingException();
			}
			WarehouseContextHolder.set((String)config.get("WAREHOUSE_SIZE"));
			MapSqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
			String query = (String) config.get("REPORT_QUERY");
			reportData = namedParameterJdbcTemplate.query(query,paramSource,new AnyObjectMapper());
		}
		return reportData;
	}
	
	/*
	 * reads the Report parameters in Json format and converts to a map
	 */
	private Map<String,Object> getParamMapFromJsonConfig(String config) throws ConfigParsingException{
		JacksonJsonParser parser = new JacksonJsonParser();
		Map<String,Object> parsedMap = null;
		try{ 
			parsedMap = parser.parseMap(config);
	    } catch(Exception e){
	        throw new ConfigParsingException();
	    }
		return parsedMap;
	}
}
