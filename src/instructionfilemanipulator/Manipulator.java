/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instructionfilemanipulator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author Nathaniel
 */
public class Manipulator {
    
    public Manipulator(String source) throws FileNotFoundException{
        readSECMInfo(source);
    }
    
    public void export_image(String source, String image_dest) throws FileNotFoundException, IOException{
        File outfile = new File(image_dest);
        outfile.createNewFile();
        PrintWriter pw = new PrintWriter(outfile);
        pw.print("#x[m],y[m],i[A]");
        for(int i = 0; i < physical_xs.length; i++){
            pw.print("\n" + physical_xs[i] + "," + physical_ys[i] + "," + true_image[i]);
        }
        pw.close();
    }
    
    public void export_if(String source, String if_dest) throws FileNotFoundException, IOException{
        SECMImage secmi = new SECMImage(physical_xs, physical_ys, true_image);
        File originalfile = new File(source);
        File newfile = new File(if_dest);
        if(source.equals(if_dest)){
            throw new IOException("Cannot read and write to the same file");
        }
        newfile.createNewFile();
        Scanner s = new Scanner(originalfile);
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(newfile)));
        String sep = ",";
        
        if(s.hasNextLine()){
            String fl = s.nextLine();
            pw.print(fl);
            if(fl.equalsIgnoreCase("##ENCODING: csv")){
                sep = ",";
            }
            else if(fl.equalsIgnoreCase("##ENCODING: tsv")){
                sep = "\t";
            }
            else{
                throw new FileNotFoundException("File incorrectly formatted.");
            }
        }
        else{
            throw new FileNotFoundException("File incorrectly formatted.");
        }
        
        while(s.hasNextLine()){
            String line = s.nextLine();
            if(!line.startsWith("#")){
                String[] linesplit = line.split(sep);
                double x = Double.parseDouble(linesplit[3]);
                double y = Double.parseDouble(linesplit[4]);
                double curr = secmi.getCurrent(x, y, SECMImage.INTERPOLATION_BICUBIC);
                pw.print("\n" + linesplit[0] + sep + linesplit[1] + sep + linesplit[2] + sep + linesplit[3] + sep + linesplit[4] + sep + curr);
            }
            else{
                pw.print("\n" + line);//copy-over commented lines
            }
        }
        
        s.close();
        pw.close();
    }
    
    static void readSECMInfo(String filepath) throws FileNotFoundException{
        File f = new File(filepath);
        String true_file = filepath;
        int minx = 0;
        int miny = 0;
        int max_x = 0;
        int max_y = 0;
        double[] trueimage;
        double[] physicalxs;
        double[] physicalys;
        int[] samplexs;
        int[] sampleys;
        String sep = ",";
        
        Scanner s = new Scanner(f);
            if(s.hasNextLine()){
                String fl = s.nextLine();
                if(fl.equalsIgnoreCase("##ENCODING: csv")){
                    sep = ",";
                }
                else if(fl.equalsIgnoreCase("##ENCODING: tsv")){
                    sep = "\t";
                }
                else{
                    throw new FileNotFoundException("File incorrectly formatted.");
                }
            }
            else{
                throw new FileNotFoundException("File incorrectly formatted.");
            }
            
            //X header
            int xstart = 0;
            int xstep = 0;
            int xnum = 0;
            if(s.hasNextLine()){
                String fl = s.nextLine();
            }
            else{
                throw new FileNotFoundException("File incorrectly formatted.");
            }
            if(s.hasNextLine()){
                String fl = s.nextLine();
                String[] tokens = fl.substring(1).trim().split(",");
                xstart = Integer.parseInt(tokens[0]);
                xstep = Integer.parseInt(tokens[1]);
                xnum = Integer.parseInt(tokens[2]);
            }
            else{
                throw new FileNotFoundException("File incorrectly formatted.");
            }
            
            //Y header
            int ystart = 0;
            int ystep = 0;
            int ynum = 0;
            if(s.hasNextLine()){
                String fl = s.nextLine();
            }
            else{
                throw new FileNotFoundException("File incorrectly formatted.");
            }
            if(s.hasNextLine()){
                String fl = s.nextLine();
                String[] tokens = fl.substring(1).trim().split(",");
                ystart = Integer.parseInt(tokens[0]);
                ystep = Integer.parseInt(tokens[1]);
                ynum = Integer.parseInt(tokens[2]);
            }
            else{
                throw new FileNotFoundException("File incorrectly formatted.");
            }
            
            //read through the main data file to pull out trueimage, physicalxs, physicalys, samplexs, sampleys, minx, miny
            minx = xstart;
            max_x = xstart;
            miny = ystart;
            max_y = ystart;
            
            int asize = (xnum)*(ynum);
            int present_index = 0;
            
            trueimage = new double[asize];
            physicalxs = new double[asize];
            physicalys = new double[asize];
            samplexs = new int[asize];
            sampleys = new int[asize];
            
            while(s.hasNextLine()){
                String line = s.nextLine();
                if(!line.startsWith("#")){
                    String[] linesplit = line.split(sep);
                    int x = Integer.parseInt(linesplit[0]);
                    int y = Integer.parseInt(linesplit[1]);
                    double px = Double.parseDouble(linesplit[3]);
                    double py = Double.parseDouble(linesplit[4]);
                    double curr = Double.parseDouble(linesplit[5]);
                    
                    if(x < minx){
                        minx = x;
                    }
                    else if(x > max_x){
                        max_x = x;
                    }
                    
                    if(y < miny){
                        miny = y;
                    }
                    else if(y > max_y){
                        max_y = y;
                    }
                    
                    //check if x,y is one of the points to be sampled
                    boolean xvalid = (x >= xstart) && (x < xstart + xnum*xstep) && ((x-xstart)%xstep == 0);
                    boolean yvalid = (y >= ystart) && (y < ystart + ynum*ystep) && ((y-ystart)%ystep == 0);
                    if(xvalid && yvalid && present_index < asize){
                        trueimage[present_index] = curr;
                        physicalxs[present_index] = px;
                        physicalys[present_index] = py;
                        samplexs[present_index] = x;
                        sampleys[present_index] = y;
                        present_index ++;
                    }
                    
                }
            }
        s.close();
        //second read of the file to get grid_switches[][]
        int[][] grid_switches = new int[max_x - minx + 1][max_y - miny + 1];
        s = new Scanner(f);
            while(s.hasNextLine()){
                String line = s.nextLine();
                if(!line.startsWith("#")){
                    String[] linesplit = line.split(sep);
                    int x = Integer.parseInt(linesplit[0]);
                    int y = Integer.parseInt(linesplit[1]);
                    int rs = Integer.parseInt(linesplit[2]);
                    
                    grid_switches[x-minx][y-miny] = rs;
                    
                }
            }
        s.close();
        
        true_image = trueimage;
        physical_xs = physicalxs;
        physical_ys = physicalys;
        sample_xs = samplexs;
        sample_ys = sampleys;
        min_x = minx;
        min_y = miny;
        grid = grid_switches;
        
    }
    
    
    /**
     * The experimental SECM currents
     */
    static double[] true_image;
    
    /**
     * The experimental x-coordinates in meters
     */
    static double[] physical_xs;
    
    /**
     * The experimental y-coordinates in meters
     */
    static double[] physical_ys;
    
    /**
     * The grid_switches x-indexes
     */
    static int[] sample_xs;
    
    /**
     * The grid_switches y-indexes
     */
    static int[] sample_ys;
    
    /**
     * The pixel grid_switches
     */
    static int[][] grid;
    
    /**
     * the minimum grid_switches x-index
     */
    static int min_x;
    
    /**
     * the minimum grid_switches y-index
     */
    static int min_y;
}
