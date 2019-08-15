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

package com.algorithmor.snowplex.repositories;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.algorithmor.snowplex.exceptions.ConfigParsingException;

@Repository
public interface SnowflakeRepository {
	
	List<Map<String, Object>> query(final String sql,String warehouse);
	
	List<Map<String, Object>> getReportConfigByName(String reportName);

	List<Map<String, Object>> executeReport(String reportName) throws ConfigParsingException;

}
