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
import com.google.gson.Gson;
import com.google.sps.data.Event;

/**
 * Generates a new Event with a unique ID, keeps storage of it on DataStore and returns the ID as JSON.
 */
@WebServlet("/create-event")
@MultipartConfig
public class GetNewEventRoomServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // Generate the new Event's ID
    String sEventID = UUID.randomUUID().toString();
    // Create a new Event entity with its UUID
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    KeyFactory keyFactory = datastore.newKeyFactory().setKind("Event");
    long timestamp = System.currentTimeMillis();
    FullEntity EventEntity =
        Entity.newBuilder(keyFactory.newKey())
            .set("id", sEventID)
            .set("timestamp", timestamp)
            .build();
    datastore.put(EventEntity);
    // Create object to store data
    Event event = new Event(sEventID, timestamp);
    // Convert Event to JSON format
    Gson gson = new Gson();
    String json = gson.toJson(event);
    // Return server response as JSON
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }
}
