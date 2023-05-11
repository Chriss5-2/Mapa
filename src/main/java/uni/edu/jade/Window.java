package uni.edu.jade;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_MAXIMIZED;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import uni.edu.core.wfcAlgorithm.Map;

public class Window {
	public final int CELL_SIDE_SIZE_SMALL = 30;
	public final int CELL_SIDE_SIZE_MEDIUM = 60;
	public final int CELL_SIDE_SIZE_LARGE = 120;

	private Map map;
	private int width, height;
	private String tittle;
	private long glfwWindow;

	private static Window window = null;

	private Window(){
		map = Map.Get();
		this.width = 1280;
		this.height = 720;
		this.tittle = "Map generation";
	}

	public static Window Get(){
		if(Window.window == null){
			Window.window = new Window();
		}
		return Window.window;
	}
	
	public void Run(){
		System.out.println("GLFW " + Version.getVersion() + "!");
		Init(this.width, this.height, this.tittle); //initial configuration
		Loop(); // loop

		//free the memory
		glfwFreeCallbacks(glfwWindow);
		glfwDestroyWindow(glfwWindow);

		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	public void Init(int width, int height, String title){

		map.generateMap(null);
		map.PrintMap();
		//Error callback
		GLFWErrorCallback.createPrint(System.err).set();

		//Initialize GLFW
		if(!glfwInit()){
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		//GLFW configuration
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

		// creating the window
		glfwWindow = glfwCreateWindow(width, height, tittle, NULL, NULL);
		if(glfwWindow == NULL){
			throw new IllegalStateException("Failed to create GLFW window");
		}

		//openGL current context
		glfwMakeContextCurrent(glfwWindow);
		//v-sync
		glfwSwapInterval(1);

		//make visible
		glfwShowWindow(glfwWindow);
		
		//!IMPORTANT
		GL.createCapabilities();
		
		Draw.CompileShaders();
		Draw.SetMapVAO(map);
		Draw.SetGridVAO(map.GetCollumns(), map.GetRows());

		System.out.println("VAO terminado!");

	}
	public void Loop(){
		while(!glfwWindowShouldClose(glfwWindow)){
			// poll events
			glfwPollEvents();

			glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // color del fondo
			glClear(GL_COLOR_BUFFER_BIT);

			//Draw.DrawTest();
			Draw.DrawMap(map);

			glfwSwapBuffers(glfwWindow);
		}
	}
}
