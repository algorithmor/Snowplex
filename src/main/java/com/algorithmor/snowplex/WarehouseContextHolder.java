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

import org.springframework.util.Assert;

public class WarehouseContextHolder {
	
  private static ThreadLocal<WarehouseContext> CONTEXT = new ThreadLocal<>();

  public static void set(String warehouseContext) {
      Assert.notNull(warehouseContext, "warehouseContext cannot be null");
      CONTEXT.set(WarehouseContext.valueOf(warehouseContext));
  }

  public static WarehouseContext getWarehouseContext() {
      return CONTEXT.get();
  }

  public static void clear() {
      CONTEXT.remove();
  }

}
