package simulator.model;

public class NewJunctionEvent extends Event {
	
	private LightSwitchingStrategy lsStrategy;
	private DequeuingStrategy dqStrategy;
	private int xCoor, yCoor;
	private String id;

	public NewJunctionEvent(int time, String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		super(time);
		if(lsStrategy == null || dqStrategy == null) {
			throw new IllegalArgumentException("Estrategias nulas");
		}
		if((xCoor < 0) || (yCoor < 0)) {
			throw new IllegalArgumentException("Las coordenadas deben ser positivas");
		}
		
		this.id = id;
		this.lsStrategy = lsStrategy;
		this.dqStrategy = dqStrategy;
		this.xCoor = xCoor;
		this.yCoor = yCoor;
	}

	@Override
	public void execute(RoadMap map) {
		Junction junction = new Junction(id, lsStrategy, dqStrategy, xCoor, yCoor);
		map.addJunction(junction);
		
	}
	
	@Override
	public String toString() {
		return "New Junction '"+this.id+"'";
	} 

}
