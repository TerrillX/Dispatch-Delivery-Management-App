
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