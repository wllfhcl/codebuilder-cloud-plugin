package dev.lsegal.jenkins.codebuilder;

import java.io.IOException;
import java.util.Arrays;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Computer;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildWrapperDescriptor;
import jenkins.model.Jenkins;
import jenkins.tasks.SimpleBuildWrapper;

public final class CodeBuilderLogger extends SimpleBuildWrapper {
  @DataBoundConstructor
  public CodeBuilderLogger() {
    super();
  }

  @Extension
  public static class DescriptorImpl extends BuildWrapperDescriptor {
    @Override
    public String getDisplayName() {
      return Messages.loggerName();
    }

    @Override
    public boolean isApplicable(final AbstractProject<?, ?> item) {
      return true;
    }
  }

  @Override
  public void setUp(Context context, Run<?, ?> build, FilePath workspace, Launcher launcher, TaskListener listener,
      EnvVars initialEnvironment) throws IOException, InterruptedException {
    Computer cpu = Arrays.asList(Jenkins.getInstance().getComputers()).stream()
        .filter(c -> c.getChannel() == launcher.getChannel()).findFirst().get();
    if (cpu instanceof CodeBuilderComputer) {
      CodeBuilderComputer cbCpu = (CodeBuilderComputer) cpu;
      listener.getLogger().print("[CodeBuilder]: " + Messages.loggerStarted() + ": ");
      listener.hyperlink(cbCpu.getBuildUrl(), cbCpu.getBuildId());
      listener.getLogger().println();
    }
  }
}
