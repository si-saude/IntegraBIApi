package br.com.saude.api.generic;

public class StringReplacer {

	private String content;
	
	public StringReplacer(String content) {
		this.content = content;
	}
	
	public StringReplacer replace(String oldValue, String newValue) {
		if(this.content.contains("["+oldValue+"]"))
			oldValue = "["+oldValue+"]";
		this.content = this.content.replace(oldValue, newValue);
		return this;
	}
	
	public String result() {
		return this.content;
	}
}
