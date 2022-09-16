package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewInterCityRoadEvent;
import simulator.model.Weather;

public class NewInterCityRoadEventBuilder extends Builder<Event> {
	private int time;
	private String id;
	private String srcJunction;
	private String destJunction;
	private int length;
	private int c02Limit;
	private int maxSpeed;
	private Weather weather;

	public NewInterCityRoadEventBuilder() {
		super("new_inter_city_road");
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
		
		return new NewInterCityRoadEvent(time, id, srcJunction, destJunction, length, c02Limit, maxSpeed, weather);
	}
}
