package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Road extends SimulatedObject {
	
	protected String id;
	protected Junction srcJunc;
	protected Junction destJunc;
	protected int maxSpeed;
	protected int contLimit;
	protected int length;
	protected Weather weather;
	protected int speedLimit;
	protected int totalContamination;
	protected List<Vehicle> vehicles;
	
	
	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id);
		
		if(srcJunc == null) {
			throw new IllegalArgumentException("El cruce origen debe ser diferente de nulo");
		}
		
		if(destJunc == null) {
			throw new IllegalArgumentException("El cruce destino debe ser diferente de nulo");
		}
		if(maxSpeed <= 0) {
			throw new IllegalArgumentException("La velocidad maxima tiene que ser un numero positivo");
		}
		
		if(contLimit < 0) {
			throw new IllegalArgumentException("La contaminacion tiene que ser un numero positivo.");
		}
		
		if(length <= 0) {
			throw new IllegalArgumentException("La anchura de la carretera debe ser positiva");
		}
		
		if(weather == null) {
			throw new IllegalArgumentException("El clima no puede ser nulo");
		}
		
		if( id =="" || id == null ) {
			throw new IllegalArgumentException("Id nulo");
		}

		this.id = id;
		this.srcJunc = srcJunc;
		this.destJunc = destJunc;
		this.maxSpeed = maxSpeed;
		this.contLimit = contLimit;
		this.length = length;
		this.weather = weather;
		this.speedLimit = maxSpeed;
		this.vehicles = new ArrayList<Vehicle>();
		
		srcJunc.addOutGoingRoad(this);
		destJunc.addIncommingRoad(this);
			
	}

	public int getLength() {
		return this.length;
	}
	
	public String getId() {
		return this.id;
	}
	
	void enter(Vehicle v) {
		if( v.getLocation() != 0) {
			throw new IllegalArgumentException("La localizacion del vehiculo debe ser 0.");
		}if ( v.getSpeed() != 0 ) {
			throw new IllegalArgumentException("la velocidad del vehiculo debe ser 0.");
		}
		vehicles.add(v);
	}
	
	void exit(Vehicle v) {
		vehicles.remove(v);
	}
	
	public void setWeather(Weather w) {
		if( w != null ) {
			this.weather = w;
		}else {
			throw new IllegalArgumentException("El clima no puede ser nulo");
		}
	}
	
	void setSpeedLimit( int speedLimit) {
		this.speedLimit = speedLimit;
	}
	
	void addContamination(int c) {
		if( c >= 0) {
			this.totalContamination += c;
		}else {
			throw new IllegalArgumentException("La contaminacion no puede ser negativa");
		}
	}
	
	public void advance(int time) {
		reduceTotalContamination();
		updateSpeedLimit();
		
		for( Vehicle v : vehicles) {
			v.setSpeed(calculateVehicleSpeed(v));
			v.advance(time);
		}
		
		vehicles.sort( new Comparator<Vehicle>() {
			@Override
			public int compare(Vehicle v1, Vehicle v2) {
				if( v1.getLocation() < v2.getLocation()) {
					return 1;
				}else if( v1.getLocation() > v2.getLocation() ) {
					return -1;
				}else {
					return 0;
				}
			}
		});
	}
	
	public JSONObject report() {
		
		JSONObject jo = new JSONObject();
		jo.put("id", id);
		jo.put("speedlimit", speedLimit);
		jo.put("weather", weather.toString());
		jo.put("co2", totalContamination);	

		JSONArray ja = new JSONArray();

		for(Vehicle v : vehicles) {
			ja.put( v.getId() );
		}
		jo.put("vehicles", ja);
		
		return jo;
	}
	
	 public Junction getDest() {
		 return this.destJunc;
	 }
	 
	 public Junction getSrc() {
		 return this.srcJunc;
	 }
	 
	 public Weather getWeather() {
		 return weather;
	 }
	 
	 public int getContLimit() {
		 return contLimit;
	 }
	 public int getMaxSpeed() {
		 return maxSpeed;
	 }
	 
	 public int getTotalCO2() {
		 return totalContamination;
	 }
	 
	 public int getSpeedLimit() {
		 return this.speedLimit;
	 }
	 
	 public List<Vehicle> getVehicles() {
		 return Collections.unmodifiableList(vehicles);
	 }
	 
	 public void setVehicles(List<Vehicle> v) {
		 this.vehicles = v;
	 }
	 
	 public void setTotalC02( int reduce) {
		 this.totalContamination = reduce;
	 }
	 
	 public void setSrcJunc(Junction srcJunc) {
			this.srcJunc = srcJunc;
	 }

	 public void setDestJunc(Junction destJunc) {
			this.destJunc = destJunc;
	 }
	
	abstract void reduceTotalContamination();
	
	abstract void updateSpeedLimit();
	
	abstract int calculateVehicleSpeed(Vehicle v);

}
