package simulator.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver  {
	
	private Controller ctrl;
	private JLabel labelTime;
	private JLabel labelCurrentTime = new JLabel();
	private JLabel labelEvent = new JLabel();
	
	StatusBar( Controller ctrl ) {
		this.ctrl = ctrl;
		ctrl.addObserver(this);
		initGUI();
	}

	private void initGUI() {
		
		this.setLayout(new FlowLayout((FlowLayout.LEFT)));
		this.setBorder(BorderFactory.createBevelBorder(1));
		
		labelTime = new JLabel("Time: ", JLabel.LEFT);
		labelCurrentTime = new JLabel("");
		this.add(labelTime);
		this.add(labelCurrentTime);
		
		this.add(new JSeparator(SwingConstants.VERTICAL));
		
		labelEvent = new JLabel("");
		this.add(labelEvent);
		
		
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		
		labelCurrentTime.setText(""+time);
		labelEvent.setText("");
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		labelCurrentTime.setText(""+time);
		labelEvent.setText(e.toString());
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		labelCurrentTime.setText(""+time);
		labelEvent.setText("");
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		labelCurrentTime.setText("" + time);
		labelEvent.setText("Welcome!");
	}

	@Override
	public void onError(String msg) {
		// TODO Auto-generated method stub
		
	}

}
