/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.debug.internal.core.sourcelookup.containers;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.sourcelookup.ISourceContainer;
import org.eclipse.debug.core.sourcelookup.containers.*;
import org.eclipse.debug.internal.core.sourcelookup.SourceLookupMessages;
import org.eclipse.debug.internal.core.sourcelookup.SourceLookupUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * A folder in the workspace.
 * 
 * @since 3.0
 */
public class FolderSourceContainerType extends AbstractSourceContainerTypeDelegate {

	/**
	 * Unique identifier for the folder source container type
	 * (value <code>org.eclipse.debug.core.containerType.folder</code>).
	 */
	public static final String TYPE_ID = DebugPlugin.getUniqueIdentifier() + ".containerType.folder";	 //$NON-NLS-1$
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.internal.core.sourcelookup.ISourceContainerType#getMemento(org.eclipse.debug.internal.core.sourcelookup.ISourceContainer)
	 */
	public String getMemento(ISourceContainer container) throws CoreException {
		FolderSourceContainer folderSourceContainer = (FolderSourceContainer)container;
		Document document = SourceLookupUtils.newDocument();
		Element element = document.createElement("folder"); //$NON-NLS-1$
		element.setAttribute("path", folderSourceContainer.getContainer().getFullPath().toString()); //$NON-NLS-1$
		String nest = "false"; //$NON-NLS-1$
		if (folderSourceContainer.isComposite()) {
			nest = "true"; //$NON-NLS-1$
		}
		element.setAttribute("nest", nest);  //$NON-NLS-1$
		document.appendChild(element);
		return SourceLookupUtils.serializeDocument(document);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.internal.core.sourcelookup.ISourceContainerType#createSourceContainer(java.lang.String)
	 */
	public ISourceContainer createSourceContainer(String memento) throws CoreException {
		Node node = SourceLookupUtils.parseDocument(memento);
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element element = (Element)node;
			if ("folder".equals(element.getNodeName())) { //$NON-NLS-1$
				String string = element.getAttribute("path"); //$NON-NLS-1$
				if (string == null || string.length() == 0) {
					abort(SourceLookupMessages.getString("FolderSourceContainerType.10"), null); //$NON-NLS-1$
				}
				String nest = element.getAttribute("nest"); //$NON-NLS-1$
				boolean nested = "true".equals(nest); //$NON-NLS-1$
				IWorkspace workspace = ResourcesPlugin.getWorkspace();
				IFolder folder = workspace.getRoot().getFolder(new Path(string));
				// TODO: what if folder does not exist
				return new FolderSourceContainer(folder, nested);
			} else {
				abort(SourceLookupMessages.getString("FolderSourceContainerType.11"), null); //$NON-NLS-1$
			}
		}
		abort(SourceLookupMessages.getString("FolderSourceContainerType.12"), null); //$NON-NLS-1$
		return null;
	}
}
