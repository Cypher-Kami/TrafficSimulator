package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver{

	private Controller ctrl;
	private List<Road> roadList;
	private String[] _cols = { "ID", "Length", "Weather", "Max Speed", "Speed Limit", "Total CO2", "CO2 Limit" };
	
	RoadsTableModel( Controller ctrl ){
		this.ctrl = ctrl;
		roadList = new ArrayList<Road>();
		this.ctrl.addObserver(this);
	}
	
	
	public boolean isCellEditable(int row, int column) {
		
		return false;
	}

	@Override
	public int getRowCount() {
		
		return roadList.size();
	}

	public int getColumnCount() {
		return _cols.length;
	}
	
	public String getColumnName(int column) {
		return _cols[column];
	}
	
	@Override
	public String getValueAt(int rowIndex, int columnIndex) {
		String s = null;
		
		Road r = roadList.get(rowIndex);

		switch (columnIndex){
			case 0:
				s = r.getId();
				break;
			case 1:
				s = Integer.toString( r.getLength());
				break;
			case 2:
				s = r.getWeather().toString();
				break;
			case 3:
				s = Integer.toString(r.getMaxSpeed());
				break;
			case 4:
				s = Integer.toString(r.getSpeedLimit());
				break;
			case 5:
				s = Integer.toString(r.getTotalCO2());
				break;
			case 6:
				s = Integer.toString(r.getContLimit());
				break;
			default:
				break;
		}
		
		return s;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		roadList = map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		roadList = map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		roadList = map.getRoads();
		fireTableDataChanged();

	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		roadList = map.getRoads();
		fireTableDataChanged();

	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		roadList = map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onError(String msg) {
		// TODO Auto-generated method stub
		
	}

}
