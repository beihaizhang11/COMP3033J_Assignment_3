package objects3D;

import static org.lwjgl.opengl.GL11.*;
import GraphicsObjects.Utils;
import org.newdawn.slick.opengl.Texture;
import java.io.IOException;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Human {
	// 定义颜色
	static float skin[] = { 0.956f, 0.867f, 0.699f, 1.0f };
	static float shirt[] = { 0.196f, 0.6f, 0.8f, 1.0f };
	static float pants[] = { 0.25f, 0.25f, 0.25f, 1.0f };
	static float shoes[] = { 0.1f, 0.1f, 0.1f, 1.0f };
	static float white[] = { 1.0f, 1.0f, 1.0f, 1.0f };
	
	private Sphere sphere;
	private Cylinder cylinder;
	private TexCube texCube;
	private TexCylinder texCylinder;
	private TexSphere texSphere;
	private Texture headTexture;
	private Texture bodyTexture;
	
	// 无参构造函数
	public Human() {
		sphere = new Sphere();
		cylinder = new Cylinder();
		texCube = new TexCube();
		texCylinder = new TexCylinder();
		texSphere = new TexSphere();
		
		// 加载纹理
		try {
			this.bodyTexture = TextureLoader.getTexture("PNG", 
				ResourceLoader.getResourceAsStream("res/textures/shirt.png"));
			this.headTexture = TextureLoader.getTexture("PNG", 
				ResourceLoader.getResourceAsStream("res/earthspace.png"));
		} catch (IOException e) {
			System.out.println("Could not load texture files");
			this.bodyTexture = null;
			this.headTexture = null;
		}
	}

	public void drawHuman(float delta, boolean GoodAnimation) {
		float theta = (float) (delta * 2 * Math.PI);
		float swingFrequency = 1.0f;
		float swingAmplitude = 25.0f;
		
		float leftArmRightLeg = GoodAnimation ? 
			(float) Math.cos(theta * swingFrequency) * swingAmplitude : 0;
		float rightArmLeftLeg = GoodAnimation ? 
			(float) Math.cos(theta * swingFrequency + Math.PI) * swingAmplitude : 0;
		
		glPushMatrix();
		{
			// 先绘制腿部
			drawLeg(0.15f, rightArmLeftLeg);
			drawLeg(-0.15f, leftArmRightLeg);
			
			// 躯干向上移动
			glTranslatef(0.0f, 0.5f, 0.0f);
			
			// 绘制躯干
			if (bodyTexture != null) {
				drawTorso(bodyTexture);
			} else {
				drawTorsoWithoutTexture();
			}
			
			// 头部和手臂
			drawHead();
			drawArm(0.3f, leftArmRightLeg);
			drawArm(-0.3f, rightArmLeftLeg);
		}
		glPopMatrix();
	}

	private void drawTorsoWithoutTexture() {
		glPushMatrix();
		{
			// 向下移动躯干高度的一半，使躯干中心点在坐标原点
			glTranslatef(0.0f, 0.25f, 0.0f);
			glColor3f(shirt[0], shirt[1], shirt[2]);
			glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(shirt));
			
			// 将躯干旋转90度使其垂直
			glRotatef(90, 1.0f, 0.0f, 0.0f);
			
			// 使用普通的cylinder绘制躯干
			cylinder.drawCylinder(0.2f, 0.8f, 32);
		}
		glPopMatrix();
	}

	private void drawHead() {
		glPushMatrix();
		{
			// 头部位置从躯干中心向上0.3单位
			glTranslatef(0.0f, 0.3f, 0.0f);
			
			// 脖子
			glColor3f(skin[0], skin[1], skin[2]);
			glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(skin));
			cylinder.drawCylinder(0.05f, 0.1f, 32);
			
			// 头部
			glTranslatef(0.0f, 0.15f, 0.0f);
			
			// 启用纹理
			if (headTexture != null) {
				glColor3f(1.0f, 1.0f, 1.0f);
				glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(white));
				glEnable(GL_TEXTURE_2D);
				headTexture.bind();
				texSphere.DrawTexSphere(0.2f, 32, 32, headTexture);
				glDisable(GL_TEXTURE_2D);
			} else {
				glColor3f(skin[0], skin[1], skin[2]);
				glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(skin));
				sphere.drawSphere(0.2f, 32, 32);
			}
		}
		glPopMatrix();
	}

	private void drawArm(float xOffset, float rotation) {
		glPushMatrix();
		{
			// 手臂位置从躯干中心向两侧偏移
			glTranslatef(xOffset, 0.15f, 0.0f);
			
			// 肩部
			glColor3f(shirt[0], shirt[1], shirt[2]);
			glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(shirt));
			sphere.drawSphere(0.1f, 32, 32);
			
			// 将手臂旋转90度使其垂直
			glRotatef(90, 1.0f, 0.0f, 0.0f);
			
			// 修改摆动旋转轴为x轴(前后摆动)
			float shoulderRotation = Math.max(-30, Math.min(30, rotation));
			glRotatef(shoulderRotation, 1.0f, 0.0f, 0.0f);
			
			// 上臂
			cylinder.drawCylinder(0.08f, 0.3f, 32);
			
			// 肘部
			glTranslatef(0.0f, 0.0f, 0.3f);
			sphere.drawSphere(0.08f, 32, 32);
			
			// 前臂的摆动稍微滞后于上臂
			float elbowRotation = Math.max(-20, Math.min(20, rotation * 0.7f));
			glRotatef(elbowRotation, 1.0f, 0.0f, 0.0f);
			
			// 添加肘部自然弯曲
			float naturalBend = 15.0f + Math.abs(rotation * 0.2f);
			glRotatef(naturalBend, 1.0f, 0.0f, 0.0f);
			
			cylinder.drawCylinder(0.06f, 0.3f, 32);
			
			// 手腕和手的部分保持不变...
		}
		glPopMatrix();
	}

	private void drawLeg(float xOffset, float rotation) {
		glPushMatrix();
		{
			// 从原点开始，只需要x轴偏移
			glTranslatef(xOffset, 0.0f, 0.0f);
			
			// 髋部
			glColor3f(pants[0], pants[1], pants[2]);
			glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(pants));
			sphere.drawSphere(0.1f, 32, 32);
			
			// 将腿部旋转90度使其垂直
			glRotatef(90, 1.0f, 0.0f, 0.0f);
			
			// 髋关节旋转
			float hipRotation = Math.max(-25, Math.min(25, rotation));
			glRotatef(hipRotation, 1.0f, 0.0f, 0.0f);
			
			// 大腿
			cylinder.drawCylinder(0.08f, 0.4f, 32);
			
			// 膝盖
			glTranslatef(0.0f, 0.0f, 0.4f);
			sphere.drawSphere(0.08f, 32, 32);
			
			// 小腿的摆动根据大腿的角度调整
			float kneeRotation;
			if (rotation < 0) {  // 腿向后摆
				// 向后摆时小腿可以比大腿摆得更大
				kneeRotation = rotation * 1.5f;
			} else {  // 腿向前摆
				// 向前摆时小腿逐渐与大腿平行
				float ratio = Math.abs(rotation) / 25.0f;  // 25是最大摆动角度
				kneeRotation = rotation * (1.0f - ratio * 0.8f);  // 随着前摆逐渐减小角度差
			}
			
			// 限制小腿摆动范围
			kneeRotation = Math.max(-45, Math.min(30, kneeRotation));
			glRotatef(kneeRotation, 1.0f, 0.0f, 0.0f);
			
			// 添加膝盖自然弯曲
			float naturalKneeBend = 5.0f + Math.abs(rotation * 0.2f);
			glRotatef(naturalKneeBend, 1.0f, 0.0f, 0.0f);
			
			cylinder.drawCylinder(0.07f, 0.4f, 32);
			
			// 脚部保持不变
			glTranslatef(0.0f, 0.0f, 0.4f);
			glColor3f(shoes[0], shoes[1], shoes[2]);
			glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(shoes));
			glScalef(1.2f, 0.8f, 1.5f);
			sphere.drawSphere(0.08f, 32, 32);
		}
		glPopMatrix();
	}

	private void drawTorso(Texture bodyTexture) {
		if (bodyTexture == null) {
			drawTorsoWithoutTexture();
			return;
		}
		
		glPushMatrix();
		{
			glTranslatef(0.0f, 0.25f, 0.0f);
			glColor3f(1.0f, 1.0f, 1.0f);
			glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(white));
			
			glEnable(GL_TEXTURE_2D);
			bodyTexture.bind();
			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			
			glRotatef(90, 1.0f, 0.0f, 0.0f);
			
			texCylinder.drawTexCylinder(0.2f, 0.8f, 32, bodyTexture);
			
			glDisable(GL_TEXTURE_2D);
		}
		glPopMatrix();
	}
}
