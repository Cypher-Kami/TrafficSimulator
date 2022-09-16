package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {
	private int timeSlot;
	
	public MostCrowdedStrategy( int timeSlot ) {
		this.timeSlot = timeSlot;
	}

	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime, int currTime) {
		int res = 0;
		if( roads.size() == 0 ||  roads == null ) {
			res = -1;
		}
		
		
		else if( currGreen == -1 ){
			
			int max_Value = 0;
			int cont = 0;
						
			for( int i = 0 ; i < qs.size(); i++ ) {
				if( max_Value < qs.get(i).size() ) {
					max_Value = qs.get(i).size();
					res = i;
				}
			}
		}
		
		else if(currTime-lastSwitchingTime < timeSlot) {
			res = currGreen;
		}
		
		else {
			
			int max_Value = 0;
			int cont = (currGreen+1) % qs.size();
						
			for( int i = 0 ; i < qs.size(); i++ ) {
				if( max_Value < qs.get(cont).size() ) {
					max_Value = qs.get(cont).size();
					res = cont;
				}
				cont++;
				if( cont == qs.size() ) {
					cont = 0;
				}
			
			}
		}
		
		return res;
	}
}
