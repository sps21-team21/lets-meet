package com.google.sps.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.gson.Gson;
import static com.google.sps.Constants.*;

import java.io.IOException;
import java.util.List;
import java.util.HashSet;
import java.util.stream.Collectors;

@WebServlet("/MatchingAlgoDate")
public class MatchingDateAlgo extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //Used to keep track of the good dates. HashSet used for constant "contains" time.
        HashSet<LongValue> goodDates = new HashSet<>();

        //Used to hold dates for each user from datastore
        List<LongValue> userDays;

        //Used to *temporarily* hold the values which overlap in current goodDates and userDays
        HashSet<LongValue> temps = new HashSet<>();

        //get objects in datastore
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        String currEvent = request.getParameter("text-input");
        Query<Entity> query = Query.newEntityQueryBuilder().setKind(USER_KIND).setFilter(PropertyFilter.eq(USER_EVENT_ID_KEY, currEvent)).build();
        QueryResults<Entity> results = datastore.run(query);
        while (results.hasNext()) {
            Entity entity = results.next();
            try {
                userDays = entity.getList(USER_CALENDAR_KEY);
            } catch (DatastoreException ignored) {
                continue;
            }
            if (goodDates.isEmpty()){
                goodDates.addAll(userDays);
            } else {
                for (LongValue userDay : userDays) {
                    if (goodDates.contains(userDay)) {
                        temps.add(userDay);
                    }
                }
                goodDates = temps;
                temps = new HashSet<LongValue>();
            }
        }

        List<Long> goodDatesList = goodDates.stream().map(Value::get).collect(Collectors.toList());
        
        Gson bson = new Gson();
        response.setContentType("application/json;");
        response.getWriter().println(bson.toJson(goodDatesList));
    }
}
