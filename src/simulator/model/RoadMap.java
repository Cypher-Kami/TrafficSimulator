package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class RoadMap {
	private List<Junction> junctionList;
	private List<Road> roadList;
	private List<Vehicle> vehicleList;
	private Map<String,Junction> junctionMap;
	private Map<String,Road> roadMap;
	private Map<String,Vehicle> vehicleMap;
	
	protected RoadMap(){
		junctionList = new ArrayList<Junction>();
		roadList = new ArrayList<Road>();
		vehicleList = new ArrayList<Vehicle>();
		junctionMap  = new HashMap<String,Junction>();
		roadMap  = new HashMap<String,Road>();
		vehicleMap  = new HashMap<String,Vehicle>();
	}
	
	public void addJunction(Junction j) {
		if( !junctionMap.containsKey(j.getId()) ) {
			junctionMap.put(j.getId(), j);
			junctionList.add(j);
		}else {
			throw new IllegalArgumentException("Cruce con id " + j.getId() + " repetido");
		}
	}
	
	public void addRoad(Road r){
		if( (roadMap.get(r.getId()) != null)) {
			throw new IllegalArgumentException("carretera con id " + r.getId() + " repetido");
		}
		if( junctionMap.get(r.getSrc().getId()) == null ) {
			throw new IllegalArgumentException("No existe el cruce origen");

		}
		if(junctionMap.get(r.getDest().getId()) == null) {
			throw new IllegalArgumentException("No existe el cruce destino");

		}
		
		roadList.add(r);
		roadMap.put(r.getId(), r);
		
	}
	
	public void addVehicle(Vehicle v){
		List<Junction> auxJuncList = v.getItinerary();
		
		if(vehicleList.contains(v)) {
			throw new IllegalArgumentException("vehiculo con id " + v.getId() + " repetido");
		}
		
		for(int i = 0; i < auxJuncList.size() - 1; i++) {
			if( auxJuncList.get(i).roadTo(auxJuncList.get(i+1)) == null ) {
				throw new IllegalArgumentException("Itinerario nulo");

			}
		}
		
		vehicleList.add(v);
		vehicleMap.put(v.getId(), v);
		
	}
	
	public Junction getJunction(String id){
		return junctionMap.get(id);
	}
	
	public Road getRoad(String id){
		return roadMap.get(id);
	}
	
	public Vehicle getVehicle(String id){
		return vehicleMap.get(id);
	}
	
	public List<Junction> getJunctions(){
		return Collections.unmodifiableList(junctionList);
		
	}
	public List<Road> getRoads(){
		return Collections.unmodifiableList(roadList);
		
	}
	public List<Vehicle> getVehicles(){
		return Collections.unmodifiableList(vehicleList);
		
	}
	void reset() {
		roadList.clear();
		vehicleList.clear();
		junctionList.clear();
		roadMap.clear();
		junctionMap.clear();
		vehicleMap.clear();
	}
	
	public JSONObject report(){
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		JSONArray jav = new JSONArray();
		JSONArray jar = new JSONArray();
		
		jo.put("junctions", ja);
		for (Junction junction : junctionList){
			ja.put(junction.report());
		}
		
		jo.put("roads", jar);
		for (Road roads : roadList){
			jar.put(roads.report());
		}
		
		jo.put("vehicles", jav);
		for (Vehicle vehicle : vehicleList){
			jav.put(vehicle.report());
		}
		
		return jo;
		
	}
	
}
