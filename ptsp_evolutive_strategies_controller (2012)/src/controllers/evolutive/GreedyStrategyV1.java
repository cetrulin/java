package controllers.evolutive;

import framework.core.*;
import framework.graph.Graph;
import framework.graph.Node;
import framework.graph.Path;
import framework.utils.Vector2d;

import java.util.HashMap;

/**
 */
public class GreedyStrategyV1 extends Controller
{
    /**
     * Graph for this controller.
     */
    private Graph m_graph;

    /**
     * Node in the graph, closest to the ship position.
     */
    private Node m_shipNode;

    /**
     * Path to the closest waypoint in the map
     */
    private Path m_pathToClosest;

    /**
     * Hash map that matches waypoints in the map with their closest node in the graph.
     */
    private HashMap<Waypoint, Node> m_collectNodes;

    /**
     * Closest waypoint to the ship.
     */
    private Waypoint m_closestWaypoint;

    private double p1, p2, p3, p4, p5, p6, p7, p8, p9, p10;

    /**
     * Constructor, that receives a copy of the game state
     * @param a_gameCopy a copy of the game state
     */
    public GreedyStrategyV1(Game a_gameCopy)
    {
        //Init the graph.
        m_graph = new Graph(a_gameCopy);

        //Init the structure that stores the nodes closest to all waypoitns
        m_collectNodes = new HashMap<Waypoint, Node>();
        for(Waypoint way: a_gameCopy.getWaypoints())
        {
            m_collectNodes.put(way, m_graph.getClosestNodeTo(way.s.x, way.s.y,true));
        }       

        //Calculate the closest waypoint to the ship.
        calculateClosestWaypoint(a_gameCopy);
    }
    

    /**
     * This function is called every execution step to get the action to execute.
     * @param a_gameCopy Copy of the current game state.
     * @param a_timeDue The time the next move is due
     * @return the integer identifier of the action to execute (see interface framework.core.Controller for definitions)
     */
    public int getAction(Game a_gameCopy, long a_timeDue)
    {
        //Get the path to the closest node, if my ship moved.
        Node oldShipId = m_shipNode;
        m_shipNode = m_graph.getClosestNodeTo(a_gameCopy.getShip().s.x, a_gameCopy.getShip().s.y,true);
        if(oldShipId != m_shipNode || m_pathToClosest == null)
        {
            //Calculate the closest waypoint to the ship.
            //calculateClosestWaypoint(a_gameCopy);
            for(Waypoint way : a_gameCopy.getWaypoints()){
            	if(way.equals(m_closestWaypoint) && way.isCollected())
            		calculateClosestWaypoint(a_gameCopy);
            }

            if(m_shipNode == null)
            {
                //No node close enough and collision free. Just go for the closest.
                m_shipNode = m_graph.getClosestNodeTo(a_gameCopy.getShip().s.x, a_gameCopy.getShip().s.y,false);
            }

            //And get the path to it from my location.
            m_pathToClosest = m_graph.getPath(m_shipNode.id(), m_collectNodes.get(m_closestWaypoint).id());
        }

        //We treat this differently if we can see the waypoint:
        boolean isThereLineOfSight = a_gameCopy.getMap().LineOfSight(a_gameCopy.getShip().s,m_closestWaypoint.s);
        if(isThereLineOfSight)
        {
        	//DOS
            int bestAction = manageStraightTravel(a_gameCopy);
            return bestAction;
        }

        //The waypoint is behind an obstacle, select which is the best action to take.
        double minDistance = Float.MAX_VALUE;
        //The waypoint is behind an obstacle, select which is the best action to take.
        int bestAction = -1;
        double bestDot = -2;
        double bestChoice = 99999999999999.0;

        if(m_pathToClosest != null)  //We should have a path...
        {
            int startAction = Controller.ACTION_NO_FRONT;
            //For each possible action...
            for(int action = startAction; action < Controller.NUM_ACTIONS; ++action)
            {
                //Simulate that we execute the action and get my potential position and direction
                Game forThisAction = a_gameCopy.getCopy();
                forThisAction.getShip().update(action);
                Vector2d nextPosition = forThisAction.getShip().s;
                Vector2d potentialDirection = forThisAction.getShip().d;

                //Get the next node to go to, from the path to the closest waypoint
                Node nextNode = getNextNode();
                Vector2d nextNodeV = new Vector2d(nextNode.x(),nextNode.y());
                nextNodeV.subtract(nextPosition);
                nextNodeV.normalise();   //This is a unit vector from my position pointing towards the next node to go to.  
                double dot = potentialDirection.dot(nextNodeV);  //Dot product between this vector and where the ship is facing to.

                //Get the distance to the next node in the tree and update the total distance until the closest waypoint
                double dist = nextNode.euclideanDistanceTo(nextPosition.x, nextPosition.y);
                double totalDistance = m_pathToClosest.m_cost + dist;
                
                /*double evaluation = totalDistance*p1+(double)forThisAction.getStepsLeft ()*p2+dot*p3;
                if(evaluation < bestChoice)
                {    	
                	bestChoice = evaluation;
                    bestAction = action;
                }*/
                
                //Keep the best action so far.
                if(totalDistance < minDistance)
                {
                    minDistance = totalDistance;
                    bestAction = action;
                    bestDot = dot;
                }
                //If the distance is the same, keep the action that faces the ship more towards the next node
                else if((int)totalDistance == (int)minDistance && dot > bestDot)
                {
                    minDistance = totalDistance;
                    bestAction = action;
                    bestDot = dot;
                }

            }
            //System.out.println(bestAction+ "    UNO    "+ bestChoice);
            //This is the best action to take.
            return bestAction;
        }

        //Default (something went wrong).
        System.out.println(bestAction+ "    WRONG    "+ a_gameCopy.getStepsLeft ());

        return Controller.ACTION_NO_FRONT;
    }

    /**
     * Returns the first node in the way to the destination
     * @return the node in the way to destination.
     */
    private Node getNextNode()
    {
        Node n0 = m_graph.getNode(m_pathToClosest.m_points.get(0));

        //If only one node in the path, return it.
        if(m_pathToClosest.m_points.size() == 1)
            return n0;

        //Heuristic: Otherwise, take the closest one to the destination
        Node n1 = m_graph.getNode(m_pathToClosest.m_points.get(1));
        Node destination =  m_graph.getNode(m_pathToClosest.m_destinationID);

        if(n0.euclideanDistanceTo(destination) < n1.euclideanDistanceTo(destination))
            return n0;
        else return n1;
    }


    /**
     * Calculates the closest waypoint to the ship.
     * @param a_gameCopy the game copy.
     */
    private void calculateClosestWaypoint(Game a_gameCopy)
    {
        double minDistance = Double.MAX_VALUE;
        
        for(Waypoint way: a_gameCopy.getWaypoints())
        {
            if(!way.isCollected())     //Only consider those not collected yet.
            {
                double fx = way.s.x-a_gameCopy.getShip().s.x, fy = way.s.y-a_gameCopy.getShip().s.y;
                double dist = Math.sqrt(fx*fx+fy*fy);
                	
                if( dist < minDistance )
                {
                    //Keep the minimum distance.
                    minDistance = dist;
                    m_closestWaypoint = way;
                }
            }
        }
    }

    /**
     * Manages straight travelling.
     * @param a_gameCopy the game copy
     * @return the id of the best action to execute.
     */
    private int manageStraightTravel(Game a_gameCopy)
    {
        int bestAction = Controller.ACTION_NO_FRONT;
        Vector2d dirToWaypoint = m_closestWaypoint.s.copy();
        dirToWaypoint.subtract(a_gameCopy.getShip().s);
        double distance = dirToWaypoint.mag();
        dirToWaypoint.normalise();

        //Check if we are facing the waypoint we are going after.
        Vector2d dir = a_gameCopy.getShip().d;
        boolean notFacingWaypoint = dir.dot(dirToWaypoint) < 0.01*p10;

        //Depending on the time left and the distance to the waypoint, we established the maximum speed.
        //(going full speed could make the ship to overshoot the waypoint... that's the reason of this method!).
        double maxSpeed = 0.01*p4;
        if(distance>(1*p7) || a_gameCopy.getStepsLeft() < (1*p8))
            maxSpeed = 0.01*p5;
        else if(distance<(1*p9)) maxSpeed = 0.01*p6;


        if(notFacingWaypoint || a_gameCopy.getShip().v.mag() > maxSpeed)
        {
            //We should not risk to throttle. Let's rotate in place to face the waypoint better.
            Game forThisAction;
            double bestChoice = 0;

            for(int i = Controller.ACTION_NO_FRONT; i <= Controller.ACTION_NO_RIGHT; ++i)
            {
                //Select the action that maximises my dot product with the target (aka. makes the ship face the target better).
                forThisAction = a_gameCopy.getCopy();
                forThisAction.getShip().update(i);
                Vector2d potentialDirection = forThisAction.getShip().d;
                double newDot = potentialDirection.dot(dirToWaypoint);
                
                double evaluation = (double)forThisAction.getStepsLeft ()*p2+newDot*p3;
                
                if(evaluation > bestChoice)
                {    
                	bestChoice = evaluation;
                    bestAction = i;
                }
            }
        } else //We can thrust
            return Controller.ACTION_THR_FRONT;
        //System.out.println(bestAction+ "    DOS    "+ a_gameCopy.getStepsLeft ());

        //There we go!
        return bestAction;
    }

    /**
     * Returns the path to the closest waypoint. (for debugging purposes)
     * @return the path to the closest waypoint
     */
    public Path getPathToClosest() {return m_pathToClosest;}

    /**
     * Returns the graph. (for debugging purposes)
     * @return the graph.
     */
    public Graph getGraph() {return m_graph;}

	@Override
	public void setIndividuo(double[] individuo) {
		// TODO Auto-generated method stub
        //Damos valor al individuo
        p1 = individuo[0];
        p2 = individuo[1];
        p3 = individuo[2];
        p4 = individuo[3];
        p5 = individuo[4];
        p6 = individuo[5];
        p7 = individuo[6];
        p8 = individuo[7];
        p9 = individuo[8];
        p10 = individuo[9];
	}

}
