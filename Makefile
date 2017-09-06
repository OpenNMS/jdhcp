# Makefile for JDHCP package
#
# Author: Jason Goldschmidt 1999/09/09

JAVAC=javac
DEBUG=-g
#Uncomment if using JDK 1.2 and want no debugging
#NODEBUG=-g:none
JAVADOC=javadoc

JAR=jar
CLASSES=classes
JARS=jars
PKGPATH=edu/bucknell/net/JDHCP
PKGNAME=edu.bucknell.net.JDCHP
INSTALL=/usr/bin/install -c
#Edit if different
INSTALL_DIR=/usr/local/lib/classes
JDHCPJAR=JDHCP.jar
DOC=api


all:	
	$(JAVAC) $(NODEBUG) -d classes $(PKGPATH)/*.java
	-@mkdir -p jars
	cd $(CLASSES)/; $(JAR) cvMf ../$(JARS)/$(JDHCPJAR) $(PKGPATH)/*.class 

jar:	
	-@mkdir -p jars
	cd $(CLASSES)/; $(JAR) cvMf ../$(JARS)/$(JDHCPJAR) $(PKGPATH)/*.class 


debug:
	$(JAVAC) $(DEBUG) -d classes $(PKGPATH)/*.java
	-@mkdir -p jars
	cd $(CLASSES)/; $(JAR) cvMf ../$(JARS)/$(JDHCPJAR) $(PKGPATH)/*.class


clean:
		-$(RM) $(CLASSES)/$(PKGPATH)/*.class 
		-$(RM) $(CLASSES)/$(PKGPATH)/*~
		-$(RM) -r $(DOC)/*

clobber:	clean
		-$(RM) $(JARS)/*.jar


#Make docs for the API only.
docs:
		-@mkdir -p $(DOC)
		$(JAVADOC) -public -author -version -classpath $(CLASSES) -d $(DOC) $(PKGPATH)/*.java


install:	
		-@mkdir -p $(INSTALL_DIR)
		$(INSTALL) $(JARS)/$(JDHCPJAR) $(INSTALL_DIR)

uninstall:	clobber
		-@rm -f $(INSTALL_DIR)/$(JDHCPJAR)
