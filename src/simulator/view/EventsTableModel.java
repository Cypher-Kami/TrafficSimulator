package simulator.view;

import java.util.ArrayList;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;


public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Event> eventList;
	private Controller ctrl;
	private String _cols[] = { "Time", "Desc." };
	
	public EventsTableModel(Controller ctrl ){
		this.ctrl = ctrl;
		eventList = new ArrayList<Event>();
		this.ctrl.addObserver(this);
	}
	


	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		eventList = events;
		fireTableDataChanged();
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		eventList = events;
		fireTableDataChanged();

	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		eventList = events;
		fireTableDataChanged();

	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		eventList = events;
		fireTableDataChanged();

	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		eventList = events;
		fireTableDataChanged();

	}

	@Override
	public void onError(String msg) {
		// TODO Auto-generated method stub
		
	}

	public int getRowCount() {
		int value;
		if( eventList == null) {
			value = 0;
		}else {
			value = eventList.size();
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
	public Object getValueAt(int rowIndex, int columnIndex) {
		String s= "";
		Event e= eventList.get(rowIndex);
		switch ( columnIndex) {
			case 0:
				s= "" + e.getTime();
				break;
			case 1:
				s= e.toString();
				break;
			default:
				assert(false);
		}
		return s;
	}

}
