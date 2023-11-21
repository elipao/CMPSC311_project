#include <arpa/inet.h>
#include <netinet/tcp.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#define SERV_TCP_FORT 18 //Server's Port Number
#define MAX_SIZE 80

int main(int argc, char *argv[]) {

    int sockfd, newsockfd, clilen;
    struct sockaddr_in serv_addr, cli_addr;
    int port;
    char string[MAX_SIZE];
    int buff_size = 0;


    //Command line: Server [port_number]
    if(argc == 2) {

        //Read the port number if provided
        sscanf(argv[1], "%d", &port);

    }
    else {

        port = SERV_TCP_FORT;

    }

    //Open a TCP socket (an Internet Stream Socket)
    if((sockfd = socket(AF_INET, SOCK_STREAM, 0)) < 0) {

        perror("Can't open stream socket");
        exit(1);

    }

    //Bind Local Address so tht the client can send to server
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

        if(newsockfd < 0) {

            perror("Can't bind local address");

        }

        //Read message from the client
        int len = read(newsockfd, string, MAX_SIZE);
        //Make sure its proper string
        string[len] = 0;
        printf("%s\n", string);

        close(newsockfd);

    }

}
