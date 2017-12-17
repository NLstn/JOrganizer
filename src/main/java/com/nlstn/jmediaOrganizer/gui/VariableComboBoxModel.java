package com.nlstn.jmediaOrganizer.gui;

import java.util.List;

import javax.swing.MutableComboBoxModel;
import javax.swing.event.ListDataListener;

import com.nlstn.jmediaOrganizer.Converter;
import com.nlstn.jmediaOrganizer.ConverterVariable;

public class VariableComboBoxModel implements MutableComboBoxModel<ConverterVariable> {

	private List<ConverterVariable>	values			= Converter.getVariables();

	private int						selectedIndex	= -1;

	public void setSelectedItem(Object anItem) {
		for (int i = 0; i < values.size(); i++) {
			if (anItem.equals(values.get(i))) {
				selectedIndex = i;
				return;
			}
		}
	}

	public Object getSelectedItem() {
		if (selectedIndex >= 0)
			return values.get(selectedIndex);
		else
			return new ConverterVariable("", "");
	}

	public int getSize() {
		return values.size();
	}

	public void addListDataListener(ListDataListener l) {

	}

	public void removeListDataListener(ListDataListener l) {

	}

	public void removeElement(Object obj) {
		values.remove(obj);
	}

	public void removeElementAt(int index) {
		values.remove(index);
	}

	public ConverterVariable getElementAt(int index) {
		return values.get(index);
	}

	@Override
	public void addElement(ConverterVariable item) {
		values.add(item);
	}

	@Override
	public void insertElementAt(ConverterVariable item, int index) {
		values.add(index, item);
	}

}
