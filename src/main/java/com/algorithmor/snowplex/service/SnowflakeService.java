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

package com.algorithmor.snowplex.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algorithmor.snowplex.exceptions.ConfigParsingException;
import com.algorithmor.snowplex.repositories.SnowflakeRepository;

@Service
public class SnowflakeService {

	@Autowired
	private SnowflakeRepository repository;
	
	public List<Map<String, Object>> select(final String sql,final String warehouse){
		return repository.query(sql,warehouse);
	}

	public List<Map<String, Object>> getReportConfigByName(String reportName) {
		return repository.getReportConfigByName(reportName);
	}
	
	public List<Map<String, Object>> executeReport(String reportName) throws ConfigParsingException{
		return repository.executeReport(reportName);
	}

}
