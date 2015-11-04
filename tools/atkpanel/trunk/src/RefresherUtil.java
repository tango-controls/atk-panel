/*
 *
 *   Copyright (C) :	2007,2008,2009,2010,2011,2012, 2013, 2014, 2015
 *			European Synchrotron Radiation Facility
 *			BP 220, Grenoble 38043
 *			FRANCE
 *
 *   This file is part of Tango.
 *
 *   Tango is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 * 
 *   Tango is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 * 
 *   You should have received a copy of the GNU General Public License
 *   along with Tango.  If not, see <http: //www.gnu.org/licenses/>.
 *
 *  
 *   File          :   RefresherUtil.java
 *  
 *   Project       :   atkpanel generic java application
 *  
 *   Description   :   The refresher utilities : handles stop and start refreshing of one
 *		       particular attribute.
 *  
 *   Author        :   Faranguiss Poncet
 *  
 *   Original      :   July 2007
 *  
 *   $Revision$				$Author$
 *   $Date$					$State$
 *  
 */
 
package atkpanel;

import java.awt.Component;
import fr.esrf.tangoatk.core.*;

class RefresherUtil {
    
        static private AttributeList spectOrImageRefresherAttList = new AttributeList();
        static private boolean       isRefreshing = false;


        static void startTabsRefresher()
        {
            isRefreshing = true;
            if (!spectOrImageRefresherAttList.isEmpty())
                spectOrImageRefresherAttList.startRefresher();
        }

        static void stopTabsRefresher()
        {
            isRefreshing = false;
            spectOrImageRefresherAttList.stopRefresher();
        }

        static void setTabsRefreshInterval(int refIt)
        {
            spectOrImageRefresherAttList.setRefreshInterval(refIt);
        }
        
        static boolean isRefreshing()
        {
            return isRefreshing;
        }
        
        
	static void enableComponentRefresh(Component comp)
	{
	    boolean  previousState = isRefreshing;
            stopTabsRefresher();
                if(comp instanceof ImagePanel)
		{
			ImagePanel imgComp = (ImagePanel)comp;
                        IAttribute  iatt = imgComp.getModel();
                        spectOrImageRefresherAttList.add(iatt);
		}
		if(comp instanceof SpectrumPanel)
		{
			SpectrumPanel specComp = (SpectrumPanel)comp;
                        IAttribute  iatt = specComp.getModel();
                        spectOrImageRefresherAttList.add(iatt);
		}
		if(comp instanceof StringSpectrumPanel)
		{
			StringSpectrumPanel specComp = (StringSpectrumPanel)comp;
                        IAttribute  iatt = specComp.getModel();
                        spectOrImageRefresherAttList.add(iatt);
		}		
		if(comp instanceof StringImagePanel)
		{
			StringImagePanel imgComp = (StringImagePanel)comp;
                        IAttribute  iatt = imgComp.getModel();
                        spectOrImageRefresherAttList.add(iatt);
		}		
		if(comp instanceof DevStateSpectrumPanel)
		{
			DevStateSpectrumPanel dssComp = (DevStateSpectrumPanel)comp;
                        IAttribute  iatt = dssComp.getModel();
                        spectOrImageRefresherAttList.add(iatt);
		}		
		if(comp instanceof RawImagePanel)
		{
			RawImagePanel rawImgComp = (RawImagePanel)comp;
                        IAttribute  iatt = rawImgComp.getModel();
                        spectOrImageRefresherAttList.add(iatt);
		}
		if(comp instanceof BooleanSpectrumPanel)
		{
			BooleanSpectrumPanel bssComp = (BooleanSpectrumPanel)comp;
                        IAttribute  iatt = bssComp.getModel();
                        spectOrImageRefresherAttList.add(iatt);
		}
            
            if (previousState)
                startTabsRefresher();
	}
	
	static void refreshComponent(Component comp)
	{
		if(comp instanceof ImagePanel)
		{
			ImagePanel    imgComp = (ImagePanel)comp;
                        IAttribute    iatt = imgComp.getModel();
                        iatt.refresh();
		}
		if(comp instanceof SpectrumPanel)
		{
			SpectrumPanel    spectComp = (SpectrumPanel)comp;
                        IAttribute       iatt = spectComp.getModel();
                        iatt.refresh();
		}
		if(comp instanceof StringSpectrumPanel)
		{
			StringSpectrumPanel strSpectComp = (StringSpectrumPanel)comp;
                        IAttribute       iatt = strSpectComp.getModel();
                        iatt.refresh();
		}		
		if(comp instanceof StringImagePanel)
		{
			StringImagePanel    strImageComp = (StringImagePanel)comp;
                        IAttribute       iatt = strImageComp.getModel();
                        iatt.refresh();
		}		
		if(comp instanceof DevStateSpectrumPanel)
		{
			DevStateSpectrumPanel dssComp = (DevStateSpectrumPanel)comp;
                        IAttribute       iatt = dssComp.getModel();
                        iatt.refresh();
		}		
		if(comp instanceof BooleanSpectrumPanel)
		{
			BooleanSpectrumPanel bssComp = (BooleanSpectrumPanel)comp;
                        IAttribute       iatt = bssComp.getModel();
                        iatt.refresh();
		}		
		if(comp instanceof RawImagePanel)
		{
			RawImagePanel  rawImgComp = (RawImagePanel)comp;
                        IAttribute       iatt = rawImgComp.getModel();
                        iatt.refresh();
		}
	}	
	
	static void disableRefreshComponent(Component comp)
	{
	    boolean  previousState = isRefreshing;
            stopTabsRefresher();

                if(comp instanceof ImagePanel)
		{
			ImagePanel imgComp = (ImagePanel)comp;
                        IAttribute  iatt = imgComp.getModel();
                        spectOrImageRefresherAttList.remove(iatt.getName());
		}
		if(comp instanceof SpectrumPanel)
		{
			SpectrumPanel specComp = (SpectrumPanel)comp;
                        IAttribute  iatt = specComp.getModel();
                        spectOrImageRefresherAttList.remove(iatt.getName());
		}
		if(comp instanceof StringSpectrumPanel)
		{
			StringSpectrumPanel specComp = (StringSpectrumPanel)comp;
                        IAttribute  iatt = specComp.getModel();
                        spectOrImageRefresherAttList.remove(iatt.getName());
		}		
		if(comp instanceof StringImagePanel)
		{
			StringImagePanel imgComp = (StringImagePanel)comp;
                        IAttribute  iatt = imgComp.getModel();
                        spectOrImageRefresherAttList.remove(iatt.getName());
		}		
		if(comp instanceof DevStateSpectrumPanel)
		{
			DevStateSpectrumPanel dssComp = (DevStateSpectrumPanel)comp;
                        IAttribute  iatt = dssComp.getModel();
                        spectOrImageRefresherAttList.remove(iatt.getName());
		}		
		if(comp instanceof BooleanSpectrumPanel)
		{
			BooleanSpectrumPanel bssComp = (BooleanSpectrumPanel)comp;
                        IAttribute  iatt = bssComp.getModel();
                        spectOrImageRefresherAttList.remove(iatt.getName());
		}		
		if(comp instanceof RawImagePanel)
		{
			RawImagePanel   rawImgComp = (RawImagePanel)comp;
                        IAttribute  iatt = rawImgComp.getModel();
                        spectOrImageRefresherAttList.remove(iatt.getName());
		}
            if (previousState)
                startTabsRefresher();
	}
	
	static void refreshAllComponents(javax.swing.JTabbedPane jtabbedPane)
	{
		Component[] components = jtabbedPane.getComponents();
		for(int i=0;i < components.length;i++)
		{
			refreshComponent(components[i]);
		}
	}
	
}
