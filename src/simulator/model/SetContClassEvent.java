package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetContClassEvent extends Event {
	
	private int time;
	private List<Pair<String,Integer>> contamination;

	public SetContClassEvent(int time , List<Pair<String,Integer>> cs) {
		super(time);
		if(cs == null || cs.size() == 0) {
			throw new IllegalArgumentException("La lista no puede ser vacia");
		}
		
		this.time = time;
		this.contamination = cs;
	}

	@Override
	public void execute(RoadMap map) {
		for (Pair<String, Integer> c : contamination ){
			
			if(map.getVehicle(c.getFirst()) == null){
				throw new IllegalArgumentException("El vehiculo con id " + c.getFirst() + " no fue encontrado");
			}
			
			map.getVehicle(c.getFirst()).setContClass(c.getSecond());
		}
		
	}
	
	public String toString() {
		return "New Set Contamination '"+this.contamination+"'";
	} 
	
}
