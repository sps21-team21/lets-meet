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
import com.google.gson.Gson;
import com.google.sps.data.CalendarUpdate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@WebServlet("/api/calendar")
public class CalendarServlet extends HttpServlet {
  Datastore datastore;

  @Override
  public void init() throws ServletException {
    super.init();
    datastore = DatastoreOptions.getDefaultInstance().getService();
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));
    CalendarUpdate update = new Gson().fromJson(reader, CalendarUpdate.class);
    System.out.println(update);

    // 1. Start transaction
    // https://cloud.google.com/datastore/docs/concepts/transactions#using_transactions
    // 2.Remove all previous calendar ranges from /meeting/user
    // https://cloud.google.com/datastore/docs/concepts/queries#ancestor_queries
    // 3. Create key factory from ancestors
    // https://cloud.google.com/datastore/docs/concepts/entities#ancestor_paths
    // 4. Add all new calendar ranges
    // https://cloud.google.com/datastore/docs/concepts/entities#creating_an_entity
    // 5. Commit transaction
  }
}
