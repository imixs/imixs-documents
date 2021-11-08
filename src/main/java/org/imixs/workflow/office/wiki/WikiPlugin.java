package org.imixs.workflow.office.wiki;
/*******************************************************************************
 *  Imixs Workflow 
 *  Copyright (C) 2001, 2011 Imixs Software Solutions GmbH,  
 *  http://www.imixs.com
 *  
 *  This program is free software; you can redistribute it and/or 
 *  modify it under the terms of the GNU General Public License 
 *  as published by the Free Software Foundation; either version 2 
 *  of the License, or (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 *  General Public License for more details.
 *  
 *  You can receive a copy of the GNU General Public
 *  License at http://www.gnu.org/licenses/gpl.html
 *  
 *  Project: 
 *  	http://www.imixs.org
 *  	http://java.net/projects/imixs-workflow
 *  
 *  Contributors:  
 *  	Imixs Software Solutions GmbH - initial API and implementation
 *  	Ralph Soika - Software Developer
 *******************************************************************************/


import java.util.logging.Logger;

import org.imixs.workflow.ItemCollection;
import org.imixs.workflow.engine.plugins.AbstractPlugin;
import org.imixs.workflow.exceptions.PluginException;

/**
 * This plugin computes the value for the property 'name' from the items
 * chapter.parent and chapter.number
 * <p>
 * 
 * @author rsoika
 * 
 */
public class WikiPlugin extends AbstractPlugin {
	

	private static Logger logger = Logger.getLogger(WikiPlugin.class
			.getSimpleName());


	
	@Override
	public ItemCollection run(ItemCollection workitem,
			ItemCollection documentActivity) throws PluginException {

			
		
		 logger.info("...........computing wiki chapter path");
         String chapter=workitem.getItemValueString("chapter.number").trim();
         String parent=workitem.getItemValueString("chapter.parent").trim();
         String path=chapter;
         // compute chapter path and set wiki flag
         if (!parent.isEmpty() && !"-".equals(parent)) {
             if (!parent.isEmpty()) {
                 path=parent + "." + chapter;
             }
         }
         workitem.setItemValue("name", path);

		return workitem;
	}



	
}
