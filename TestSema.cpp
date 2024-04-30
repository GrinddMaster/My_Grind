#include <stdlib.h>
#include <stdio.h>
#include <pthread.h>
#include <semaphore.h>
#include <unistd.h>
#include  <iostream>

#define Customers 5
int count = 0;

sem_t Room,Fork;

void eat(int a)
{printf("Coustomer: %d Is Eating\n",a);}
void Think()
{sleep(1);}


void* routine(void* arg)
{
    Think();
    sem_wait(&Room);
    sem_wait(&Fork);
    sem_wait(&Fork);
    eat(*(int*)arg);
    sem_post(&Room);
    sem_post(&Fork);
    sem_post(&Fork);
    printf("Coustomer: %d Burps and leaves the room\n",*(int*)arg);

    free(arg);
    return nullptr;
}





int main(int argc, char* argv[])
{

    sem_init(&Room,4,4);
    sem_init(&Fork,0,5);

  pthread_t p[Customers];
    
for(int i=0;i<Customers;i++)
{
    int* a = new int;
    *a = i;
    pthread_create(&p[i],NULL,&routine,a);
}

 for(int i=0;i<Customers;i++)
    {
        pthread_join(p[i],NULL);
    }
    sem_destroy(&Room);
    sem_destroy(&Fork);

return 0;
}
