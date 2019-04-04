package br.com.saude.api.util;

public class StringSorter {
	
	private Object[] array;
	
	public static StringSorter newInstance(Object[] array) {
		StringSorter ss = new StringSorter();
		ss.array = array;
		return ss;
	}
	
	private StringSorter() {
		
	}
	
	public StringSorter sort() {
		for (int i = 0; i < this.array.length; i++) {
			for (int j = 0; j < this.array.length; j++) {
				if (i < j && this.array[j].toString().compareTo(this.array[i].toString()) < 0) {
					Object s = this.array[j];
					this.array[j] = this.array[i];
					this.array[i] = s;
				}
			}
		}
		return this;
	}
	
	public Object[] get() {
		return this.array;
	}
}
