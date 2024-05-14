// Class made to detect and manage mouse movements

package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Mouse extends MouseAdapter{
	// x,y coordinate of mouse
	public int x,y;
	// boolean value of if the mouse is in pressed state
	public boolean pressed;
	
	@Override
	public void mousePressed(MouseEvent e){
		pressed = true;
	}
	
	@Override
	public void mouseMoved(MouseEvent e){
		x = e.getX(); // getting the coordinates of the mouse on the frame
		y = e.getY();
	}
	
	@Override
	public void mouseReleased(MouseEvent e){
		pressed = false;
	}
	
	@Override
	public void mouseDragged(MouseEvent e){
		x = e.getX(); // getting the coordinates of the mouse on the frame
		y = e.getY();
	}
}
