import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Formatter;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")

public class MapViewerLite extends JFrame implements MouseMotionListener, MouseListener, MouseWheelListener {
	
	final static String listOfGeoSrvices_Str[] = {
		" 2Gis map",
        " Yandex map",
        " Yandex satellite",
        " Yandex narod",
        " Yandex hybrid",
        " Google roadmap",
        " Google satellite", 
        " Google terrain",
        " Google hybrid",
        " OpenStreetMap",
        " Nokia map",
        " Nokia satellite",
        " Nokia roadmap",
        " Nokia hybrid",
    };
		
    int xSizeOfTile_Int=650, ySizeOfTile_Int=450, zoom_Int=15, indexOflistOfGeoSrv_Int=0,  xFirstPressed, yFirstPressed;
    Double xGeo_Double=30.719179, yGeo_Double=46.476796;
    public boolean isDragged= false;
    
	JComboBox<String> choicesJCBox = new JComboBox<String>( listOfGeoSrvices_Str );
	ImagePanel panel = new ImagePanel();
	String path; // = "http://static-maps.yandex.ru/1.x/?ll=30.719179,46.476796&z=18&size=350,350&l=sat,trf";
    URL url;
    BufferedImage image;
	JSlider zoomSlider = new JSlider(10, 20);
	
	public MapViewerLite() {
		
		Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
		int vert = sSize.height;
		int hor  = sSize.width;
		
		setBounds( (hor-700)/2, (vert-500)/2 , 700, 500);
		setResizable(false);
		setTitle("MapViewerLite");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		choicesJCBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
                JComboBox<String> cb = (JComboBox<String>)e.getSource();
                setIndexOflistOfGeoSrvices(cb.getSelectedIndex());
                redraw();
                repaint();
            };
		});
				
				
		zoomSlider.addChangeListener( new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {

		        JSlider slider = (JSlider)e.getSource();
		        setZoom(slider.getValue());
		        redraw();
			};
		});
		
		
		add("North", choicesJCBox);
		add("Center", panel);		
		add("South", zoomSlider);
		MouseWheelListener mouseWheelListener = null;
		panel.addMouseWheelListener(this);
		panel.addMouseListener(this);
		panel.addMouseMotionListener(this);
		redraw();
	}
	
	public class ImagePanel extends JPanel {
	    private Image image;
	    public Image getImage() {
	        return image;
	    }
	    public void setImage(Image image) {
	        this.image = image;
	    }
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        if(image != null){
	            g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	        }
	    }
	}
	
    @Override
    public void mousePressed(MouseEvent e) {

    	xFirstPressed = e.getX();
        yFirstPressed = e.getY();
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {

    	isDragged = true;
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    	if (isDragged){
    		
    		double k = (double) (zoom_Int );
    		k /= 400000;
    		double x = k * (e.getX() - xFirstPressed);
    		double y = k * (e.getY() - yFirstPressed);    		
    		
    		xGeo_Double -= x;
    		yGeo_Double += y;
    		isDragged = false;
    		redraw();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

	
    public void mouseWheelMoved(MouseWheelEvent e) {
    	
        int notches = e.getWheelRotation();       
        zoomSlider.setValue(getZoom() - notches);
    }
    

	public void redraw() {
		
		try {
			
			url = new URL(getUrl());
	        image = ImageIO.read(url);
	     
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		panel.setImage(image);
		repaint();
	}

	public void setIndexOflistOfGeoSrvices(int i) {
		
		indexOflistOfGeoSrv_Int = i;
	}
	
	public int getZoom() {
		
		return zoom_Int;		
	}
	
	public void setZoom(int z) {
		
		zoom_Int = z;		
	}
		
	private String getUrl() {
		
		switch (indexOflistOfGeoSrv_Int) {
		
		case 0: 
			path = "http://static.maps.2gis.com/1.0?center=" + (new Formatter()).format(Locale.US, "%.6f", xGeo_Double) + "," + 
					(new Formatter()).format(Locale.US, "%.6f", yGeo_Double) + "&zoom=" + zoom_Int + "&size=" + xSizeOfTile_Int + ","
					+ ySizeOfTile_Int;
			break;
		case 1: 
			path = "http://static-maps.yandex.ru/1.x/?ll=" + (new Formatter()).format(Locale.US, "%.6f", xGeo_Double) + "," + 
					(new Formatter()).format(Locale.US, "%.6f", yGeo_Double) + "&z=" + zoom_Int + "&size=" + xSizeOfTile_Int + ","
					+ ySizeOfTile_Int + "&l=map";
			break;
		case 2: 
			path = "http://static-maps.yandex.ru/1.x/?ll=" + (new Formatter()).format(Locale.US, "%.6f", xGeo_Double) + "," + 
					(new Formatter()).format(Locale.US, "%.6f", yGeo_Double) + "&z=" + zoom_Int + "&size=" + xSizeOfTile_Int + ","
					+ ySizeOfTile_Int + "&l=sat";
			break;
		case 3: 
			path = "http://static-maps.yandex.ru/1.x/?ll=" + (new Formatter()).format(Locale.US, "%.6f", xGeo_Double) + "," + 
					(new Formatter()).format(Locale.US, "%.6f", yGeo_Double) + "&z=" + zoom_Int + "&size=" + xSizeOfTile_Int + ","
					+ ySizeOfTile_Int + "&l=pmap";
			break;
		case 4: 
			path = "http://static-maps.yandex.ru/1.x/?ll=" + (new Formatter()).format(Locale.US, "%.6f", xGeo_Double) + "," + 
					(new Formatter()).format(Locale.US, "%.6f", yGeo_Double) + "&z=" + zoom_Int + "&size=" + xSizeOfTile_Int + ","
					+ ySizeOfTile_Int + "&l=sat,skl";
			break;
		case 5: 
			path = "http://maps.googleapis.com/maps/api/staticmap?center=" + (new Formatter()).format(Locale.US, "%.6f", yGeo_Double) + "," + 
					(new Formatter()).format(Locale.US, "%.6f", xGeo_Double) + "&zoom=" + zoom_Int + "&size=" + xSizeOfTile_Int + "x"
					+ ySizeOfTile_Int + "&maptype=roadmap";
			break;
		case 6: 
			path = "http://maps.googleapis.com/maps/api/staticmap?center=" + (new Formatter()).format(Locale.US, "%.6f", yGeo_Double) + "," + 
					(new Formatter()).format(Locale.US, "%.6f", xGeo_Double) + "&zoom=" + zoom_Int + "&size=" + xSizeOfTile_Int + "x"
					+ ySizeOfTile_Int + "&maptype=satellite";
			break;
		case 7: 
			path = "http://maps.googleapis.com/maps/api/staticmap?center=" + (new Formatter()).format(Locale.US, "%.6f", yGeo_Double) + "," + 
					(new Formatter()).format(Locale.US, "%.6f", xGeo_Double) + "&zoom=" + zoom_Int + "&size=" + xSizeOfTile_Int + "x"
					+ ySizeOfTile_Int + "&maptype=terrain";
			break;
		case 8: 
			path = "http://maps.googleapis.com/maps/api/staticmap?center=" + (new Formatter()).format(Locale.US, "%.6f", yGeo_Double) + "," + 
					(new Formatter()).format(Locale.US, "%.6f", xGeo_Double) + "&zoom=" + zoom_Int + "&size=" + xSizeOfTile_Int + "x"
					+ ySizeOfTile_Int + "&maptype=hybrid";
			break;
		case 9: 
			path = "http://staticmap.openstreetmap.de/staticmap.php?center=" + (new Formatter()).format(Locale.US, "%.6f", yGeo_Double) + "," + 
					(new Formatter()).format(Locale.US, "%.6f", xGeo_Double) + "&zoom=" + zoom_Int + "&size=" + xSizeOfTile_Int + "x"
					+ ySizeOfTile_Int;
			break;			
		case 10: 
			path = "http://image.maps.cit.api.here.com/mia/1.6/mapview?app_id=DemoAppId01082013GAL&app_code=AJKnXv84fjrb0KIHawS0Tg&c=" + 
					(new Formatter()).format(Locale.US, "%.6f", yGeo_Double) + "," +(new Formatter()).format(Locale.US, "%.6f", xGeo_Double) + 
					"&z=" + zoom_Int + "&h=" + ySizeOfTile_Int + "&w=" + xSizeOfTile_Int + "&nodot" + "&ml=rus" + "&t=2";
			break;
		case 11: 
			path = "http://image.maps.cit.api.here.com/mia/1.6/mapview?app_id=DemoAppId01082013GAL&app_code=AJKnXv84fjrb0KIHawS0Tg&c=" + 
					(new Formatter()).format(Locale.US, "%.6f", yGeo_Double) + "," +(new Formatter()).format(Locale.US, "%.6f", xGeo_Double) + 
					"&z=" + zoom_Int + "&h=" + ySizeOfTile_Int + "&w=" + xSizeOfTile_Int + "&nodot" + "&ml=rus" + "&t=1";
			break;
		case 12: 
			path = "http://image.maps.cit.api.here.com/mia/1.6/mapview?app_id=DemoAppId01082013GAL&app_code=AJKnXv84fjrb0KIHawS0Tg&c=" + 
					(new Formatter()).format(Locale.US, "%.6f", yGeo_Double) + "," +(new Formatter()).format(Locale.US, "%.6f", xGeo_Double) + 
					"&z=" + zoom_Int + "&h=" + ySizeOfTile_Int + "&w=" + xSizeOfTile_Int + "&nodot" + "&ml=rus" + "&t=0";
			break;
		case 13: 
			path = "http://image.maps.cit.api.here.com/mia/1.6/mapview?app_id=DemoAppId01082013GAL&app_code=AJKnXv84fjrb0KIHawS0Tg&c=" + 
					(new Formatter()).format(Locale.US, "%.6f", yGeo_Double) + "," +(new Formatter()).format(Locale.US, "%.6f", xGeo_Double) + 
					"&z=" + zoom_Int + "&h=" + ySizeOfTile_Int + "&w=" + xSizeOfTile_Int + "&nodot" + "&ml=rus" + "&t=3";
			break;
		}	
			
		return path;		
	}
	
	public static void main(String[] args) {

		MapViewerLite frame = new MapViewerLite();		
	}
}
