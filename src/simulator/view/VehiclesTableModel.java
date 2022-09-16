package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver {
	
	private Controller ctrl;
	private List<Vehicle> vehicleList;
	private String[] _col = { "Id", "Location", "Itinerary", "CO2 Class", "Max Speed", "Speed", "Total CO2", "Distance" };
	
	public VehiclesTableModel( Controller ctrl ){
		this.ctrl = ctrl;
		vehicleList = new ArrayList<Vehicle>();
		this.ctrl.addObserver(this);
	}
	
	
	public boolean isCellEditable(int row, int column) {
		
		return false;
	}

	public int getRowCount() {
		return vehicleList.size();
	}
	
	public int getColumnCount() {
		return _col.length;
	}
	
	public String getColumnName(int column) {
		return _col[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s= "";
		Vehicle v = vehicleList.get(rowIndex);
		
		switch ( columnIndex) {
			case 0:
				s = v.getId();
				break;
				
			case 1:
				switch (v.getStatus()) {
					case PENDING:
						s= "Pending";
						break;
					case TRAVELING:
						s= v.getRoad().getId() + ":" +v.getLocation();
						break;
					case WAITING:
						s= "Waiting:"+v.getRoad().getDest().getId();
						break;
					case ARRIVED:
						s= "Arrived";
						break;
				}
				break;
			case 2:
				s = v.getItinerary();
				break;
			case 3:
				s = v.getContClass();
				break;
			case 4:
				s = v.getMaxSpeed();
				break;
			case 5:
				s = v.getSpeed();
				break;
			case 6:
				s = v.getTotalCO2();
				break;
			case 7:
				s = v.getLocation();
				break;
		}
		return s;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		vehicleList = map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		vehicleList = map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		vehicleList = map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		vehicleList = map.getVehicles();
		fireTableDataChanged();
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		vehicleList = map.getVehicles();
		fireTableDataChanged();
		
	}

	@Override
	public void onError(String msg) {
		// TODO Auto-generated method stub
		
	}

}
