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

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class DatesRange {
  public long start, end;

  public List<Long> serialize() {
    return List.of(start, end);
  }

  @Override
  public String toString() {
    String startDate = DateFormat.getDateInstance().format(new Date(start));
    String endDate = DateFormat.getDateInstance().format(new Date(end));
    return "DatesRange { start: \"" + startDate + "\", end: \"" + endDate + "\" }";
  }
}
