package com.google.sps.models;

import java.text.DateFormat;
import java.util.Date;

public class DatesRange {
  public long start, end;

  @Override
  public String toString() {
    String startDate = DateFormat.getDateInstance().format(new Date(start));
    String endDate = DateFormat.getDateInstance().format(new Date(end));
    return "DatesRange { start: \"" + startDate + "\", end: \"" + endDate + "\" }";
  }
}
