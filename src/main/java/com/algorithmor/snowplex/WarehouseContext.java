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

public enum WarehouseContext {
	XS_WH("XS_WH"),
	S_WH("S_WH"),
	M_WH("M_WH"),
	L_WH("L_WH"),
	XL_WH("XL_WH"),
	XXL_WH("XXL_WH"),
	XXXL_WH("XXXL_WH"),
	XXXXL_WH("XXXXL_WH");	
	
	private String warehouseName = null;
	
	WarehouseContext(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String value() {
		return warehouseName;
	}

}
