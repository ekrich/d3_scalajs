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

logLevel := Level.Warn

// addSbtPlugin("com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings" % "latest.release")

// Plugins used for generating the library website
addSbtPlugin("com.eed3si9n" % "sbt-unidoc" % "0.4.1")

// Packaging and publishing related plugins
addSbtPlugin("com.github.gseitz" % "sbt-release"  % "1.0.7")
addSbtPlugin("com.jsuereth"      % "sbt-pgp"      % "1.1.1")
addSbtPlugin("org.xerial.sbt"    % "sbt-sonatype" % "2.0")

// Generally useful plugin.
addSbtPlugin("io.get-coursier" %  "sbt-coursier" % "1.0.1") // Provides fast dependency resolution.

// ScalaJS
addSbtPlugin("org.scala-js"  % "sbt-scalajs"         % "0.6.22")
addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "0.12.0")
addSbtPlugin("com.lihaoyi"   % "workbench"           % "0.4.0")
