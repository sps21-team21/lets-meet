// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.data;

import java.util.List;

public class CalendarUpdate {
  public long meeting, user;
  public List<DatesRange> ranges;

  @Override
  public String toString() {
    StringBuilder calendarStr = new StringBuilder();
    calendarStr.append("CalendarUpdate {\n");
    calendarStr.append("\tmeeting: ").append(meeting).append(",\n");
    calendarStr.append("\tuser: ").append(user).append(",\n");
    calendarStr.append("\tranges: [\n");
    for (DatesRange range: ranges) {
      calendarStr.append("\t\t").append(range.toString()).append('\n');
    }
    calendarStr.append("\t]\n");
    calendarStr.append("}");
    return calendarStr.toString();
  }
}