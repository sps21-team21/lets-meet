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
import com.google.cloud.datastore.DoubleValue;
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

/* Servlet that updates and fetchs the location on the database */
@WebServlet("/api/maps")
public class MapsServlet extends HttpServlet {
    Datastore datastore;
    
    @Override
    public void init() throws ServletException {
        super.init();
        datastore = DatastoreOptions.getDefaultInstance().getService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String event = request.getParameter("event");
        String user = request.getParameter("user");
        Query<Entity> userQuery = queryForUser(event, user);
        QueryResults<Entity> userResults = datastore.run(userQuery);
        Entity userEntity = assertUserFound(userResults, user);
        List<DoubleValue> coords = List.of();
        try {
            coords = userEntity.getList(USER_LOCATION_KEY);
        } 
        catch (DatastoreException ignored) {}

        List<Double> loc = coords.stream().map(Value::get).collect(Collectors.toList());
        String jsonCoords = new Gson().toJson(loc);
        response.setContentType("application/json;");
        response.getWriter().println(jsonCoords);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get the request parameters.
        double lat = Double.parseDouble(request.getParameter("lat"));
        double lng = Double.parseDouble(request.getParameter("lng"));
        String event = request.getParameter("event");
        String user = request.getParameter("user");

        List<DoubleValue> coords = List.of();
        coords.add(DoubleValue.of(lat));
        coords.add(DoubleValue.of(lng));

        Transaction txn = datastore.newTransaction();
        try {
            Query<Entity> userQuery = queryForUser(event, user);
            QueryResults<Entity> userResults = txn.run(userQuery);
            Entity userEntity = assertUserFound(userResults, user);
            Entity updatedUserEntity = Entity.newBuilder(userEntity)
                .set(USER_LOCATION_KEY, coords)
                .build();
            txn.update(updatedUserEntity);
            txn.commit();
            }
        finally {
            if (txn.isActive()) {
                txn.rollback();
            }
        }
        // Output
        /* response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(translatedText); */
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
