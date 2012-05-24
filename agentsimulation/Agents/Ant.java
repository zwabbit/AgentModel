package agentsimulation.Agents;

import agentsimulation.World;
import agentsimulation.Messages.Message;

public class Ant extends Agent{

	protected int foodCarrying;
	
	public Ant (int x, int y){
		super(x, y);
	}
	@Override
	protected void Execute() {
		// TODO Auto-generated method stub
		//check if has food
		if (foodCarrying > 0){
			//if yes, check if in drop zone
				//if yes, drop food, end turn
				//if no, move toward drop zone
		}
		else{
			//if no, check current patch for food (ignoring food in drop zone)
			Patch currLoc = World.patchMap.get(position);
			//if(currLoc.getFood() > 0){
				
			//}
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
		
	}

}
