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

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import static com.google.sps.Constants.*;

/**
 * Tries to find an event with the provided ID, if it does, 
 * redirects to page that returns ID of the user, handling exceptions.
 */
@WebServlet("/enter-room")
@MultipartConfig
public class EnterRoomServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // Sanitize user input to remove HTML tags and JavaScript.
    String sEventID = Jsoup.clean(request.getParameter("event-id"), Whitelist.none()); // event-id is arbitrary for now (needs to be referenced on html file)
    // Run query that looks for the provided ID
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    Query<Entity> query = Query.newEntityQueryBuilder().setKind(EVENT_KIND)
            .setFilter(PropertyFilter.eq(EVENT_ID_KEY, sEventID))
            .setOrderBy(OrderBy.desc(EVENT_TIMESTAMP_KEY))
            .build();
    QueryResults<Entity> eventResults = datastore.run(query);
    // Handle event not existing
    if (!eventResults.hasNext()) {
        response.sendRedirect("/event-not-found.html"); // name of the page is arbitrary, needs to be added
    } else {
        // Generate the new user's ID
        String sUserID = UUID.randomUUID().toString();
        // Create a new User entity with its UUID, and Event UUID
        KeyFactory keyFactory = datastore.newKeyFactory().setKind(USER_KIND);
        long timestamp = System.currentTimeMillis();
        FullEntity userEntity =
            Entity.newBuilder(keyFactory.newKey())
                .set(USER_ID_KEY, sUserID)
                .set(USER_EVENT_ID_KEY, sEventID)
                .set(USER_TIMESTAMP_KEY, timestamp)
                .build();
        datastore.put(userEntity);
        // redirects page that refers to userID and eventID
        response.sendRedirect("/user.html?event=" + sEventID + "&user=" + sUserID); // html will call this servlet that handles user creation
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // Sanitize user input to remove HTML tags and JavaScript.
    String sEventID = Jsoup.clean(request.getParameter("event-id"), Whitelist.none()); // event-id is arbitrary for now (needs to be referenced on html file)
    // Run query that looks for the provided ID
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    Query<Entity> query = Query.newEntityQueryBuilder().setKind(EVENT_KIND)
            .setFilter(PropertyFilter.eq(EVENT_ID_KEY, sEventID))
            .setOrderBy(OrderBy.desc(EVENT_TIMESTAMP_KEY))
            .build();
    QueryResults<Entity> eventResults = datastore.run(query);
    // Handle event not existing
    if (!eventResults.hasNext()) {
        response.sendRedirect("/event-not-found.html"); // name of the page is arbitrary, needs to be added
    } else {
        // Generate the new user's ID
        String sUserID = UUID.randomUUID().toString();
        // Create a new User entity with its UUID, and Event UUID
        KeyFactory keyFactory = datastore.newKeyFactory().setKind(USER_KIND);
        long timestamp = System.currentTimeMillis();
        FullEntity userEntity =
            Entity.newBuilder(keyFactory.newKey())
                .set(USER_ID_KEY, sUserID)
                .set(USER_EVENT_ID_KEY, sEventID)
                .set(USER_TIMESTAMP_KEY, timestamp)
                .build();
        datastore.put(userEntity);
        // redirects page that refers to userID and eventID
        response.sendRedirect("/user.html?event=" + sEventID + "&user=" + sUserID); // html will call this servlet that handles user creation
    }
  }
}
