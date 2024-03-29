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

package com.algorithmor.snowplex.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.algorithmor.snowplex.exceptions.ConfigParsingException;
import com.algorithmor.snowplex.service.SnowflakeService;

@Controller
@RequestMapping(path="/snowplex")
public class SnowplexController {
	
	@Autowired 
	private SnowflakeService snowflakeService;
	
	@PostMapping(path="/api/query")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<Map<String,Object>> select(@RequestParam String query,@RequestParam String warehouse) {
		return snowflakeService.select(query,warehouse);
	}
	
	@GetMapping(path="api/reportConfig/{reportName}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<Map<String,Object>> getReportConfigByName(@PathVariable String reportName) {
		return snowflakeService.getReportConfigByName(reportName);
	}
	
	@GetMapping(path="api/getReport/{reportName}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<Map<String,Object>> executeReport(@PathVariable String reportName) throws ConfigParsingException {
			return snowflakeService.executeReport(reportName);
	}
}


