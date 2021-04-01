package com.google.sps.servlets;

import com.google.cloud.datastore.*;
import com.google.gson.Gson;
import com.google.sps.models.CalendarUpdate;

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
