package dev.lsegal.jenkins.codebuilder;

import java.io.IOException;
import java.util.Arrays;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Computer;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildWrapperDescriptor;
import jenkins.tasks.SimpleBuildWrapper;

/**
 * CodeBuilderLogger class.
 *
 * @author Loren Segal
 */
public final class CodeBuilderLogger extends SimpleBuildWrapper {
  /**
   * Constructor for CodeBuilderLogger.
   */
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

  /** {@inheritDoc} */
  @Override
  public void setUp(Context context, Run<?, ?> build, FilePath workspace, Launcher launcher, TaskListener listener,
      EnvVars initialEnvironment) throws IOException, InterruptedException {
    Computer cpu = Arrays.asList(CodeBuilderCloud.jenkins().getComputers()).stream()
        .filter(c -> c.getChannel() == launcher.getChannel()).findFirst().get();
    if (cpu instanceof CodeBuilderComputer) {
      CodeBuilderComputer cbCpu = (CodeBuilderComputer) cpu;
      listener.getLogger().print("[CodeBuilder]: " + Messages.loggerStarted() + ": ");
      listener.hyperlink(cbCpu.getBuildUrl(), cbCpu.getBuildId());
      listener.getLogger().println();
    }
  }
}
