/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ant.internal.ui.preferences;

public class GlobalClasspathEntries extends AbstractClasspathEntry {
	private String name;
	/**
	 * 
	 */
	public GlobalClasspathEntries(String name, IClasspathEntry parent) {
		this.parent= parent;
		this.name= name;
	}
		
	public void addEntry(ClasspathEntry entry) {
		childEntries.add(entry);
	}
	
	public void removeEntry(ClasspathEntry entry) {
		childEntries.remove(entry);
	}
	
	public boolean contains(ClasspathEntry entry) {
		return childEntries.contains(entry);
	}
	
	public String toString() {
		return name;
	}

	public void removeAll() {
		childEntries.clear();
	}

}
