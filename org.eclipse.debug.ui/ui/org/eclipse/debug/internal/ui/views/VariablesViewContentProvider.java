package org.eclipse.debug.internal.ui.views;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */

import java.util.HashMap;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Provide the contents for a variables viewer.
 */
public class VariablesViewContentProvider implements ITreeContentProvider {
	
	private HashMap fParentCache;
	
	/**
	 * Constructs a new provider
	 */
	public VariablesViewContentProvider() {
		fParentCache = new HashMap(10);
	}

	/**
	 * @see ITreeContentProvider#getChildren(Object)
	 */
	public Object[] getChildren(Object parent) {
		Object[] children= null;
		try {
			if (parent instanceof IStackFrame) {
				IStackFrame sf= (IStackFrame)parent;
				if (sf.isSuspended()) {
					children = sf.getVariables();
				}
			} else if (parent instanceof IVariable) {
				children = ((IVariable)parent).getValue().getVariables();
			}
			if (children != null) {
				for (int i = 0; i < children.length; i++) {
					fParentCache.put(children[i], parent);
				}
				return children;
			}
		} catch (DebugException e) {
			DebugUIPlugin.logError(e);
		}
		return new Object[0];
	}

	/**
	 * Returns the <code>IVariable</code>s for the given <code>IDebugElement</code>.
	 */
	public Object[] getElements(Object parent) {
		return getChildren(parent);
	}

	/**
	 * @see ITreeContentProvider
	 */
	public Object getParent(Object item) {
		return fParentCache.get(item);
	}


	/**
	 * Unregisters this content provider from the debug plugin so that
	 * this object can be garbage-collected.
	 */
	public void dispose() {
		fParentCache=null;
	}
	
	protected void clearCache() {
		fParentCache.clear();
	}
	
	/**
	 * @see ITreeContentProvider#hasChildren(Object)
	 */
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

	/**
	 * @see IContentProvider#inputChanged(Viewer, Object, Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

}

