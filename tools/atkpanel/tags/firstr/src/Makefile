#*********************************************************************
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
# $Log$
#
# Copyright (c) 2002 by European Synchrotron Radiation Facility,
#		       Grenoble, France
#
#                       All Rights Reserved
#
#
#**********************************************************************
#

#---------------------------------------------------------------------
#	This Makefile works with the GNU make (sometimes called gmake)
#	It makes use of the GNU make conditional statements to support
#	multiple platforms. To use this makefile for a particular platform
#	call GNU make with the appropriate symbol for that platform
#	defined e.g. "gmake _solaris=1 unix=1 all". The following symbols
#	are used to identify the following platforms :
#
#	_solaris	=	Solaris 
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
LIB_HOME = $(TANGO_HOME)/lib/java


#               The JAVA Compilers for UNIX
ifdef _solaris
JC      = javac -d $(CLASS_HOME)
JCHART_CLS = /segfs/graphics/JClassChart/lib/jcchart451K.jar
ATK_CLS = $(LIB_HOME)/ATKCore.jar:$(LIB_HOME)/ATKWidget.jar:$(LIB_HOME)/log4j.jar
#ATK_CLS = /users/poncet/atk/lib:$(LIB_HOME)/log4j.jar
TANGO_CLS = $(LIB_HOME)/TangoJacorb.jar:$(LIB_HOME)/jacorb.jar
CL_PATH = $(CLASS_HOME):$(ATK_CLS):$(TANGO_CLS):$(JCHART_CLS)
JC_FLAGS = -g -classpath $(CL_PATH)
#JC_FLAGS = -g -deprecation -classpath $(CL_PATH)
endif


#               class files
CLS	=	SpectrumPanel.class \
		ImagePanel.class \
		MainPanel.class
		
#		Jar file to be built
JARFILE =	atkpanel.jar
		
		
ifdef unix
# define a rule for making java class files (suffice .class) from java source files
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JC_FLAGS) $*.java

endif


#---------------------------------------------------------------------
# build the package

	
all:		$(CLS)
		cd $(CLASS_HOME); jar cvf $(JARFILE) atkpanel/*.class



clean:	
		cd $(CLASS_HOME); rm -f atkpanel/*.class $(JARFILE)



