#!/usr/bin/env groovy
def mavenScala = new File("2.7.7-MVN")
def ant = new AntBuilder()

if (mavenScala.exists()) {
	ant.move(file:"2.7.7", tofile:"2.7.7-OSGI")
	ant.move(file:"2.7.7-MVN", tofile:"2.7.7")
} else {
	ant.move(file:"2.7.7", tofile:"2.7.7-MVN")
	ant.move(file:"2.7.7-OSGI", tofile:"2.7.7")
}