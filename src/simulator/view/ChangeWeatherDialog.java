package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.Vehicle;
import simulator.model.Weather;

public class ChangeWeatherDialog extends JDialog {
	private int status = 0;
	
	private JPanel visible;
	private JLabel text;
	private JLabel textRoad;
	private JComboBox<Weather> weather;
	private JLabel textWeather;
	private JComboBox<Road> road;
	private JLabel textTicks;
	
	private JPanel buttons;
	private JPanel options;
	private JButton ok;
	private JButton cancel;

	
	private DefaultComboBoxModel<Road> roadModel;
	private DefaultComboBoxModel<Weather> weatherModel;
	
	private JSpinner ticks;
	
	ChangeWeatherDialog(Frame parent) {
		
		super(parent, true);
		initGUI();
	}
	
	private void initGUI() {
		// TODO Auto-generated method stub
		setTitle("Change Road Weather");
		
		visible = new JPanel();
		visible.setLayout(new BoxLayout(visible, BoxLayout.Y_AXIS));
		setContentPane(visible);
		
		text = new JLabel("Schedule an event to change the weather.");
		text.setAlignmentX(CENTER_ALIGNMENT);
		visible.add(text);
		visible.add(Box.createRigidArea(new Dimension(0, 20)));		
	
		buttons = new JPanel();
		buttons.setAlignmentX(CENTER_ALIGNMENT);
		visible.add(buttons);
		
		textWeather = new JLabel("Weather: ", JLabel.CENTER);
		
		weatherModel = new DefaultComboBoxModel<Weather>();
		weather = new JComboBox<Weather>(weatherModel);
		weather.setVisible(true);
		
		buttons.add(textWeather);
		buttons.add(weather);
		
		textRoad = new JLabel("Road: ", JLabel.CENTER);
		roadModel = new DefaultComboBoxModel<Road>();
		road = new JComboBox<Road>(roadModel);
		road.setVisible(true);
		buttons.add(textRoad);
		buttons.add(road);
		
		ticks = new JSpinner();
		textTicks = new JLabel("Ticks: ", JLabel.CENTER);
		ticks = new JSpinner(new SpinnerNumberModel(10, 1, 99999, 1));
		ticks.setMinimumSize(new Dimension(80, 30));
		ticks.setMaximumSize(new Dimension(200, 30));
		ticks.setPreferredSize(new Dimension(80, 30));
		
		buttons.add(textTicks);
		buttons.add(ticks);
		
		options = new JPanel();
		options.setAlignmentX(CENTER_ALIGNMENT);
		visible.add(options);
		
		cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				status = 0;
				ChangeWeatherDialog.this.setVisible(false);
			}
		});
		options.add(cancel);

		ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if ((weatherModel.getSelectedItem() != null) && (roadModel.getSelectedItem() != null)){
					status = 1;
					ChangeWeatherDialog.this.setVisible(false);
				}
			}
		});
		options.add(ok);

		setPreferredSize(new Dimension(500, 200));
		pack();
		setResizable(false);
		setVisible(false);
	}

	public int open( RoadMap map) {
		
		for (Road r : map.getRoads()){
			roadModel.addElement(r);
		}
		
		for (Weather w : Weather.values()){
			weatherModel.addElement(w);
		}
		setLocation(getParent().getLocation().x + 10, getParent().getLocation().y + 10);
		setVisible(true);
		
		return status;
	}
	
	public int getTicks () {
		int ticksF = (Integer) ticks.getValue();
		return ticksF;
	}
	
	public Road getRoad () {
		Road r = (Road) roadModel.getSelectedItem();
		return r;
	}
	
	public Weather getWeather() {
		Weather weather = (Weather) weatherModel.getSelectedItem();
		return weather;
	}
}
