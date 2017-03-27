package com.shengliedu.teacher.teacher.bean;

public class ConsoleMenu {
	public int number;
	public int menuImag;
	public String menuName;
	
	public ConsoleMenu(int number, int menuImag, String menuName) {
		super();
		this.number = number;
		this.menuImag = menuImag;
		this.menuName = menuName;
	}

	public ConsoleMenu() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + number;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConsoleMenu other = (ConsoleMenu) obj;
		if (number != other.number)
			return false;
		return true;
	}
}
