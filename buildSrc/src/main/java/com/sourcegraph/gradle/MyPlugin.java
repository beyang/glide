package com.sourcegraph.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class MyPlugin implements Plugin<Project> {
    public void apply(Project project) {
        project.getTasks().create("hello", Greeting.class, (task) -> {
                task.setMessage("Hello");
                task.setRecipient("World");
            });
    }
}
