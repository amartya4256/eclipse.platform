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
package org.eclipse.ant.ui.internal.launchConfigurations;


import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.ant.ui.internal.model.AntUtil;
import org.eclipse.ant.ui.internal.model.IAntUIConstants;
import org.eclipse.ant.ui.internal.model.IAntUIHelpContextIds;
import org.eclipse.ant.ui.internal.preferences.AntClasspathBlock;
import org.eclipse.ant.ui.internal.preferences.IAntBlockContainer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.help.WorkbenchHelp;

public class AntClasspathTab extends AbstractLaunchConfigurationTab implements IAntBlockContainer {

	private Button useDefaultButton;
	private AntClasspathBlock antClasspathBlock= new AntClasspathBlock();
	
	/**
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Font font= parent.getFont();
		Composite top = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 2;
		layout.marginWidth = 2;
		top.setLayout(layout);
		top.setLayoutData(new GridData(GridData.FILL_BOTH));
		top.setFont(font);

		setControl(top);
		WorkbenchHelp.setHelp(top, IAntUIHelpContextIds.ANT_CLASSPATH_TAB);
		
		createChangeClasspath(top);
		antClasspathBlock.setContainer(this);
		antClasspathBlock.createContents(top);
	}

	private void createChangeClasspath(Composite top) {
		Font font= top.getFont();
		Composite changeClasspath = new Composite(top, SWT.NONE);
		changeClasspath.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		changeClasspath.setLayout(layout);
		changeClasspath.setFont(font);

		useDefaultButton = new Button(changeClasspath, SWT.CHECK);
		useDefaultButton.setFont(font);
		useDefaultButton.setText(AntLaunchConfigurationMessages.getString("AntClasspathTab.Use_&global")); //$NON-NLS-1$
		useDefaultButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				toggleUseDefaultClasspath();
				updateLaunchConfigurationDialog();
			}

		});
	}

	private void toggleUseDefaultClasspath() {
		boolean enable = !useDefaultButton.getSelection();
		antClasspathBlock.setEnabled(enable);
	}
	
	/**
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
	}

	/**
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	public void initializeFrom(ILaunchConfiguration configuration) {
		String urlStrings= null;
		try {
			urlStrings = configuration.getAttribute(IAntUIConstants.ATTR_ANT_CUSTOM_CLASSPATH, (String) null);
		} catch (CoreException e) {
		}
		if (urlStrings == null) {
			useDefaultButton.setSelection(true);
			antClasspathBlock.setTablesEnabled(false);
			antClasspathBlock.initializeAntHome(null);
		} else {
			String antHomeString= null;
			try {
				antHomeString= configuration.getAttribute(IAntUIConstants.ATTR_ANT_HOME, (String)null);
			} catch (CoreException e) {
			}
			antClasspathBlock.initializeAntHome(antHomeString);
			useDefaultButton.setSelection(false);

			List userURLs= new ArrayList();
			List antURLs= new ArrayList();
			AntUtil.getCustomClasspaths(configuration, antURLs, userURLs);
			antClasspathBlock.setUserTableInput(userURLs);
			antClasspathBlock.setAntTableInput(antURLs);
			antClasspathBlock.setTablesEnabled(true);
		}

		toggleUseDefaultClasspath();
	}

	/**
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		if (useDefaultButton.getSelection()) {
			configuration.setAttribute(IAntUIConstants.ATTR_ANT_CUSTOM_CLASSPATH, (String)null);
			configuration.setAttribute(IAntUIConstants.ATTR_ANT_HOME, (String)null);
			return;
		}
		List antUrls= antClasspathBlock.getAntURLs();
		List userUrls= antClasspathBlock.getUserURLs();
		StringBuffer urlString= new StringBuffer();
		Iterator antUrlsItr= antUrls.iterator();
		while (antUrlsItr.hasNext()) {
			URL url = (URL) antUrlsItr.next();
			urlString.append(url.getFile());
			urlString.append(',');
		}
		if (userUrls.size() > 0) {
			urlString.append('*');
		}
		Iterator userUrlsItr= userUrls.iterator();
		while (userUrlsItr.hasNext()) {
			URL url = (URL) userUrlsItr.next();
			urlString.append(url.getFile());
			urlString.append(',');
		}
		if (urlString.length() > 0) {
			configuration.setAttribute(IAntUIConstants.ATTR_ANT_CUSTOM_CLASSPATH, urlString.substring(0, urlString.length() - 1));
		} else {
			configuration.setAttribute(IAntUIConstants.ATTR_ANT_CUSTOM_CLASSPATH, (String)null);
		}
		
		String antHomeText= antClasspathBlock.getAntHome();
		configuration.setAttribute(IAntUIConstants.ATTR_ANT_HOME, antHomeText);
	}

	/**
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
	 */
	public String getName() {
		return AntLaunchConfigurationMessages.getString("AntClasspathTab.Classpath_6"); //$NON-NLS-1$
	}
	
	/**
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getImage()
	 */
	public Image getImage() {
		return antClasspathBlock.getClasspathImage();
	}

	/**
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#isValid(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	public boolean isValid(ILaunchConfiguration launchConfig) {
		setErrorMessage(null);
		setMessage(null);
		if (antClasspathBlock.isAntHomeEnabled()) {
			return antClasspathBlock.validateAntHome();
		} else {
			return super.isValid(launchConfig);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.externaltools.internal.ant.preferences.IAntClasspathBlockContainer#setMessage(java.lang.String)
	 */
	public void setMessage(String message) {
		super.setMessage(message);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.externaltools.internal.ant.preferences.IAntClasspathBlockContainer#setErrorMessage(java.lang.String)
	 */
	public void setErrorMessage(String message) {
		super.setErrorMessage(message);
	}
	
	/* (non-Javadoc)
	* @see org.eclipse.ui.externaltools.internal.ant.preferences.IAntClasspathBlockContainer#createPushButton(org.eclipse.swt.widgets.Composite, java.lang.String)
	*/
	public Button createPushButton(Composite parent, String buttonText) {
		return super.createPushButton(parent, buttonText, null);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.externaltools.internal.ant.preferences.IAntClasspathBlockContainer#update()
	 */
	public void update() {
		updateLaunchConfigurationDialog();
	}
}
