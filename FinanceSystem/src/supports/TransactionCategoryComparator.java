package supports;
import java.util.*;

import models.*;

public class TransactionCategoryComparator implements Comparator<Transaction>{

	@Override
	public int compare(Transaction o1, Transaction o2) {
		return o1.getCategory().compareTo(o2.getCategory());
	}
	
}
