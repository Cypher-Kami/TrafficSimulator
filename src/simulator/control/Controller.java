package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller {
	private TrafficSimulator sim;
	private Factory<Event> eventsFactory;
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory){
		
		if( sim == null) {
			throw new IllegalArgumentException("El simulador no puede ser nulo");
		}
		if( eventsFactory == null) {
			throw new IllegalArgumentException("La factoria no puede ser nula");
		}
		this.sim = sim;
		this.eventsFactory = eventsFactory;
	}
	
	public void loadEvents(InputStream in) {
		JSONObject jo = new JSONObject(new JSONTokener(in));
		JSONArray ja = jo.getJSONArray("events");
		
		if (!jo.has("events")){
			throw new IllegalArgumentException("JSONObject etiqueta en controller debe ser events");
		}
		
		for (int i = 0; i < ja.length(); i++){
			sim.addEvent(eventsFactory.createInstance(ja.getJSONObject(i)));
		}
	}
	
	public void run(int n, OutputStream out) {
		//PrintStream p = new PrintStream(out);
		//p.println("{");
		//p.println(" \"states\": [");


		for(int i = 0; i < n; i++) {
			sim.advance();
		}
	}
	
	public void reset() {
		sim.reset();
	}
	
	public void addObserver(TrafficSimObserver o){
		sim.addObserver(o);
	}
	
	public void removeObserver(TrafficSimObserver o) {
		sim.removeObserver(o);
	}
	
	public void addEvent(Event e) {
		sim.addEvent(e);
	}
}
