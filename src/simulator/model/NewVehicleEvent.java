package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class NewVehicleEvent extends Event{
	
	private String id;
	private int contClass;
	private int maxSpeed;
	private List<String> itinerary;
	
	public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itinerary) {
		super(time);
		if(time < 0) {
			throw new IllegalArgumentException("El tiempo no puede ser un numero negativo");
		}
		if(maxSpeed < 0) {
			throw new IllegalArgumentException("La velocidad maxima no puede ser negativa");
		}
		if(contClass < 0 || contClass > 10) {
			throw new IllegalArgumentException("El grado de contaminacion tiene que tener un valor entre 0 y 10");
		}
		if(itinerary.size() < 2) {
			throw new IllegalArgumentException("El itinerario debe tener al menos dos elementos");
		}
		
		this.id = id;
		this.maxSpeed = maxSpeed;
		this.itinerary = itinerary;
		this.contClass = contClass;
	}

	@Override
	public void execute(RoadMap map) {
		List<Junction> auxList = new ArrayList<Junction>();
			
		for (String i: itinerary){
			auxList.add(map.getJunction(i));
		}
		
		Vehicle v = new Vehicle(this.id, this.maxSpeed, this.contClass, auxList);
		map.addVehicle(v);
		v.moveToNextRoad();
	}
	
	@Override
	public String toString() {
		return "New Vehicle '"+this.id+"'";
	} 
}
