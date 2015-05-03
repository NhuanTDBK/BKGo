package model;

import java.io.Serializable;

public class FileCursor implements Serializable, Comparable<FileCursor> {
	private int tid;
	private int index;
	public FileCursor()
	{
		this.tid = 0;
		this.index = 0;
	}
	public FileCursor(String tid2, String index2) {
		// TODO Auto-generated constructor stub
		this.tid = Integer.parseInt(tid2);
		this.index = Integer.parseInt(index2);
	}
	public FileCursor(int tid,int index)
	{
		this.tid = tid;
		this.index = index;
	}
	public FileCursor(String tid2, int index)
	{
		this.tid = Integer.parseInt(tid2);
		this.index = index;
	}
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		if(this.index==0||this.index<index)
			this.index = index;
	}
	@Override
	public int compareTo(FileCursor o) {
		// TODO Auto-generated method stub
		try{
//		if(this.getTid()<o.getTid()) return -1;
//		else if(this.getTid()>o.getTid()) return 1;
//		else if(this.getTid()==o.getTid())
//		{
//			if(this.getIndex()<o.getIndex()) return -1;
//			else if(this.getIndex()>o.getIndex()) return 1;
//			else if(this.getIndex()==o.getIndex()) return 0;
//		}
			int total1 = this.getTid()*10+this.getIndex();
			int total2 = o.getTid()*10 + o.getIndex();
			return Integer.compare(total1, total2);
		}catch(NullPointerException ex)
		{
			System.out.println("Null object");
		}
		return 0;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		System.out.println("{TID,Index} = {"+this.tid+","+this.index+"}");
		return Integer.toString(tid)+"@"+Integer.toString(index);
	}
}
