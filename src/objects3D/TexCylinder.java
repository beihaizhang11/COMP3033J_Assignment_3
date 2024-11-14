package objects3D;

import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.opengl.Texture;

public class TexCylinder {
    
    public TexCylinder() {
    }
    
    public void drawTexCylinder(float radius, float height, int nSegments, Texture texture) {
        float x, y, z;
        float s, t;
        float angle;
        
        // 绘制圆柱体侧面
        glBegin(GL_QUAD_STRIP);
        for (int i = 0; i <= nSegments; i++) {
            angle = (float)(2.0 * Math.PI * i / nSegments);
            x = (float)Math.cos(angle) * radius;
            y = (float)Math.sin(angle) * radius;
            s = (float)i / nSegments;
            
            // 底部顶点
            glNormal3f(x/radius, y/radius, 0.0f);
            glTexCoord2f(s, 0.0f);
            glVertex3f(x, y, 0.0f);
            
            // 顶部顶点
            glTexCoord2f(s, 1.0f);
            glVertex3f(x, y, height);
        }
        glEnd();
        
        // 绘制顶部和底部圆
        for(int j = 0; j < 2; j++) {
            glBegin(GL_TRIANGLE_FAN);
            z = j * height;
            glNormal3f(0.0f, 0.0f, j == 0 ? -1.0f : 1.0f);
            glTexCoord2f(0.5f, 0.5f);
            glVertex3f(0.0f, 0.0f, z); // 中心点
            
            for (int i = 0; i <= nSegments; i++) {
                angle = (float)(2.0 * Math.PI * i / nSegments);
                x = (float)Math.cos(angle) * radius;
                y = (float)Math.sin(angle) * radius;
                s = 0.5f + 0.5f * (float)Math.cos(angle);
                t = 0.5f + 0.5f * (float)Math.sin(angle);
                glTexCoord2f(s, t);
                glVertex3f(x, y, z);
            }
            glEnd();
        }
    }
} 