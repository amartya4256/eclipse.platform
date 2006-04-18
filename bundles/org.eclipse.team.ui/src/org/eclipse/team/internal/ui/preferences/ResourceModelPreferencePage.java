/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.team.internal.ui.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.team.internal.ui.*;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class ResourceModelPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage, IPreferenceIds {

	private RadioGroupFieldEditor defaultLayout;

	public ResourceModelPreferencePage() {
		super(GRID);
		setTitle(TeamUIMessages.SynchronizationCompareAdapter_0); 
		setDescription(TeamUIMessages.ResourceModelPreferencePage_0); 
		setPreferenceStore(TeamUIPlugin.getPlugin().getPreferenceStore());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
	    defaultLayout = new RadioGroupFieldEditor(SYNCVIEW_DEFAULT_LAYOUT, 
	    		TeamUIMessages.SyncViewerPreferencePage_0, 3,  
	            new String[][] {
	            	{TeamUIMessages.SyncViewerPreferencePage_1, FLAT_LAYOUT}, 
	            	{TeamUIMessages.SyncViewerPreferencePage_2, TREE_LAYOUT}, 
	            	{TeamUIMessages.SyncViewerPreferencePage_3, COMPRESSED_LAYOUT} 
	    		}, 
	    		getFieldEditorParent(), true /* use a group */);
	    addField(defaultLayout);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		// Nothing to do
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	public boolean performOk() {
		TeamUIPlugin.getPlugin().savePluginPreferences();
		return super.performOk();
	}
	
}
