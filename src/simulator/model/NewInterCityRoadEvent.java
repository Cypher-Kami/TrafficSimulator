package simulator.model;

public class NewInterCityRoadEvent extends Event{
	
	private String id;
	private int time;
	private String srcJunction;
	private String destJunction;
	private int length;
	private int co2Limit;
	private int maxSpeed;
	private Weather weather;
	
	public NewInterCityRoadEvent(int time, String id, String srcJun, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather){
		super(time);

		if(time < 0) {
			throw new IllegalArgumentException("El tiempo debe se rpositivo");
		}
		if(srcJun == null) {
			throw new IllegalArgumentException("El cruce de origen no puede ser nulo");
		}
		if(destJunc == null) {
			throw new IllegalArgumentException("El cruce de destino no puede ser nulo");
		}
		if(length < 0) {
			throw new IllegalArgumentException("La longitud debe ser positiva");
		}
		if(co2Limit < 0) {
			throw new IllegalArgumentException("El limite de contaminacion debe ser positivo");
		}
		if(maxSpeed < 0) {
			throw new IllegalArgumentException("La velocidad maxima debe se positiva");
		}
		if(weather == null) {
			throw new IllegalArgumentException("El clima no puede ser nulo ");
		}
		
		this.time = time;
		this.id = id;
		this.srcJunction = srcJun;
		this.destJunction = destJunc;
		this.length = length;
		this.co2Limit = co2Limit;
		this.maxSpeed = maxSpeed;
		this.weather = weather;
	}
	
	@Override
	public void execute(RoadMap map) {
		
		Road road = new InterCityRoad(id, map.getJunction(srcJunction), map.getJunction(destJunction), maxSpeed, co2Limit, length, weather );
		map.addRoad(road);
	}
	
	public String toString() {
		return "New Inter City Road '"+this.id+"'";
	} 

}
