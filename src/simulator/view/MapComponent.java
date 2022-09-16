package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
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
import simulator.model.VehicleStatus;

public class MapComponent extends JPanel implements TrafficSimObserver {

	private static final int _JRADIUS = 10;

	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;

	private RoadMap _map;

	private Image _car;

	MapComponent(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}

	private void initGUI() {
		_car = loadImage("car_front.png");
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
			
		} else {
			updatePrefferedSize();
			drawMap(g);
		}
	}

	private void drawMap(Graphics g) {
		drawRoads(g);
		drawVehicles(g);
		drawJunctions(g);
	}

	private void drawRoads(Graphics g) {
		for (Road r : _map.getRoads()) {

			int x1 = r.getSrc().getX();
			int y1 = r.getSrc().getY();
			int x2 = r.getDest().getX();
			int y2 = r.getDest().getY();

			Color arrowColor = _RED_LIGHT_COLOR;
			
			Junction j = r.getDest();
			
			int index = j.getGreenLightIndex();
			
			if (index != -1 && r.equals(j.getInRoads().get(index))) {
				arrowColor = _GREEN_LIGHT_COLOR;
			}

			int roadColorValue = 200 - (int) (200.0 * Math.min(1.0, (double) r.getTotalCO2() / (1.0 + (double) r.getContLimit())));
			Color roadColor = new Color(roadColorValue, roadColorValue, roadColorValue);

			drawLineWithArrow(g, x1, y1, x2, y2, 15, 5, roadColor, arrowColor);
		}
	}

	private void drawVehicles(Graphics g) {
		
		for (Vehicle v : _map.getVehicles()) {
			
			if (v.getStatus() != VehicleStatus.ARRIVED) {

				Road r = v.getRoad();
							
				int x1 = r.getSrc().getX();
				int y1 = r.getSrc().getY();
				int x2 = r.getDest().getX();
				int y2 = r.getDest().getY();

				double locationN = ((float)v.getLocation()) / r.getLength();
				
				int vX = (int)(x1 + (x2-x1)*locationN); 
				int vY = (int)(y1 + (y2-y1)*locationN);

				int vLabelColor = (int) (25.0 * (10.0 - (double) v.getContClass()));
				g.setColor(new Color(0, vLabelColor, 0));

				g.fillOval(vX - 1, vY - 6, 14, 14);
				g.drawImage(_car, vX, vY - 6, 12, 12, this);
				g.drawString(v.getId(), vX, vY - 6);

			}
		}
	}

	private void drawJunctions(Graphics g) {
		for (Junction j : _map.getJunctions()) {

			int x = j.getX();
			int y = j.getY();

			g.setColor(_JUNCTION_COLOR);
			g.fillOval(x - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);

			g.setColor(_JUNCTION_LABEL_COLOR);
			g.drawString(j.getId(), x, y);
		}
	}

	private void updatePrefferedSize() {
		int maxCX = 200;
		int maxCY = 200;
		for (Junction j : _map.getJunctions()) {
			maxCX = Math.max(maxCX, j.getX());
			maxCY = Math.max(maxCY, j.getY());
		}
		maxCX += 20;
		maxCY += 20;
		
		setPreferredSize(new Dimension(maxCX, maxCY));
		setSize(new Dimension(maxCX, maxCY));
	}

	private void drawLineWithArrow(
		Graphics g, 
		int x1, int y1, 
		int x2, int y2, 
		int w, int h, 
		Color lineColor, Color arrowColor) {

		int dx = x2 - x1, dy = y2 - y1;
		double D = Math.sqrt(dx * dx + dy * dy);
		double xm = D - w, xn = xm, ym = h, yn = -h, x;
		double sin = dy / D, cos = dx / D;

		x = xm * cos - ym * sin + x1;
		ym = xm * sin + ym * cos + y1;
		xm = x;

		x = xn * cos - yn * sin + x1;
		yn = xn * sin + yn * cos + y1;
		xn = x;

		int[] xpoints = { x2, (int) xm, (int) xn };
		int[] ypoints = { y2, (int) ym, (int) yn };

		g.setColor(lineColor);
		g.drawLine(x1, y1, x2, y2);
		g.setColor(arrowColor);
		g.fillPolygon(xpoints, ypoints, 3);
	}

	private Image loadImage(String img) {
		Image image = null;
		try {
			image= ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return image;
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
