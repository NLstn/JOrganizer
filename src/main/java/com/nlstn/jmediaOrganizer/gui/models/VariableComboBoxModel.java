package com.nlstn.jmediaOrganizer.gui.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.MutableComboBoxModel;
import javax.swing.event.ListDataListener;

import com.nlstn.jmediaOrganizer.processing.Converter;
import com.nlstn.jmediaOrganizer.processing.ConverterVariable;

public class VariableComboBoxModel implements MutableComboBoxModel<ConverterVariable> {

    private static final ConverterVariable EMPTY = new ConverterVariable("", "");

    private final List<ConverterVariable> values = new ArrayList<>(Converter.getVariables());

    private int selectedIndex = -1;

    @Override
    public void setSelectedItem(Object anItem) {
        if (anItem == null) {
            return;
        }
        for (int i = 0; i < values.size(); i++) {
            if (anItem.equals(values.get(i))) {
                selectedIndex = i;
                return;
            }
        }
    }

    @Override
    public Object getSelectedItem() {
        if (selectedIndex >= 0) {
            return values.get(selectedIndex);
        }
        return EMPTY;
    }

    @Override
    public int getSize() {
        return values.size();
    }

    @Override
    public void addListDataListener(ListDataListener l) {
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
    }

    @Override
    public void removeElement(Object obj) {
        values.remove(obj);
    }

    @Override
    public void removeElementAt(int index) {
        values.remove(index);
    }

    @Override
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
