# CMPSC311_project
CMPSC 311 Final Project: Chat Application with Client and Server Components

Run Requirements
- Linux OS
- Java version 11 

Run Instructions:
0. Download tcp_server.c, clientGUI.java, and GetName.java in the same file directory. 
1. In the terminal, compile and run tcp_server.c code. 
```
gcc tcp_server.c -lpthread -o server 
sudo ./server
 ```
2. In another terminal page, compile clientGUI.java and GetName.java and run clientGUI.java:
```
javac clientGUI.java GetName.java
java clientGUI
```
3. Enter a valid name and choose 1 of 3 themes: 

