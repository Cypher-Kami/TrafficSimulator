package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class Vehicle extends SimulatedObject {
	
	protected List<Junction> itinerary;
	protected int maxSpeed;
	protected int currentSpeed;
	protected VehicleStatus status;
	protected Road road;
	protected int location;
	protected int contClass;
	protected int totalContamination;
	protected int distance;
	protected String id;
	protected int lastJunction;
	protected Junction srcJunction;
	protected Junction destJunction;
	
	
	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) {
		super(id);
		if( id =="" || id == null ) {
			throw new IllegalArgumentException("Id nulo");
		}
		this.id = id;
		if( maxSpeed > 0) {
			this.maxSpeed = maxSpeed;
		}else {
			throw new IllegalArgumentException("ERROR DEBE SER UN NUMERO POSITIVO");
		}
		
		if( contClass >= 0 && contClass <= 10) {
			this.contClass = contClass;
		}else {
			throw new IllegalArgumentException("VALOR FUERA DEL RANGO ENTRE 1 Y 10");
		}
		if( itinerary.size() >= 2) {
			this.itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));
		}else {
			throw new IllegalArgumentException("NUMERO DE ELEMENTOS INCORRECTO, DEBEN SER AL MENOS 2");
		}
		this.currentSpeed = 0;
		this.road = null;
		this.location = 0;
		this.distance = 0;
		this.status = VehicleStatus.PENDING;
		this.lastJunction = 0;
		this.srcJunction = null;
		this.destJunction = null;
	}

	@Override
	void advance(int time) {
		int newLocation, c, prevLoc;
		if( status == VehicleStatus.TRAVELING) {
			
			newLocation = Math.min(location + currentSpeed, road.getLength());
			prevLoc = location;
			c = contClass * (newLocation - location);
			distance += (newLocation - location);
			this.totalContamination += c;
			road.addContamination( c );
			this.location = newLocation;
			
			if( this.location == road.getLength() ) {
				road.getDest().enter(this);
				this.status = VehicleStatus.WAITING;
				this.currentSpeed = 0;
				this.lastJunction++;
			}
			
		}else {
			this.currentSpeed = 0;
		}
		
	}
	
	public void moveToNextRoad() {
		
		if( status != VehicleStatus.PENDING && status != VehicleStatus.WAITING) {
			throw new IllegalArgumentException("NO SE PUEDE CRUZAR");
		}
		if( road != null) {
			road.exit(this);
		}
		
		if( this.lastJunction == itinerary.size() - 1 ) {
			this.status = VehicleStatus.ARRIVED;
			this.location = 0;
			this.road = null;
		}else {
			this.srcJunction = itinerary.get(lastJunction);
			this.destJunction = itinerary.get(lastJunction+1);
			Road aux = srcJunction.roadTo(destJunction);
			this.road = aux;
			this.location = 0;
			this.road.enter(this);
			this.status = VehicleStatus.TRAVELING;

		}
		
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("id", id);
		jo.put("speed", currentSpeed);
		jo.put("distance", distance);
		jo.put("co2", totalContamination);
		jo.put("class", contClass);
		jo.put("status", status.toString());
		if(status != VehicleStatus.PENDING && status != VehicleStatus.ARRIVED) {
			jo.put("road", road.getId());
			jo.put("location", location);
		}
		
		return jo;
	}
	
	public void setSpeed(int s) {
		if( s >= 0) {
			if(status == VehicleStatus.TRAVELING) {
				this.currentSpeed = Math.min(s, maxSpeed);
			}
		}else {
			throw new IllegalArgumentException("ERROR DEBE SER UN NUMERO POSITIVO");
		}
	}
	
	public void setContClass(int c) {
		if( c >= 0 && c <= 10) {
			this.contClass = c;
		}else {
			throw new IllegalArgumentException("VALOR FUERA DEL RANGO ENTRE 1 Y 10");
		}
	}
	
	public void setItinerary(List<Junction> itinerary) {
		this.itinerary = itinerary;
	}
	
	public String getId() {
		return this.id;
	}
	
	public int getLocation(){
		return this.location;
	}
	
	public int getSpeed(){
		return this.currentSpeed;
	}
	
	public int getMaxSpeed(){
		return this.maxSpeed;
	}
	
	public int getContClass(){
		return this.contClass;
	}
	
	public VehicleStatus getStatus(){
		return status;
	}
	
	public int getTotalCO2(){
		return this.totalContamination;
	}
	
	public List<Junction> getItinerary(){
		return itinerary;
	}
	
	public Road getRoad(){
		return road;
	}

}
