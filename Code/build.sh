rm -rf ./BUILD
mkdir BUILD && cd Launcher  && javac *.java && jar -0 --create --file ./../BUILD/Launcher.jar --main-class=Launcher *.class && rm -rf ./*.class && cd ./../SubLauncher && javac *.java && jar -0 --create --file ./../BUILD/SubLauncher.jar --main-class=sub *.class && rm -rf ./*.class
