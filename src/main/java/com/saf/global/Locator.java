package com.saf.global;

public class Locator {

	public String value;
	public String name;

	public Locator(String value) {
		this.value=value;
	}
	public Locator(String name,String value) {
		this.name=name;
		this.value=value;
	}

	public String format(String values) {
		String[] replacements = values.split(",");
		String locator=this.value;
		for (int i = 0; i < replacements.length; i++) {
			locator=locator.replace(("$"+i),replacements[i]);
		}
		return locator;
	}
	
}
