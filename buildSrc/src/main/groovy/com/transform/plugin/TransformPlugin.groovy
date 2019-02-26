package com.transform.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class TransformPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.task('hello') {
            doLast {
                println 'Hello transform plugin'
            }
        }
    }
}