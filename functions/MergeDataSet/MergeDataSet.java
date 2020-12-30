
import com.catalyst.Context;
import com.catalyst.basic.BasicIO;
import com.catalyst.basic.ZCFunction;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.zc.common.ZCProject;
import com.zc.component.cache.ZCCache;
import com.zc.component.object.ZCTable;

public class MergeDataSet implements ZCFunction {
	private static final Logger LOGGER = Logger.getLogger(MergeDataSet.class.getName());
	
	@Override
    public void runner(Context context, BasicIO basicIO) throws Exception {
		try {
			
			Object ratingList = basicIO.getArgument("rating_list");
			Object typeRatingList = basicIO.getArgument("traveller_type_rating_list");
			ObjectMapper mapper = new ObjectMapper();
			List<String> travellerRatingList = mapper.readValue(ratingList.toString(), mapper.getTypeFactory().constructCollectionType(List.class, String.class));;
			List<String> travellerTypeRatingList = mapper.readValue(typeRatingList.toString(), mapper.getTypeFactory().constructCollectionType(List.class, String.class));;
			List<String> finalList = new ArrayList<>();
			for(int i=0 ; i< travellerTypeRatingList.size() ; i++) {
				
				if(travellerRatingList.contains(travellerTypeRatingList.get(i))) {
					finalList.add(travellerTypeRatingList.get(i).toString());
				}
			}
			JSONObject respJson = new JSONObject();
			respJson.put("museum_list", finalList);
			LOGGER.log(Level.INFO, "Museum Count"+finalList.size()); //You can view this log from Logs in the Catalyst console
			basicIO.write(new ObjectMapper().writeValueAsString(respJson));
			
		}
		catch(Exception e) {
			e.printStackTrace();
			LOGGER.log(Level.SEVERE,"Exception in MergeDataSet",e); //You can view this log from Logs in the Catalyst console
			basicIO.setStatus(500);
		}
	}
}
