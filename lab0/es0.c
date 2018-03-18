#include <stdlib.h>
#include <stdio.h>
#include <pthread.h>
#include <unistd.h>

int counter = 0;

long unsigned int fID(int i){
	//printf("This is the thread launched in cycle n: %i\n", i);
	int j = 0;
	for(; j<10; j++){
		counter++;
		sleep(1);
	}
	return pthread_self();
}

int main(int argc, char* argv[]){
	 if(argc<2){
	 	puts("usage: ./es0 <THREAD NUMBER>");
	 	exit(EXIT_FAILURE);
	 }
	 long unsigned int returnv;
	 // init thread array:
	 pthread_t* threads = (pthread_t*)malloc(sizeof(pthread_t)*atoi(argv[1]));
	 int i=0;
	 
	 //printf("Starting %i threads:\n", atoi(argv[1]));
	 for(;i<atoi(argv[1]); i++){
	 	if(pthread_create(&threads[i], NULL, (void*)*fID, (void*)i)<0){
	 		puts("Unable to create thread. Abort");
	 		exit(EXIT_FAILURE);
	 	}
	 }

	 i=0;

	 for(; i<atoi(argv[1]); i++){
	 	pthread_join(threads[i], (void*)&returnv);
	 	//printf("%lu just exited\n", returnv);
	 }

	 printf("Counter value is: %i\n", counter);

	 return 0;
}