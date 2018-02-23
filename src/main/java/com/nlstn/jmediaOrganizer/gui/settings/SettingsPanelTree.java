package com.nlstn.jmediaOrganizer.gui.settings;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * Creation: 23 Feb 2018
 *
 * @author Niklas Lahnstein
 */
public class SettingsPanelTree extends JTree{

	private static final long serialVersionUID = -189531945765251059L;

	public SettingsPanelTree() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Settings");
		DefaultTreeModel treeModel = new DefaultTreeModel(root);
		
		DefaultMutableTreeNode general = new DefaultMutableTreeNode("General");
		treeModel.insertNodeInto(general, root, 0);

		DefaultMutableTreeNode converter = new DefaultMutableTreeNode("Converter");
		treeModel.insertNodeInto(converter, root, 1);
		
		setRootVisible(false);
		
		setModel(treeModel);
	}
	
}
