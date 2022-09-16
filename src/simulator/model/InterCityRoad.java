package simulator.model;


public class InterCityRoad extends Road{

	public InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		int totalCont = getTotalCO2();
		int totalContamination = 0;
		switch(getWeather()) {
			case SUNNY:
				totalContamination = ((100-2)*totalCont)/100;
				setTotalC02(totalContamination);
			break;
			case CLOUDY:
				totalContamination = ((100-3)*totalCont)/100;
				setTotalC02(totalContamination);

			break;
			case RAINY:
				totalContamination = ((100-10)*totalCont)/100;
				setTotalC02(totalContamination);

			break;
			case WINDY:
				totalContamination = ((100-15)*totalCont)/100;
				setTotalC02(totalContamination);

			break;
			case STORM:
				totalContamination = ((100-20)*totalCont)/100;
				setTotalC02(totalContamination);

			break;
		}
		
	}

	@Override
	void updateSpeedLimit() {
		if( getTotalCO2() > getContLimit()) {
			int speedLimit = getMaxSpeed()/2;
			setSpeedLimit( speedLimit );
		}else {
			int speedLimit = getMaxSpeed();
			setSpeedLimit( speedLimit );
		}
		
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		int speed = 0;
		if( getWeather() == Weather.STORM ) {
			speed = (speedLimit*8)/10;
		}else {
			speed = speedLimit;
		}
		
		return speed;
	}

}
