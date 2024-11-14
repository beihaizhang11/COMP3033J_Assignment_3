package objects3D;

import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.opengl.Texture;

import GraphicsObjects.Point4f;
import GraphicsObjects.Vector4f;

public class TexCube {

	public TexCube() {

	}

	// Implement using notes and looking at TexSphere
	public void drawTexCube(Texture myTexture) {
		Point4f vertices[] = { 
			new Point4f(-1.0f, -1.0f, -1.0f, 0.0f), 
			new Point4f(-1.0f, -1.0f, 1.0f, 0.0f),
			new Point4f(-1.0f, 1.0f, -1.0f, 0.0f), 
			new Point4f(-1.0f, 1.0f, 1.0f, 0.0f),
			new Point4f(1.0f, -1.0f, -1.0f, 0.0f), 
			new Point4f(1.0f, -1.0f, 1.0f, 0.0f),
			new Point4f(1.0f, 1.0f, -1.0f, 0.0f), 
			new Point4f(1.0f, 1.0f, 1.0f, 0.0f) 
		};

		// 定义每个面的顶点顺序和纹理坐标
		int faces[][] = { 
			{0, 4, 5, 1}, // 前面
			{0, 2, 6, 4}, // 左面
			{0, 1, 3, 2}, // 底面
			{4, 6, 7, 5}, // 右面
			{1, 5, 7, 3}, // 后面
			{2, 3, 7, 6}  // 顶面
		};
		
		float texCoords[][] = {
			{0.0f, 0.0f}, {1.0f, 0.0f}, {1.0f, 1.0f}, {0.0f, 1.0f}
		};

		glBegin(GL_QUADS);
		
		for (int face = 0; face < 6; face++) {
			// 计算法向量
			Vector4f v = vertices[faces[face][1]].MinusPoint(vertices[faces[face][0]]);
			Vector4f w = vertices[faces[face][3]].MinusPoint(vertices[faces[face][0]]);
			Vector4f normal = v.cross(w).Normal();
			glNormal3f(normal.x, normal.y, normal.z);
			
			// 绘制带纹理的四边形
			for(int i = 0; i < 4; i++) {
				glTexCoord2f(texCoords[i][0], texCoords[i][1]);
				glVertex3f(
					vertices[faces[face][i]].x,
					vertices[faces[face][i]].y,
					vertices[faces[face][i]].z
				);
			}
		}
		
		glEnd();
	}

}

/*
 * 
 * 
 * }
 * 
 */
