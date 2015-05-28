/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.log;


public class Log {
	private String author;
	private String message;
	private Long date;
	
	public Log(String author, String message, Long date) {
		this.author = author;
		this.message = message;
		this.date = date;
	}
	
	public Log(String author, String message) {
		this(author, message, System.currentTimeMillis());
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Long getDate() {
		return date;
	}
	
	public void setDate(Long date) {
		this.date = date;
	}
}
