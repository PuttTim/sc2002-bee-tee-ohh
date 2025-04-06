package stores;

import models.*;

import java.io.*; // csv file


public class Database {
    /*
    // search the CSV file for the name of a Project, returns true if project found
    public Boolean databaseFindProject(String projectName){
        // if project found
        return true;
        // if project was not found
        return false;
    }

    // search the CSV file for the name of a Project and delete it
    public Boolean databaseRemoveProject(String projectName){
        // if project is found, delete it and return true
        return true;
        // if project is not found, return false
        return false;
    }

    // upload project details into the CSV file
    public void databaseStoreProject(Project project){

    }
    */


    // test interacting with CSV file
    public static void main(String[] args) {
        // change the file path according to your IDE's default working dir
        String projects = "src/stores/projectlist.csv"; // src/stores/projectlist.csv, src\\stores\\projectlist.csv
        BufferedReader reader = null;
        String line = "";

        try{
            reader = new BufferedReader(new FileReader(projects));
            int count = 0;
            while((line = reader.readLine()) != null){
                
                String[] row = line.split(",");

                System.out.printf("|");
                for(String data : row){
                    if(count < 15){
                        count++;
                        continue;
                    }

                    // if(count % 10 == 5){
                    //     System.out.printf("|");
                    // }
                    System.out.printf("%s|", data);
                    System.out.printf("%d",count);
                    count++;
                }
                System.out.println();
            }
        }
        catch(Exception e){
            e.printStackTrace(); // if something goes wrong
            return;
        }
        finally{
            try{
                reader.close();
            } 
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
    
    
    

