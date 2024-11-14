package objects3D;

import static org.lwjgl.opengl.GL11.*;
import GraphicsObjects.Point4f;
import GraphicsObjects.Vector4f;
import java.math.*;

public class Cylinder {

	
	public Cylinder() { 
	}
	
	// remember to use Math.PI isntead PI 
	// Implement using notes and examine Tetrahedron to aid in the coding  look at lecture  7 , 7b and 8 
	public void drawCylinder(float radius, float height, int nSegments) {
		float x, y, z;
		float angle;
		
		// Draw the tube
		glBegin(GL_QUAD_STRIP);
		for (int i = 0; i <= nSegments; i++) {
			angle = (float)(2.0 * Math.PI * i / nSegments);
			x = (float)Math.cos(angle) * radius;
			y = (float)Math.sin(angle) * radius;
			
			// Bottom vertex
			glNormal3f(x/radius, y/radius, 0.0f);
			glVertex3f(x, y, 0.0f);
			// Top vertex
			glVertex3f(x, y, height);
		}
		glEnd();
		
		// Draw top and bottom circles
		for(int j = 0; j < 2; j++) {
			glBegin(GL_TRIANGLE_FAN);
			z = j * height;
			glNormal3f(0.0f, 0.0f, j == 0 ? -1.0f : 1.0f);
			glVertex3f(0.0f, 0.0f, z); // Center point
		 
			for (int i = 0; i <= nSegments; i++) {
				angle = (float)(2.0 * Math.PI * i / nSegments);
				x = (float)Math.cos(angle) * radius;
				y = (float)Math.sin(angle) * radius;
				glVertex3f(x, y, z);
			}
			glEnd();
		}
	}
}
