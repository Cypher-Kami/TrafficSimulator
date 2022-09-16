package simulator.model;

import java.util.List;

import simulator.misc.Pair;


public class SetWeatherEvent extends Event {
	
	private int time;
	private List<Pair<String,Weather>> timeList;
	
	public SetWeatherEvent(int time, List<Pair<String,Weather>> ws) {
		super(time);
		if(ws == null) {
			throw new IllegalArgumentException("La lista no puede estar vacia");
		}
		
		this.time = time;
		timeList = ws;
	}

	@Override
	public void execute(RoadMap map) {
		for (Pair<String, Weather> w : timeList){
			if (map.getRoad(w.getFirst()) == null){
				throw new IllegalArgumentException("No se pudo encontrar la carretera");
			}
			map.getRoad(w.getFirst()).setWeather(w.getSecond());
		}
		
	}
	
	@Override
	public String toString() {
		return "New Set Weather '"+this.time+"'";
	} 

}
