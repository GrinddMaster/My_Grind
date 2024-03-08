// C program to demonstrate 
// drawing a circle using 
// OpenGL 
#include<stdio.h> 
#include<GL/glut.h> 
#include<math.h> 
#include<string.h>
#define pi 3.142857 

// function to initializ
int phyWidth= 600;
int phyHeight= 600;
int logWidth=300;
int logHeight=300;
int centerX=logWidth/2;
int centerY=logHeight/2;

void DrawCircle(float cx, float cy, float r, int num_segments) {
glBegin(GL_POLYGON);
for (int i = 0; i < num_segments; i++) {
float theta = 2.0f * 3.1415926f * float(i) / float(num_segments);//get the current angle
float x = r * cosf(theta);//calculate the x component
float y = r * sinf(theta);//calculate the y component
glVertex2f(x + cx, y + cy);//output vertex
}
glEnd();
}

void printSome(char *str,int x,int y) {
	
glColor3f (1, 1.0, 1.0);
glRasterPos2d(x,y);
for (int i=0;i<strlen(str);i++){glutBitmapCharacter(GLUT_BITMAP_HELVETICA_12,str[i]);}
glFlush();
}


void init2D (void) 
{ 
	// making background color black as first 
	// 3 arguments all are 0.0 
	glClearColor(0, 173, 150, 0.4); 
	
	// making picture color green (in RGB mode), as middle argument is 1.0 
	glColor3f(0.0, 1.0, 0.0); 
	
	// breadth of picture boundary is 1 pixel 
	glPointSize(2.0); 
	glMatrixMode(GL_PROJECTION); 
	glLoadIdentity(); 
	
	// setting window dimension in X- and Y- direction 
	gluOrtho2D(-780, 780, -420, 420); 
} 

void Display (void) 
{ glClear(GL_COLOR_BUFFER_BIT);
glColor3f(1.0, 1.0, 1.0);
DrawCircle(-450,300,60,1000);
DrawCircle(-400,330,60,1000);
DrawCircle(-350,290,60,1000);	

DrawCircle(450,330,60,1000);
DrawCircle(400,350,60,1000);
DrawCircle(350,330,60,1000);	


glBegin(GL_QUADS);
glColor3f(0, 0, 0);
glVertex2i(400,-400);
glVertex2i(-400,-400);
glVertex2i(-100,50);
glVertex2i(100,50);
glEnd();

glBegin(GL_POLYGON);
glColor3f(1, 1, 0);
glVertex2i(-800,-400);
glVertex2i(-350,-400);
glVertex2i(-100,50);
glVertex2i(-800,50);
glEnd();

glBegin(GL_POLYGON);
glColor3f(1, 1, 0);
glVertex2i(800,-400);
glVertex2i(350,-400);
glVertex2i(100,50);
glVertex2i(800,50);
glEnd();

glBegin(GL_QUADS);
glColor3f(1, 1, 1);
glVertex2i(25,-400);
glVertex2i(-25,-400);
glVertex2i(-25,-300);
glVertex2i(25,-300);
glEnd();

glBegin(GL_QUADS);
glColor3f(1, 1, 1);
glVertex2i(25,-250);
glVertex2i(-25,-250);
glVertex2i(-25,-150);
glVertex2i(25,-150);
glEnd();

glBegin(GL_QUADS);
glColor3f(0.647, 0.165, 0.165);
glVertex2i(25,-100);
glVertex2i(-25,-100);
glVertex2i(-25,150);
glVertex2i(25,150);
glEnd();
glColor3f(1, 1, 1);
DrawCircle(0,150,90,6);
glColor3f(1, 0, 0);
DrawCircle(0,150,85,6);

printSome("Stop",-30,150);


glutSwapBuffers();
glFlush();


} 
//g++ *filename.cpp* -o *FolderName* -lglut -lGLU -lGL


int main (int argc, char** argv) 
{ 
	glutInit( &argc, argv);
glutInitDisplayMode( GLUT_DOUBLE | GLUT_RGB);
glutInitWindowPosition( 300, 300);
glutInitWindowSize(phyWidth, phyHeight);
glutCreateWindow( "Assignment1");
init2D();
glutDisplayFunc(Display);
glutMainLoop();
} 
