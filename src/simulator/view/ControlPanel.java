package simulator.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.SetContClassEvent;
import simulator.model.SetWeatherEvent;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.Weather;

public class ControlPanel extends JPanel implements TrafficSimObserver  {
		
	private JToolBar toolBar;
	private JButton buttonFile;
	private JButton buttonC02;
	private JButton buttonWeather;
	private JButton run;
	private JButton stop;
	private JButton buttonExit;
	
	private JLabel textTicks;
	private JSpinner _ticksSpinner;
	
	private JFileChooser selectFile;
	
	private Controller ctrl;
	private RoadMap roadMap;
	private int time;
	private boolean _stop;
	
	private int status = 0;
	
	ControlPanel( Controller ctrl ) {
		this.ctrl = ctrl;
		initGUI();
		ctrl.addObserver(this);
		_stop = true;
	}
	
	private void initGUI() {
		
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		this.setLayout(new BorderLayout());
		this.add(toolBar, BorderLayout.PAGE_START);

		selectFile = new JFileChooser();
		
		toolBar.addSeparator();
		buttonChargeFile();
		
		toolBar.addSeparator();
		buttonC02();
		
		buttonWeather();	
		
		toolBar.addSeparator();
		
		buttonRun();
		buttonStop();
		
		spinnerTicks();
		
		toolBar.add(Box.createHorizontalGlue());
		toolBar.addSeparator();
		buttonExit();
	}

	private void buttonExit() {
		buttonExit = new JButton();
		buttonExit.setToolTipText("Exit");
		buttonExit.setAlignmentX(RIGHT_ALIGNMENT);
		buttonExit.setIcon(new ImageIcon("./resources/icons/exit.png"));
		buttonExit.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				
				exit();
			}
			
		});
		
		toolBar.add(buttonExit);

	}
	
	private void exit() {
		int confirm = JOptionPane.showConfirmDialog(null, "Exit?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if( confirm == 0 ) {
			System.exit(0);
		}
		
	}

	private void spinnerTicks() {
		_ticksSpinner = new JSpinner(new SpinnerNumberModel(10,1,999999,1));
		_ticksSpinner.setMaximumSize(new Dimension(80, 30));
		_ticksSpinner.setMinimumSize(new Dimension(200, 40));
		_ticksSpinner.setPreferredSize(new Dimension(80, 30));
		
		textTicks = new JLabel();
		
		textTicks.setText("Ticks:");
		textTicks.setAlignmentX(CENTER_ALIGNMENT);
		toolBar.add(textTicks);
		toolBar.add(_ticksSpinner);
		
	}

	private void buttonStop() {
		stop = new JButton();
		stop.setToolTipText("Stop");
		stop.setIcon(new ImageIcon("./resources/icons/stop.png"));	
		
		stop.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				
				stop_sim();
			}
			
		});
		toolBar.add(stop);		
	}
	
	private void stop_sim() {
		_stop = true;
	}

	private void buttonRun() {
		run = new JButton();
		run.setToolTipText("Run");
		run.setIcon(new ImageIcon("./resources/icons/run.png"));
		
		run.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				start();
			}
			
		});
		toolBar.add(run);		
	}

	protected void start() {
		this._stop = false;
				
		int ticks = (int) _ticksSpinner.getValue();

		enableToolBar(false);
	
		run_sim(ticks);
	}

	private void buttonWeather() {
		buttonWeather = new JButton();
		buttonWeather.setToolTipText("Weather");
		buttonWeather.setIcon(new ImageIcon("./resources/icons/weather.png"));
		
		buttonWeather.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				changeWeather();
			}
			
		});
		toolBar.add(buttonWeather);

	}

	private void buttonC02() {
		buttonC02 = new JButton();
		buttonC02.setToolTipText("C02");
		buttonC02.setIcon(new ImageIcon("./resources/icons/co2class.png"));
		
		buttonC02.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				ChangeC02Class();
			}
			
		});
		
		toolBar.add(buttonC02);		
	}

	protected void ChangeC02Class() {

		ChangeCO2ClassDialog c02Dialog = new ChangeCO2ClassDialog((Frame)SwingUtilities.getWindowAncestor(this));
		
		status = c02Dialog.open( roadMap );
		
		if( status == 1 ) {
			
			List<Pair<String , Integer>> pairList = new ArrayList<Pair<String , Integer>>();
			
			String v = c02Dialog.getVehicle().getId();
			
			int c = c02Dialog.getC02Class();
			
			int timeTicks = c02Dialog.getTicks();
			
			pairList.add( new Pair <String , Integer> (v , c));
			
			
			try {
				SetContClassEvent contEvent = new SetContClassEvent(time + timeTicks , pairList);
				ctrl.addEvent( contEvent );
			}catch( Exception e ) {
	        	  JOptionPane.showMessageDialog(null, "Error: C02 dialog ", "Error" , JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void changeWeather() {
		ChangeWeatherDialog weatherDialog = new ChangeWeatherDialog((Frame)SwingUtilities.getWindowAncestor(this));
		int status = weatherDialog.open( roadMap );
		
		if( status == 1 ) {
			
			List<Pair<String , Weather>> pairList = new ArrayList<Pair<String , Weather>>();
			
			String r = weatherDialog.getRoad().getId();
			
			Weather c = weatherDialog.getWeather();
			
			int timeTicks = weatherDialog.getTicks();
			
			pairList.add( new Pair <String , Weather> (r , c));
			
			try {
				SetWeatherEvent weatherEvent = new SetWeatherEvent(time + timeTicks , pairList);
				ctrl.addEvent( weatherEvent );
			}catch( Exception e ) {
	        	  JOptionPane.showMessageDialog(null, "Error: C02 dialog ", "Error" , JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void buttonChargeFile() {
		selectFile.setDialogTitle("Carga de fichero de datos");
		selectFile.setCurrentDirectory(new File("./resources/examples/"));
		selectFile.setMultiSelectionEnabled(false);
		selectFile.setFileFilter(new FileNameExtensionFilter("JavaScript Object Notation (JSON)", "json"));

		buttonFile = new JButton();
		buttonFile.setToolTipText("Carga los archivos de datos a la aplicacion");
		buttonFile.setIcon(new ImageIcon("./resources/icons/open.png"));
		
		buttonFile.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				loadFile();
			}
			
			private void loadFile() {
				updateUI();
				int returnValue = selectFile.showOpenDialog(getParent());
		        if (returnValue == JFileChooser.APPROVE_OPTION) {
		        	InputStream in;
			        try {
			          in = new FileInputStream( selectFile.getSelectedFile());
			         
			        	  ctrl.reset();
			        	  ctrl.loadEvents(in);
			        	  
			          }catch( FileNotFoundException e ) {
			        	  JOptionPane.showMessageDialog(null, "Error: fichero no existe ", "Error" , JOptionPane.ERROR_MESSAGE);
	
			          }
		        }
			}

			
		});
		
		toolBar.add(buttonFile);
	}
	
	private void run_sim(int n) {

		if (n > 0 && !_stop) {
			try {
				
				ctrl.run(1, null);
				
			} catch (Exception e) {
	        	JOptionPane.showMessageDialog(null, "Error: Ejecutando el programa ", "Error" , JOptionPane.ERROR_MESSAGE);
	        	enableToolBar(true);
				_stop = true;
			}
			SwingUtilities.invokeLater(new Runnable() {
			@Override
				public void run() {
					run_sim(n - 1);
				}
			});
			} else {
				
	        	enableToolBar(true);
				_stop = true;
			}
	}
		
	private void enableToolBar(boolean b) {
		buttonFile.setEnabled(b);
		buttonC02.setEnabled(b);
		buttonWeather.setEnabled(b);
		run.setEnabled(b);
		buttonExit.setEnabled(b);
		_ticksSpinner.setEnabled(b);
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		roadMap = map;
		this.time = time;
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		roadMap = map;
		this.time = time;
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		roadMap = map;
		this.time = time;
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		roadMap = map;
		this.time = time;
	}

	@Override
	public void onError(String msg) {
		// TODO Auto-generated method stub
		
	}
	
	

}
