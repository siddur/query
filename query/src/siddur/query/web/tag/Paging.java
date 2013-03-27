package siddur.query.web.tag;

import java.util.List;

public class Paging<E> {
	public List<E> data;
	public Integer pageIndex;
	public Integer pageSize;
	public Integer total;
}
