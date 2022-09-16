package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event>{
	private Factory<LightSwitchingStrategy> lssFactory;
	private Factory<DequeuingStrategy> dqs;
	private int time;
	private String id;
	private int xcoor,ycoor;
	private LightSwitchingStrategy lssFactoryStrategy;
	private DequeuingStrategy dqStrategy;
		
	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lssFactoryFactory, Factory<DequeuingStrategy> dqsFactory) {
		
		super("new_junction");
		this.lssFactory = lssFactoryFactory;
		this.dqs = dqsFactory;
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		
		time = data.getInt("time");
		id = data.getString("id");
		xcoor = data.getJSONArray("coor").getInt(0);
		ycoor = data.getJSONArray("coor").getInt(1);
		
		JSONObject ls = data.getJSONObject("ls_strategy");
		JSONObject dq = data.getJSONObject("dq_strategy");
		lssFactoryStrategy = lssFactory.createInstance(ls);
		dqStrategy = dqs.createInstance(dq);
		
		return new NewJunctionEvent(time, id, lssFactoryStrategy, dqStrategy, xcoor, ycoor);
	}
}
