/*******************************************************************************
 * Copyright (c) 2014 eBay Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.ebaysf.ostara.telemetry.mongodb;

public class UserFilter {
  static final String[] KNOWN_PREFIXES = {"renyedi"};
  
  /** Low-tech filtering */
  public static boolean isTeamMember(String userId) {
    if(userId == null) {
      return false;
    }
    
    for(String s : KNOWN_PREFIXES) {
      if(userId.contains(s)) {
        return true;
      }
    }
    
    return false;
  }
}
