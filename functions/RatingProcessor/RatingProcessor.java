
import com.catalyst.Context;
import com.catalyst.basic.BasicIO;
import com.catalyst.basic.ZCFunction;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.zc.common.ZCProject;
import com.zc.component.cache.ZCCache;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RatingProcessor implements ZCFunction {
 
 private static final Logger LOGGER = Logger.getLogger(RatingProcessor.class.getName());
 
 private static Map<String, Integer> map = new HashMap() { 
 	{
          put("Excellent",0); 
          put("Very good",1); 
          put("Average",2); 
          put("Poor",3);
          put("Terrible",4);
 	}
      };
 
 @Override
 @SuppressWarnings("unchecked")
    public void runner(Context context, BasicIO basicIO) throws Exception {
 	try {
 		
 		Integer topCount = 10;
 		if(basicIO.getParameter("count") != null) {
 			
 			topCount = Integer.parseInt(basicIO.getParameter("count").toString());
 		}
 		String travellerRating = (String) basicIO.getParameter("type");
 		Integer type = 0;
 		if(travellerRating != null) {
 			if(map.containsKey(travellerRating)) {
 				type = map.get(travellerRating);
 			}
 		}
 		String url = "https://www.zoho.com/catalyst/downloads/static/tutorial/dataset/traveller-rating.json";
 		ZCProject.initProject();
 		HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
 		OkHttpClient client = new OkHttpClient().newBuilder().build();
 		Request.Builder requestsBuilder = new Request.Builder();
 		requestsBuilder.url(httpBuilder.build());
 		requestsBuilder.get();
 		Response responses = client.newCall(requestsBuilder.build()).execute();
 		String responsebody = responses.body().string();
 		JSONParser parser = new JSONParser();
 		JSONObject dataJSON = (JSONObject) parser.parse(responsebody);
 		HashMap<String, Long> execellentRating = new HashMap<>();
 		
 		Iterator<String> itrJson = dataJSON.keySet().iterator();
 		while(itrJson.hasNext()) {
 			
 			String museumName = itrJson.next();
 			List<String> ratingList = new ObjectMapper().convertValue(dataJSON.get(museumName), List.class);
 			Long exellentRating = Long.parseLong(ratingList.get(type));
 			execellentRating.put(museumName, exellentRating);
 		}
 		execellentRating = RatingProcessor.sortByValue(execellentRating);
 		List<String> execellentMuseum = new ArrayList<>();
 		itrJson = execellentRating.keySet().iterator();
 		Integer i= 0;
 		while(i++ < topCount && itrJson.hasNext()) {
 			execellentMuseum.add(itrJson.next());
 		}
 		basicIO.write(new ObjectMapper().writeValueAsString(execellentMuseum));
 	}
 	catch(Exception e) {
 		LOGGER.log(Level.SEVERE,"Exception in RatingProcessor",e);
 		basicIO.setStatus(500);
 	}
 }
 
 public static HashMap<String, Long> sortByValue(HashMap<String, Long> hm) 
    { 
        // Create a list from elements of HashMap 
        List<Map.Entry<String, Long> > list = 
               new LinkedList<Map.Entry<String, Long> >(hm.entrySet()); 
        // Sort the list 
        Collections.sort(list, new Comparator<Map.Entry<String, Long> >() { 
            public int compare(Map.Entry<String, Long> o1,  
                               Map.Entry<String, Long> o2) 
            { 
                return (o2.getValue()).compareTo(o1.getValue()); 
            } 
        }); 
        // Put the data from the sorted list to the HashMap  
        HashMap<String, Long> temp = new LinkedHashMap<String, Long>(); 
        for (Map.Entry<String, Long> aa : list) { 
            temp.put(aa.getKey(), aa.getValue()); 
        } 
        return temp; 
    } 
 }
