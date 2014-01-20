#*********************************************************************
# 
#    Copyright (C) :	2002,2003,2004,2005,2006,2007,2008,2009,2010,2011,2012
# 			European Synchrotron Radiation Facility
# 			BP 220, Grenoble 38043
# 			FRANCE
# 
#    This file is part of Tango.
# 
#    Tango is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#  
#    Tango is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#  
#    You should have received a copy of the GNU General Public License
#    along with Tango.  If not, see <http: //www.gnu.org/licenses/>.
# 
#
# File          :   Makefile
#
# Project       :   atkpanel generic java application
#
# Description   :   The Makefile to build the java application for monitoring
#                   a device
#
# Author        :   Faranguiss Poncet
#
# Original      :   Mars 2002
#
# $Revision$				$Author$
# $Date$					$State$
#
#
#**********************************************************************
#

#---------------------------------------------------------------------
#	This Makefile works with the GNU make (sometimes called gmake)
#	It makes use of the GNU make conditional statements to support
#	multiple platforms. To use this makefile for a particular platform
#	call GNU make with the appropriate symbol for that platform
#	defined e.g. "gmake _solaris9=1 unix=1 all". The following symbols
#	are used to identify the following platforms :
#
# solaris9: gmake _solaris9=1 (host kidiboo)
# redhat4:  gmake _redhate4=1 (host esrflinux1-1)
#	
#--------------------------------------------------------------------
#
#       The variables classpath used for compiling is set inside this Makefile.
#       So it doesnot use your own environment varaible $CLASSPATH.
##
#
#--------------------------------------------------------------------
#
TANGO_HOME = /segfs/tango
CLASS_HOME = ../bin
LIB_HOME = $(TANGO_HOME)/release/java/lib


#               The JAVA Compilers for UNIX
# JAVAC : defined in env
# JAR : defined in env



JC      = $(JAVAC) -d $(CLASS_HOME)
ATK_CLS = $(LIB_HOME)/ATKCore.jar:$(LIB_HOME)/ATKWidget.jar
#ATK_CLS = /users/poncet/atk/lib
JIVE_CLS = $(TANGO_HOME)/release/java/appli/Jive.jar
TANGO_CLS = $(LIB_HOME)/TangORB.jar
CL_PATH = $(CLASS_HOME):$(ATK_CLS):$(JIVE_CLS):$(TANGO_CLS)
JC_FLAGS = -g -classpath $(CL_PATH)
#JC_FLAGS = -g -deprecation -Xlint:unchecked -classpath $(CL_PATH)


#               class files
CLS	=	SpectrumPanel.class \
		StringSpectrumPanel.class \
		DevStateSpectrumPanel.class \
		BooleanSpectrumPanel.class \
		ImagePanel.class \
		StringImagePanel.class \
		RawImagePanel.class \
		RefresherUtil.class \
		MainPanel.class
		
#		Jar file to be built
JARFILE =	atkpanel.jar
		
		
# define a rule for making java class files (suffice .class) from java source files
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JC_FLAGS) $*.java



#---------------------------------------------------------------------
# build the package

	
all:		$(CLS)
		cd $(CLASS_HOME); $(JAR) cvf $(JARFILE) atkpanel/*.class



clean:	
		cd $(CLASS_HOME); rm -f atkpanel/*.class $(JARFILE)



