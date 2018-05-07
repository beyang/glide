package com.sourcegraph.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.Project;
import org.gradle.internal.impldep.com.amazonaws.util.json.Jackson;

public class Greeting extends DefaultTask {
    /**
     * The root project
     */
    private Project project;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @TaskAction
    void printProjectHierarchy() {
        printProjectHierarchy(getProject());

        getProject().subprojects(p -> {
            printProjectHierarchy(p);
        });
    }

    private static void printProjectHierarchy(Project p) {
        System.out.printf("######## project: %s\n", p.getName());
        ProjectConfig cfg = new ProjectConfig();

        cfg.setArtifactId(p.getName()); // assume project name is also artifact ID
        cfg.setGroupId(p.getGroup().toString()); // assume project group is also artifact Group

        System.out.printf("  %s\n", Jackson.toJsonString(cfg));


//        cfg.
//        p.getDependencies().getModules()

//        System.out.printf("  configurations: %s\n", p.getConfigurations());

//        System.out.printf("  dependencies: %s\n", p.getDependencies().toString());
//        System.out.printf("  properties: %s\n", p.getProperties().toString());
//        System.out.printf("  artifacts: %s\n", p.getArtifacts().toString());
//        System.out.printf("  repositories: %s\n", p.getRepositories().toString());
//        System.out.printf("  version: %s\n", p.getVersion().toString());
//        System.out.printf("  deps: %s\n", p.getDependencies().toString());
    }
}
