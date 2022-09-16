package simulator.model;

import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject {
	
	private List<Road> srcJunc;
	private Map<Junction,Road> destJunc;
	private List<List<Vehicle >> queueList;
	private Map<Road,List<Vehicle>> queueRoad;
	private int currenGreen;
	private int lastStep;
	private LightSwitchingStrategy lsStrategy;
	private DequeuingStrategy dqStrategy;
	private int xCoor, yCoor;
	private String id;

	
	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
			super(id);
			if( id =="" || id == null ) {
				throw new IllegalArgumentException("Id nulo");
			}
			
			this.id = id;
			if( lsStrategy != null && dqStrategy != null ) {
				this.lsStrategy = lsStrategy;
				this.dqStrategy = dqStrategy;
			}else {
				throw new IllegalArgumentException("Las estrategias no pueden ser nulas.");
			}
			
			if( xCoor >= 0 && yCoor >= 0 ) {
				this.xCoor = xCoor;
				this.yCoor = yCoor;
			}else {
				throw new IllegalArgumentException("Las coordenadas no pueden ser negativas");
			}
			
			this.lastStep = 1;
			this.srcJunc = new ArrayList<Road>();
			this.destJunc = new HashMap<Junction,Road>();
			this.queueRoad = new HashMap<Road,List<Vehicle>>();
			this.queueList = new ArrayList <List<Vehicle >>();
			this.currenGreen = -1;
	}
	
	void addIncommingRoad(Road r) {
		if(!this.equals(r.getDest())) {
			throw new IllegalArgumentException("No es carretera entrante");
		}
		srcJunc.add(r);
		List<Vehicle> vehicleLinked = new LinkedList<Vehicle>();
		queueList.add(vehicleLinked);
		queueRoad.put(r, vehicleLinked);
	}
	
	void addOutGoingRoad(Road r) {
		if(!this.equals(r.getSrc())){
			throw new IllegalArgumentException("No existe el cruce.");
		}
		if(destJunc.get(r.getDest()) != null){
			throw new IllegalArgumentException("Mas de un cruce");
		}
		destJunc.put( r.getDest(), r);
	}
	
	void enter(Vehicle v) {
		List<Vehicle> auxList = new ArrayList<Vehicle>();
		Road road = v.getRoad();
		auxList = queueRoad.get(road);
		auxList.add(v);
	}
	
	public String getId() {
		return this.id;
	}
	
	public int getX() {
		return this.xCoor;
	}
	
	public int getY() {
		return this.yCoor;
	}
	
	public int getGreenLightIndex() {
		return currenGreen;
	}
	
	Road roadTo(Junction j) {
		return destJunc.get(j);
	}

	@Override
	void advance(int time) {
		List<Vehicle> auxList = new ArrayList<Vehicle>();
		List<Vehicle> ok = new ArrayList<Vehicle>();
		
		if( currenGreen != -1 && queueList.size() > 0 && queueList != null ) {
			
			auxList = queueList.get(currenGreen);
			ok = dqStrategy.dequeue(auxList);
			
			for( Vehicle v : ok ) {
				v.moveToNextRoad();
				auxList.remove(v);
			}
		}
		
		int auxChangeColor = lsStrategy.chooseNextGreen(srcJunc, queueList, currenGreen, lastStep, time);
		
		if( auxChangeColor != currenGreen ) {
			currenGreen = auxChangeColor;
			lastStep = time;
		}
		
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("id", id);
		if( currenGreen == -1 ) {
			jo.put("green", "none");
		}else {
			jo.put("green", srcJunc.get(currenGreen).getId());
		}
		
		JSONArray ja = new JSONArray();
		jo.put("queues", ja);
				
		for( Road ro : srcJunc) {
			JSONObject subJo = new JSONObject();
			ja.put(subJo);
			subJo.put("road", ro.getId());
			JSONArray subJa = new JSONArray();
			subJo.put("vehicles", subJa);
			for( Vehicle v : queueRoad.get(ro) ){
				subJa.put( v.getId() );
			}
		}
		
		return jo;
	}

	public List<Road> getInRoads() {
		return srcJunc;
	}

}
