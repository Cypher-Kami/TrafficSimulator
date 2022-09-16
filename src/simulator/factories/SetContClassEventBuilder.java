package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event>{
	
	private int time;
	private List<Pair<String, Integer>> listPair = new ArrayList<Pair<String, Integer>>();

	public SetContClassEventBuilder() {
		super("set_cont_class");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		time = data.getInt("time");
		JSONArray ja = data.getJSONArray("info");
		for(int i = 0; i < ja.length(); i++){
			listPair.add( new Pair<String, Integer>(ja.getJSONObject(i).getString("vehicle"), ja.getJSONObject(i).getInt("class")));
		}
		return new SetContClassEvent(time , listPair);
	}

}
