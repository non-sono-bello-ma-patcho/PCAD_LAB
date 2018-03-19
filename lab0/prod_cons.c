/*
Producer Consumer exercise
*/

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>

#define BUFFER_SIZE 16

// Defining monitor structure
typedef struct{
	int buffer[BUFFER_SIZE]; // buffer is the main shared variable;
	pthread_mutex_t buffer_lock; // init mutex;
	int read_pos, write_pos; // first occupied place of buffer, first free place in buffer;
	int cont; // items in buffer, i suggest;
	pthread_cond_t notempty; // condition variable for consumer;
	pthread_cond_t notfull; // condition variable for producer;
} monitor;

monitor smonitor;

/*
	Operations defined on monitor:
		* init: initialize monitor;
		* insert: insert item in buffer;
		* extract: consume buffer;
*/


void init_buffer(monitor* m){
	pthread_mutex_init(&m->buffer_lock, NULL); // init lock on buffer;
	pthread_cond_init(&m->notempty, NULL);
	pthread_cond_init(&m->notfull, NULL);
	m->cont = 0;
	m->read_pos = 0;
	m->write_pos = 0;
}

void insert(monitor* m, int data){
	// gain lock:
	pthread_mutex_lock(&m->buffer_lock);

	// wait for buffer to be not full:
	while(m->cont == BUFFER_SIZE) pthread_cond_wait(&m->notfull, &m->buffer_lock);

	// write data on buffer and update monitor status:
	m->buffer[m->write_pos] = data;
	m->cont++;
	m->write_pos++;
	// if buffer is full move its free place pointer to its begin:
	if(m->write_pos==BUFFER_SIZE) m->write_pos = 0;
	// awake suspended threads:
	pthread_cond_signal(&m->notempty);
	pthread_mutex_unlock(&m->buffer_lock);
}

// returns consumed data:
int extract(monitor* m){
	int data;
	pthread_mutex_lock(&m->buffer_lock); // gainlock on resource;
	// while empty wait for signal from producer:
	while(m->cont==0) pthread_cond_wait(&m->notempty, &m->buffer_lock);
	data = m->buffer[m->read_pos]; // obtain data;
	m->cont--;
	m->read_pos++;
	if(m->read_pos>=BUFFER_SIZE) m->read_pos = 0;
	pthread_cond_signal(&m->notfull);
	pthread_mutex_unlock(&m->buffer_lock); // release lock;
	return data;
}

void* producer(){
	int n;
	puts("I'm a producer thread:");
	for(n=0; n<20; n++){
		printf("producer thread %d -->\n", n);
		insert(&smonitor, n);
	}
	insert(&smonitor, -1);
}

void* consumer(void* data){
	int d;
	printf("I'm consumer thread\n");
	while (1){
		d = extract(&smonitor);
		if (d == -1)
		break;
		printf("consumer thread: --> %d\n",  d);
	}
}

int main(int argc, char* argv[]){
	pthread_t consumer_thread, producer_thread;
	void* retval;

	init_buffer(&smonitor);

	// create threads:
	if(pthread_create(&producer_thread, NULL, producer, 0)<0) exit(EXIT_FAILURE);
	if(pthread_create(&consumer_thread, NULL, consumer, 0)<0) exit(EXIT_FAILURE);
	
	// wait for temination:
	pthread_join(producer_thread, &retval);
	pthread_join(consumer_thread, &retval);
	return 0;
}