/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.debug.internal.core.sourcelookup;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.sourcelookup.*;

/**
 * Proxy to contributed source path computer extension.
 */
public class SourcePathComputer implements ISourcePathComputer {
	
	// lazily instantiated delegate
	private ISourcePathComputerDelegate fDelegate = null;
	
	// extension definition
	private IConfigurationElement fElement = null;
	
	/**
	 * Constructs a source path computer on the given extension.
	 * 
	 * @param element extension definition
	 */
	public SourcePathComputer(IConfigurationElement element) {
		fElement = element;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.internal.core.sourcelookup.ISourceContainerType#getId()
	 */
	public String getId() {
		return fElement.getAttribute("id"); //$NON-NLS-1$
	}
	
	/**
	 * Lazily instantiates and returns the underlying source container type.
	 * 
	 * @exception CoreException if unable to instantiate
	 */
	private ISourcePathComputerDelegate getDelegate() throws CoreException {
		if (fDelegate == null) {
			fDelegate = (ISourcePathComputerDelegate) fElement.createExecutableExtension("class"); //$NON-NLS-1$
		}
		return fDelegate;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.internal.core.sourcelookup.ISourcePathComputer#computeSourceContainers(org.eclipse.debug.core.ILaunchConfiguration, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public ISourceContainer[] computeSourceContainers(ILaunchConfiguration configuration, IProgressMonitor monitor) {
		try {
			return getDelegate().computeSourceContainers(configuration, monitor);
		} catch (CoreException e) {
			DebugPlugin.log(e);
		}
		return new ISourceContainer[0];
	}
}
