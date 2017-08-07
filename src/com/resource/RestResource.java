package com.resource;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.omg.CORBA_2_3.portable.OutputStream;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.*;


@Path("/hello")
public class RestResource{
	private String workspace="L:/workspace";
	private String configUploadDirectory="R:/configs";
    @GET
    @Path("/init")
    @Produces("application/json")
    public String sayHello() {
    	JUnitCore j= new JUnitCore();
    	//ResourcesPlugin.
    	//File f= new File();
    	String dirPath = "L:/workspace";
    	File dir = new File(dirPath);
    	String[] dirs = dir.list();
    	String files="{";
    	Gson gson=new Gson();
    	
    	
    	ArrayList<String> res= new ArrayList<String>(); 
    	if (dirs.length == 0) {
    	    //System.out.println("The directory is empty");
    	} else {
    	    for (String aFile : dirs) {
    	        //System.out.println(aFile);
    	        res.add(aFile);
    	        files+="'"+aFile+"'";
    	        files+=",";
    	    }
    	}
    	
    	files+="}";
    	JSONArray result=new JSONArray(dirs); 
		return gson.toJson(dirs);
    }
    
    @GET
    @Path("/useVersion")
    @Produces("application/json")
    public String useVersion(@QueryParam("project") String project,@QueryParam("version") String version){
    	String file1=configUploadDirectory+"/"+project+"/"+version;
    	String file2=workspace+"/"+project+"/src/main/config/config.properties";
    	//FileChannel f1=null;
    	//FileChannel f2=null;
    	Gson gson = new Gson();
    	System.out.println("file1 is "+file1);
    	System.out.println("file2 is "+file2);
    	FileReader fr = null;
        FileWriter fw = null;
        try {
            fr = new FileReader(file1);
            fw = new FileWriter(file2);
            int c = fr.read();
            while(c!=-1) {
                fw.write(c);
                c = fr.read();
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    	return gson.toJson("success");//Response.status(200).entity("Successfully Replaced").build();
    }
    
    
    @GET
    @Path("/getConfigs")
    @Produces("application/json")
    public String getConfigs(@QueryParam("project") String project){
    	String res="";
    	Gson gson = new Gson();
    	File dir=new File(configUploadDirectory+"/"+project);
    	if(dir!=null && dir.list()!=null){
    		int count=dir.list().length;
    		if(count!=0){
    			res=gson.toJson(dir.list());
    		}
    	}
    	return res;
    }
    
    @GET
    @Path("/getcsv")
    @Produces("application/json")
    public String getcsv(@QueryParam("project") String project){
    	String res="";
    	Gson gson = new Gson();
    	File dir=new File(configUploadDirectory+"/"+project);
    	if(dir!=null && dir.list()!=null){
    		int count=dir.list().length;
    		if(count!=0){
    			res=gson.toJson(dir.list());
    		}
    	}
    	return res;
    }
    
    @GET
    @Path("testConfig")
    @Produces("application/json")
    public String testFile(@QueryParam("project") String project){
    	String res="";
    	Runtime rt=Runtime.getRuntime();
    	ArrayList<String> result=new ArrayList<String>();
    	Gson gson =new Gson();
    	File dir=new File(workspace+"/"+project+"/bin/main/java");
    	if(dir!=null){
    		String[] javaFile=dir.list();
    		String classFile=javaFile[0].replace(".class","");
    		System.out.println(classFile);
    		
    		String line=null;
    		try {
    			String cmd="java -classpath "+workspace+"/"+project+"/target/classes "+"main.java."+classFile;
				Process p=rt.exec(cmd);
				System.out.println(cmd);
				BufferedReader r=new BufferedReader(new InputStreamReader(p.getInputStream()));
				while((line=r.readLine())!=null){
					System.out.println(line);
					result.add(line);
				}
				p.waitFor();
				System.out.println("exit status is "+p.exitValue());
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    	}
    	//rt.exec(workspace+"/"+project+"/src/main/java/");
    	return gson.toJson(result);
    }
    
    
    
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
    	@FormDataParam("file") InputStream inputDataStream,
    	@FormDataParam("file") FormDataContentDisposition fileDetail,@QueryParam("project") String project){
    	System.out.println(project);
    	long count=0;
    	if(inputDataStream ==null || fileDetail==null)
    		return Response.status(400).entity("Invalid data").build();
    	try{
    		createFolder(configUploadDirectory+"/"+project);
    		File dir=new File(configUploadDirectory+"/"+project);
    		count=dir.list().length;
    		count+=1;
    		System.out.println("count is "+count);
    	}
    	catch(Exception e){
    		return Response.status(500).entity("Can not create destination folder").build();
    	}
    	String uploadFile=configUploadDirectory+"/"+project+"/"+"config"+count+".properties";
    	try{
    		FileOutputStream out=null;
    		int read=0;
    		byte[] bytes=new byte[1024];
    		out=new FileOutputStream(new File(uploadFile));
    		while((read=inputDataStream.read(bytes))!=-1)
    			out.write(bytes,0,read);
    		out.flush();
    		out.close();
    	}
    	catch(IOException e){
    		return Response.status(500).entity("Can not save file").build();
    	}
    	
    	return Response.status(200).entity("<h2>Successfully Uploaded "+project+"<h2>").build();
    }
    private void createFolder(String dir) throws SecurityException{
    	File directory= new File(dir);
    	if(!directory.exists())
    		directory.mkdir();
    }
    
}