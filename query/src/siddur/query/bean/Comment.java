package siddur.query.bean;

import java.util.Date;

public class Comment {
	public int id;
	public String content;
	public Date writeAt = new Date();
	public String writeBy;
	public int target = -1;
	public int project;
}
