package com.dch.app.analyst.util;

public class ForkJoinRunnerSingle implements ForkJoinRunner {

    @Override
    public void awaitJobsDone() {

    }

    @Override
    public void execute(Runnable command) {
        command.run();
    }
}
