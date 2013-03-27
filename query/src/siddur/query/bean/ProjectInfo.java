package siddur.query.bean;

import java.util.List;

public class ProjectInfo {
	public int id;
	public String name;
	
	@Override
	public String toString() {
		return name;
	}
	public static ProjectInfo findProjectById(List<ProjectInfo> list, int id){
		for(ProjectInfo p : list){
			if(id == p.id)
				return p;
		}
		return null;
	}
}
