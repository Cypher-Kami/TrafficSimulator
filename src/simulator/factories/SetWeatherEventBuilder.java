package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event> {
	private int time;
	private List<Pair<String, Weather>> listPair = new ArrayList<Pair<String, Weather>>();

	public SetWeatherEventBuilder() {
		super("set_weather");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		time = data.getInt("time");
		JSONArray ja = data.getJSONArray("info");
		
		for (int i = 0; i < ja.length(); i++){
			listPair.add(new Pair<String, Weather>(ja.getJSONObject(i).getString("road"), Weather.valueOf(ja.getJSONObject(i).getString("weather"))));
		}
		return new SetWeatherEvent(time, listPair);
	}

}
