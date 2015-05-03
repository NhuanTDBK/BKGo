package model;

import java.util.concurrent.ConcurrentHashMap;

public class MemCached {
	private ConcurrentHashMap<String,FileCursor> userTransaction;
	public MemCached()
	{
		userTransaction = FileChangeDAO.getAllCursor();
		System.out.println("Init first time");
	}
	public ConcurrentHashMap<String, FileCursor> getUserTransaction() {
		return userTransaction;
	}
	public void setUserTransaction(ConcurrentHashMap<String, FileCursor> userTransaction) {
		this.userTransaction = userTransaction;
	}
	public void put(String userId, FileCursor cursor)
	{
		try{
			FileCursor current = userTransaction.get(userId);
			if(current.compareTo(cursor)<0)
			{
				userTransaction.replace(userId, cursor);
			}
			else
			{
				System.out.println("Cursor nho hon cursor hien tai");
			}
		}catch(Exception ex)
		{
			userTransaction.put(userId, cursor);
		}
	}
	public FileCursor get(String userId)
	{
		FileCursor result = new FileCursor();
		result = userTransaction.get(userId);
		return result;
	}
	public static void main(String [] args)
	{
		FileCursor one = new FileCursor("2","3");
		FileCursor two = new FileCursor("2","6");
		MemCached mem = new MemCached();
		System.out.println(mem.get("1"));
		mem.put("1", one);
		System.out.println(mem.get("1"));
		mem.put("1", two);
		System.out.println(mem.get("1"));
		
	}
}
