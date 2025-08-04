# SoH7Test

## Compile
cd into Client/Server

Compile each class and create jar file
'
javac *.java & jar cfm server.jar *.txt *.class
javac *.java & jar cfm client.jar *.txt *.class
'

Then run jar files in terminal
'
java -jar server.jar
java -jar client.jar
'
## Kill process on port:
netstat -ano | findstr : [PID]12345

tasklist /FI "PID eq [PID]"
