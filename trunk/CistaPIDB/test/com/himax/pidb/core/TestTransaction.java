package com.himax.pidb.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.himax.pidb.admin.dao.FunctionDao;
import com.himax.pidb.admin.to.FunctionTo;
import com.himax.pidb.md.erp.ReleaseERPException;

public class TestTransaction {
	public void action() {
        TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
            public void doInTransactionWithoutResult(final TransactionStatus status) {
        		Dummy dummy = new Dummy();
        		Object o = null;
        		String result = dummy.release(o);
        		if (result != null) {
        			throw new ReleaseERPException(result); 
        		}
            }
        };

        try {
			new FunctionDao().doInTransaction(callback);
			System.out.println("Release success.");
		} catch (ReleaseERPException e) {
			
			System.out.println("Release fail. " + e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		TestTransaction tt = new TestTransaction();
		tt.action();
	}
}

class Dummy {
	/** Logger. */
    private Log logger = LogFactory.getLog(getClass());
	String release(Object o) {
		//Try insert something to DB.
		FunctionDao dao = new FunctionDao();
		FunctionTo to = new FunctionTo();
		to.setFuncName("Dummy");
		to.setPosition(-100);
		to.setIsMenu(false);
		dao.insert(to);

		if (o == null) {
			logger.error("Object is null.");
			return "Object is null.";
		} else {
			return null;
		}
	}
}