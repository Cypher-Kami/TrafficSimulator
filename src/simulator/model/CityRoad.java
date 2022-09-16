package simulator.model;

public class CityRoad extends Road{

	public CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		int totalCont = getTotalCO2();
		int aux = 0;
		
		if(getWeather() == Weather.WINDY || getWeather() == Weather.STORM) {
			aux = 10;
		}else if (getWeather() == Weather.SUNNY || getWeather() == Weather.CLOUDY || getWeather() == Weather.RAINY){
			aux = 2;
		}
		setTotalC02(Math.max(totalCont - aux , 0));
		
	}

	@Override
	void updateSpeedLimit() {
		speedLimit = maxSpeed;
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		int speed = 0;
		
		if(v.getStatus() == VehicleStatus.TRAVELING) {
			speed = ((11-v.getContClass())*getSpeedLimit())/11;
		}
		return speed;
	}

}
