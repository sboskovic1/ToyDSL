clear
javac -d bin/ -cp "lib/*" src/Examples.java src/dsl/*.java src/test/*.java src/utils/*.java src/utils/functions/*.java
java -cp "bin:lib/junit-4.13.1.jar:lib/hamcrest-all-1.3.jar" org.junit.runner.JUnitCore test.UTestDSL