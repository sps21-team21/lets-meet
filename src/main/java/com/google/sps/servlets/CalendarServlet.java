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

package com.google.sps.servlets;

import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.sps.Constants.*;

/**
 * This servlet receives requests to fetch and update
 * the calendar availability of a user in a meeting
 */
@WebServlet("/api/calendar")
public class CalendarServlet extends HttpServlet {
  Datastore datastore;

  @Override
  public void init() throws ServletException {
    super.init();
    datastore = DatastoreOptions.getDefaultInstance().getService();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String event = req.getParameter("event");
    String user = req.getParameter("user");
    Query<Entity> userQuery = queryForUser(event, user);
    QueryResults<Entity> userResults = datastore.run(userQuery);
    Entity userEntity = assertUserFound(userResults, user);
    List<LongValue> datastoreDays = List.of();
    try {
      datastoreDays = userEntity.getList(USER_CALENDAR_KEY);
    } catch (DatastoreException ignored) {
    }
    List<Long> days = datastoreDays.stream().map(Value::get).collect(Collectors.toList());
    String jsonDaysList = new Gson().toJson(days);
    resp.setContentType("application/json;");
    resp.getWriter().println(jsonDaysList);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String event = req.getParameter("event");
    String user = req.getParameter("user");
    BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));
    Type listOfLongs = new TypeToken<List<Long>>() {}.getType();
    List<Long> days = new Gson().fromJson(reader, listOfLongs);
    List<LongValue> updatedDays = days
        .stream()
        .map(LongValue::of)
        .collect(Collectors.toList());

    Transaction txn = datastore.newTransaction();
    try {
      Query<Entity> userQuery = queryForUser(event, user);
      QueryResults<Entity> userResults = txn.run(userQuery);
      Entity userEntity = assertUserFound(userResults, user);
      Entity updatedUserEntity = Entity.newBuilder(userEntity)
          .set(USER_CALENDAR_KEY, updatedDays)
          .build();
      txn.update(updatedUserEntity);
      txn.commit();
    } finally {
      if (txn.isActive()) {
        txn.rollback();
      }
    }
  }

  private Query<Entity> queryForUser(String eventID, String userID) {
    return Query.newEntityQueryBuilder()
        .setKind(USER_KIND)
        .setFilter(PropertyFilter.eq(USER_EVENT_ID_KEY, eventID))
        .setFilter(PropertyFilter.eq(USER_ID_KEY, userID))
        .build();
  }

  private Entity assertUserFound(QueryResults<Entity> userResults, String userId) {
    if (!userResults.hasNext()) {
      throw new RuntimeException("User \"" + userId + "\" not found");
    }
    return userResults.next();
  }
}
