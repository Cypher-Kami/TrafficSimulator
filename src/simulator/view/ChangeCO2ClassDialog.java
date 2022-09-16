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

import simulator.model.RoadMap;
import simulator.model.Vehicle;

public class ChangeCO2ClassDialog extends JDialog{
	
	private int status = 0;
	
	private JPanel visible;
	private JLabel text;
	private JLabel textVehicle;
	private JComboBox<Vehicle> vehicle;
	private JLabel textCO2;
	private JComboBox<Integer> co2;
	private JLabel textTicks;
	private JSpinner ticks;
	
	private JPanel buttons;
	private JPanel options;
	private JButton ok;
	private JButton cancel;

	
	private DefaultComboBoxModel<Vehicle> vehicleModel;
	private DefaultComboBoxModel<Integer> contaminationModel;
	
	ChangeCO2ClassDialog( Frame parent){
		super(parent, true);
		initGUI();
	}
	
	private void initGUI() {
		
		setTitle("Change CO2 Class");
		
		visible = new JPanel();
		visible.setLayout(new BoxLayout(visible, BoxLayout.Y_AXIS));
		setContentPane(visible);
		
		text = new JLabel("Schedule an event to change the CO2 class.");
		text.setAlignmentX(CENTER_ALIGNMENT);
		visible.add(text);
		visible.add(Box.createRigidArea(new Dimension(0, 20)));		
	
		buttons = new JPanel();
		buttons.setAlignmentX(CENTER_ALIGNMENT);
		visible.add(buttons);
		
		textVehicle = new JLabel("Vehicle: ", JLabel.CENTER);
		vehicleModel = new DefaultComboBoxModel<Vehicle>();
		vehicle = new JComboBox<Vehicle>(vehicleModel);
		vehicle.setVisible(true);
		buttons.add(textVehicle);
		buttons.add(vehicle);
		
		textCO2 = new JLabel("CO2 Class: ", JLabel.CENTER);
		contaminationModel = new DefaultComboBoxModel<Integer>();
		
		co2 = new JComboBox<Integer>(contaminationModel);
		co2.setVisible(true);
		
		buttons.add(textCO2);
		buttons.add(co2);
		
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
				ChangeCO2ClassDialog.this.setVisible(false);
			}
		});
		options.add(cancel);

		ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if ((vehicleModel.getSelectedItem() != null) && (contaminationModel.getSelectedItem() != null)){
					status = 1;
					ChangeCO2ClassDialog.this.setVisible(false);
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
		
		for( Vehicle v : map.getVehicles() ) {
			vehicleModel.addElement(v);
		}

		for (int i = 0; i <= 10; i++){
			contaminationModel.addElement(i);
		}
		
		setLocation(getParent().getLocation().x + 10, getParent().getLocation().y + 10);
		setVisible(true);
		
		return this.status;
	}
	
	public int getTicks () {
		int ticksF = (Integer) ticks.getValue();
		return ticksF;
	}
	
	public Vehicle getVehicle () {
		Vehicle v = (Vehicle) vehicleModel.getSelectedItem();
		return v;
	}
	
	public int getC02Class() {
		int classCont = (Integer) contaminationModel.getSelectedItem();
		return classCont;
	}
	
}
