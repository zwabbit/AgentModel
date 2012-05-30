package agentsimulation.Agents;

import agentsimulation.GUI.BoardState;
import agentsimulation.GUI.States;
import agentsimulation.Messages.Message;
import agentsimulation.Messages.UpdateGUI;
import agentsimulation.World;
import java.awt.Point;
import java.util.Random;

public class Ant extends Agent{

	protected int foodCarrying;
	private Random moveRand = new Random();
	
	public Ant (int x, int y){
		super(x, y);
		BoardState.setState(x, y, States.ANT);
	}
	@Override
	protected void Execute() {
		// TODO Auto-generated method stub
		//check if has food
		//System.out.println("ant execution started");
		if (foodCarrying > 0){
			foodCarrying = 0;
			//System.out.println("carrying food");
			//if yes, check if in drop zone
				//if yes, drop food, end turn
				//if no, move toward drop zone
		}
		else{
			//if no, check current patch for food (ignoring food in drop zone)
			Patch currLoc = World.patchMap.get(position);
			int tempFood = currLoc.Eat(50);
			if (tempFood > 0){
				//System.out.println("got food");
				foodCarrying = tempFood; 
			}
			else{
				int xDir = moveRand.nextInt(3) - 1;
				int yDir = moveRand.nextInt(3) - 1;
				boolean moveSuccess = Move(new Point(position.x + xDir, position.y + yDir));
				while(!moveSuccess){
					xDir = moveRand.nextInt(3) - 1;
					yDir = moveRand.nextInt(3) - 1;
					moveSuccess =  Move(new Point(position.x + xDir, position.y + yDir));
				}
			}
				//if yes, pick up food and end turn
				//if no, check for food in vision radius
					//if yes, move toward food
					//if no, check pheromones in area
						//if yes, do pheromone action
						//if no, move to a random (empty?) patch
		}
		
			
	}

	@Override
	protected void ExecuteMessage(Message message) {
		// TODO Auto-generated method stub
		if(message instanceof UpdateGUI){
	
		}
	}
	

}
