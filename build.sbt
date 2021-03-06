/* Copyright 2017-18, Emmanouil Antonios Platanios. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import ReleaseTransformations._
import sbtrelease.Vcs

scalaVersion in ThisBuild := "2.12.4"
crossScalaVersions in ThisBuild := Seq("2.11.12", "2.12.4")

organization in ThisBuild := "org.platanios"

// // In order to update the snapshots more frequently, the Coursier "Time-To-Live" (TTL) option can be modified. This can
// // be done by modifying the "COURSIER_TTL" environment variable. Its value is parsed using
// // 'scala.concurrent.duration.Duration', so that things like "24 hours", "5 min", "10s", or "0s", are fine, and it also
// // accepts infinity ("Inf") as a duration. It defaults to 24 hours, meaning that the snapshot artifacts are updated
// // every 24 hours.
// resolvers in ThisBuild += Resolver.sonatypeRepo("snapshots")

// val tensorFlowForScalaVersion = "0.1.2-SNAPSHOT"

addCompilerPlugin("io.tryp" % "splain" % "0.2.9" cross CrossVersion.patch)

autoCompilerPlugins in ThisBuild := true

scalacOptions in ThisBuild ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Yno-adapted-args",
  "-Xfuture",
  "-Xsource:2.13",
  "-P:splain:all",
  "-P:splain:infix",
  "-P:splain:foundreq",
  "-P:splain:implicits",
  "-P:splain:color",
  "-P:splain:tree",
  "-P:splain:boundsimplicits")

lazy val commonSettings = Seq(
  // Plugin that prints better implicit resolution errors.
  addCompilerPlugin("io.tryp"  % "splain" % "0.2.7" cross CrossVersion.patch))

lazy val testSettings = Seq(
  libraryDependencies ++= Seq(
    "org.scalactic" %%% "scalactic" % "3.0.4",
    "org.scalatest" %%% "scalatest" % "3.0.5" % "test"),
  logBuffered in Test := false,
  fork in test := false,
  testForkedParallel in Test := false,
  parallelExecution in Test := false)

// lazy val tensorFlowSettings = Seq(
//   libraryDependencies += "org.platanios" %% "tensorflow" % tensorFlowForScalaVersion) // classifier "linux-gpu-x86_64")

lazy val all = (project in file("."))
    .aggregate(d3, d3Examples)
    .dependsOn(d3, d3Examples)
    .settings(moduleName := "d3", name := "D3 Scala")
    .enablePlugins(ScalaUnidocPlugin)
    .settings(publishSettings)
    .settings(
      sourcesInBase := false,
      unmanagedSourceDirectories in Compile := Nil,
      unmanagedSourceDirectories in Test := Nil,
      unmanagedResourceDirectories in Compile := Nil,
      unmanagedResourceDirectories in Test := Nil,
      publishArtifact := true)

lazy val d3 = (project in file("./d3"))
    .settings(moduleName := "d3", name := "D3 ScalaJS")
    .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin, WorkbenchPlugin)
    .settings(commonSettings)
    .settings(testSettings)
    //.settings(tensorFlowSettings)
    .settings(publishSettings)
    .settings(
      libraryDependencies ++= Seq(
        "org.scala-js" %%% "scalajs-dom" % "0.9.5",
        "com.lihaoyi"  %%% "scalatags"   % "0.6.7"),
      npmDependencies in Compile ++= Seq(
        "d3" -> "5.0.0",
        "d3-hsv" -> "0.1.0"),
      skip in packageJSDependencies := false,
      webpackBundlingMode := BundlingMode.LibraryOnly())

lazy val d3Examples = (project in file("./d3-examples"))
    .settings(moduleName := "d3-examples", name := "D3 ScalaJS - Examples")
    .dependsOn(d3)
    .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin, WorkbenchPlugin)
    .settings(commonSettings)
    .settings(testSettings)
    .settings(publishSettings)
    .settings(
      libraryDependencies ++= Seq(
        "org.scala-js" %%% "scalajs-dom" % "0.9.5",
        "com.lihaoyi"  %%% "scalatags"   % "0.6.7"),
      skip in packageJSDependencies := false,
      webpackBundlingMode := BundlingMode.LibraryAndApplication())

lazy val noPublishSettings = Seq(
  publish := Unit,
  publishLocal := Unit,
  publishArtifact := false,
  skip in publish := true,
  releaseProcess := Nil)

lazy val publishSettings = Seq(
  publishArtifact := true,
  homepage := Some(url("https://github.com/eaplatanios/d3_scalajs")),
  licenses := Seq("Apache License 2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.txt")),
  scmInfo := Some(ScmInfo(url("https://github.com/eaplatanios/d3_scalajs"),
                          "scm:git:git@github.com:eaplatanios/d3_scalajs.git")),
  developers := List(
    Developer(
      id="eaplatanios",
      name="Emmanouil Antonios Platanios",
      email="e.a.platanios@gmail.com",
      url=url("http://platanios.org/"))
  ),
  autoAPIMappings := true,
  apiURL := Some(url("http://eaplatanios.github.io/d3_scalajs/api/")),
  releaseCrossBuild := true,
  releaseTagName := {
    val buildVersionValue = (version in ThisBuild).value
    val versionValue = version.value
    s"v${if (releaseUseGlobalVersion.value) buildVersionValue else versionValue}"
  },
  releaseVersionBump := sbtrelease.Version.Bump.Next,
  releaseUseGlobalVersion := true,
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  releaseVcs := Vcs.detect(baseDirectory.value),
  releaseVcsSign := true,
  releaseIgnoreUntrackedFiles := true,
  useGpg := true,  // Bouncy Castle has bugs with sub-keys, so we use gpg instead
  pgpPassphrase := sys.env.get("PGP_PASSWORD").map(_.toArray),
  pgpPublicRing := file("~/.gnupg/pubring.gpg"),
  pgpSecretRing := file("~/.gnupg/secring.gpg"),
  publishMavenStyle := true,
  // publishArtifact in Test := false,
  pomIncludeRepository := Function.const(false),
  publishTo := Some(
    if (isSnapshot.value)
      Opts.resolver.sonatypeSnapshots
    else
      Opts.resolver.sonatypeStaging
  ),
  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    publishArtifacts,
    setNextVersion,
    commitNextVersion,
    releaseStepCommand("sonatypeReleaseAll"),
    pushChanges
  ),
  // For Travis CI - see http://www.cakesolutions.net/teamblogs/publishing-artefacts-to-oss-sonatype-nexus-using-sbt-and-travis-ci
  credentials ++= (for {
    username <- Option(System.getenv().get("SONATYPE_USERNAME"))
    password <- Option(System.getenv().get("SONATYPE_PASSWORD"))
  } yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)).toSeq)
