package uni.edu.jade;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

import uni.edu.core.wfcAlgorithm.Map;
import uni.edu.core.wfcAlgorithm.Room;

public class Draw {
	public final int DEFAULT_SHADER = 0;
	private static Shader defaultShader;

	private static ArrayList<DrawableObject> mapVAO = new ArrayList<>();

	public static void CompileShaders(){

		defaultShader = new Shader(
			"src/main/java/uni/edu/assets/shaders/defaultVertex.glsl", 
			"src/main/java/uni/edu/assets/shaders/defaultFragment.glsl"
		);
	}

	public static void SetMapVAO(Map map){
		ArrayList<Room> rooms = map.GetRooms();
		for (Room room : rooms) {
			mapVAO.add(new DrawRoom(room));
			
		}
	}

	public static void DrawTest(){

		float[] vertexArray = {
			//position			//color
			0.5f, 0.0f, 0.0f,	0.37f, 0.73f, 0.58f, 1.0f,
			-0.5f, 0.0f, 0.0f,	0.37f, 0.73f, 0.58f, 1.0f,
			0.0f, 0.5f, 0.0f,	0.37f, 0.73f, 0.58f, 1.0f,	
		};

		int[] elementArray = {
			0,1,2
		};
		
		int vaoID, vboID, eboID;

		// ============================================================
        // Generate VAO, VBO, and EBO buffer objects, and send to GPU
        // ============================================================
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);


		defaultShader.Use();
		// Bind the VAO that we're using
        glBindVertexArray(vaoID);

        // Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        glUseProgram(0);

	}

	public static void DrawMap(Map map){
		defaultShader.Use();
		for (DrawableObject object : mapVAO) {
			object.Draw();
		}
	}

	public Shader GetShader(int shaderID){
		switch(shaderID){
			case 0: 
				return defaultShader;
			default:
				System.out.println("ERROR: shaderID unknown");
				return null;
		}
	}

}
