package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.MostCrowdedStrategy;

public class MostCrowdedStrategyBuilder extends Builder<LightSwitchingStrategy>{
	
	public MostCrowdedStrategyBuilder() {
		
		super("most_crowded_lss");
	}
	
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {

		MostCrowdedStrategy mCS;
		
		if (data.has("timeslot")){
			mCS = new MostCrowdedStrategy(data.getInt("timeslot"));
		}
		else{
			mCS = new MostCrowdedStrategy(1);
		}
		
		return mCS;
	}
}
