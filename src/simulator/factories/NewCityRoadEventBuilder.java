package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewCityRoadEvent;
import simulator.model.Weather;

public class NewCityRoadEventBuilder extends Builder<Event>{
	
	private String id;
	private int time;
	private String srcJunction;
	private String destJunction;
	private int length;
	private int c02Limit;
	private int maxSpeed;
	private Weather weather;

	public NewCityRoadEventBuilder() {
	
		super("new_city_road");
	}

	protected Event createTheInstance(JSONObject data) {
		
		time = data.getInt("time");
		id = data.getString("id");
		srcJunction = data.getString("src");
		destJunction = data.getString("dest");
		length = data.getInt("length");
		c02Limit = data.getInt("co2limit");
		maxSpeed = data.getInt("maxspeed");
		weather = Weather.valueOf(data.getString("weather"));
		
		return new NewCityRoadEvent(time, id, srcJunction, destJunction, length, c02Limit, maxSpeed, weather);
	}
}
