/*
 * Copyright 2012 Goldman Sachs.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gs.collections.codegenerator.maven;

import com.gs.collections.codegenerator.GsCollectionsCodeGenerator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * @goal generate
 * @phase generate-sources
 * @requiresDependencyResolution compile
 */
public class GenerateMojo extends AbstractMojo
{
    /**
     * Skips code generation if true.
     *
     * @parameter expression="${skipCodeGen}"
     */
    private boolean skipCodeGen;
    /**
     * @parameter expression="${project.build.directory}/generated-sources"
     * @required
     */
    private String templateDirectory;

    /**
     * The Maven project to act upon.
     *
     * @parameter expression="${project}"
     * @required
     */
    private MavenProject project;

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        if (this.skipCodeGen)
        {
            this.getLog().info("Skipping code generation in " + this.project.getArtifactId());
        }
        else
        {
            this.getLog().info("Generating sources to " + this.project.getArtifactId());
            GsCollectionsCodeGenerator gsCollectionsCodeGenerator =
                    new GsCollectionsCodeGenerator(this.templateDirectory, this.project.getBasedir());
            gsCollectionsCodeGenerator.generate();

            if (gsCollectionsCodeGenerator.isTest())
            {
                this.project.addTestCompileSourceRoot(GsCollectionsCodeGenerator.GENERATED_TEST_SOURCES_LOCATION);
            }
            else
            {
                this.project.addCompileSourceRoot(GsCollectionsCodeGenerator.GENERATED_SOURCES_LOCATION);
            }
        }
    }
}