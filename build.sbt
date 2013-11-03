
javaHome := Some(file(System.getenv("JAVA_HOME")))

name := "abacussfx"

version := "1.0-SNAPSHOT"

organization := "org.scalafx.abacussfx"

scalaVersion := "2.9.3"

resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"

javacOptions ++= Seq("-source", "1.6", "-target", "1.6")

// append -deprecation to the options passed to the Scala compiler
scalacOptions += "-deprecation"

libraryDependencies += "org.scalafx" % "scalafx_2.9.3" % "1.0.0-M6"

// set the main class for the main 'run' task
// change Compile to Test to set it for 'test:run'
//mainClass in (Compile, run) := Some("org.scalafx.abacussfx.Abacus2AddAllBallsAndOnClick")

// set the prompt (for this build) to include the project id.
shellPrompt in ThisBuild := { state => Project.extract(state).currentRef.project + "> " }

// set the prompt (for the current project) to include the username
shellPrompt := { state => System.getProperty("user.name") + "> " }

// disable printing timing information, but still print [success]
showTiming := false

// disable printing a message indicating the success or failure of running a task
showSuccess := false

// fork a new JVM for 'run' and 'test:run'
fork := true

// add JavaFX 2.0 to the unmanaged classpath
// unmanagedJars in Compile += Attributed.blank(file(System.getenv("JAVAFX_HOME") + "/rt/lib/jfxrt.jar"))
// For Java 7 update 06 the JFXRT JAR is part of the Java Runtime Environment
unmanagedJars in Compile += Attributed.blank(file(System.getenv("JAVA_HOME") + "/jre/lib/jfxrt.jar"))

