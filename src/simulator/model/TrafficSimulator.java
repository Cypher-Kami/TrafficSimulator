package simulator.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.json.JSONObject;

import simulator.misc.SortedArrayList;


public class TrafficSimulator implements Observable <TrafficSimObserver> {
	protected RoadMap roadMap;	
	protected List<Event> eventList;
	protected int time;
	private List<TrafficSimObserver> observer;
	
	public TrafficSimulator() {
		roadMap = new RoadMap();
		eventList = new SortedArrayList<Event>();
		observer = new ArrayList<TrafficSimObserver>();
		this.time = 0;
	}
	
	public void addEvent(Event e){
		eventList.add(e);
		
		for ( TrafficSimObserver obs : observer ) {
			obs.onEventAdded( roadMap , eventList , e , time );
		}
		
	}
	public void advance() {
		time++;
		for ( TrafficSimObserver obs : observer ) {
			obs.onAdvanceStart( roadMap , eventList , time );
		}
		
		try {
			while ( eventList.size() > 0 && eventList.get(0).getTime() == time ) {
				eventList.remove(0).execute( roadMap );
			}
			
			for( Junction j : roadMap.getJunctions() ) {
				j.advance(time);
			}
			
			for( Road r : roadMap.getRoads() ) {
				r.advance(time);
			}
		}catch( Exception e) {
			for ( TrafficSimObserver obs : observer ){
				obs.onError(e.getMessage());
			}
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		
		
		
		for ( TrafficSimObserver obs : observer ) {
			obs.onAdvanceEnd( roadMap , eventList , time );
		}
	}
	
	public void reset(){
		roadMap.reset();
		eventList.clear();
		time = 0;
		for ( TrafficSimObserver obs : observer ) {
			obs.onReset( roadMap , eventList , time );
		}
	}
	
	public JSONObject report() {

		JSONObject jo = new JSONObject();
		
		jo.put("time", time);
		jo.put("state", roadMap.report());
		
		return jo;
		
	}

	@Override
	public void addObserver(TrafficSimObserver o) {
		
		if( !observer.contains(o) ) {
			observer.add(o);
		}
		o.onRegister(roadMap, eventList, time);
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		if( observer.contains(o) ) {
			observer.remove(o);
		}
	}
	
}
