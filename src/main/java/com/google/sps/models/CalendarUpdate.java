package com.google.sps.models;

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
