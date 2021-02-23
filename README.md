This is a sample app to test out the [Robolectric 4.0 automatic migration tool](http://robolectric.org/automated-migration/).



The documentation instructs us to add the following config to a gradle file:

```
plugins {
    id "net.ltgt.errorprone" version "0.6" apply false
}

String roboMigration = System.getenv("ROBOLECTRIC_MIGRATION")
if (roboMigration) {
    apply plugin: "net.ltgt.errorprone"

    dependencies {
        errorprone "com.google.errorprone:error_prone_core:2.3.1"
        errorproneJavac "com.google.errorprone:javac:9+181-r4173-1"

        errorprone "org.robolectric:errorprone:4.0"
    }

    afterEvaluate {
        tasks.withType(JavaCompile) { t ->
            options.errorprone.errorproneArgs += [
                    '-XepPatchChecks:' + roboMigration,
                    '-XepPatchLocation:IN_PLACE',
            ]
        }
    }
}
```

However, it doesn't indicate to which gradle file this should be added (root `build.gradle`? app `build.gradle`?).

The documentation then instructs to execute some commands:
```
ROBOLECTRIC_MIGRATION=DeprecatedMethods ./gradlew clean :compileDebugUnitTestJava
ROBOLECTRIC_MIGRATION=ShadowUsageCheck ./gradlew clean :compileDebugUnitTestJava
```

The documentation says that these commands will make modifications to the source code:

> The migration tool will make changes directly to source files in your codebase, which you can review and commit to your source control system.


With this sample app, the tool doesn't work as described.

If the above snippet is added to the **root** `build.gradle` file, the above commands fail, as the root gradle file doesn't have the java compilation task:

```
ROBOLECTRIC_MIGRATION=DeprecatedMethods ./gradlew clean :compileDebugUnitTestJava

FAILURE: Build failed with an exception.

* What went wrong:
Task 'compileDebugUnitTestJava' not found in root project 'RobolectricMigration'.
```

If we keep the snippet in the **root** gradle file, but specify the task to run on the **app** subproject,
it completes but does nothing (no output or code changes regarding the deprecated code in our unit test):
```
ROBOLECTRIC_MIGRATION=DeprecatedMethods ./gradlew clean :app:compileDebugUnitTestJava

BUILD SUCCESSFUL in 4s
19 actionable tasks: 18 executed, 1 up-to-date
```


If we move the snippet to the **app** gradle file, there are warnings output to the console about the deprecated code,
but the tool ends with a stacktrace and doesn't modify any of the code:
```
ROBOLECTRIC_MIGRATION=DeprecatedMethods ./gradlew clean :app:compileDebugUnitTestJava

> Task :app:compileDebugJavaWithJavac
Refactoring changes were successfully applied to file:///Users/calvarez/dev/projects/RobolectricMigration/app/build/generated/not_namespaced_r_class_sources/debug/processDebugResources/r/androidx/versionedparcelable/R.java, please check the refactored code and recompile.
Refactoring changes were successfully applied to file:///Users/calvarez/dev/projects/RobolectricMigration/app/build/generated/not_namespaced_r_class_sources/debug/processDebugResources/r/example/com/robolectricmigration/R.java, please check the refactored code and recompile.
Refactoring changes were successfully applied to file:///Users/calvarez/dev/projects/RobolectricMigration/app/build/generated/not_namespaced_r_class_sources/debug/processDebugResources/r/android/support/coreutils/R.java, please check the refactored code and recompile.
Refactoring changes were successfully applied to file:///Users/calvarez/dev/projects/RobolectricMigration/app/build/generated/not_namespaced_r_class_sources/debug/processDebugResources/r/android/support/asynclayoutinflater/R.java, please check the refactored code and recompile.
Refactoring changes were successfully applied to file:///Users/calvarez/dev/projects/RobolectricMigration/app/build/generated/not_namespaced_r_class_sources/debug/processDebugResources/r/android/support/v7/viewpager/R.java, please check the refactored code and recompile.
Refactoring changes were successfully applied to file:///Users/calvarez/dev/projects/RobolectricMigration/app/build/generated/not_namespaced_r_class_sources/debug/processDebugResources/r/android/support/v7/appcompat/R.java, please check the refactored code and recompile.
Refactoring changes were successfully applied to file:///Users/calvarez/dev/projects/RobolectricMigration/app/build/generated/not_namespaced_r_class_sources/debug/processDebugResources/r/android/support/compat/R.java, please check the refactored code and recompile.
Refactoring changes were successfully applied to file:///Users/calvarez/dev/projects/RobolectricMigration/app/build/generated/not_namespaced_r_class_sources/debug/processDebugResources/r/android/support/drawerlayout/R.java, please check the refactored code and recompile.
Refactoring changes were successfully applied to file:///Users/calvarez/dev/projects/RobolectricMigration/app/build/generated/not_namespaced_r_class_sources/debug/processDebugResources/r/android/support/interpolator/R.java, please check the refactored code and recompile.
Refactoring changes were successfully applied to file:///Users/calvarez/dev/projects/RobolectricMigration/app/build/generated/not_namespaced_r_class_sources/debug/processDebugResources/r/android/support/coreui/R.java, please check the refactored code and recompile.
Refactoring changes were successfully applied to file:///Users/calvarez/dev/projects/RobolectricMigration/app/build/generated/not_namespaced_r_class_sources/debug/processDebugResources/r/android/support/documentfile/R.java, please check the refactored code and recompile.
Refactoring changes were successfully applied to file:///Users/calvarez/dev/projects/RobolectricMigration/app/build/generated/not_namespaced_r_class_sources/debug/processDebugResources/r/android/support/print/R.java, please check the refactored code and recompile.
Refactoring changes were successfully applied to file:///Users/calvarez/dev/projects/RobolectricMigration/app/build/generated/not_namespaced_r_class_sources/debug/processDebugResources/r/android/support/localbroadcastmanager/R.java, please check the refactored code and recompile.
Refactoring changes were successfully applied to file:///Users/calvarez/dev/projects/RobolectricMigration/app/build/generated/not_namespaced_r_class_sources/debug/processDebugResources/r/android/support/cursoradapter/R.java, please check the refactored code and recompile.
Refactoring changes were successfully applied to file:///Users/calvarez/dev/projects/RobolectricMigration/app/build/generated/not_namespaced_r_class_sources/debug/processDebugResources/r/android/support/swiperefreshlayout/R.java, please check the refactored code and recompile.
Refactoring changes were successfully applied to file:///Users/calvarez/dev/projects/RobolectricMigration/app/build/generated/not_namespaced_r_class_sources/debug/processDebugResources/r/android/support/fragment/R.java, please check the refactored code and recompile.
Refactoring changes were successfully applied to file:///Users/calvarez/dev/projects/RobolectricMigration/app/build/generated/not_namespaced_r_class_sources/debug/processDebugResources/r/android/support/loader/R.java, please check the refactored code and recompile.
Refactoring changes were successfully applied to file:///Users/calvarez/dev/projects/RobolectricMigration/app/build/generated/not_namespaced_r_class_sources/debug/processDebugResources/r/android/support/customview/R.java, please check the refactored code and recompile.
Refactoring changes were successfully applied to file:///Users/calvarez/dev/projects/RobolectricMigration/app/build/generated/not_namespaced_r_class_sources/debug/processDebugResources/r/android/support/slidingpanelayout/R.java, please check the refactored code and recompile.
Refactoring changes were successfully applied to file:///Users/calvarez/dev/projects/RobolectricMigration/app/build/generated/not_namespaced_r_class_sources/debug/processDebugResources/r/android/support/graphics/drawable/R.java, please check the refactored code and recompile.
Refactoring changes were successfully applied to file:///Users/calvarez/dev/projects/RobolectricMigration/app/build/generated/not_namespaced_r_class_sources/debug/processDebugResources/r/android/support/coordinatorlayout/R.java, please check the refactored code and recompile.
Refactoring changes were successfully applied to file:///Users/calvarez/dev/projects/RobolectricMigration/app/build/generated/not_namespaced_r_class_sources/debug/processDebugResources/r/android/arch/core/R.java, please check the refactored code and recompile.
Refactoring changes were successfully applied to file:///Users/calvarez/dev/projects/RobolectricMigration/app/build/generated/not_namespaced_r_class_sources/debug/processDebugResources/r/android/arch/lifecycle/viewmodel/R.java, please check the refactored code and recompile.
Refactoring changes were successfully applied to file:///Users/calvarez/dev/projects/RobolectricMigration/app/build/generated/not_namespaced_r_class_sources/debug/processDebugResources/r/android/arch/lifecycle/livedata/core/R.java, please check the refactored code and recompile.
Refactoring changes were successfully applied to file:///Users/calvarez/dev/projects/RobolectricMigration/app/build/generated/not_namespaced_r_class_sources/debug/processDebugResources/r/android/arch/lifecycle/livedata/R.java, please check the refactored code and recompile.
Refactoring changes were successfully applied to file:///Users/calvarez/dev/projects/RobolectricMigration/app/build/generated/not_namespaced_r_class_sources/debug/processDebugResources/r/android/arch/lifecycle/R.java, please check the refactored code and recompile.
Refactoring changes were successfully applied to file:///Users/calvarez/dev/projects/RobolectricMigration/app/build/generated/source/buildConfig/debug/example/com/robolectricmigration/BuildConfig.java, please check the refactored code and recompile.

> Task :app:compileDebugUnitTestJavaWithJavac FAILED
/Users/calvarez/dev/projects/RobolectricMigration/app/src/test/java/example/com/robolectricmigration/ExampleUnitTest.java:13: warning: [DeprecatedMethods] Robolectric shadows shouldn't be stored to variables or fields.
public class ExampleUnitTest {
       ^
    (see http://errorprone.info/bugpattern/DeprecatedMethods)
  Did you mean 'Intent nextActivity = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();'?
1 warning
An exception has occurred in the compiler ((version info not available)). Please file a bug against the Java compiler via the Java bug reporting page (http://bugreport.java.com) after checking the Bug Database (http://bugs.java.com) for duplicates. Include your program and the following diagnostic in your report. Thank you.
java.lang.AssertionError: unexpected type: (android.app.Application)org.robolectric.shadows.ShadowApplication
        at com.sun.tools.javac.tree.TreeMaker.Type(TreeMaker.java:787)
        at com.sun.tools.javac.comp.TransTypes.cast(TransTypes.java:121)
        at com.sun.tools.javac.comp.TransTypes.coerce(TransTypes.java:148)
        at com.sun.tools.javac.comp.TransTypes.retype(TransTypes.java:186)
        at com.sun.tools.javac.comp.TransTypes.visitApply(TransTypes.java:728)
        at com.sun.tools.javac.tree.JCTree$JCMethodInvocation.accept(JCTree.java:1628)
        at com.sun.tools.javac.tree.TreeTranslator.translate(TreeTranslator.java:58)
        at com.sun.tools.javac.comp.TransTypes.translate(TransTypes.java:532)
        at com.sun.tools.javac.comp.TransTypes.visitSelect(TransTypes.java:883)
        at com.sun.tools.javac.tree.JCTree$JCFieldAccess.accept(JCTree.java:2104)
        at com.sun.tools.javac.tree.TreeTranslator.translate(TreeTranslator.java:58)
        at com.sun.tools.javac.comp.TransTypes.translate(TransTypes.java:532)
        at com.sun.tools.javac.comp.TransTypes.visitApply(TransTypes.java:706)
        at com.sun.tools.javac.tree.JCTree$JCMethodInvocation.accept(JCTree.java:1628)
        at com.sun.tools.javac.tree.TreeTranslator.translate(TreeTranslator.java:58)
        at com.sun.tools.javac.comp.TransTypes.translate(TransTypes.java:532)
        at com.sun.tools.javac.comp.TransTypes.visitVarDef(TransTypes.java:588)
        at com.sun.tools.javac.tree.JCTree$JCVariableDecl.accept(JCTree.java:950)
        at com.sun.tools.javac.tree.TreeTranslator.translate(TreeTranslator.java:58)
        at com.sun.tools.javac.tree.TreeTranslator.translate(TreeTranslator.java:70)
        at com.sun.tools.javac.tree.TreeTranslator.visitBlock(TreeTranslator.java:167)
        at com.sun.tools.javac.tree.JCTree$JCBlock.accept(JCTree.java:1014)
        at com.sun.tools.javac.tree.TreeTranslator.translate(TreeTranslator.java:58)
        at com.sun.tools.javac.comp.TransTypes.translate(TransTypes.java:532)
        at com.sun.tools.javac.comp.TransTypes.visitMethodDef(TransTypes.java:567)
        at com.sun.tools.javac.tree.JCTree$JCMethodDecl.accept(JCTree.java:866)
        at com.sun.tools.javac.tree.TreeTranslator.translate(TreeTranslator.java:58)
        at com.sun.tools.javac.tree.TreeTranslator.translate(TreeTranslator.java:70)
        at com.sun.tools.javac.tree.TreeTranslator.visitClassDef(TreeTranslator.java:139)
        at com.sun.tools.javac.comp.TransTypes.translateClass(TransTypes.java:994)
        at com.sun.tools.javac.comp.TransTypes.visitClassDef(TransTypes.java:553)
        at com.sun.tools.javac.tree.JCTree$JCClassDecl.accept(JCTree.java:774)
        at com.sun.tools.javac.tree.TreeTranslator.translate(TreeTranslator.java:58)
        at com.sun.tools.javac.comp.TransTypes.translate(TransTypes.java:532)
        at com.sun.tools.javac.comp.TransTypes.translateTopLevelClass(TransTypes.java:1018)
        at com.sun.tools.javac.main.JavaCompiler.desugar(JavaCompiler.java:1545)
        at com.sun.tools.javac.main.JavaCompiler.desugar(JavaCompiler.java:1418)
        at com.sun.tools.javac.main.JavaCompiler.compile(JavaCompiler.java:946)
        at com.sun.tools.javac.api.JavacTaskImpl.lambda$doCall$0(JavacTaskImpl.java:100)
        at com.sun.tools.javac.api.JavacTaskImpl.handleExceptions(JavacTaskImpl.java:142)
        at com.sun.tools.javac.api.JavacTaskImpl.doCall(JavacTaskImpl.java:96)
        at com.sun.tools.javac.api.JavacTaskImpl.call(JavacTaskImpl.java:90)
        at org.gradle.api.internal.tasks.compile.IncrementalAnnotationProcessingCompileTask.call(IncrementalAnnotationProcessingCompileTask.java:73)
        at org.gradle.api.internal.tasks.compile.JdkJavaCompiler.execute(JdkJavaCompiler.java:50)
        at org.gradle.api.internal.tasks.compile.JdkJavaCompiler.execute(JdkJavaCompiler.java:37)
        at org.gradle.api.internal.tasks.compile.daemon.AbstractDaemonCompiler$CompilerRunnable.run(AbstractDaemonCompiler.java:87)
        at org.gradle.workers.internal.DefaultWorkerServer.execute(DefaultWorkerServer.java:36)
        at org.gradle.workers.internal.WorkerDaemonServer.execute(WorkerDaemonServer.java:46)
        at org.gradle.workers.internal.WorkerDaemonServer.execute(WorkerDaemonServer.java:30)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke(Method.java:498)
        at org.gradle.process.internal.worker.request.WorkerAction.run(WorkerAction.java:100)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke(Method.java:498)
        at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:35)
        at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:24)
        at org.gradle.internal.remote.internal.hub.MessageHubBackedObjectConnection$DispatchWrapper.dispatch(MessageHubBackedObjectConnection.java:146)
        at org.gradle.internal.remote.internal.hub.MessageHubBackedObjectConnection$DispatchWrapper.dispatch(MessageHubBackedObjectConnection.java:128)
        at org.gradle.internal.remote.internal.hub.MessageHub$Handler.run(MessageHub.java:404)
        at org.gradle.internal.concurrent.ExecutorPolicy$CatchAndRecordFailures.onExecute(ExecutorPolicy.java:63)
        at org.gradle.internal.concurrent.ManagedExecutorImpl$1.run(ManagedExecutorImpl.java:46)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at org.gradle.internal.concurrent.ThreadFactoryImpl$ManagedThreadRunnable.run(ThreadFactoryImpl.java:55)
        at java.lang.Thread.run(Thread.java:748)


FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':app:compileDebugUnitTestJavaWithJavac'.
> Compilation failed; see the compiler error output for details.
```

