import java.io.*;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;
public class SimpleShell{

	public static boolean isNumber(String s) {
	    try {
		Double.parseDouble(s);
		return true;
	    } catch (NumberFormatException e) {
		return false;
	    }
	}
	public static void main(String[] args) throws IOException, ArrayIndexOutOfBoundsException, IndexOutOfBoundsException, NumberFormatException{
		String path = System.getProperty("user.dir");
		File dir = new File(path);
		boolean h_flag =false;
		ArrayList<String> commandLine= new ArrayList<String>();
		String command;
		String[] current;
		String history_temp="";
		while(true){
			if(!h_flag){
				BufferedReader console=new BufferedReader(new InputStreamReader(System.in));	
				System.out.print("jsh>");
				command= console.readLine();
				current= command.split(" ");
			}else{
				current=history_temp.split(" ");
				command= history_temp;
				h_flag=false;
			}
			ProcessBuilder pb=new ProcessBuilder(command);
			pb.directory(dir);
			if(current[0].equals("exit") || current[0].equals("quit")){
				System.out.println("Good bye");
				System.exit(0);
			}else{
				try{
					if (current[0].equals("cd")){
						if(current.length==1){
							String dest="/home/ubuntu";
							File newPath= new File(dest);
							dir= newPath;
							pb.directory(dir);
						}else{
							if(current[1].charAt(0)=='/'){
								//--------absolute path---------------
								String dest= current[1];
								File newPath= new File(dest);
								boolean exists= newPath.exists();
								if(exists){
									dir=newPath;
									pb.directory(dir);
								}else{
									System.out.println("file does not exist");
								}
							}else{
								//relative path
								File newPath= new File(dir, current[1]);
								boolean exists= newPath.exists();
								if(exists){
									dir=newPath;
									pb.directory(dir);
								}else{
									System.out.println("Invalid path, enter valid path again");
								}
							}
						}
						System.out.println("current path is:"+ dir.toPath().normalize());	
					}else if(current[0].equals("history")){
						// if arraylist is empty
						if(commandLine.isEmpty()){
							System.out.println("No previous command");
						}else{
							if(current.length==1){
								int i=0;
								for(String cur: commandLine){
									System.out.println( i++ +" " + cur);
								}
							}else{
								//if history !!, run previous command
								if(current[1].equals("!!")){
									history_temp=commandLine.get(commandLine.size()-1);
									h_flag=true;
									continue;
								}else if(current[1].substring(0,1).equals("!")&& isNumber(current[1].substring(1))){
									// case history !(integer), run the ith command 	
									int cnt=Integer.parseInt(current[1].substring(1));
									if(cnt>commandLine.size()){
										System.out.println("Integer exceeds size of history list");	
									}else{
										history_temp=commandLine.get(cnt);
										h_flag=true;
									}
								}else if(isNumber(current[1])){
									int cnt= Integer.parseInt(current[1]);
									if(commandLine.size()>cnt){
										for(int i= commandLine.size()-cnt; i<commandLine.size(); i++){
											System.out.println(i+ " "+ commandLine.get(i));
										}
									}else{
										System.out.println("Integer exceeds size of history list");
									}
								}
							}
						}		
					}else{
						Process process=pb.start();
						InputStream is=process.getInputStream();
						InputStreamReader isr=new InputStreamReader(is);
						BufferedReader br= new BufferedReader(isr);			
						String line;
						while((line=br.readLine())!=null)
							System.out.println(line);
						br.close();	
					}
					if(!commandLine.isEmpty()){
						if(!commandLine.get(commandLine.size()-1).equals(command)){
							commandLine.add(command);
						}
					}else{
						commandLine.add(command);
					}
				}catch (IOException e){
					System.out.println("Input Error, Please try again!");
				}catch (ArrayIndexOutOfBoundsException e){
					System.out.println("Index out of bound error.");
					System.out.println("Enter proper Input again.	");
				}catch(NumberFormatException e) {
			    		System.out.println("Invalid type, check the type");
			    	}catch(IndexOutOfBoundsException e) {
					System.out.println("Index Out Of Bounds Exception");
				}	    	
			}
		}
	}
}
