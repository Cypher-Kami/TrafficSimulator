package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class MapByRoadComponent extends JPanel implements TrafficSimObserver  {
	
	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _TAG_ROAD_COLOR = Color.BLACK;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;
	private static final int _JRADIUS = 5;
	
	private RoadMap _map;
	
	private Image _car, weather,contamination;

	 MapByRoadComponent( Controller ctrl ) {
		ctrl.addObserver(this);
		initGUI();
	}

	private void initGUI() {
		_car = loadImage("car.png");
		setPreferredSize (new Dimension (300, 200));
	}
	
	private Image loadImage(String img) {
		Image image = null;
		try {
			image = ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return image;
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (_map == null || _map.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		}
		drawMap(g);
	}

	private void drawMap( Graphics g ) {
		int y = 50;
		int x1 = 50;
		int x2 = getWidth() - 100;
		
		for(Road r : _map.getRoads()){
			
			g.setColor (_TAG_ROAD_COLOR);
			
			g.drawString(r.getId(), x1, y);
			
			g.drawLine( x1, y , x2, y);
			
			g.setColor(_JUNCTION_COLOR);
			
			g.fillOval( x1 -  _JRADIUS/2, y -  _JRADIUS/2, _JRADIUS, _JRADIUS );
			
			int index = r.getDest().getGreenLightIndex();
			
			Junction j = r.getDest();
			
			if( index != -1 && r.equals(j.getInRoads().get(index)) ) {
				g.setColor(_GREEN_LIGHT_COLOR);
			}else {
				g.setColor(_RED_LIGHT_COLOR);
			}
			
			g.fillOval(x2 - _JRADIUS/2, y - _JRADIUS/2, _JRADIUS, _JRADIUS);
			
			g.setColor(_JUNCTION_LABEL_COLOR);
			g.drawString(r.getSrc().toString(), x1, y - _JRADIUS);
			g.drawString(j.toString(), x2 , y - _JRADIUS);
			
			switch(r.getWeather()) {
			
				case SUNNY:
					weather = loadImage("sun.png");
					break;
				case CLOUDY:
					weather = loadImage("cloud.png");
					break;
				case RAINY:
					weather = loadImage("rain.png");
					break;
				case WINDY:
					weather = loadImage("wind.png");
					break;
				case STORM:
					weather = loadImage("storm.png");
					break;
			}
			g.drawImage(weather, x2 + 15, y - _JRADIUS*2, 32, 32, this);
			
			int c = (int) Math.floor(Math.min((double)r.getTotalCO2()/(1.0 + (double)r.getContLimit()),1.0) / 0.19);
			switch(c){
				case 0:
					contamination = loadImage("cont_0.png");
					break;
				case 1:
					contamination = loadImage("cont_1.png");
					break;
				case 2:
					contamination = loadImage("cont_2.png");
					break;
				case 3:
					contamination = loadImage("cont_3.png");
					break;
				case 4:
					contamination = loadImage("cont_4.png");
					break;
				case 5:
					contamination = loadImage("cont_5.png");
					break;
				case 6:
					contamination = loadImage("cont_6.png");
					break;
			}
			g.drawImage(contamination, x2 + 55, y - _JRADIUS*2, 32, 32, this);
			
			if( r.getVehicles() != null || r.getVehicles().size() != 0 ) {
				int coordX;
				for( Vehicle v : r.getVehicles() ) {
					coordX =  x1+(int)((x2-x1)*((double)v.getLocation()/(double)r.getLength()));
					
					g.setColor(_GREEN_LIGHT_COLOR);
					g.drawString(v.getId(), coordX, y - _JRADIUS-5);
					g.drawImage(_car, coordX, y - _JRADIUS - 3, 16, 16, this);
				}
			}
			
			y= y+60;

		}
		
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		_map = map;
		repaint();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		_map = map;
		repaint();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_map = map;
		repaint();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		_map = map;
		repaint();
	}

	@Override
	public void onError(String err) {
	}

}
