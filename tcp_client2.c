#include <arpa/inet.h>
#include <netinet/tcp.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <netdb.h>
#include <fcntl.h>
#include <pthread.h>

#define SERV_TCP_PORT 18 //Server's Port Number
#define MAX_SIZE 1024

void loadPreviousPosts(int sockfd) {
    /*
    int fd;
    char buffer[MAX_SIZE];
    ssize_t bytesRead;

    fd = open("Server_messages.log", O_RDONLY);

    if (fd < 0) {
        close(fd);
    }

    // Read and send previous posts
    while ((bytesRead = read(fd, buffer, sizeof(buffer))) > 0) {
        printf("%s", buffer);
    }

    close(fd);
    */

}

void *postPost(void *arg) {
    char input[1024];
    char user_input[1024];
    int sockfd = *((int *) arg);
    while(1) {

        //Write Message to the Server
        printf("Enter a message: ");
        fgets(input, sizeof(input), stdin);

        //Appends Username (Can be edited later for more dynamic Username)
        strcpy(user_input, "User2: ");
        strcat(user_input, input);


        write(sockfd, user_input, strlen(user_input));

    }
}

void *readPosts(void *arg) {
    int sockfd = *((int *) arg);
    while (1) {
        char receivedString[MAX_SIZE];
        int len = read(sockfd, receivedString, MAX_SIZE);

        if(len <= 0)
            break;

        receivedString[len] = 0;

        printf("%s\n", receivedString);
    }
}

int main(int argc, char *argv[]) {

    int sockfd;
    struct sockaddr_in serv_addr;
    char *serv_host = "localhost";
    struct hostent *host_ptr;
    int port;
    int len;

    //Command line: host [host port]
    if(argc >= 2) {

        serv_host = argv[1]; //Read the host port if provided

    }
    if(argc == 3) {

        sscanf(argv[2], "%d", &port);

    }
    else {

        port = SERV_TCP_PORT;

    }

    //get the address of the host
    if((host_ptr = gethostbyname(serv_host)) == NULL) {

        perror("gethostbyname error");
        exit(1);

    }
    if(host_ptr -> h_addrtype != AF_INET) {

        perror("unknown address type");
        exit(1);

    }

    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) {
        perror("Socket Error: Can't open stream socket");
        exit(1);
    }

    //Bind Local Address so that the client can send to server
    bzero((char *) &serv_addr, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = ((struct in_addr *)host_ptr -> h_addr_list[0]) -> s_addr;
    serv_addr.sin_port = htons(port);

    if(connect(sockfd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0) {

        perror("Cant connect to the server");
        exit(1);

    }

    pthread_t postThread;
    if(pthread_create(&postThread, NULL, postPost, (void *)&sockfd) != 0) {

            perror("Error Creating Thread");
            close(sockfd);
            return -1;
    }

    pthread_t readThread;
    if(pthread_create(&readThread, NULL, readPosts, (void *)&sockfd) != 0) {

            perror("Error Creating Thread");
            close(sockfd);
            return -1;
    }

    while(1);

    close(sockfd);

}
