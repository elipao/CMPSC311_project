#include <arpa/inet.h>
#include <netinet/tcp.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <pthread.h>
#include <sys/stat.h>

#define SERV_TCP_PORT 18 //Server's Port Number
#define MAX_SIZE 1024
int newsockfdList[5];


void writeToClient(char message[MAX_SIZE]) {
    for (int i = 0; i < sizeof(newsockfdList) / 4; i++) {
        //printf("size: (%d)\ni: (%d)\nnewsockfd: (%d)\n", sizeof(newsockfdList), i, newsockfdList[i]);
        if (newsockfdList[i] != 0) {
            write(newsockfdList[i], message, strlen(message));
        }
    }
}

void *new_client(void *arg) {

    int newsockfd = *((int *)arg);
    char string[MAX_SIZE];
    char buffer[MAX_SIZE];
    ssize_t bytesRead;
    int fd;
    fd = open("Server_messages.log", O_RDWR | O_CREAT | O_APPEND, 0777);

    int i = 0;

    while ((bytesRead = read(fd, buffer, sizeof(buffer))) > 0) {
        i++;
        write(newsockfd, buffer, sizeof(buffer));
    }
    printf("Entering for loop\n");
    for(;;) {
        //Read message from the client
        int len = read(newsockfd, string, MAX_SIZE);

        if(len <= 0)
            break;

        string[len] = 0;

        // Add a newline character to the received string
        strcat(string, "\n");
        // Write the received string to the log file
        write(fd, string, strlen(string));

        printf("%s\n", string);

        writeToClient(string);

    }

    close(newsockfd);
    pthread_exit(NULL);

}



int main(int argc, char *argv[]) {

    int sockfd, newsockfd, clilen;
    int clientsConnected = 0;
    struct sockaddr_in serv_addr, cli_addr;
    int port;
    char string[MAX_SIZE];


    //Command line: Server [port_number]
    if(argc == 2) {

        //Read the port number if provided
        sscanf(argv[1], "%d", &port);

    }
    else {

        port = SERV_TCP_PORT;

    }

    //Open a TCP socket (an Internet Stream Socket)
    if((sockfd = socket(AF_INET, SOCK_STREAM, 0)) < 0) {

        perror("Can't open stream socket");
        exit(1);

    }

    //Bind Local Address so that the client can send to server
    bzero((char *) &serv_addr, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);
    serv_addr.sin_port = htons(port);

    if(bind(sockfd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0) {

        perror("Can't bind local address");
        exit(1);

    }

    //Listen to the sockets
    listen(sockfd, 5);

    for(;;) {

        //wait for connection from a client; this is an iterative server
        clilen = sizeof(cli_addr);
        newsockfd = accept(sockfd, (struct sockaddr *) &cli_addr, &clilen);
        printf("newsockfd: (%d)\n", newsockfd);
        if(newsockfd < 0) {

            perror("Can't bind local address");

            //Allows Server to Keep Listening instead of Terminating
            continue;

        }
        newsockfdList[clientsConnected] = newsockfd;
        clientsConnected++;
        pthread_t new_connection;
        if(pthread_create(&new_connection, NULL, new_client, (void *)&newsockfd) != 0) {

            perror("Error Creating Thread");
            close(newsockfd);

        }


    }

    close(sockfd);

}
