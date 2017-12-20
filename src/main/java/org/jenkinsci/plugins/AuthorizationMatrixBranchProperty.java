package org.jenkinsci.plugins;

import hudson.Extension;
import hudson.model.Job;
import hudson.model.JobProperty;
import hudson.model.Run;
import hudson.security.AuthorizationMatrixProperty;
import jenkins.branch.BranchProperty;
import jenkins.branch.BranchPropertyDescriptor;
import jenkins.branch.JobDecorator;
import hudson.security.Permission;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;
import java.util.*;

public class AuthorizationMatrixBranchProperty extends BranchProperty {
    /**
     * This will the URL to the project main branch.
     */
    private final Map<Permission, Set<String>> grantedPermissions = new HashMap<>();

    @DataBoundConstructor
    public AuthorizationMatrixBranchProperty(Map<Permission, Set<String>> grantedPermissions) {
        super();
        for (Map.Entry<Permission, Set<String>> e : grantedPermissions.entrySet())
            this.grantedPermissions.put(e.getKey(), new HashSet<>(e.getValue()));
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <P extends Job<P, B>, B extends Run<P, B>> JobDecorator<P, B> jobDecorator(final Class<P> clazz) {
        return (JobDecorator<P, B>) new JobDecorator<P, B>() {
            @Nonnull
            @Override
            public List<JobProperty<? super P>> jobProperties(@Nonnull List<JobProperty<? super P>> jobProperties) {
                List<JobProperty<? super P>> result = asArrayList(jobProperties);
                result.add((JobProperty<? super P>) new AuthorizationMatrixProperty(AuthorizationMatrixBranchProperty.this.grantedPermissions));
                return result;
            }
        };
    }

    @Extension
    public static class DescriptorImpl extends BranchPropertyDescriptor {
        @Nonnull
        @Override
        public String getDisplayName() {
            return new AuthorizationMatrixProperty.DescriptorImpl().getDisplayName();
        }
    }

}
