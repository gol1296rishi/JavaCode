package Game_Engine.Engine.engine;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.concurrent.CountDownLatch;

import javax.swing.Timer;

import BryceMath.Geometry.Rectangle;
import Game_Engine.Engine.Objs.ImageB;
import Game_Engine.Engine.Objs.Obj_Container;


/*
 * Game Looper Class, Conceived by Bryce Summers 12 - 31 - 2012.
 * Purpose: Glues the game together and updates it.
 * 			1. Provides an initial source of Events.
 * 			2. Initializes chains of updates, and drawings.
 * 			3. This is the source of an independent game world,
 *			   that has independent events.
 */

public class Game_looper
{
	// -- Local Variables.
	
	// The given root Container object.
	private Obj_Container root_container;

	// The two local timer's
	private Timer updater;
	private Timer drawer;
	private Timer windower;
	
	public static int stepsElapsed = 0;
	
	private Game_output output;
	
	// The draw region is used to limit the amount of drawing that
	// needs to be performed to the parts of the screen that have changed.
	Rectangle DEFAULT_DRAW_REGION = null;
	Rectangle draw_region;
	public static boolean GUI_DRAW_ENABLED = false;
	
	public Game_looper(Obj_Container data_in, int fps, Game_output out)
	{
		// Initializes this looper's data.
		root_container = data_in;
		
		frame_image = new BufferedImage(root_container.getW(), root_container.getH(), BufferedImage.TYPE_4BYTE_ABGR);	
		
		// Initialize the timers.
		updater = new Timer(1000/fps, new Update());
		drawer  = new Timer(1000/fps, new Drawer());
		//windower= new Timer(1000/fps, (ActionListener) new Windows());
		
		output = out;
		
		setDrawingMode();

		// Start rooms by drawing everything.
		drawAll();
	
		// Start the timers.
		start();
	}
	
	private Rectangle drawAll()
	{
		draw_region = new Rectangle(0, 0, root_container.getW(), root_container.getH());
		return draw_region;
	}
	
	private void setDrawingMode()
	{
		if(DEFAULT_DRAW_REGION != null)
		{
			draw_region = DEFAULT_DRAW_REGION;
			return;
		}
		
		if(GUI_DRAW_ENABLED)
		{
			enterGuiDrawingMode();
		}
		else
		{
			enterFullAnimationMode();
		}
	}
	
	// Allows for the data to be changed,
	// for instance if there is a room change.
	public void setData(Obj_Container data_in)
	{
		root_container = data_in;
	}
	
	public void start()
	{
		updater.start();
		drawer.start();
		//windower.start();
	}
	
	public void stop()
	{
		updater.stop();
		drawer.stop();
		//windower.stop();
	}
	
	// Screen Drawing optimization code.
	public void enterGuiDrawingMode()
	{
		DEFAULT_DRAW_REGION = null;
		draw_region = null;
	}

	// Redraw every pixel on the screen every frame.
	public void enterFullAnimationMode()
	{
		DEFAULT_DRAW_REGION = drawAll();
	}
	
	// Allow for the chain of objects to tag areas for drawing.
	public void addDrawingRegion(Rectangle r)
	{
		if(draw_region == null)
		{
			draw_region = r;
			return;
		}
		
		draw_region = draw_region.union(r);
	}
	
	private class Update implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
        {
			stepsElapsed++;
			root_container.update();
        }
	}// End of private class

	
	// The Storage buffer for pixels that will be transported to the screen.
	BufferedImage frame_image;
	
	private class Drawer implements ActionListener
	{	
		
		public void actionPerformed(ActionEvent e)
		{			
			CountDownLatch final_latch = new CountDownLatch(1);
			
			ImageB imageB = new ImageB(frame_image);

			if(draw_region == null)
			{
				return;
			}
			
			imageB = imageB.getSubImage(draw_region);
			
			// Reset Draw Region;
			draw_region = DEFAULT_DRAW_REGION;

			if(imageB == null)
			{
				return;
			}
			
			
			// Perform the Actual Drawing Work.
			Graphics g = imageB.getGraphics();
		
			// Draw the Background Color.
			g.setColor(output.getColor());
			g.fillRect(0, 0, root_container.getW(), root_container.getH());
			
			// Start the drawing tree.
			root_container.draw(imageB, new AffineTransform(), final_latch);

			// Wait for the drawing tree to conclude its operations.
			try
			{
				final_latch.await();
			}
			catch (InterruptedException e1)
			{
				e1.printStackTrace();
			}

			// Draw the final rendered frame onto the output channel.
			output.draw(frame_image);
			
			// Attempt to smooth out garbage collection.
			if(stepsElapsed % 200 == 0)
			{
				System.gc();
			}
		}
	}

	// Used to handle various events in the windowing system such as closing and opening.
	private class Windows implements WindowListener
	{

		@Override
		public void windowActivated(WindowEvent arg0)
		{
			start();
			
			// Repush the current pixels to the screen.
			output.draw(frame_image);
		}
		
		@Override
		public void windowDeactivated(WindowEvent arg0)
		{
			stop();
		}

		@Override
		public void windowClosed(WindowEvent arg0)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosing(WindowEvent arg0)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent arg0)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent arg0)
		{
			// TODO Auto-generated method stub
		}

		@Override
		public void windowOpened(WindowEvent arg0)
		{
			start();
			
			// Repush the current pixels to the screen.
			output.draw(frame_image);
		}
		
	}
	
	public static int getElapsedSteps()
	{
		int output = stepsElapsed;
		stepsElapsed = 0;
		return output;
	}
	
}// End of Game_Loop class
