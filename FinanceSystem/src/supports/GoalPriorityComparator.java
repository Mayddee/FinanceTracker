package supports;
import java.util.*;

import models.*;

public class GoalPriorityComparator implements Comparator<Goal>{

	@Override
	public int compare(Goal o1, Goal o2) {
		if(o1.getPriority() > o2.getPriority()) return 1;
		if(o1.getPriority() < o2.getPriority()) return -1;
		return 0;
	}

}
