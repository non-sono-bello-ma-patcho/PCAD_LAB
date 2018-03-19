#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>

static int shared_var=0;
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

void asynch(){
	// something unuseful;
	// modify a global variable, so that the caller obtain a value before the function returns;
	int i = 0;
	for(;i<5; i++){
		pthread_mutex_lock(&mutex);
		shared_var = i;
		pthread_mutex_unlock(&mutex);
		sleep(5);
		puts("\t\e[94mDoing something unuseful, seriuosly...\e[0m");
	}
}

int get(){
	int r;
	pthread_mutex_lock(&mutex);
	r = shared_var;
	pthread_mutex_unlock(&mutex);
	return r;
}

int main(int argc, char* argv[]){
	pthread_t asynch_t;
	if(pthread_create(&asynch_t, NULL, (void*)asynch, NULL)<0) exit(EXIT_FAILURE); // create asynch's thread;
	while(1){
		sleep(7);
		int got_value = get();
		if(got_value==shared_var) printf("obtained value match shared_var: %i\n", got_value);
		else printf("obtained value doesn't match shared_var(%i,%i)\n", got_value, shared_var);
		printf("shared variable: %i\n", shared_var);
		if(got_value == 4) break;
	}
	pthread_join(asynch_t, NULL);
	return 0;
}
