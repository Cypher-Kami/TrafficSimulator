package simulator.model;

import java.util.List;

public class RoundRobinStrategy implements LightSwitchingStrategy {
	
	private int timeSlot;
	
	public RoundRobinStrategy( int timeSlot ) {
		this.timeSlot = timeSlot;
	}
	
	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime, int currTime) {
		int res = 0;
		if( roads.size() == 0 ||  roads == null ) {
			res = -1;
		}
		else if( currGreen == -1 ){
			res = 0;
		}
		else if( currTime-lastSwitchingTime < this.timeSlot ) {
			res = currGreen;
		}
		else {
			res = (currGreen + 1) % roads.size();
		}
		
		return res;
	}
	
}
