using RVO;
using System.Collections.Generic;
using System;

public class ExampleBlocks : IExample
{
    private IList<Vector2> goals = new List<Vector2>();

    private const int RAND_MAX = 0x7fff;

    public void setupScenario()
    {
        /* Specify the global time step of the simulation. */
        Simulator.Instance.setTimeStep(0.25f);

        /* Specify the default parameters for agents that are subsequently added. */
        Simulator.Instance.setAgentDefaults(15.0f, 10, 5.0f, 5.0f, 2.0f, 2.0f, new Vector2());

        for (int i = 0; i < 4; ++i)
        {
            for (int j = 0; j < 3; ++j)
            {
                Simulator.Instance.addAgent(new Vector2(55.0f + i * 10.0f, 55.0f + j * 10.0f));
                goals.Add(new Vector2(-75.0f, -75.0f));

                Simulator.Instance.addAgent(new Vector2(-55.0f - j * 10.0f, 55.0f + j * 10.0f));
                goals.Add(new Vector2(75.0f, -75.0f));
				/*
                Simulator.Instance.addAgent(new Vector2(55.0f + i * 10.0f, -55.0f - j * 10.0f));
                goals.Add(new Vector2(-75.0f, 75.0f));

                Simulator.Instance.addAgent(new Vector2(-55.0f - i * 10.0f, -55.0f - j * 10.0f));
                goals.Add(new Vector2(75.0f, 75.0f));
                */
            }
        }
		
		/*
        IList<Vector2> obstacle1 = new List<Vector2>();
        IList<Vector2> obstacle2 = new List<Vector2>();
        IList<Vector2> obstacle3 = new List<Vector2>();
        IList<Vector2> obstacle4 = new List<Vector2>();
		
        obstacle1.Add(new Vector2(-10.0f, 40.0f));
        obstacle1.Add(new Vector2(-40.0f, 40.0f));
        obstacle1.Add(new Vector2(-40.0f, 10.0f));
        obstacle1.Add(new Vector2(-10.0f, 10.0f));

        obstacle2.Add(new Vector2(10.0f, 40.0f));
        obstacle2.Add(new Vector2(10.0f, 10.0f));
        obstacle2.Add(new Vector2(40.0f, 10.0f));
        obstacle2.Add(new Vector2(40.0f, 40.0f));

        obstacle3.Add(new Vector2(10.0f, -40.0f));
        obstacle3.Add(new Vector2(40.0f, -40.0f));
        obstacle3.Add(new Vector2(40.0f, -10.0f));
        obstacle3.Add(new Vector2(10.0f, -10.0f));

        obstacle4.Add(new Vector2(-10.0f, -40.0f));
        obstacle4.Add(new Vector2(-10.0f, -10.0f));
        obstacle4.Add(new Vector2(-40.0f, -10.0f));
        obstacle4.Add(new Vector2(-40.0f, -40.0f));

        Simulator.Instance.addObstacle(obstacle1);
        Simulator.Instance.addObstacle(obstacle2);
        Simulator.Instance.addObstacle(obstacle3);
        Simulator.Instance.addObstacle(obstacle4);
		*/
        /* Process the obstacles so that they are accounted for in the simulation. */
        Simulator.Instance.processObstacles();
    }

    private Random _random = new Random();

    public void setPreferredVelocities()
    {
        /* 
         * Set the preferred velocity to be a vector of unit magnitude (speed) in the
         * direction of the goal.
         */
        for (int i = 0; i < Simulator.Instance.getNumAgents(); ++i)
        {
            Vector2 goalVector = goals[i] - Simulator.Instance.getAgentPosition(i);

            if (RVOMath.absSq(goalVector) > 1.0f)
            {
                goalVector = RVOMath.normalize(goalVector);
            }

            Simulator.Instance.setAgentPrefVelocity(i, goalVector);

            /*
             * Perturb a little to avoid deadlocks due to perfect symmetry.
             */
            float angle = _random.Next(RAND_MAX) * 2.0f * (float)Math.PI / RAND_MAX;
            float dist = _random.Next(RAND_MAX) * 0.0001f / RAND_MAX;

            Simulator.Instance.setAgentPrefVelocity(i, Simulator.Instance.getAgentPrefVelocity(i) +
                                      dist * new Vector2((float)Math.Cos(angle), (float)Math.Sin(angle)));
        }
    }

    public bool reachedGoal()
    {
        /* Check if all agents have reached their goals. */
        for (int i = 0; i < Simulator.Instance.getNumAgents(); ++i)
        {
            if (RVOMath.absSq(Simulator.Instance.getAgentPosition(i) - goals[i]) > 20.0f * 20.0f)
            {
                return false;
            }
        }

        return true;
    }

}
