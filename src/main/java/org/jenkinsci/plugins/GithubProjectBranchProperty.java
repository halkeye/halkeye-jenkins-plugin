package org.jenkinsci.plugins;

import com.coravy.hudson.plugins.github.GithubProjectProperty;
import hudson.Extension;
import hudson.model.*;
import jenkins.branch.*;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class GithubProjectBranchProperty extends BranchProperty {
    /**
     * This will the URL to the project main branch.
     */
    private String projectUrl;

    @DataBoundConstructor
    public GithubProjectBranchProperty(final String projectUrl) {
        super();
        this.projectUrl = projectUrl;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <P extends Job<P, B>, B extends Run<P, B>> JobDecorator<P, B> jobDecorator(final Class<P> clazz) {
        return (JobDecorator<P, B>) new JobDecorator<P, B>() {
            @Nonnull
            @Override
            public List<JobProperty<? super P>> jobProperties(@Nonnull List<JobProperty<? super P>> jobProperties) {
                List<JobProperty<? super P>> result = asArrayList(jobProperties);
                result.add((JobProperty<? super P>) new GithubProjectProperty(GithubProjectBranchProperty.this.projectUrl));
                return result;

            }
        };
    }

    @Extension
    public static class DescriptorImpl extends BranchPropertyDescriptor {
        @Nonnull
        @Override
        public String getDisplayName() {
            return new GithubProjectProperty.DescriptorImpl().getDisplayName();
        }
    }

}
