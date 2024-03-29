#include<stdio.h>//In this program to find all tylers in multiple flights, the number of passenger (in text) on each flight MUST MATCH 
#include<string.h>// If it's a single flight you can add as many passengers (in text) as you like.
typedef struct users{
	int passprot;
	char name[50];
	int flight;
	int seat;
}us;
typedef struct date{
	
	int day,month,year;
}dt;
typedef struct flight{
	
	int flight_no;
 char departure[100]; 
 char destination[100]; 
 int ticket_price;
 dt date_flight;
	us usrs[100];
}fl;
//list of funcntions used.
void read(fl f[],int n);
void sort(fl f[],int n);
void search(fl f[],int n);
void display(fl f[],int n);
void scan_flight(fl f[],int n,FILE *fpt3);
int scan_users(fl f[],int m,int n,FILE *fpt);//This function returns int in order to make the copying from on text file to another in separate functions.
void print_user(fl f[],int m,int n,int c,FILE *fpt2);
void print_flight(fl f[],int n,FILE *fpt4);
void find_tyler(fl f[],int m,int n);

int main(){
	int n,m,x,c;
	FILE *fpt,*fpt2,*fpt3,*fpt4; // All the text file pointers.
	fpt = fopen("users.txt","r");
	fpt2 = fopen("users2.txt","w");
	fpt3 = fopen("flights.txt","r");
	fpt4 = fopen("flights2.txt","a");
	
	//You can ignore all this printfs
	printf("1. Scan N flgihts\n2.sort flights by flight no\n3.search for a flight by using flight departure\n4.Display the number of flights (count) that has a ticket price less than 100\n");
	printf("5.Scan N users with all their variables from a text\n6.Scan N flights with all their variables from a text file\n");
	printf("7.Print all the users that have even passport number from their array to the text file\n8.Print all flights from their array to a text file\n");
	printf("9.Print the departure and destination of all flights that have on board a passenger named Tyler\n");
	printf("Note: (to print anything on console you must start with option 1)\n");
	
	printf("Choose a number: \n"); // Steps from line 48 to 58 are for aesthetic reasons
	scanf("%d",&x);
	if(x==1){
	printf("Enter N number of flights: \n");
	scanf("%d",&n);
}
fl f[n];
if(x==1)
{
	read(f,n);
}
	while(x!=10){
		printf("Choose a number: \n");
		scanf("%d",&x);
		
		switch(x)
		{	
	case 2:sort(f,n);break;
	case 3:search(f,n);break;
	case 4:display(f,n);break;
	
	case 5:printf("enter N number of users: \n");
	scanf("%d",&m);
	c =scan_users(f,m,n,fpt);break;//As you can see when it scans the number of users it returns a count in variable c
	case 6:print_user(f,m,n,c,fpt2);break;// variable c is sent to "Print_user" in order to act as a counter control
	case 7:scan_flight(f,n,fpt3);break;
	case 8:print_flight(f,n,fpt4);break;
	case 9:find_tyler(f,m,n);break;
	default:printf("Give me 10 marks please :) ");
	}
}
	return 0;
}/*end main function*/

void read(fl f[],int n)// This function reads and displays on console
{
	int i;
	char c;
	printf("Enter flight data: (flight number, departure, destination ,ticket price & day,month,year) \n");
	for(i=0;i<n;i++)
	{
		scanf("%d%s%s%d%d%d%d",&f[i].flight_no,f[i].departure,f[i].destination,&f[i].ticket_price,&f[i].date_flight.day,&f[i].date_flight.month,&f[i].date_flight.year);
		scanf("%c",&c);
	}
}
void sort(fl f[],int n)// display on console
{
	int i,j,temp;
	printf("Sorted flight Number -> \n");
	for(i=0;i<n;i++)
	{
		for(j=0;j<n-1-i;j++)
		{
			if(f[i].flight_no > f[j+1].flight_no)
			{
				 temp = f[i].flight_no;
				f[i].flight_no = f[j+1].flight_no;
				f[j+1].flight_no = temp;
			}
		}
	}
	for(i=0;i<n;i++)
	{
		printf("%d ",f[i].flight_no);
	}
	puts("");
}
void search(fl f[],int n)//display on console
{
	char d[100],c;
	printf("Enter flight departure: ");
	scanf("%s",d);
	scanf("%c",&c);
	int i;
	for(i=0;i<n;i++)
	{
		if(strcmp(d,f[i].departure)==0)
		{
			printf("%d ",f[i].flight_no);
		}
	}
	puts("");
	
}
void display(fl f[],int n)//display on console
{
	int i,count=0;
	for(i=0;i<n;i++)
	{
		if(f[i].ticket_price<100)
		{
			count++;
		}
	}
	printf("The number of flights with ticket price less than 100: %d \n",count);
}
int scan_users(fl f[],int m,int n,FILE *fpt)//Scans from text
{
	int i,j,count=0;
	while(!feof(fpt))
	{
	for(i=0;i<n;i++)
	{
		for(j=0;j<m;j++)
		{
			fscanf(fpt,"%d%s%d%d",&f[i].usrs[j].passprot,f[i].usrs[j].name,&f[i].usrs[j].flight,&f[i].usrs[j].seat);
		}
	}
		
		count++;
	}
	fclose(fpt);
	return count;
}
void scan_flight(fl f[],int n,FILE *fpt3)//scans from text
{
	int i,j;
	while(!feof(fpt3))
	{
	
	for(i=0;i<=n;i++)
	{
		fscanf(fpt3,"%d%s%s%d%d%d%d",&f[i].flight_no,f[i].departure,f[i].destination,&f[i]. ticket_price,&f[i].date_flight.day,&f[i].date_flight.month,&f[i].date_flight.year);
	}
}
	fclose(fpt3);
	
}
void print_user(fl f[],int m,int n,int c,FILE *fpt2)//prints on text (users2)
{
	int i,j,count=0;
	
	while( count<c )//As you can see variable c acts a counter control. Thus Print_user and Scan_user are in separate functions and copying process is completed.
	{
		for(i=0;i<n;i++)
		{
			for(j=0;j<m;j++)
			{
				if(f[i].usrs[j].passprot%2==0)
				{
				
				fprintf(fpt2,"%d\n%s\n%d\n%d\n",f[i].usrs[j].passprot,f[i].usrs[j].name,f[i].usrs[j].flight,f[i].usrs[j].seat);
				}
			}
		}
		count++;
	}
fclose(fpt2);
}
void print_flight(fl f[],int n,FILE *fpt4)//appends to text (flights2)
{
	
	for(int i=0;i<n;i++)
	{
		fprintf(fpt4,"%d\n%s\n%s\n%d\n%d/%d/%d\n",f[i].flight_no,f[i].departure,f[i].destination,f[i]. ticket_price,f[i].date_flight.day,f[i].date_flight.month,f[i].date_flight.year);
	}
	fclose(fpt4);
	
}
void find_tyler(fl f[],int m,int n)// find the departure and destination of flights with a tyler on board
{
	
	for(int i=0;i<n;i++)
	{
		for(int j=0;j<m;j++)
		{
			if((strcmp(f[i].usrs[j].name,"tyler")==0) && (f[i].flight_no == f[i].usrs[j].flight))
			{
				printf("%d: %s,%s\n",f[i].flight_no,f[i].departure,f[i].destination);
		}
		
	}
	
	
}
}
