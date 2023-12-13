package org.suai.todo.model;

import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZoneId;
import java.sql.Timestamp;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Record {
	public Integer id;
	public Integer parent; 
	public Integer owner;
	public Integer priority;
	public Integer done;
	public LocalDateTime created;
	public LocalDateTime deadline;
	public String caption;
	public String comment;
	
	public Record(Integer owner) {
		this.id = 0;
		this.parent = 0;
		this.owner = owner;
		this.priority = 0;
		this.done = 0;
		this.created = LocalDateTime.now();
		this.deadline = LocalDateTime.now();
		this.caption = "";
		this.comment = "";
	}
	
	public Record(ResultSet rs) throws SQLException {
		id       = rs.getInt("id");
		parent   = rs.getInt("parent");
		owner    = rs.getInt("owner");
		priority = rs.getInt("priority");
		done     = rs.getInt("done");
		created  = fromUnixTimestamp(rs.getLong("created"));
		deadline = fromUnixTimestamp(rs.getLong("deadline"));
		caption  = rs.getString("caption");
		comment  = rs.getString("comment");
	}
	
	public static Long toUnixTimestamp(LocalDateTime ldt) {
		return Timestamp.valueOf(ldt).getTime();
	}
	
	public static LocalDateTime fromUnixTimestamp(Long ts) {
		return Instant.ofEpochMilli(ts).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
}
