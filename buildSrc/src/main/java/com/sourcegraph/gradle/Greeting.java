package com.sourcegraph.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.invocation.Gradle;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.Project;
import org.gradle.internal.impldep.com.amazonaws.util.json.Jackson;
import org.gradle.internal.impldep.com.google.common.collect.ImmutableMap;
import org.gradle.internal.impldep.org.testng.collections.Maps;
import org.gradle.internal.impldep.org.testng.collections.Sets;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import groovy.json.JsonOutput;

public class Greeting extends DefaultTask {
    /**
     * The root project
     */
    private Project rootProject;

    public Project getProject() {
        return rootProject;
    }

    public void setProject(Project project) {
        this.rootProject = project;
    }

    private final static HashSet<String> configWhitelist = new HashSet<>();
    static {
        configWhitelist.add("androidTestApi");
        configWhitelist.add("androidTestCompile");
        configWhitelist.add("androidTestCompileOnly");
        configWhitelist.add("debugApi");
        configWhitelist.add("debugCompile");
        configWhitelist.add("debugCompileOnly");
        configWhitelist.add("debugImplementation");
        configWhitelist.add("releaseApi");
        configWhitelist.add("releaseCompile");
        configWhitelist.add("releaseCompileOnly");
        configWhitelist.add("releaseImplementation");
        configWhitelist.add("testApi");
        configWhitelist.add("testCompile");
        configWhitelist.add("testCompileOnly");
        configWhitelist.add("testImplementation");
        configWhitelist.add("testCompileClasspath");
        configWhitelist.add("api");
        configWhitelist.add("implementation");
        configWhitelist.add("compile");
        configWhitelist.add("compileOnly");
        configWhitelist.add("compileClasspath");
    }

    @TaskAction
    void printProjectHierarchy() {
        Map<String, Object> info = new HashMap<>();

        getProject().allprojects(p -> {
            p.afterEvaluate(project -> {
                info.put("artifactId", project.getName());
                info.put("groupId", project.getGroup());
                info.put("version",  project.getVersion());

                List<ImmutableMap<String, String>> deps = project.getConfigurations().stream()
                        .filter(c -> configWhitelist.contains(c.getName()))
                        .flatMap(c -> c.getAllDependencies().stream().map(d -> {
                            return ImmutableMap.of("group", d.getGroup(), "name", d.getName(), "version", d.getVersion());
                        })).distinct().collect(Collectors.toList());
                info.put("dependencies", deps);

                // TODO
            });
        });

        getProject().getGradle().buildFinished(__ -> {
            JsonOutput.prettyPrint(JsonOutput.toJson(ImmutableMap.of(
                    "projects", info
            )));
        });
    }

}
