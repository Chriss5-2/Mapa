package uni.edu.jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import uni.edu.core.wfcAlgorithm.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
	private int width, height;
	private String tittle;
	private long glfwWindow;

	private static Window window = null;

	private Window(){
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
		Map map = Map.Get();
		map.generateMap();
		map.PrintMap();
		System.out.println("GLFW " + Version.getVersion() + "!");
		//Init(this.width, this.height, this.tittle); //initial configuration
		//Loop(); // loop
	}

	public void Init(int width, int height, String title){
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

	}
	public void Loop(){
		while(!glfwWindowShouldClose(glfwWindow)){
			// poll events
			glfwPollEvents();

			glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // color del fondo
			glClear(GL_COLOR_BUFFER_BIT);

			glfwSwapBuffers(glfwWindow);
		}
	}
}
