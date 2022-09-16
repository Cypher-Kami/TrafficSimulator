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

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver {
	
	private Controller ctrl;
	private List<Junction> junctionList;
	private String[] _cols = { "Id", "Green", "Queues" };
	
	JunctionsTableModel( Controller ctrl ){
		this.ctrl = ctrl;
		junctionList = new ArrayList<Junction>();
		this.ctrl.addObserver(this);
	}
	

	@Override
	public int getRowCount() {
		int value;
		if( junctionList == null ) {
			value = 0;
		}else {
			value =  junctionList.size();
		}
		return value;
	}

	public int getColumnCount() {
		return _cols.length;
	}
	
	public String getColumnName(int column) {
		return _cols[column];
	}
	public boolean isCellEditable(int row, int column) {
		
		return false;
	}

	@Override
	public String getValueAt(int rowIndex, int columnIndex) {
		String s= "";
		Junction j = junctionList.get(rowIndex);
		
		switch ( columnIndex ) {
			case 0:
				s = j.getId();
				break;
			case 1:
				int index = j.getGreenLightIndex();
				
				if( index == -1) {
					s = "NONE";
					
				}else {
					s = j.getInRoads().get(index).getId();
				}
				break;
				
			case 2:
				for (Road r: j.getInRoads()) {
					s= s+ " " + r.getId() + ":" + r.getVehicles().toString();
				}
				break;
			}
		return s;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		junctionList = map.getJunctions();
		fireTableDataChanged();
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		junctionList = map.getJunctions();
		fireTableDataChanged();
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		junctionList = map.getJunctions();
		fireTableDataChanged();
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		junctionList = map.getJunctions();
		fireTableDataChanged();
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		junctionList = map.getJunctions();
		fireTableDataChanged();
		
	}

	@Override
	public void onError(String msg) {
		// TODO Auto-generated method stub
		
	}
	
}
