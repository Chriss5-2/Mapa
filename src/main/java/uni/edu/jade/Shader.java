package uni.edu.jade;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Shader {

    /**
     *Shader Identifier
     */
    private int ID;

    /**
     * Construcs and compile the shader especified using the vertex path and the fragment path
     * @param vertexPath
     * @param fragmentPath
     */
    public Shader(String vertexPath, String fragmentPath){
        String vertexCode = "";
        String fragmentCode = "";

        try {
            File vShaderFile = new File(vertexPath);
            File fShaderFile = new File(fragmentPath);
            
            Scanner vShaderScanner, fShadScanner;
            vShaderScanner = new Scanner(vShaderFile);
            fShadScanner = new Scanner(fShaderFile);

            while(vShaderScanner.hasNextLine()){
                vertexCode += vShaderScanner.nextLine() + "\n";
            }

            while(fShadScanner.hasNextLine()){
                fragmentCode += fShadScanner.nextLine() + "\n";
            }

            vShaderScanner.close();
            fShadScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: file not succesfully read");
            e.printStackTrace();
        }
        
        int vertex,fragment,success;
        
        vertex = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertex, vertexCode);
        glCompileShader(vertex);
        success = glGetShaderi(vertex, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            //TODO: cambiar estos println por objetos Error
            int len = glGetShaderi(vertex, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: vertex_compilation_error");
            System.out.println(glGetShaderInfoLog(vertex, len));
        }

        fragment = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragment, fragmentCode);
        glCompileShader(fragment);
        success = glGetShaderi(fragment, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(fragment, GL_COMPILE_STATUS);
            System.out.println("ERROR: fragment_compilation_error");
            System.out.println(glGetShaderInfoLog(fragment, len));
        }

        ID = glCreateProgram();
        glAttachShader(ID, vertex);
        glAttachShader(ID, fragment);
        glLinkProgram(ID);
        success = glGetProgrami(ID, GL_LINK_STATUS);
        if(success == GL_FALSE){
            int len = glGetProgrami(ID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: link_program_error");
            System.out.println(glGetProgramInfoLog(ID, len));
        }

        glDeleteShader(fragment);
        glDeleteShader(vertex);

        System.out.println("Shader compilado!");

    }
    
    void Use(){
        glUseProgram(ID);
    }
}
