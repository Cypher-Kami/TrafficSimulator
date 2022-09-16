package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class MoveAllStrategy implements DequeuingStrategy {
	
	public MoveAllStrategy () {
		
	}

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		for(Vehicle v : q) {
			vehicles.add(v);
		}
		return vehicles;
	}

}
