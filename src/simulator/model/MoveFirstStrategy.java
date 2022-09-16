package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class MoveFirstStrategy implements DequeuingStrategy{
	
	public MoveFirstStrategy () {
		
	}

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		List<Vehicle> vehicles = new ArrayList<Vehicle>(1);
		
		if(q.size() > 0) {
			vehicles.add(q.get(0));
		}
		return vehicles;
	}

}
