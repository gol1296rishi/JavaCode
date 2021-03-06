package Projects.Math.rooms;

//import Data_Structures.Structures.UBA;
import java.io.File;

import util.Web;

import BryceMath.Calculations.Colors;
import Data_Structures.Structures.List;
import Game_Engine.Engine.Objs.Room;
//import Game_Engine.Engine.Objs.actionLogging.Command;
import Game_Engine.Engine.engine.Game_output;

import Game_Engine.GUI.Components.group_components.Label_Explainer;
import Game_Engine.GUI.Components.small.gui_button;
import Game_Engine.GUI.Components.small.gui_label;
import Game_Engine.GUI.Components.small.buttons.gui_functionRoomButton;
import Game_Engine.GUI.Components.small.buttons.gui_roomChange;
import Game_Engine.GUI.Components.small.buttons.gui_exit;

import Projects.Math.Dormant;
import Projects.Math.aamathMain;
import Projects.Math.Spr;
import Projects.Math.functionalRooms.room_DocumentEditor;
import Projects.Math.LessonLogic.ProblemData;

/*
 * Main menu class.
 * 
 * Written by Bryce Summers in the year of 2013.
 * Informative comments written on 8 - 13 - 2013.
 * 
 * Purpose : 
 * 	- Provides a list of buttons that allow the user to direct the program to where they want to go.
 */

public class room_menu extends Room
{
	gui_button help_button;
	
	// The initial room constructor.
	public room_menu(Game_output out)
	{
		super(out);
	
		createGlobalCursor();
		
	}
	
	public room_menu()
	{
		/* Used to implement generic rooms. */
	}
	
	@Override
	public void iObjs()
	{
		List<gui_label> labels = new List<gui_label>();
		
		File saveDir;
		gui_functionRoomButton<Boolean> open_button;
		
		gui_label title = new gui_label(0, 0, getW(), getH()/3);
		title.setText("Summers CAMP");
		//title.makeTransparent();
		title.setColor(Colors.C_BLUE_HEADING);
		title.setTextSize(getW()/11);
		title.setDrawBorders(true);
		obj_create(title);

		int borderSize = Spr.gui_borderSize;
		
		room_DocumentEditor problem_room = new room_DocumentEditor();
		
		gui_functionRoomButton<ProblemData> b2;
		b2 = new gui_functionRoomButton<ProblemData>(getW() - borderSize*2, 100, problem_room);
		b2.setText("New File");
		b2.setImage(Spr.icon_new);
		b2.INFO("Create a new document.");
		labels.add(b2);

		// A button that shifts the user to the document opening room.
		saveDir = aamathMain.DIR_SAVES;
		room_open room2 = new room_open(saveDir);	
		open_button = new gui_functionRoomButton<Boolean>(getW() - borderSize*2, 100, room2);
		open_button.setText("Open Saved File");
		open_button.setImage(Spr.icon_open);
		open_button.INFO("Open a document for editing.");
		labels.add(open_button);
		
		// A button that shifts the user to the document opening room.
		saveDir = aamathMain.DIR_PROBLEMS;
		room2 = new room_open(saveDir);
		open_button = new gui_functionRoomButton<Boolean>(getW() - borderSize*2, 100, room2);
		open_button.setText("Example Documents");
		open_button.setImage(Spr.icon_example);
		open_button.INFO("View a repository of example documents.");
		labels.add(open_button);
		
		// A button that shifts the user to the document opening room.
		//saveDir = aamathMain.DIR_HELP;
		room2 = new room_open(saveDir);
		//open_button = new gui_functionRoomButton<Boolean>(getW() - borderSize*2, 100, room2);
		
		help_button = new gui_button(0, 0, getW() - borderSize*2, 100);
		help_button.setText("Help");
		help_button.setImage(Spr.icon_help);
		help_button.INFO("View Helpful Internet Tutorials.");
		labels.add(help_button);
		
		gui_roomChange b3 = new gui_roomChange(0, 0, getW() - borderSize*2, 100, "room_credits");
		b3.setText("Credits");
		b3.setImage(Spr.icon_credits);
		b3.INFO("See a list of good people.");
		labels.add(b3);
		
		gui_exit exit = new gui_exit(getW() - borderSize*2, 100);
		exit.setText("Exit");
		exit.setImage(Spr.icon_exit);
		exit.INFO("Exit this program.");
		labels.add(exit);
		
		// Format the main menu.
		
		int w = getW();
		int h = getH();
		
		int row_h = ((h - 100) + (h/3))/2 - 50;
		
		// Displays the descriptions of the buttons to the screen.
		Label_Explainer L = new Label_Explainer(0, h - 100, w, 100);
		L.setColor(Colors.C_BLUE_HEADING);
		L.setDefaultText("Don't Panic! Please click on a Button!");
		L.setDrawBorders(true);
		obj_create(L);
		
		// 6 = # of components, 7 is the number of white spaces that exists between buttons.
		int xinc = (w - 6*100)/7;
		int x = xinc;
		
		for(gui_label l : labels)
		{
			l.setX(x);
			l.setY(row_h);
			l.setW(100);
			l.setH(100);
			obj_create(l);
			L.addComponent(l);
			
			x += xinc + 100;
		}
		
		
		
		
	}
	
	public void update()
	{
		super.update();
		
		if(help_button.flag())
		{
			Web.viewWebsite("https://www.youtube.com/channel/UCbDKxCh70C4wnAYVjRlw3UA/feed?view_as=public");
		}
	}
	
	// Instantiate what amounts to a movie.
	// This is dormant functionality for logging the user's key presses to make
	// tutorial videos and capture their learning experience.
	@Dormant
	public void createGlobalCursor()
	{		
		// Log the complete user experience.
		//globalLog();/*
		/*
				
		disableInput();
		
		UBA<Command> program = new UBA<Command>();
		hardCode(program);
		
		// Create a cursor that will go through all of the correct movements.
		global_proxy_cursor =  new obj_cursor(0, 0);
		global_proxy_cursor.makeGlobal();
		global_proxy_cursor.setSprite(Spr.global_cursor_symbol);
		obj_create(global_proxy_cursor);
			
		// Program the proxy cursor.
		global_proxy_cursor.program(Command.serialFile);/*
		global_proxy_cursor.program(program);//*/
		
	}
	
	// Allows me to hardcode in programs.
	//private void hardCode(UBA<Command> program)
	{

	}

}
