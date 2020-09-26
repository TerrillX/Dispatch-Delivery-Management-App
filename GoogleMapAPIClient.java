import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GoogleMapAPIClient {

    @Value("${api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/robotOption", method = RequestMapping.GET)
    public Route getRoute(@RequestParam("from") String from, @RequestParam("to") String to, @RequestParam("DEFAULT_STATION") String station) {
        
        DirectionsResponse response = restTemplate.getForObject(
            "https://maps.googleapis.com/maps/api/directions/json?"
            + "origin=" + station
            + "&destination=" + to
            + "&waypoints=" + from
            + "&key=" + apiKey,
            DirectionsResponse.class
        );
        Route result = new Route(response.getStartAddress(), response.getEndAddress(), response.getDuration);
        return new Route();
    }
}

class DirectionsResponse {
        /* String json = "{'id': 1001, "
            + "'firstName': 'Lokesh',"
            + "'lastName': 'Gupta',"
            + "'email': 'howtodoinjava@gmail.com'}";
        */
    private JsonElement jsonElement = new JsonParser().parse(json);
    private JsonObject jsonObject = jsonElement.getAsJsonObject();
    private JsonArray jsonArray = jsonObject.getJsonArray("routes");
    
    public String getStartAddress() {
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i) == "start_location") {
                String start_address = jsonArray.getJSONObject(i).getString("start_address");
            }
        }
        return start_address;
    }
       
    public String getEndAddress() {
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i) == "start_location") {
                String end_address = jsonArray.getJSONObject(i).getString("end_address");
            }
        }
        return end_address;
    }

    public String getDuration() {
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i) == "duration") {
                String duration = jsonArray.getJSONObject(i).getString("duration");
            }
        }
        return duration;
    }

    public String getDistance() {
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i) == "distance") {
                String duration = jsonArray.getJSONObject(i).getString("value");
            }
        }
        return duration;
    }

    public String getDistance() {
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i) == "distance") {
                String duration = jsonArray.getJSONObject(i).getString("value");
            }
        }
        return duration;
    }

    public Long getDroneDuration() {
        long dst = Long.parseLong(getDistance());
        return dst / 6400;
    }
}