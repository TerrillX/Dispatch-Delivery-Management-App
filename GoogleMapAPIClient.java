import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GoogleMapAPIClient {

    @Value("${api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

//   @RequestMapping("/robotOption", method = RequestMapping.GET)
//    public Route getRoute(@RequestParam("from") String from, @RequestParam("to") String to, @RequestParam("DEFAULT_STATION") String station) {
    public Route getRoute(String from, String to, String station) {     
        restTemplate = new RestTemplate();
        String[] response = new String[3];

        response[0] = restTemplate.getForObject(
            "https://maps.googleapis.com/maps/api/directions/json?"
            + "origin=" + station
            + "&destination=" + from
            //+ "&waypoints=" + xxx
            + "&key=" + apiKey,
            String.class
        );

        response[1] = restTemplate.getForObject(
            "https://maps.googleapis.com/maps/api/directions/json?"
            + "origin=" + from
            + "&destination=" + to
            //+ "&waypoints=" + xxx
            + "&key=" + apiKey,
            String.class
        );

        response[2] = restTemplate.getForObject(
            "https://maps.googleapis.com/maps/api/directions/json?"
            + "origin=" + to
            + "&destination=" + station
            //+ "&waypoints=" + xxx
            + "&key=" + apiKey,
            String.class
        );
        
        DirectionsResponse[] directionsResponse = new DirectionsResponse[3];

        directionsResponse[0] = new DirectionsResponse(response[0]);
        directionsResponse[1] = new DirectionsResponse(response[1]);
        directionsResponse[2] = new DirectionsResponse(response[2]);

        Route result = new Route(directionsResponse[1].getStartAddress(),
                                directionsResponse[1].getEndAddress(),

                                // For robot
                                // - station2user
                                directionsResponse[0].getRobotDuration(),
                            
                                // - user2dest
                                directionsResponse[1].getRobotDuration(),
                                directionsResponse[1].getRobotDistance(),

                                // - dest2station
                                directionsResponse[2].getRobotDuration(),


                                // For drone
                                // - station2user
                                directionsResponse[0].getDroneDuration(),

                                // - user2dest
                                directionsResponse[1].getDroneDuration(),
                                directionsResponse[1].getDroneDistance(),

                                // - dest2station
                                directionsResponse[2].getDroneDuration(),
                                );
        return result;
    }

    private class DirectionsResponse {

        private JsonElement jsonElement;
        private JsonObject jsonObject;
        private JsonArray jsonObjectResult;
        
        public DirectionsResponse(String json) {
            jsonElement = new JsonParser().parse(json);
            jsonObject = jsonElement.getAsJsonObject();

            // Get route array and pick up the first array by default
            jsonObjectResult = jsonObject.getAsJsonArray("routes").get(0).getAsJsonObject();
        }

        // Method for getting start_address
        public String getStartAddress() {
            String start_address = jsonObjectResult.get("start_address").getAsString();
            return start_address;
        }
        
        // Method for getting end_address
        public String getEndAddress() {
            String end_address = jsonObjectResult.get("end_address").getAsString();
            return end_address;
        }
    
        // Method for getting robotDuration
        public long getRobotDuration() {
                    String duration = jsonObjectResult.get("duration").getAsObject().get("value").getAsString();
            long durationValue = Long.parseLong(duration);
            return durationValue;
        }
    
        // Method for getting robotDistance
        public long getRobotDistance() {
            String robotDistance = jsonObjectResult.get("distance").getAsObject().get("value").getAsString();
            long robotDistanceValue = Long.parseLong(robotDistance);
            return robotDistanceValue;
        }

        // Method for getting droneDuration
        public Long getDroneDuration() {
            long droneDistanceValue = getDroneDistance();
            return droneDistanceValue / 6400;
        }

        // Method for getting droneDistance
        public long getDroneDistance() {
            String droneDistance = jsonObjectResult.get("distance").getAsObject().get("value").getAsString();
            long droneDistanceValue = Long.parseLong(droneDistance);
            return droneDistanceValue;
        }
    }
}