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
<img width="211" alt="image" src="https://github.com/elipao/CMPSC311_project/assets/63485234/a2760ddb-793d-43f2-b347-fd850a4035c3">

Default Theme:

<img width="455" alt="image" src="https://github.com/elipao/CMPSC311_project/assets/63485234/6492a376-34a8-480d-a8b0-ef24f0841d21">

NightMode Theme: 

<img width="452" alt="image" src="https://github.com/elipao/CMPSC311_project/assets/63485234/53570c5a-55c4-4359-835f-f44f614957c7">

Blue Theme: 

<img width="450" alt="image" src="https://github.com/elipao/CMPSC311_project/assets/63485234/95af2752-079c-4d5f-9bab-7674e996bb77">

4. Open up to 5 clients using the running clientGUI.java. 



