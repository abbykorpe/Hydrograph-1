package com.bitwise.app.propertywindow.widgets.customwidgets.mapping.datastructures;

import org.eclipse.swt.widgets.Text;

public class RowData {
	Text in,out,clazz;
	
	public RowData(Text in, Text out, Text clazz) {
		super();
		this.in = in;
		this.out = out;
		this.clazz = clazz;
	}

	public Text getIn() {
		return in;
	}

	public void setIn(String in) {
		this.in.setText(in);
	}

	public Text getOut() {
		return out;
	}

	public void setOut( String out) {
		this.out.setText(out);
	}

	public Text getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz.setText(clazz);
	}
	
	@Override
	public String toString() {
		return "[ " + in.getText() + ", " + clazz.getText() + ", " + out.getText() + " ]";
	}
	
}
