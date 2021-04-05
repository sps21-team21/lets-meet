
package com.google.sps.servlets;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.HashSet;

@WebServlet("/MatchingAlgoDate")
public class MatchingDateAlgo extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //Used to keep track of the good dates. HashSet used for constant "contains" time.
        HashSet<LongValue> GoodDates = new HashSet<LongValue>();

        //Used to hold dates for each user from datastore
        List<LongValue> userDays;

        //Used to *temporarily* hold the values which overlap in current GoodDates and userDays
        HashSet<LongValue> temps = new HashSet<LongValue>();

        //get objects in datastore
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        String currEvent = request.getParameter("text-input");
        Query<Entity> query = Query.newEntityQueryBuilder().setKind("Event").setFilter(PropertyFilter.eq("event-id", currEvent)).build();
        QueryResults<Entity> results = datastore.run(query);
        while (results.hasNext()) {
            Entity entity = results.next();
            userDays = entity.getList("dates");
            if(GoodDates.isEmpty()){
                for(int i = 0; i < userDays.size(); i++){
                    GoodDates.add(userDays.get(i));
                }
            }
            else{
                for(int i = 0; i < userDays.size(); i++){
                    if(GoodDates.contains(userDays.get(i))){
                        temps.add(userDays.get(i));
                    }
                }
                GoodDates = temps;
                temps.clear();
            }
        }
        
        Gson bson = new Gson();
        response.setContentType("application/json;");
        response.getWriter().println(bson.toJson(GoodDates));
    }
}